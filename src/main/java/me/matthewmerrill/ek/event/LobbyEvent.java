package me.matthewmerrill.ek.event;

import me.matthewmerrill.ek.Lobby;

public class LobbyEvent {
	
	private final Lobby lobby;
	
	public LobbyEvent(Lobby lobby) {
		this.lobby = lobby;
	}
	
	public Lobby getLobby() {
		return lobby;
	}
	
}
