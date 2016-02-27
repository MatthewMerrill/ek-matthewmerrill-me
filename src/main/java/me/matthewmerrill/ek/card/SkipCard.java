package me.matthewmerrill.ek.card;

import me.matthewmerrill.ek.Lobby;
import me.matthewmerrill.ek.LobbyState;
import me.matthewmerrill.ek.Player;
import me.matthewmerrill.ek.LobbyState.AttackTurn;
import me.matthewmerrill.ek.websocket.Chat;

public class SkipCard extends Card {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	enum SkipType {
		
		DEFAULT("");
		
		final String suffix;
		SkipType(String suffix) {
			this.suffix = suffix;
		}
		
		public String suffix() {
			return suffix;
		}
		
		@Override
		public String toString() {
			return "Skip" + suffix();
		}
	}
	
	public SkipCard(SkipType type) {
		super("skip", "skip" + type.suffix(), "Skip");
		put(DESCRIPTION, "Ends Your Turn Immediately"); // SpellCheck: fImmediately
	}
	
	
	public static Card[] startingCards() {
		return new Card[] {
			new SkipCard(SkipType.DEFAULT),
			new SkipCard(SkipType.DEFAULT),
			new SkipCard(SkipType.DEFAULT),
			new SkipCard(SkipType.DEFAULT)
		};
	}


	@Override
	public void played(Lobby lobby, Deck deck, Player player) {
		player.getDeck().remove(this);
		Chat.broadcastMessage("Server", player.getName() +" played " + get(Card.NAME) + "!", lobby);
		LobbyState state = lobby.getState();
		lobby.setState(new LobbyState.Nope(lobby, player, lobby.getState(),
				(l, p, m) -> {
					lobby.setState(state.next());
					return true;
				}, "Skip"));
	}
	

}
