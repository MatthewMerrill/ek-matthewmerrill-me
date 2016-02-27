package me.matthewmerrill.ek.websocket;
import static j2html.TagCreator.article;
import static j2html.TagCreator.b;
import static j2html.TagCreator.p;
import static j2html.TagCreator.span;
import static spark.Spark.init;
import static spark.Spark.staticFileLocation;
import static spark.Spark.webSocket;

import java.net.HttpCookie;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jetty.websocket.api.Session;
import org.json.JSONObject;

import me.matthewmerrill.ek.Lobby;
import me.matthewmerrill.ek.Player;
import me.matthewmerrill.ek.UserData;

public class Chat {

    static Map<String, Session> ssidMap = new HashMap<>();
    static int nextUserNumber = 1; //Used for creating the next username

    public static void main(String[] args) {
        staticFileLocation("/public"); //index.html is served at localhost:4567 (default port)
        webSocket("/chat", ChatWebSocketHandler.class);
        init();
    }
  
    //Sends a message from one user to all users in a current lobby, along with a list of current usernames
    public static void broadcastMessage(String sender, String message, Lobby lobby) {
    	broadcastMessage(sender, message, lobby, SoundManager.CHAT_MSG);
    }

	public static void broadcastMessage(String sender, String message, Lobby lobby, boolean chatSound) {
		broadcastMessage(sender, message, lobby, null);
	}
	public static void broadcastMessage(String sender, String message, Lobby lobby, String chatSound) {
    	List<String> userlist = new ArrayList<String>();
    	lobby.getPlayers().forEach(
    			(player) -> userlist.add((String) player.get(Player.NAME)));
    	
    	lobby.getPlayers().forEach((player) -> {
    		String ssid = player.get(Player.SESSION_ID).toString();
    		Session session = ssidMap.get(ssid);
    		
    		if (session != null && session.isOpen()) {
	            sendMessage(sender, message, ssid, chatSound);
    		} else {
    			//System.out.println(player);
    		}
    	});
	}
    /*
  //Sends a message from one user to all users, along with a list of current usernames
    public static void broadcastMessage(String sender, String message) {
        ssidMap.values().stream().filter(Session::isOpen).forEach(session -> {
        	
        	List<String> userlist = new ArrayList<String>();
        	UserData.getData().getLobby().getPlayers().forEach((p) -> userlist.add((String) p.get(Player.NAME)));
        	
            try {
                session.getRemote().sendString(String.valueOf(new JSONObject()
                	.put("key", "chatMsg")
                    .put("userMessage", createHtmlMessageFromSender(sender, message))
                    .put("userlist",  userlist)
                ));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }*/

    public static void sendMessage(String sender, String message, String ssidTarget) {
    	Chat.sendMessage(sender, message, ssidTarget, SoundManager.CHAT_MSG);
    }
    
    public static void sendMessage(String sender, String message, String ssidTarget, String chatSound) {
    	
    	try {
        	List<String> userlist = new ArrayList<String>();
        	UserData.getData(ssidTarget).getLobby().getPlayers().forEach((p) -> userlist.add((String) p.get(Player.NAME)));
	        
	        ssidMap.get(ssidTarget).getRemote().sendString(String.valueOf(new JSONObject()
	        	.put("key", "chatMsg")
	            .put("userMessage", createHtmlMessageFromSender(sender, message))
	            .put("userlist",  userlist)));
	            
	        if (chatSound != null)
	        	SoundManager.playSound(chatSound, ssidTarget);
    	} catch  (Exception e) {
    		e.printStackTrace();
    	}
    }
    
    public static void sendSandbox(String html, String section, String ssid) {
		try {
			ssidMap.get(ssid).getRemote().sendString(String.valueOf(new JSONObject()
					.put("key", "updateSandbox")
					.put(section + "Html", html)));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

    public static void sendPrompt(String ssid, String lobbyId, String message, String header) {
		try {
			ssidMap.get(ssid).getRemote().sendString(String.valueOf(new JSONObject()
					.put("key", "prompt")
					.put("message", message)
					.put("lobbyId", lobbyId)
					.put("header", header)));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    public static void kickPlayer(Player player) {
    	try {
    		String ssid = player.get(Player.SESSION_ID).toString();
    		ssidMap.get(ssid).getRemote().sendString(String.valueOf(new JSONObject()
    				.put("key", "kicked")));
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }
    
    //Builds a HTML element with a sender-name, a message, and a timestamp,
    private static String createHtmlMessageFromSender(String sender, String message) {
        return article().with(
                b(sender + " says:"),
                p(message),
                span().withClass("timestamp").withText(new SimpleDateFormat("HH:mm:ss").format(new Date()))
        ).render();
    }
    
    public static String getSSID(Session session) {
		HttpCookie cookie = session.getUpgradeRequest().getCookies().stream().filter(c -> c.getName().equals("ssid"))
				.findFirst().get();
		
		return cookie.getValue();
    }
    
}