package me.matthewmerrill.ek.card.admin;

import me.matthewmerrill.ek.Lobby;
import me.matthewmerrill.ek.Player;
import me.matthewmerrill.ek.card.Card;
import me.matthewmerrill.ek.card.Deck;
import me.matthewmerrill.ek.websocket.Chat;

public class StopCard extends Card {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public StopCard() {
		super("stop", "stop", "Stop");
		put(DESCRIPTION, "Stop the Game");
	}


	@Override
	public void played(Lobby lobby, Deck deck, Player player) {
		if (lobby.getAdmin().get(Player.SESSION_ID).equals(player.get(Player.SESSION_ID))){
			Chat.broadcastMessage("Server", player.get(Player.NAME) + " stopped the game.", lobby);
			lobby.stop();
		}
	}
	

}
