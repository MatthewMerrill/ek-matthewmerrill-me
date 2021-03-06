package me.matthewmerrill.ek.card;

import java.util.HashMap;
import java.util.Map;

import me.matthewmerrill.ek.Lobby;
import me.matthewmerrill.ek.Player;
import me.matthewmerrill.ek.websocket.Chat;

public abstract class Card extends HashMap<String, Object> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String NAME = "name";
	public static final String ID = "id";

	public static final String ACTIVE = "active";
	public static final String IMAGE_URL = "imageUrl";

	public static final String DESCRIPTION = "description";
	
	private static Map<String, Card> cardMap = new HashMap<>();

	public Card(String id, String imageUrl, String name, boolean active) {
		put(ID, id);
		put(IMAGE_URL, imageUrl);
		put(NAME, name);
		put(ACTIVE, active);
		
		cardMap.put(id, this);
	}
	
	public Card(String id, String imageUrl, String name) {
		put(ID, id);
		put(IMAGE_URL, imageUrl);
		put(NAME, name);
		put(ACTIVE, false);

		cardMap.put(id, this);
	}
	
	@Override
	public boolean equals(Object obj) {
		return get(ID).equals(((Card)obj).get(ID));
	}
	
	@Override
	public String toString() {
		return get(ID).toString();
	}
	
	public abstract void played(Lobby lobby, Deck deck, Player player);
	
	public void pickedUp(Lobby lobby, Deck deck, Player player) {
		player.giveCard(this);
		lobby.nextTurn();
	}
	
	public static void played(Lobby lobby, Deck deck, Player player, String cardId) {
		try {
			Card card = cardMap.get(cardId);
			card.played(lobby, deck, player);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
