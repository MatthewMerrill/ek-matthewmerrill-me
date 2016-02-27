package me.matthewmerrill.ek.websocket;

import org.json.JSONObject;

import me.matthewmerrill.ek.Lobby;

public class SoundManager {
	
	public static final String NO_SOUND = "NO_SOUND";
	
	// http://kenney.nl/assets/ui-audio
	public static final String STATE_CHANGE = "ui-sfx/switch14.wav";
	public static final String CHAT_MSG = "ui-sfx/click3.wav";
	
	// http://www.freesound.org/people/oceanictrancer/sounds/187502/
	public static final String BOMB_PICKUP = "sfx/bomb.ogg";
	
	// http://www.freesound.org/people/thearxx08/sounds/333916/
	public static final String MEOW = "sfx/meow.ogg";
	
	// http://www.freesound.org/people/fins/sounds/171671/
	public static final String WIN = "sfx/win.ogg";
	
	public static void playSound(String sound, String ssid) {
		
		if (sound == null || NO_SOUND.equals(sound))
			return;
		
        try {
			Chat.ssidMap.get(ssid).getRemote().sendString(String.valueOf(new JSONObject()
			    	.put("key", "playSound")
			        .put("sound", sound)));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void playSound(String sound, Lobby lobby) {
		lobby.getPlayers().forEach((p) -> playSound(sound, p.getSsid()));
	}

}
