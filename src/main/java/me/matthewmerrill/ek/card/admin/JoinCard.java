package me.matthewmerrill.ek.card.admin;

import me.matthewmerrill.ek.Lobby;
import me.matthewmerrill.ek.Player;
import me.matthewmerrill.ek.card.Card;
import me.matthewmerrill.ek.card.Deck;

public class JoinCard extends Card {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5660836405532350239L;
	
	public JoinCard() {
		super("join", "join", "Join");
		put(DESCRIPTION, "Get in on the Action!");
	}
	
	@Override
	public void played(Lobby lobby, Deck deck, Player player) {
		if (!lobby.isRunning() && lobby.getPlaying().size() < 4 && !lobby.getPlaying().contains(player)) {
			lobby.addPlayer(player);	
		}
	}

}
