package me.matthewmerrill.ek.websocket.prompt;

import me.matthewmerrill.ek.Lobby;
import me.matthewmerrill.ek.Player;
import me.matthewmerrill.ek.websocket.LobbyCallback;
import me.matthewmerrill.ek.websocket.UserPrompt;

public class JoinGamePrompt extends UserPrompt {
	
	public JoinGamePrompt(Lobby lobby, Player player, LobbyCallback callback) {
		super(lobby, player, (Lobby l, Player p, String s) -> {
			
			l.getPlayers().add(p);
			
			return false;
		}, "joingame");
	}

	@Override
	public boolean validResponse(String response) {
		return !lobby.getPlayers().contains(player);
	}
	
}
