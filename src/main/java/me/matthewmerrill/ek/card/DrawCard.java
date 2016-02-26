package me.matthewmerrill.ek.card;

import me.matthewmerrill.ek.Lobby;
import me.matthewmerrill.ek.Player;
import me.matthewmerrill.ek.html.GameRender;
import me.matthewmerrill.ek.websocket.Chat;

public class DrawCard extends Card {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public DrawCard() {
		super("draw", "draw", "Draw", true);
		put(DESCRIPTION, "Pick Up a Card");
	}
	
	
	public static Card[] startingCards() {
		return new Card[0];
	}


	@Override
	public void played(Lobby lobby, Deck deck, Player player) {
		Card drawn = deck.draw();
		
		drawn.pickedUp(lobby, deck, player);
		
		Chat.sendSandbox(
				GameRender.render(player.getDeck(), lobby, player, "Your Cards:"),
				"west",
				player.get(Player.SESSION_ID).toString());		
				
		//ChatWebSocketHandler.updateLobby(lobby);
	}
	

}
