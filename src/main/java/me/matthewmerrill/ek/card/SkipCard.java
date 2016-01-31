package me.matthewmerrill.ek.card;

import me.matthewmerrill.ek.Lobby;
import me.matthewmerrill.ek.Player;

public class SkipCard extends Card {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

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
	
	public SkipCard(SkipType type, String id) {
		super(id, "skip" + type.suffix());
	}
	
	
	public static Card[] startingCards() {
		return new Card[] {
			new SkipCard(SkipType.DEFAULT, "0"),
			new SkipCard(SkipType.DEFAULT, "1"),
			new SkipCard(SkipType.DEFAULT, "2"),
			new SkipCard(SkipType.DEFAULT, "3"),	
		};
	}


	@Override
	public void played(Lobby lobby, Deck deck, Player player) {
		//lobby.setPlayerIndex(lobby.getPlayerIndex() + lobby.direction);
	}
	

}
