package me.matthewmerrill.ek;

import com.google.common.eventbus.Subscribe;

import me.matthewmerrill.ek.event.PlayerJoinLobbyEvent;

public class LobbyListener {
	
	private final Lobby lobby;
	
	public LobbyListener(Lobby lobby) {
		this.lobby = lobby;
	}

	@Subscribe
	public void cardDrawEvent(PlayerJoinLobbyEvent event) {
		System.out.println("Received Event!");
		event.setMessage(event.getMessage() + " :)");
	}

}
