package me.matthewmerrill.ek;

import me.matthewmerrill.ek.card.Card;
import me.matthewmerrill.ek.card.Deck;

public class Player {
	
	public final String name;
	
	public final Deck playerDeck;
	
	public boolean killed = false;
	
	public Player(String name) {
		this.name = name;
		this.playerDeck = new Deck();
	}
	
	public void giveCard(Card card) {
		playerDeck.add(card);
	}

}
