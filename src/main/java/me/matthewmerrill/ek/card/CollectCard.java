package me.matthewmerrill.ek.card;

import me.matthewmerrill.ek.Lobby;
import me.matthewmerrill.ek.Player;

public class CollectCard extends Card {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	enum CollectType {
		
		A("A"),
		B("B"),
		C("C"),
		D("D");
		
		final String suffix;
		CollectType(String suffix) {
			this.suffix = suffix;
		}
		
		public String suffix() {
			return suffix;
		}
		
		@Override
		public String toString() {
			return "Collect" + suffix();
		}
	}
	
	public CollectCard(CollectType type, String id) {
		super(id, "collect" + type.suffix(), id);
	}
	
	
	public static Card[] startingCards() {
		return new Card[] {
			new CollectCard(CollectType.A, "A0"),
			new CollectCard(CollectType.B, "B0"),
			new CollectCard(CollectType.C, "C0"),
			new CollectCard(CollectType.D, "D0"),
			
			new CollectCard(CollectType.A, "A1"),
			new CollectCard(CollectType.B, "B1"),
			new CollectCard(CollectType.C, "C1"),
			new CollectCard(CollectType.D, "D1"),
			
			new CollectCard(CollectType.A, "A2"),
			new CollectCard(CollectType.B, "B2"),
			new CollectCard(CollectType.C, "C2"),
			new CollectCard(CollectType.D, "D2"),
			
			new CollectCard(CollectType.A, "A3"),
			new CollectCard(CollectType.B, "B3"),
			new CollectCard(CollectType.C, "C3"),
			new CollectCard(CollectType.D, "D3"),
		};
	}


	@Override
	public void played(Lobby lobby, Deck deck, Player player) {
		lobby.nextTurn();
	}
	

}
