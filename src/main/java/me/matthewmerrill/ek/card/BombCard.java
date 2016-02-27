package me.matthewmerrill.ek.card;

import me.matthewmerrill.ek.Lobby;
import me.matthewmerrill.ek.LobbyState;
import me.matthewmerrill.ek.Player;
import me.matthewmerrill.ek.websocket.Chat;
import me.matthewmerrill.ek.websocket.SoundManager;

public class BombCard extends Card {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public enum BombType {
		
		DEFAULT("");
		
		final String suffix;
		BombType(String suffix) {
			this.suffix = suffix;
		}
		
		public String suffix() {
			return suffix;
		}
		
		@Override
		public String toString() {
			return "Bomb" + suffix();
		}
	}
	
	public BombCard(BombType type) {
		super("bomb",  "bomb" + type.suffix(), "Bomb");
		put(DESCRIPTION, "Defuse, Otherwise You're out.");
	}
	
	
	public static Card[] startingCards() {
		return new Card[] {
			new BombCard(BombType.DEFAULT),
			new BombCard(BombType.DEFAULT),
			new BombCard(BombType.DEFAULT),
			new BombCard(BombType.DEFAULT),	
		};
	}


	@Override
	public void played(Lobby lobby, Deck deck, Player player) {
		
	}
	
	@Override
	public void pickedUp(Lobby lobby, Deck deck, Player player) {

		Chat.broadcastMessage("Server", player.getName() + " picked up a bomb!", lobby, SoundManager.NO_SOUND);
		
		lobby.setState(new LobbyState.Bomb(lobby, (LobbyState.Turn)lobby.getState().next(), player));
	}
	
}
