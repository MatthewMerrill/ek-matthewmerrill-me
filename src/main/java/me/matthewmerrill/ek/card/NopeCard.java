package me.matthewmerrill.ek.card;

import me.matthewmerrill.ek.Lobby;
import me.matthewmerrill.ek.Player;

public class NopeCard extends Card {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public NopeCard() {
		super("nope", "nope", "Nope");
		
		put(DESCRIPTION, "Used After Drawing Bomb");
	}
	
	
	public static Card[] startingCards() {
		return new Card[] {
			new NopeCard(),
			new NopeCard(),
			new NopeCard(),
			new NopeCard(),
			new NopeCard()
		};
	}


	@Override
	public void played(Lobby lobby, Deck deck, Player player) {
		//Iterator<Card> itr = player.getDeck().iterator();
		//while (itr.hasNext()) {
		//	Card c = itr.next();
		//	if (c.get(Card.ID).equals("nope")) {
		//		LobbyState next = lobby.getState().next();
		//		lobby.setState(new LobbyState.Defusing(lobby, player, next));
		//		itr.remove();
		//		continue;
		//	}
		//}
	}
	

}
