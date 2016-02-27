package me.matthewmerrill.ek.card;

import java.util.Iterator;

import me.matthewmerrill.ek.Lobby;
import me.matthewmerrill.ek.LobbyState;
import me.matthewmerrill.ek.Player;
import me.matthewmerrill.ek.websocket.Chat;

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
		Iterator<Card> itr = player.getDeck().iterator();
		while (itr.hasNext()) {
			Card c = itr.next();
			if (c.equals(this)) {
				itr.remove();
				Chat.broadcastMessage("Server", player.getName() +" played " + get(Card.NAME) + "!", lobby);
				LobbyState next = lobby.getState().next();
				lobby.setState(new LobbyState.Defusing(lobby, player, next));
				break;
			}
		}
	}
	

}
