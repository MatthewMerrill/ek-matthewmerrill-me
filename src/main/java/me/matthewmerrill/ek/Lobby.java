package me.matthewmerrill.ek;

import java.math.BigInteger;
import java.util.*;

import me.matthewmerrill.ek.card.*;

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

	public static final String DRAW_DECK = "drawDeck";
	public static final String BOMBS_EXPLODED = "bombsExploded";
	public static final String BOMBS_REMAINING = "bombsRemaining";
	
	public static final String PLAYER_TURN = "playerTurn";
	public static final String TURN_INDEX = "turnIndex";
	public static final String TURN_DIRECTION = "turnDirection";
	
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
		
		put(PLAYERS, new HashSet<Player>());
		put(MAX_PLAYERS, 4);
		
		put(DRAW_DECK, new Deck());
		put(BOMBS_EXPLODED, 0);
		put(BOMBS_REMAINING, 0);
		
		put(PLAYER_TURN, 0);
		put(TURN_DIRECTION, 1);
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

	
	@SuppressWarnings("unchecked")
	public Set<Player> getPlayers() {
		return (Set<Player>) get(PLAYERS);
	}
	
	public int getTurnIndex() {
		return (Integer) get(TURN_INDEX);
	}
	public void setPlayerIndex(int index) {
		index %= getPlayers().size();
		put(TURN_INDEX, index);		
	}

	public void deal() {
		
		Deck deck = getDrawDeck();
		Set<Player> players = getPlayers();

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
	}

	public void nextTurn() {
		// TODO Auto-generated method stub
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
}
