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
		
		@Override
		public String toString() {
			return "Bomb" + suffix();
		}
	}
	
	public BombCard(BombType type, String id) {
		super(id,  "bomb" + type.suffix(), "Bomb");
	}
	
	
	public static Card[] startingCards() {
		return new Card[] {
			new BombCard(BombType.DEFAULT, "BOMB0"),
			new BombCard(BombType.DEFAULT, "BOMB1"),
			new BombCard(BombType.DEFAULT, "BOMB2"),
			new BombCard(BombType.DEFAULT, "BOMB3"),	
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
