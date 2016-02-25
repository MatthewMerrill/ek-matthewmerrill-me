package me.matthewmerrill.ek.websocket;

import java.util.HashMap;
import java.util.Map;

import me.matthewmerrill.ek.Player;

public class PromptCallbackManager {
	
	// Map SessionUUID -> Callback
	private static Map<String, UserPrompt> callbackMap = new HashMap<>();
	
	public static void sendPrompt(UserPrompt prompt) {
		
		callbackMap.put(prompt.getHeader(), prompt);
		
		Chat.sendPrompt(prompt.player.get(Player.SESSION_ID).toString(), prompt.lobby.getId(), prompt.message, prompt.header);
	}
	
	public static void receivedAnswer(String header, String response) {
		
		UserPrompt prompt = callbackMap.get(header);
		
		if (prompt == null) {
			return;
		}
		
		prompt.gotResponse(response);
	}

}
