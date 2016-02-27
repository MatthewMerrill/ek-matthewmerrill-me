package me.matthewmerrill.ek.card;

import me.matthewmerrill.ek.Lobby;
import me.matthewmerrill.ek.LobbyState;
import me.matthewmerrill.ek.LobbyState.Nope;
import me.matthewmerrill.ek.websocket.Chat;
import me.matthewmerrill.ek.Player;

public class ShuffleCard extends Card {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6186426905584031073L;

	public ShuffleCard() {
		super("shuffle", "shuffle", "Shuffle");
		put(DESCRIPTION, "Mix Up the Deck!");
	}
	
	public static Card[] startingCards() {
		return new Card[] {
			new ShuffleCard(),
			new ShuffleCard(),
			new ShuffleCard(),
			new ShuffleCard()
		};
	}

	@Override
	public void played(Lobby lobby, Deck deck, Player player) {
		player.getDeck().remove(this);
		Chat.broadcastMessage("Server", player.getName() +" played " + get(Card.NAME) + "!", lobby);
		LobbyState nextState = lobby.getState();
		
		lobby.setState(new Nope(lobby, player, nextState, (l, p, m) -> {
			lobby.getDrawDeck().shuffle();
			lobby.setState(nextState);
			return true;
		}, "Shuffle"));
	}

}
