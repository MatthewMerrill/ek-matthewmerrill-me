package me.matthewmerrill.ek.card;

import me.matthewmerrill.ek.Lobby;
import me.matthewmerrill.ek.LobbyState;
import me.matthewmerrill.ek.LobbyState.AttackTurn;
import me.matthewmerrill.ek.Player;

public class AttackCard extends Card {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4149702421535310268L;

	public AttackCard() {
		super("attack", "attack", "Attack");
	}


	public static Card[] startingCards() {
		return new Card[] {
			new AttackCard(),
			new AttackCard(),
			new AttackCard(),
			new AttackCard()
		};
	}
	
	@Override
	public void played(Lobby lobby, Deck deck, Player player) {
		lobby.setState(new LobbyState.Nope(lobby, player, lobby.getState(), new AttackTurn(lobby, lobby.getState().next()), "Attack"));
		player.getDeck().remove(this);
	}
	
	@Override
	public boolean equals(Object obj) {
		return this.get(ID).equals(((Card)obj).get(ID));
	}

}
