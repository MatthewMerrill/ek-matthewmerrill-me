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
	
	public CollectCard(CollectType type) {
		super("collect" + type.suffix, "collect" + type.suffix(), type.suffix);
		put(DESCRIPTION, "Use Two of a Kind to Steal a Card");
	}
	
	
	public static Card[] startingCards() {
		return new Card[] {
			new CollectCard(CollectType.A),
			new CollectCard(CollectType.B),
			new CollectCard(CollectType.C),
			new CollectCard(CollectType.D),
			
			new CollectCard(CollectType.A),
			new CollectCard(CollectType.B),
			new CollectCard(CollectType.C),
			new CollectCard(CollectType.D),
			
			new CollectCard(CollectType.A),
			new CollectCard(CollectType.B),
			new CollectCard(CollectType.C),
			new CollectCard(CollectType.D),
			
			new CollectCard(CollectType.A),
			new CollectCard(CollectType.B),
			new CollectCard(CollectType.C),
			new CollectCard(CollectType.D),
			
			new CollectCard(CollectType.A),
			new CollectCard(CollectType.B),
			new CollectCard(CollectType.C),
			new CollectCard(CollectType.D),
		};
	}


	@Override
	public void played(Lobby lobby, Deck deck, Player player) {
		//lobby.nextTurn();
	}
	

}
