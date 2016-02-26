package me.matthewmerrill.ek;

import java.util.HashMap;

import me.matthewmerrill.ek.card.Card;
import me.matthewmerrill.ek.card.Deck;

public class Player extends HashMap<String, Object> implements Comparable<Player> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String NAME = "name";
	public static final String SESSION_ID = "sessionId";
	public static final String DECK = "playerDeck";
	public static final String KILLED = "killed";
	
	public Player(String name, String sessionId) {
		put(NAME, name);
		put(SESSION_ID, sessionId);
		put(DECK, new Deck());
		put(KILLED, false);
	}
	
	public Deck getDeck() {
		return (Deck) get(DECK);
	}
	
	public void giveCard(Card card) {
		Deck deck = (Deck) get(DECK);
		deck.add(0, card);
		put(DECK, deck);
	}
	
	@Override
	public boolean equals(Object obj) {
		return compareTo((Player)obj) == 0;
	}
	
	@Override
	public int compareTo(Player player) {
		return get(SESSION_ID).toString().compareTo(player.get(SESSION_ID).toString());
	}
	
	@Override
	public int hashCode() {
		return get(SESSION_ID).hashCode();
	}

	public String getSsid() {
		return (String) get(SESSION_ID);
	}

}
