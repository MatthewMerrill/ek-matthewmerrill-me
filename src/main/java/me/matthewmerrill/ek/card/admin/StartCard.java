package me.matthewmerrill.ek.card.admin;

import me.matthewmerrill.ek.Lobby;
import me.matthewmerrill.ek.Player;
import me.matthewmerrill.ek.card.Card;
import me.matthewmerrill.ek.card.Deck;

public class StartCard extends Card {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public StartCard() {
		super("start", "start", "Start");
	}


	@Override
	public void played(Lobby lobby, Deck deck, Player player) {
		if (lobby.getAdmin().get(Player.SESSION_ID).equals(player.get(Player.SESSION_ID)))
			lobby.deal();
	}
	

}
