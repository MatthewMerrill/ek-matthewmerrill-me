package me.matthewmerrill.ek.card;

import me.matthewmerrill.ek.Lobby;
import me.matthewmerrill.ek.Player;

public class SkipCard extends Card {

	enum SkipType {
		
		DEFAULT("");
		
		final String suffix;
		SkipType(String suffix) {
			this.suffix = suffix;
		}
		
		public String suffix() {
			return suffix;
		}
	}
	
	public SkipCard(SkipType type) {
		super("skip" + type.suffix());
	}
	
	
	public static Card[] startingCards() {
		return new Card[] {
			new SkipCard(SkipType.DEFAULT),
			new SkipCard(SkipType.DEFAULT),
			new SkipCard(SkipType.DEFAULT),
			new SkipCard(SkipType.DEFAULT),	
		};
	}


	@Override
	public void played(Lobby lobby, Deck deck, Player player) {
		lobby.setPlayerIndex(lobby.getPlayerIndex() + lobby.direction);
	}
	

}
