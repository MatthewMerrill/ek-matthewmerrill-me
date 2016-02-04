package me.matthewmerrill.ek.websocket;

import java.net.HttpCookie;
import java.util.concurrent.TimeUnit;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.*;

import me.matthewmerrill.ek.*;

@WebSocket
public class ChatWebSocketHandler {

	private String sender, msg;

	@OnWebSocketConnect
	public void onConnect(Session user) throws Exception {

		String ssid = "";
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
			lobby.getPlayers().add(new Player(username, ssid));
			
		} catch (Exception e) {
			
			e.printStackTrace();
			
			username = "User" + Chat.nextUserNumber++;
			user.getUpgradeRequest().getCookies().add(new HttpCookie("pname", username));
			
			System.out.println("Error");
		}
		
		Chat.ssidMap.put(ssid, user);

		if (lobby != null)
			Chat.broadcastMessage(sender = "Server", msg = (username + " joined the chat"), lobby);
		else
			System.out.println("Lobby NULL!");
			
		user.setIdleTimeout(TimeUnit.HOURS.toMillis(10));
	}

	@OnWebSocketClose
	public void onClose(Session user, int statusCode, String reason) {
		String ssid = Chat.getSSID(user);
		
		Chat.ssidMap.remove(ssid);
		UserData.getData(ssid).getLobby().getPlayers().removeIf(
				(player) -> player.get(Player.SESSION_ID).equals(ssid));
		
		Chat.broadcastMessage(sender = "Server", msg = (UserData.getUsername(ssid) + " left the chat"));
	}

	@OnWebSocketMessage
	public void onMessage(Session user, String message) {

		System.out.println("Received: " + message);

		if (message.startsWith("chat.message:")) {
			
			String ssid = Chat.getSSID(user);

			Chat.broadcastMessage(sender = UserData.getUsername(ssid),
					msg = message.substring(message.indexOf(':') + 1), UserData.getData(ssid).getLobby());

			System.out.println("Broadcasted: " + msg);

			return;
		}

	}

}