package me.matthewmerrill.ek;

import java.util.HashMap;

import me.matthewmerrill.ek.card.Card;
import me.matthewmerrill.ek.websocket.PromptCallbackManager;
import me.matthewmerrill.ek.websocket.prompt.DefuseBombPrompt;

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
	
	public static class Turn extends LobbyState {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = -9108454037643910646L;
		
		public final int index;
		public String activeSsid;
		public int direction = -1;

		public Turn(Lobby lobby) {
			super(lobby, lobby.getPlayers().get(lobby.getPlayers().size()-1).get(Player.NAME) +"'s Turn.");
			this.index = lobby.getPlayers().size() - 1;
			this.activeSsid = (String)lobby.getPlayers().get(index).get(Player.SESSION_ID);
		}
		
		public Turn(Lobby lobby, int index) {
			super(lobby, lobby.getPlayers().get(index).get(Player.NAME) +"'s Turn.");
			this.index = index;
			this.activeSsid = (String)lobby.getPlayers().get(index).get(Player.SESSION_ID);
		}
		
		@Override
		public LobbyState next() {
			return new Turn(lobby, (index + direction + lobby.getPlayers().size()) % lobby.getPlayers().size());
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
		
		public final Turn nextTurn;
		public final String defuserSsid;
		
		public Bomb(Lobby lobby, Turn nextTurn, String defuserSsid) {
			super(lobby, "Defusing Bomb");
			this.nextTurn = nextTurn;
			this.defuserSsid = defuserSsid;
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
		private final String ssid;
		
		public Defusing(Lobby lobby, Player player, LobbyState next) {
			super(lobby, "Defusing Bomb...");
			this.next = next;
			this.ssid = player.getSsid();
			
			DefuseBombPrompt prompt = new DefuseBombPrompt(lobby, player);
			prompt.send();
			PromptCallbackManager.promptSent(prompt, 30000L, "0");
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

}
