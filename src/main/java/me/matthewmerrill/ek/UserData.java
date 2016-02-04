package me.matthewmerrill.ek;

import java.util.*;

public class UserData {
	
	// SessionId -> Userdata
	private static Map<String, UserData> dataMap = new HashMap<>();
	
	// SessionId -> Username
	//private static Map<String, String> usernameMap = new HashMap<>();
	
	
	public static Set<String> getUsernames() {
		return new HashSet<String>(dataMap.keySet());
	}
	
	public static Set<String> getSessionIds() {
		return new HashSet<String>(dataMap.keySet());
	}
	
	public static UserData getData(String sessionId) {
		return dataMap.get(sessionId);
	}

	public static String getUsername(String sessionId) {
		return getData(sessionId).username;
	}
	
	
	public final String username;
	public final String sessionId;
	
	private Lobby lobby = null;
	
	
	private UserData(String username, String sessionId) {
		this.username = username;
		this.sessionId = sessionId;
	}
	
	public UserData(String username, String sessionId, Lobby lobby) {
		this.username = username;
		this.sessionId = sessionId;
		this.lobby = lobby;
	}
	
	public Lobby getLobby() {
		return lobby;
	}
	public void setLobby(Lobby lobby) {
		this.lobby = lobby;
	}

	public static UserData addUser(String uname, String ssid) {
		UserData udata = new UserData(uname, ssid);
		
		dataMap.put(ssid, udata);
		
		System.out.println(dataMap);
		
		return udata;
	}

}
