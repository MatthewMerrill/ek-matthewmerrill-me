package me.matthewmerrill.ek;

import java.util.HashMap;

import me.matthewmerrill.ek.card.Card;
import me.matthewmerrill.ek.card.Deck;

public class Player extends HashMap<String, Object> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String NAME = "name";
	public static final String DECK = "playerDeck";
	public static final String KILLED = "killed";
	
	public Player(String name) {
		put(NAME, name);
		put(DECK, new Deck());
		put(KILLED, false);
	}
	
	public Deck getDeck() {
		return (Deck) get(DECK);
	}
	
	public void giveCard(Card card) {
		Deck deck = (Deck) get(DECK);
		deck.add(card);
		put(DECK, deck);
	}

}
