package me.matthewmerrill.ek;

import java.util.HashMap;
import java.util.Map;

public class LobbyManager extends HashMap<String, Lobby>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void add(Lobby lobby) {
		put(lobby.getId(), lobby);
	}

	public void purge() {
		for (Map.Entry<String, Lobby> entry : this.entrySet()) {
			if (entry.getValue().getPlayers().isEmpty())
				remove(entry.getKey());
		}
	}
	
}
