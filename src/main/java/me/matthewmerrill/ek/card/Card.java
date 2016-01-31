package me.matthewmerrill.ek.card;

import java.util.HashMap;

import me.matthewmerrill.ek.Lobby;
import me.matthewmerrill.ek.Player;

public abstract class Card extends HashMap<String, Object> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String ID = "id";
	public static final String IMAGE_URL = "imageUrl";
	
	public Card(String id, String imageUrl) {
		put(ID, id);
		put(IMAGE_URL, imageUrl);
	}
	
	@Override
	public String toString() {
		return get(ID).toString();
	}
	
	public abstract void played(Lobby lobby, Deck deck, Player player);
	
	public void pickedUp(Lobby lobby, Deck deck, Player player) {
		player.giveCard(this);
	}
	
	
}
