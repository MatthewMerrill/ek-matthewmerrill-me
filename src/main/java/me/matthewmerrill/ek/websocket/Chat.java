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

import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import me.matthewmerrill.ek.Lobby;
import me.matthewmerrill.ek.Player;
import me.matthewmerrill.ek.UserData;
import spark.ModelAndView;
import spark.template.freemarker.FreeMarkerEngine;

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
    	List<String> userlist = new ArrayList<String>();
    	lobby.getPlayers().forEach(
    			(player) -> userlist.add((String) player.get(Player.NAME)));
    	
    	lobby.getPlayers().forEach((player) -> {
    		String ssid = player.get(Player.SESSION_ID).toString();
    		Session session = ssidMap.get(ssid);
    		
    		if (session != null && session.isOpen()) {
	            try {
	                session.getRemote().sendString(String.valueOf(new JSONObject()
	                    .put("key", "chatMsg")
	                    .put("userMessage", createHtmlMessageFromSender(sender, message))
	                    .put("userlist",  userlist)
	                ));
	            } catch (Exception e) {
	                e.printStackTrace();
	            }
    		} else {
    			System.out.println(player);
    		}
    	});
    }
    
  //Sends a message from one user to all users, along with a list of current usernames
    public static void broadcastMessage(String sender, String message) {
        ssidMap.values().stream().filter(Session::isOpen).forEach(session -> {
        	
        	List<String> userlist = new ArrayList<String>();
        	UserData.getData(sender).getLobby().getPlayers().forEach((p) -> userlist.add((String) p.get(Player.NAME)));
        	
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
    }
    
    public static void sendSandbox(String html) {
    	ssidMap.entrySet().stream().filter((entry) -> {return entry.getValue().isOpen();}).forEach((entry) -> {
		
			String ssid = entry.getKey();
			Session session = entry.getValue();
			
			UserData udata = UserData.getData(ssid);
			udata.getLobby().deal();
			udata.getLobby().getPlayers().stream().filter((Player player) -> player.get(Player.SESSION_ID).equals(ssid)).forEach(player -> {
				
				Map<String, Object> attributes = new HashMap<String, Object>();
				attributes.put("playername", player.get(Player.NAME));
				attributes.put("cards", player.getDeck());
				
				FreeMarkerEngine engine = new FreeMarkerEngine();
				
				/*
				Configuration config = new Configuration();
				config.setTemplateLoader(new ClassTemplateLoader(Chat.class, "/src/main/resources/spark/template/freemarker"));
				engine.setConfiguration(config);
				*/
				
				// System.out.println(attributes);
				
				ModelAndView mv = engine.modelAndView(attributes, "cardDeck.ftl");
    			
				try {	
        			session.getRemote().sendString(String.valueOf(new JSONObject()
        					.put("key", "updateSandbox")
        					.put("playHtml", engine.render(mv))));
        			
        		} catch (Exception e) {
        			e.printStackTrace();
        		}
			});
			
    	});
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