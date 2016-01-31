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
	}
	
	public DefuseCard(DefuseType type, String id) {
		super(id, "defuse" + type.suffix());
	}
	
	
	public static Card[] startingCards() {
		return new Card[] {
			new DefuseCard(DefuseType.DEFAULT, "0"),
			new DefuseCard(DefuseType.DEFAULT, "1"),
			new DefuseCard(DefuseType.DEFAULT, "2"),
			new DefuseCard(DefuseType.DEFAULT, "3"),	
			new DefuseCard(DefuseType.DEFAULT, "4"),
			new DefuseCard(DefuseType.DEFAULT, "5"),	
		};
	}


	@Override
	public void played(Lobby lobby, Deck deck, Player player) {
		//player.killed = false;
		// TODO: player.prompt(/*where to put bomb*/);
	}
	

}
