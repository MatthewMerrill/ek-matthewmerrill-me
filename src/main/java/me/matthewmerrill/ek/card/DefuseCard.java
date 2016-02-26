package me.matthewmerrill.ek.card;

import me.matthewmerrill.ek.Lobby;
import me.matthewmerrill.ek.LobbyState;
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
	
	public DefuseCard(DefuseType type) {
		super("defuse", "defuse" + type.suffix(), "Defuse");
		
		put(DESCRIPTION, "Used After Drawing Bomb");
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
		LobbyState next = lobby.getState().next();
		lobby.setState(new LobbyState.Defusing(lobby, player, next));
	}
	

}
