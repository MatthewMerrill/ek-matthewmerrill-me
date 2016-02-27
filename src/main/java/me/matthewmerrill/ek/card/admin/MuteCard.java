package me.matthewmerrill.ek.card.admin;

import me.matthewmerrill.ek.Lobby;
import me.matthewmerrill.ek.Player;
import me.matthewmerrill.ek.card.Card;
import me.matthewmerrill.ek.card.Deck;

public class MuteCard extends Card {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public MuteCard() {
		super("mute", "mute", "Mute");
		put(DESCRIPTION, "Toggle Game Sounds Off");
	}


	public boolean toggle = true;
	
	@Override
	public void played(Lobby lobby, Deck deck, Player player) {	}
	

}
