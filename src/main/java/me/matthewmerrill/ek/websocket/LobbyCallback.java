package me.matthewmerrill.ek.websocket;

import me.matthewmerrill.ek.Lobby;
import me.matthewmerrill.ek.Player;

public interface LobbyCallback {
	
	public boolean callback(Lobby lobby, Player player, String message);
	
}
