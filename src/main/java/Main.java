import static spark.Spark.get;
import static spark.Spark.init;
import static spark.Spark.port;
import static spark.Spark.staticFileLocation;
import static spark.Spark.webSocket;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.heroku.sdk.jdbc.DatabaseUrl;

import me.matthewmerrill.ek.Lobby;
import me.matthewmerrill.ek.LobbyManager;
import me.matthewmerrill.ek.Player;
import spark.ModelAndView;
import spark.template.freemarker.FreeMarkerEngine;


public class Main {

	public static LobbyManager lm = new LobbyManager();
	
	public static void main(String[] args) {
		
		port(Integer.valueOf(System.getenv("PORT")));

        staticFileLocation("/public"); //index.html is served at localhost:4567 (default port)
		webSocket("/chat", ChatWebSocketHandler.class);
        init();
        
		get("/hello", (req, res) -> "Hello World");

		get("/chat", (request, response) -> {
			Map<String, Object> attributes = new HashMap<>();
			attributes.put("message", "Hello World!");

			return new ModelAndView(attributes, "chattest.ftl");
		} , new FreeMarkerEngine());
		
		get("/play", (req, res) -> {
			Map<String, Object> attributes = new HashMap<>();
			attributes.put("message", "Hello World!");
			
			attributes.put("lobbies", lm);

			return new ModelAndView(attributes, "lobbyBrowse.ftl");
		} , new FreeMarkerEngine());
		
		get("/play/*", (req, res) -> {
			Connection connection = null;
			Map<String, Object> attributes = new HashMap<>();
			try {
				
				return new ModelAndView(attributes, "db.ftl");
			} catch (Exception e) {
				attributes.put("message", "There was an error: " + e);
				return new ModelAndView(attributes, "error.ftl");
			} finally {
				if (connection != null)
					try {
						connection.close();
					} catch (SQLException e) {
					}
			}
		} , new FreeMarkerEngine());
        
        Lobby lobby = new Lobby("AAA");
        lobby.getPlayers().add(new Player("PLAYER1"));
        lobby.getPlayers().add(new Player("PLAYER2"));
        lobby.getPlayers().add(new Player("PLAYER3"));
        lobby.deal();
        
        lm.add(lobby);
        lm.add(lobby);
        lm.add(lobby);
        lm.add(lobby);
        lm.add(lobby);
        lm.add(lobby);
        lm.add(lobby);
//*/
	}

}
