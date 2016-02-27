package me.matthewmerrill.ek.card.admin;

import me.matthewmerrill.ek.Lobby;
import me.matthewmerrill.ek.Player;
import me.matthewmerrill.ek.card.Card;
import me.matthewmerrill.ek.card.Deck;

public class LeaveCard extends Card {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5660836405532350239L;
	
	public LeaveCard() {
		super("leave", "leave", "Leave");
		put(DESCRIPTION, "Sit out this game");
	}
	
	@Override
	public void played(Lobby lobby, Deck deck, Player player) {
		if (lobby.getPlaying().contains(player)) {
			lobby.killedPlayer(player);	
		}
	}

}
