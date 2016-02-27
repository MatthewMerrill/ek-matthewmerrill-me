package me.matthewmerrill.ek.card;

import me.matthewmerrill.ek.Lobby;
import me.matthewmerrill.ek.LobbyState;
import me.matthewmerrill.ek.LobbyState.AttackTurn;
import me.matthewmerrill.ek.websocket.Chat;
import me.matthewmerrill.ek.Player;

public class FavorCard extends Card {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4149702421535310268L;

	public FavorCard() {
		super("favor", "favor", "Favor");
		put(DESCRIPTION, "Ends Your Turn, Next Player Plays Double!");
	}


	public static Card[] startingCards() {
		return new Card[] {
		/*	new Favor(),
			new Favor(),
			new Favor(),
			new Favor()
		*/};
	}
	
	@Override
	public void played(Lobby lobby, Deck deck, Player player) {
/*		player.getDeck().remove(this);
		Chat.broadcastMessage("Server", player.getName() +" played " + get(Card.NAME) + "!", lobby);
		LobbyState nextState = lobby.getState().next();
		lobby.setState(new LobbyState.Nope(lobby, player, lobby.getState(),
				(l, p, m) -> {
					lobby.setState(new AttackTurn(lobby, nextState));
					return true;
				}, "Favor"));
	*/}
	
	@Override
	public boolean equals(Object obj) {
		return this.get(ID).equals(((Card)obj).get(ID));
	}

}
