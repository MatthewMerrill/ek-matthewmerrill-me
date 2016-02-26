package me.matthewmerrill.ek.websocket;

import java.net.HttpCookie;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import me.matthewmerrill.ek.Lobby;
import me.matthewmerrill.ek.LobbyState;
import me.matthewmerrill.ek.Main;
import me.matthewmerrill.ek.Player;
import me.matthewmerrill.ek.QueryMap;
import me.matthewmerrill.ek.UserData;
import me.matthewmerrill.ek.card.Card;
import me.matthewmerrill.ek.card.Deck;
import me.matthewmerrill.ek.card.DrawCard;
import me.matthewmerrill.ek.html.GameRender;
import spark.ModelAndView;
import spark.template.freemarker.FreeMarkerEngine;

@WebSocket
public class ChatWebSocketHandler {

	private String sender, msg;

	@OnWebSocketConnect
	public void onConnect(Session user) throws Exception {

		String ssid;
		String username = "";
		Lobby lobby = null;
		
		try {
	
			System.out.println(user.getUpgradeRequest().getCookies());
			
			HttpCookie cookie = user.getUpgradeRequest().getCookies().stream().filter(c -> c.getName().equals("ssid"))
					.findFirst().get();
			
			ssid = cookie.getValue();
			
			System.out.println(ssid);
			
			username = UserData.getData(ssid).username;
			
			System.out.print(user.getUpgradeRequest().getQueryString());
			QueryMap qmap = new QueryMap(user.getUpgradeRequest().getQueryString());
			lobby = Main.lm.get(qmap.get("id"));
			
			UserData data = UserData.getData(username);
			
			if (data == null)
				data =UserData.addUser(username, ssid);
			
			data.setLobby(lobby);
			lobby.addPlayer(new Player(username, ssid));
			
		} catch (Exception e) {
			
			e.printStackTrace();
			
			username = "User" + Chat.nextUserNumber++;
			user.getUpgradeRequest().getCookies().add(new HttpCookie("pname", username));
			
			System.out.println("Error");
			return;
		}
		
		Chat.ssidMap.put(ssid, user);

		Chat.broadcastMessage(sender = "Server", msg = (username + " joined the chat"), lobby);
		
		// Fill in Sandbox
		UserData udata = UserData.getData(ssid);
		updateLobby(udata.getLobby());

		user.setIdleTimeout(TimeUnit.HOURS.toMillis(10));
	}
	
	public static void updateLobby(Lobby lobby) {
		lobby.getPlayers().forEach(player -> {
			
			String ssid = player.get(Player.SESSION_ID).toString();
			
			String center = "Hello World!";
			String north = "<h2>Playing as: " + player.get(Player.NAME) + "</h2>";
			String south = "";
			String east = "";
			String west = GameRender.render(player.getDeck(), lobby, player, "Your Cards:");
			
			// NORTH
			{
				//if (!lobby.getState().get(LobbyState.NAME).epped")) {
					north = "<h2>" + lobby.getState().get(LobbyState.NAME) + "</h2>";
				//}
			}
			
			// CENTER
			{
				/*Map<String, Object> attributes = new HashMap<String, Object>();
				attributes.put("card", new DrawCard());
				
				FreeMarkerEngine engine = new FreeMarkerEngine();
				ModelAndView centMV = engine.modelAndView(attributes, "card.ftl");
				center = engine.render(centMV);
				*/
				DrawCard card = new DrawCard();
				center = GameRender.cardTag(card, 1, lobby.getState().isActive(card, player)).render();
			}
			
			// EAST
			{
				try {
					if (ssid.equals(lobby.getAdmin().get(Player.SESSION_ID))) {
						east = GameRender.render(Deck.ADMIN_DECK, null, null, "Admin Tools:");
					}
				} catch (Exception ignored) {}
			}
			
			
			Chat.sendSandbox(center, "center", ssid);
			Chat.sendSandbox(north, "north", ssid);
			//Chat.sendSandbox(south, "south", ssid);
			Chat.sendSandbox(east, "east", ssid);
			Chat.sendSandbox(west, "west", ssid);
		});
	}

	@OnWebSocketClose
	public void onClose(Session user, int statusCode, String reason) {
		String ssid = Chat.getSSID(user);
		
		if (Chat.ssidMap.get(ssid).equals(user))
			Chat.ssidMap.remove(ssid);

		try {
			Lobby lobby = UserData.getData(ssid).getLobby();
			
			lobby.getPlayers().removeIf(
					(player) -> player.get(Player.SESSION_ID).equals(ssid));
			
			UserData.getData(ssid).setLobby(null);
			
			QueryMap qmap = new QueryMap(user.getUpgradeRequest().getQueryString());
			lobby = Main.lm.get(qmap.get("id"));
			
			Chat.broadcastMessage(sender = "Server", msg = (UserData.getUsername(ssid) + " left the chat"), lobby);
		} catch (Exception ignored) {}
	}

	@OnWebSocketMessage
	public void onMessage(Session user, String message) {

		System.out.println("Received: " + message);

		if (message == null) {
			System.out.println("Null Message!");
			return;
		}
		
		if (message.startsWith("chat.message:")) {
			
			String ssid = Chat.getSSID(user);

			Chat.broadcastMessage(sender = UserData.getUsername(ssid),
					msg = message.substring(message.indexOf(':') + 1), UserData.getData(ssid).getLobby());

			System.out.println("Broadcasted: " + msg);

			return;
		} else if (message.startsWith("card.played:")) {
			String id = message.substring(message.indexOf(':') + 1);
			System.out.println("Playing Card:" + id);
			
			String ssid = Chat.getSSID(user);
			Lobby lobby = UserData.getData(ssid).getLobby();
			Deck deck = lobby.getDrawDeck();
			Player player = lobby.getPlayer(ssid);
			
			System.out.println(id + lobby + deck + player);
			
			Card.played(lobby, deck, player, id);
		} else if (message.startsWith("prompt:")) {
			System.err.println("Processing1 " + message);
			String header = message.substring(message.indexOf(':') + 1, message.lastIndexOf(':'));
			PromptCallbackManager.receivedAnswer(Chat.getSSID(user), message.substring(message.lastIndexOf(':') + 1));
		} else {
			System.out.println("Unknown Message Type!");
		}

	}

}