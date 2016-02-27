package me.matthewmerrill.ek;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Consumer;

import me.matthewmerrill.ek.card.Card;
import me.matthewmerrill.ek.websocket.Chat;
import me.matthewmerrill.ek.websocket.LobbyCallback;
import me.matthewmerrill.ek.websocket.PromptCallbackManager;
import me.matthewmerrill.ek.websocket.SoundManager;
import me.matthewmerrill.ek.websocket.prompt.DefuseBombPrompt;
import me.matthewmerrill.ek.websocket.prompt.NopePrompt;

public abstract class LobbyState extends HashMap<String, Object> {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = -6839744457523758266L;
	
	public static enum StateType {
		TURN,
		NOPE,
		BOMB_PLANT,
		DEFUSE,
		ATTACK,
		STOPPED
	}
	
	public static final String NEXT = "next";
	public Lobby lobby;
	public static final String TYPE = "type";
	public static final String NAME = "name";
	
	public LobbyState(Lobby lobby, String name) {
		this.lobby = lobby;
		put(NAME, name);
	}
	
	@Override
	public Object get(Object key) {
		if (NEXT.equals(key))
			return next();
		return super.get(key);
	}
	
	public abstract LobbyState next();
	public abstract boolean isActive(Card card, Player holder);

	public void openedState(){};
	public void leftState(){};
	public String sound(){return SoundManager.STATE_CHANGE;};
	
	public static class Turn extends LobbyState {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = -9108454037643910646L;
		
		public final int index;
		public String activeSsid;
		public int direction = -1;

		public Turn(Lobby lobby) {
			super(lobby, lobby.getPlaying().get(lobby.getPlaying().size()-1).get(Player.NAME) +"'s Turn.");
			this.index = lobby.getPlaying().size() - 1;
			this.activeSsid = (String)lobby.getPlaying().get(index).get(Player.SESSION_ID);
		}
		
		public Turn(Lobby lobby, int index) {
			super(lobby, lobby.getPlaying().get(index).get(Player.NAME) +"'s Turn.");
			this.index = index;
			this.activeSsid = (String)lobby.getPlaying().get(index).get(Player.SESSION_ID);
		}
		
		@Override
		public LobbyState next() {
			return new Turn(lobby, (index + direction + lobby.getPlaying().size()) % lobby.getPlaying().size());
		}
		
		@Override
		public boolean isActive(Card card, Player player) {
			
			if (!activeSsid.equals(player.get(Player.SESSION_ID)))
				return false;
			
			switch((String)card.get(Card.ID)) {
			case("nope"):
			case("defuse"):
				return false;
			default:
				return true;
			}
		}
		
	}
	public static class AttackTurn extends LobbyState {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = -9108454037643910646L;
		
		private final LobbyState next;
		public final int index;
		public String activeSsid;
		public int direction = -1;


		public AttackTurn(Lobby lobby, LobbyState next) {
			super(lobby, lobby.getPlaying().get(lobby.getPlaying().size()-1).get(Player.NAME) +"'s Attack Turn.");
			this.next = next;
			this.index = lobby.getPlaying().size() - 1;
			this.activeSsid = (String)lobby.getPlaying().get(index).get(Player.SESSION_ID);
		}
		
		@Override
		public LobbyState next() {
			if (next != null)
				return next;
			
			return new Turn(lobby, (index + direction + lobby.getPlaying().size()) % lobby.getPlaying().size());
		}
		
		@Override
		public boolean isActive(Card card, Player player) {
			
			if (!activeSsid.equals(player.get(Player.SESSION_ID)))
				return false;
			
			switch((String)card.get(Card.ID)) {
			case("nope"):
			case("defuse"):
				return false;
			default:
				return true;
			}
		}
		
	}
	
	
	public static class Bomb extends LobbyState {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = -9108454037643910646L;

		private Timer timer = null;
		public final Turn nextTurn;
		public final String defuserSsid;
		
		public Bomb(Lobby lobby, Turn nextTurn, Player player) {
			super(lobby, player.getName() + " is defusing...");
			
			this.nextTurn = nextTurn;
			this.defuserSsid = player.getSsid();
			
			if (!player.getDeck().stream().anyMatch((c) -> c.get(Card.ID).equals("defuse"))) {
				this.timer = new Timer();
				timer.schedule(new TimerTask(){

					@Override
					public void run() {
						Chat.broadcastMessage("Server", player.getName() + " could not defuse!", lobby,
								(lobby.getPlaying().size() > 2) ? SoundManager.MEOW : SoundManager.NO_SOUND);
						
						lobby.killedPlayer(player);
						
						lobby.nextTurn();
					}}, 3000L);
			}
			
		}

		@Override
		public void leftState() {
			if (timer != null) {
				try {
					timer.cancel();
					timer.purge();
				} catch (Exception ignored) {};
			}
		}
		
		@Override
		public String sound() {
			return SoundManager.BOMB_PICKUP;
		}
		
		@Override
		public LobbyState next() {
			return nextTurn;
		}

		@Override
		public boolean isActive(Card card, Player player) {
			if (!defuserSsid.equals(player.getSsid()))
				return false;
			
			switch((String)card.get(Card.ID)) {
			case("defuse"):
				return true;
			default:
				return false;
			}
		}
	}
	
	public static class Stopped extends LobbyState {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = -8465393700700858797L;

		public Stopped(Lobby lobby) {
			super(lobby, "Game Stopped");
		}
		
		@Override
		public String sound() {
			return SoundManager.NO_SOUND;
		}
		
		@Override
		public LobbyState next() {
			return this;
		}

		@Override
		public boolean isActive(Card card, Player holder) {
			switch((String)card.get(Card.ID)) {
			default:
				return false;
			}
		}
	}

	public static class Defusing extends LobbyState {

		/**
		 * 
		 */
		private static final long serialVersionUID = 2383214843446516292L;
		private final LobbyState next;
		
		public Defusing(Lobby lobby, Player player, LobbyState next) {
			super(lobby, player.getName() + " is placing bomb...");
			this.next = next;
			
			DefuseBombPrompt prompt = new DefuseBombPrompt(lobby, player);
			prompt.send();
			PromptCallbackManager.promptSent(prompt, player.getSsid(), 30000L, "0");
		}

		@Override
		public LobbyState next() {
			return next;
		}

		@Override
		public boolean isActive(Card card, Player holder) {
			return false;
		}

	}


	public static class Nope extends LobbyState {

		/**
		 * 
		 */
		private static final long serialVersionUID = 2383214843446516292L;

		private Timer timer;
		private LobbyCallback callback;
		private NopePrompt prompt;
		private boolean noped = false;
		
		private final LobbyState prev;
		private final LobbyState next;
		
		private final String action;
		private final Player player;
		
		/*
		// If we get NOPE'd, go back to prev. 
		public Nope(Lobby lobby, Player player, LobbyState prev, LobbyState next, String actionName) {
			super(lobby, "Allowing Nopes...");
			this.prev = prev;
			this.next = next;
			
			this.action = actionName;
			this.player = player;
			
			this.timer = new Timer();
			timer.schedule(new TimerTask(){

				@Override
				public void run() {
					prompt.cancel();
					lobby.setState(next);
				}}, 7000L);
			
			prompt = new NopePrompt(lobby, player, this, actionName);
			prompt.send();
		}
		*/
		
		// If we get NOPE'd, go back to prev. 
		public Nope(Lobby lobby, Player player, LobbyState prev, LobbyCallback callback, String actionName) {
			super(lobby, "Allowing Nopes...");
			this.prev = prev;
			this.next = null;
			this.callback = callback;
			
			this.action = actionName;
			this.player = player;
		}
		
		private void send() {
			leftState();
			
			this.timer = new Timer();
			timer.schedule(new TimerTask(){
				
				@Override
				public void run() {
					prompt.cancel();
					callback.callback(lobby, player, action);
				}}, 7000L);
			
			prompt = new NopePrompt(lobby, player, this, action);
			prompt.send();
		}

		@Override
		public void openedState() {
			send();
		}
		
		@Override
		public void leftState() {
			try {
				timer.cancel();
				timer.purge();
			} catch (Exception ignored) {};
		}
		
		@Override
		public LobbyState next() {
			return (noped || next == null) ? prev : next;
		}
		
		public void nope(Player noper) {
			Chat.broadcastMessage("Server", noper.getName() + " Nope'd " + player.getName() + "'s " + action, lobby);
			lobby.setState(new LobbyState.Nope(lobby, noper, this, (l, p, m) -> {
				lobby.setState(prev);
				return true;}, "Nope"));
		}

		@Override
		public boolean isActive(Card card, Player holder) {
			return false;
		}

		public String getAction() {
			return action;
		}

	}
/*
	public static class Shuffle extends LobbyState {
	
		private static final long serialVersionUID = -9108454037643910646L;

		private LobbyState next;
		
		public Shuffle(Lobby lobby, LobbyState next) {
			super(lobby, "Shuffling...");
			this.next = next;
			
			lobby.getDrawDeck().shuffle();
			lobby.getDrawDeck().shuffle();
			
			lobby.setState(next);
		}
		
		@Override
		public LobbyState next() {
			return next;
		}
		
		@Override
		public boolean isActive(Card card, Player player) {
			
			switch((String)card.get(Card.ID)) {
			default:
				return false;
			}
		}
		
	}*/
}
