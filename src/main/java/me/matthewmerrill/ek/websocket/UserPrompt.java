package me.matthewmerrill.ek.websocket;

import me.matthewmerrill.ek.Lobby;
import me.matthewmerrill.ek.Player;

public abstract class UserPrompt {
	
	public abstract boolean validResponse(String response);
	
	protected final Lobby lobby;
	protected final Player player;
	protected final LobbyCallback callback;
	protected final String header;
	
	public UserPrompt(Lobby lobby, Player player, LobbyCallback callback, String header) {
		
		if (player == null || callback == null || header.equals(null) || header.length() == 0)
			throw new IllegalArgumentException("Constructor arguments cannot be null!");
		
		this.lobby = lobby;
		this.player = player;
		this.callback = callback;
		this.header = header;
	}
	
	public String getHeader() {
		return header;
	}
	
	public boolean gotResponse(String response) {
		return callback.callback(lobby, player, response);
	}
	
	@Override
	public int hashCode() {
		return player.get(Player.SESSION_ID).hashCode();
	}

}
