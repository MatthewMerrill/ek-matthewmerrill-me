package me.matthewmerrill.ek.card;

import me.matthewmerrill.ek.Lobby;
import me.matthewmerrill.ek.Player;

public abstract class Card {

	public final String imageUrl;
	
	public Card(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	
	@Override
	public String toString() {
		return imageUrl;
	}
	
	public abstract void played(Lobby lobby, Deck deck, Player player);
	
	public void pickedUp(Lobby lobby, Deck deck, Player player) {
		player.playerDeck.add(this);
	}
	
	
}
