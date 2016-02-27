package me.matthewmerrill.ek.card.admin;

import me.matthewmerrill.ek.Lobby;
import me.matthewmerrill.ek.LobbyState;
import me.matthewmerrill.ek.Player;
import me.matthewmerrill.ek.card.Card;
import me.matthewmerrill.ek.card.Deck;
import me.matthewmerrill.ek.websocket.Chat;
import me.matthewmerrill.ek.websocket.ChatWebSocketHandler;

public class StartCard extends Card {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public StartCard() {
		super("start", "start", "Start");
		put(DESCRIPTION, "Start/Restart the Game");
	}


	@Override
	public void played(Lobby lobby, Deck deck, Player player) {
		if (lobby.getAdmin().get(Player.SESSION_ID).equals(player.get(Player.SESSION_ID))) {
			if (lobby.getPlaying().size() >= 2) {
				Chat.broadcastMessage("Server", player.get(Player.NAME) + " started the game.", lobby);
				lobby.deal();
				lobby.setState(new LobbyState.Turn(lobby));
			} else {
				Chat.broadcastMessage("Server", "A minimum of 2 players is required to play.", lobby);
			}
		}
	}
	

}
