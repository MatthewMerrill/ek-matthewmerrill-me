package me.matthewmerrill.ek.event;

import me.matthewmerrill.ek.Lobby;
import me.matthewmerrill.ek.Player;

public class PlayerJoinLobbyEvent extends LobbyEvent {

	private String message;
	
	public PlayerJoinLobbyEvent(Lobby lobby, Player player) {
		super(lobby);
		message = player.get(Player.NAME) + " has joined the lobby.";
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	public String getMessage() {
		return message;
	}

}
