package me.matthewmerrill.ek.websocket;

import java.util.HashMap;
import java.util.Map;

public class PromptCallbackManager {
	
	// Map SessionUUID -> Callback
	private static Map<String, UserPrompt> callbackMap = new HashMap<>();
	
	public static void sendPrompt(UserPrompt prompt) {
		
		callbackMap.put(prompt.getHeader(), prompt);
		
	}

}
