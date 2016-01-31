package me.matthewmerrill.ek.card;

import me.matthewmerrill.ek.Lobby;
import me.matthewmerrill.ek.Player;

public class BombCard extends Card {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	enum BombType {
		
		DEFAULT("");
		
		final String suffix;
		BombType(String suffix) {
			this.suffix = suffix;
		}
		
		public String suffix() {
			return suffix;
		}
	}
	
	public BombCard(BombType type, String id) {
		super(id,  "bomb" + type.suffix());
	}
	
	
	public static Card[] startingCards() {
		return new Card[] {
			new BombCard(BombType.DEFAULT, "0"),
			new BombCard(BombType.DEFAULT, "1"),
			new BombCard(BombType.DEFAULT, "2"),
			new BombCard(BombType.DEFAULT, "3"),	
		};
	}


	@Override
	public void played(Lobby lobby, Deck deck, Player player) {
		//player.killed = true;
	}
	
	@Override
	public void pickedUp(Lobby lobby, Deck deck, Player player) {
		//player.killed = true;
	}
	
}
