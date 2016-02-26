package me.matthewmerrill.ek;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.common.eventbus.EventBus;

import me.matthewmerrill.ek.card.BombCard;
import me.matthewmerrill.ek.card.Card;
import me.matthewmerrill.ek.card.CollectCard;
import me.matthewmerrill.ek.card.Deck;
import me.matthewmerrill.ek.card.DefuseCard;
import me.matthewmerrill.ek.card.SkipCard;
import me.matthewmerrill.ek.event.PlayerJoinLobbyEvent;
import me.matthewmerrill.ek.websocket.ChatWebSocketHandler;

public class Lobby extends HashMap<String, Object> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 852865897444942442L;
	
	public static final String NAME = "name";
	public static final String ID = "id";
	
	public static final String PASSWORD = "password";
	
	public static final String PLAYERS = "players";
	public static final String PLAYER_COUNT = "playerCount";
	public static final String MAX_PLAYERS = "maxPlayers";
	public static final String ADMIN = "admin";

	public static final String DRAW_DECK = "drawDeck";
	public static final String BOMBS_EXPLODED = "bombsExploded";
	public static final String BOMBS_REMAINING = "bombsRemaining";
	
	public static final String PLAYER_TURN = "playerTurn";
	public static final String TURN_INDEX = "turnIndex";
	public static final String TURN_DIRECTION = "turnDirection";

	private static final String STATE = "state";
	
	public EventBus eventBus;
	
	private static BigInteger curId = BigInteger.valueOf((int) Math.ceil(Math.random() * 1000));
	
	public Lobby(String name) {
		this(
			(curId = curId.add(BigInteger.ONE)).toString(16),
			name);
	}
	
	public Lobby(String id, String name) {
		put(NAME, name);
		put(ID, id);
		
		put(PASSWORD, null);
		
		put(PLAYERS, new ArrayList<Player>());
		put(MAX_PLAYERS, 4);
		
		put(DRAW_DECK, new Deck());
		put(BOMBS_EXPLODED, 0);
		put(BOMBS_REMAINING, 0);
		
		put(PLAYER_TURN, 0);
		put(TURN_DIRECTION, 1);
		
		put(STATE, new LobbyState.Stopped(this));
		
		eventBus = new EventBus();
		eventBus.register(new LobbyListener(this));
	}
	
	public Deck getDrawDeck() {
		return (Deck) get(DRAW_DECK);
	}

	public String getName() {
		return get(NAME).toString();
	}

	public String getId() {
		return get(ID).toString();
	}

	
	public void addPlayer(Player player) {
		List<Player> players = getPlayers();
		if (players.isEmpty())
			put(ADMIN, player);
		players.add(player);
		
		PlayerJoinLobbyEvent e = new PlayerJoinLobbyEvent(this, player);
		eventBus.post(e);
		//Chat.broadcastMessage("Server", e.getMessage(), this);
		//Chat.broadcastMessage("Server", e.getMessage());
	}
	
	@SuppressWarnings("unchecked")
	public List<Player> getPlayers() {
		return (List<Player>) get(PLAYERS);
	}

	public Player getPlayer(String ssid) {
		for (Player pl : getPlayers()) {
			if (pl.get(Player.SESSION_ID).equals(ssid))
				return pl;
		}
		
		return null;
	}
	
	public int getTurnIndex() {
		return (Integer) get(TURN_INDEX);
	}
	public void setPlayerIndex(int index) {
		index %= getPlayers().size();
		put(TURN_INDEX, index);		
	}

	public void stop() {
		getDrawDeck().clear();
		
		for (Player player : getPlayers()) {
			player.getDeck().clear();
		}

		put(BOMBS_EXPLODED, 0);
		put(BOMBS_REMAINING, 0);
		
		ChatWebSocketHandler.updateLobby(this);
	}
	
	public void deal() {
		
		Deck deck = getDrawDeck();
		List<Player> players = getPlayers();

		deck.clear();
		players.forEach((Player player) ->
			((Deck)player.get(Player.DECK)).clear());

		// Deal bulk of player cards
		{
			for (Card card : SkipCard.startingCards())
				deck.add(card);
			for (Card card : CollectCard.startingCards())
				deck.add(card);

			deck.shuffle();

			players.forEach((Player player) -> {
				while (player.getDeck().size() < 4)
					player.giveCard(deck.draw());
			});
		}

		// Handle defuse cards
		{
			final Deck defuseDeck = new Deck();

			for (Card card : DefuseCard.startingCards())
				defuseDeck.add(card);

			defuseDeck.shuffle();

			players.forEach((Player player) -> player.giveCard(defuseDeck.draw()));
			deck.addAll(defuseDeck);

			defuseDeck.clear();
		}

		// Add bomb cards, shuffle once more and we're done!
		{
			for (Card card : BombCard.startingCards())
				deck.add(card);

			deck.shuffle();
		}
		
		put(BOMBS_EXPLODED, 0);
		put(BOMBS_REMAINING, 0);
		
		ChatWebSocketHandler.updateLobby(this);
	}

	public void nextTurn() {
		setState(getState().next());
	}

	public boolean hasPassword() {
		return containsKey(PASSWORD) && getPassword() != null;
	}
	
	public String getPassword() {
		return (String) get(PASSWORD);
	}
	
	public boolean isPassword(String password) {
		if (!hasPassword())
			return true;
		
		return getPassword().equals(password);
	}
	
	public void setPassword(String password) {
		put(PASSWORD, password);
	}

	public void updateAdmin() {
		Player admin = getAdmin();
		if (admin == null || 
			!getPlayers().stream().anyMatch(
					(p) -> p.get(Player.SESSION_ID).equals(admin.get(Player.SESSION_ID)))) {
			
			if (getPlayers().isEmpty()) { 
				remove(ADMIN);
			} else {
				put(ADMIN, getPlayers().iterator().next());
				ChatWebSocketHandler.updateLobby(this);
			}
		}
	}
	
	public Player getAdmin() {
		return (Player) get(ADMIN);
	}

	public LobbyState getState() {
		return (LobbyState) get(STATE);
	}
	public void setState(LobbyState state) {
		this.put(STATE, state);
		ChatWebSocketHandler.updateLobby(this);
	}
}
