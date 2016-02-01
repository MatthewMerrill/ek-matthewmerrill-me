package me.matthewmerrill.ek;

import java.util.HashMap;

public class LobbyManager extends HashMap<String, Lobby>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void add(Lobby lobby) {
		put(lobby.getId(), lobby);
	}
	
}
