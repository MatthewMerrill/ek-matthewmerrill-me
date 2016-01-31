package me.matthewmerrill.ek.card;

import me.matthewmerrill.ek.Lobby;
import me.matthewmerrill.ek.Player;

public class DefuseCard extends Card {

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
	
	public DefuseCard(DefuseType type) {
		super("defuse" + type.suffix());
	}
	
	
	public static Card[] startingCards() {
		return new Card[] {
			new DefuseCard(DefuseType.DEFAULT),
			new DefuseCard(DefuseType.DEFAULT),
			new DefuseCard(DefuseType.DEFAULT),
			new DefuseCard(DefuseType.DEFAULT),	
			new DefuseCard(DefuseType.DEFAULT),
			new DefuseCard(DefuseType.DEFAULT),	
		};
	}


	@Override
	public void played(Lobby lobby, Deck deck, Player player) {
		player.killed = false;
		// TODO: player.prompt(/*where to put bomb*/);
	}
	

}
