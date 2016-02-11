package me.matthewmerrill.ek.card;

import me.matthewmerrill.ek.Lobby;
import me.matthewmerrill.ek.Player;

public class DefuseCard extends Card {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	enum DefuseType {
		
		DEFAULT("");
		
		final String suffix;
		DefuseType(String suffix) {
			this.suffix = suffix;
		}
		
		public String suffix() {
			return suffix;
		}
		
		@Override
		public String toString() {
			return "Defuse" + suffix();
		}
	}
	
	public DefuseCard(DefuseType type, String id) {
		super(id, "defuse" + type.suffix(), "Defuse");
	}
	
	
	public static Card[] startingCards() {
		return new Card[] {
			new DefuseCard(DefuseType.DEFAULT, "DFUS0"),
			new DefuseCard(DefuseType.DEFAULT, "DFUS1"),
			new DefuseCard(DefuseType.DEFAULT, "DFUS2"),
			new DefuseCard(DefuseType.DEFAULT, "DFUS3"),	
			new DefuseCard(DefuseType.DEFAULT, "DFUS4"),
			new DefuseCard(DefuseType.DEFAULT, "DFUS5"),	
		};
	}


	@Override
	public void played(Lobby lobby, Deck deck, Player player) {
		//player.killed = false;
		// TODO: player.prompt(/*where to put bomb*/);
	}
	

}
