package me.matthewmerrill.ek.card;

import me.matthewmerrill.ek.Lobby;
import me.matthewmerrill.ek.Player;
import me.matthewmerrill.ek.UserData;
import me.matthewmerrill.ek.html.GameRender;
import me.matthewmerrill.ek.websocket.Chat;

public class DrawCard extends Card {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public DrawCard() {
		super("0", "???", "Draw");
	}
	
	
	public static Card[] startingCards() {
		return new Card[0];
	}


	@Override
	public void played(Lobby lobby, Deck deck, Player player) {
		player.giveCard(deck.draw());
		
		Chat.sendSandbox(
				GameRender.render(deck),
				"west",
				player.get(Player.SESSION_ID).toString());		
	}
	

}
