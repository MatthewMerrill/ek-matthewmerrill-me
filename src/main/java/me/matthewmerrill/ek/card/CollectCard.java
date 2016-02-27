package me.matthewmerrill.ek.card;

import me.matthewmerrill.ek.Lobby;
import me.matthewmerrill.ek.LobbyState;
import me.matthewmerrill.ek.Player;
import me.matthewmerrill.ek.LobbyState.AttackTurn;
import me.matthewmerrill.ek.websocket.Chat;

public class CollectCard extends Card {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	enum CollectType {
		
		A("Alpha"),
		B("Beta"),
		C("Charlie"),
		D("Delta");
		
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
	/*		new CollectCard(CollectType.A),
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
		*/};
	}


	@Override
	public void played(Lobby lobby, Deck deck, Player player) {
	/*	player.getDeck().remove(this);
		Chat.broadcastMessage("Server", player.getName() +" played " + get(Card.NAME) + "!", lobby);
		LobbyState nextState = lobby.getState().next();
		lobby.setState(new LobbyState.Nope(lobby, player, lobby.getState(),
				(l, p, m) -> {
					//lobby.setState(new CollectTurn(lobby, nextState));
					return true;
				}, "Attack"));
	*/}
	

}
