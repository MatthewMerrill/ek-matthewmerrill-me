package me.matthewmerrill.ek.websocket;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class PromptCallbackManager {
	
	// Map SessionUUID -> Callback
	private static Map<String, UserPrompt> callbackMap = new HashMap<>();
	private static Map<String, Timer> timerMap = new HashMap<>();
	private static Map<String, Timer> warnTimerMap = new HashMap<>();
	
	public static void promptSent(UserPrompt prompt) {
		String ssid = prompt.player.getSsid();
		clearTimer(ssid);
		callbackMap.put(ssid, prompt);
	}
	
	public static void promptSent(UserPrompt prompt, long timerDelay, String def) {
		final String ssid = prompt.player.getSsid();
		clearTimer(ssid);
		callbackMap.put(prompt.player.getSsid(), prompt);
		
		Timer timer = new Timer();
		timerMap.put(ssid, timer);
		timer.schedule(new TimerTask(){
			@Override
			public void run() {
				System.out.println("Default time!");
				receivedAnswer(ssid, def);
				Chat.sendSandbox("", "south", ssid);
				Chat.sendMessage("Server", String.format("Prompt defaulted to '%s'.", def), ssid);
			}
		}, timerDelay);
		
		if (timerDelay > 15000L) {
			Timer warnTimer = new Timer();
			warnTimerMap.put(ssid, warnTimer);
			timer.schedule(new TimerTask(){
	
				@Override
				public void run() {
					System.out.println("Warning time!");
					Chat.sendMessage("Server", String.format("Prompt will default to '%s' in 15 seconds.", def), ssid);
				}
				
			}, timerDelay-15000L);
			
		}
	}
	
	public static void clearTimer(String ssid) {
		try {
			Timer timer = timerMap.remove(ssid);
			timer.cancel();
			timer.purge();
		} catch (Exception ignored) {}
		try {
			Timer timer = warnTimerMap.remove(ssid);
			timer.cancel();
			timer.purge();
		} catch (Exception ignored) {}
	}
	
	public static void receivedAnswer(String ssid, String response) {
		//System.out.println("Processing2 " +  + " with response " + response);
		clearTimer(ssid);
		
		UserPrompt prompt = callbackMap.get(ssid);
		
		if (prompt == null) {
			return;
		}
		
		prompt.gotResponse(response);
	}

}
