package me.matthewmerrill.ek.websocket;

import me.matthewmerrill.ek.Lobby;
import me.matthewmerrill.ek.Player;

public abstract class UserPrompt {
	
	public abstract boolean validResponse(String response);
	
	protected final Lobby lobby;
	protected final Player player;
	protected final LobbyCallback callback;
	protected final String header;
	protected final String message;
	
	public UserPrompt(Lobby lobby, Player player, String header, String message, LobbyCallback callback) {
		
		if (player == null || callback == null || header.equals(null) || header.length() == 0)
			throw new IllegalArgumentException("Constructor arguments cannot be null!");
		
		this.lobby = lobby;
		this.player = player;
		this.callback = callback;
		this.header = header;
		this.message = message;
	}

	public String getHeader() {
		return header;
	}
	
	public boolean gotResponse(String response, String ssid) {
		return callback.callback(lobby, lobby.getPlayer(ssid), response);
	}
	
	@Override
	public int hashCode() {
		return player.get(Player.SESSION_ID).hashCode();
	}

}
