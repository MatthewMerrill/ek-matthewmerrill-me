package me.matthewmerrill.ek.card;

import me.matthewmerrill.ek.Lobby;
import me.matthewmerrill.ek.Player;

public class BombCard extends Card {

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
	
	public BombCard(BombType type) {
		super("bomb" + type.suffix());
	}
	
	
	public static Card[] startingCards() {
		return new Card[] {
			new BombCard(BombType.DEFAULT),
			new BombCard(BombType.DEFAULT),
			new BombCard(BombType.DEFAULT),
			new BombCard(BombType.DEFAULT),	
		};
	}


	@Override
	public void played(Lobby lobby, Deck deck, Player player) {
		player.killed = true;
	}
	
	@Override
	public void pickedUp(Lobby lobby, Deck deck, Player player) {
		player.killed = true;
	}
	
}
