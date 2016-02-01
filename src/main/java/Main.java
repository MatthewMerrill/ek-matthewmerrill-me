import static spark.Spark.*;

import java.sql.*;
import java.util.*;

import me.matthewmerrill.ek.Lobby;
import me.matthewmerrill.ek.LobbyManager;
import me.matthewmerrill.ek.Player;
import me.matthewmerrill.ek.websocket.ChatWebSocketHandler;
import me.matthewmerrill.ek.websocket.GameWebSocketHandler;
import spark.Filter;
import spark.ModelAndView;
import spark.TemplateViewRoute;
import spark.template.freemarker.FreeMarkerEngine;

public class Main {

	public static LobbyManager lm = new LobbyManager();
	
	public static void main(String[] args) {
		
		port(Integer.valueOf(System.getenv("PORT")));

        staticFileLocation("/public"); //index.html is served at localhost:4567 (default port)
		webSocket("/chat", ChatWebSocketHandler.class);
        webSocket("/game", GameWebSocketHandler.class);
        init();

		get("/", (request, response) -> {
			Map<String, Object> attributes = new HashMap<>();
			//attributes.put("message", "Hello World!");

			return new ModelAndView(attributes, "index.ftl");
		} , new FreeMarkerEngine());
		
		get("/hello", (req, res) -> "Hello World");
		
		get("/test", (req, res) -> {
			StringJoiner sj = new StringJoiner("\n");

			req.attributes().forEach((str) -> sj.add(str + "=" + req.attribute(str)));
			req.session().attributes().forEach((str) -> sj.add(str + "=" + req.session().attribute(str)));
			
			return sj.toString();
		});

		TemplateViewRoute playRoute = (req, res) -> {
			Connection connection = null;
			Map<String, Object> attributes = new HashMap<>();
			try {
				
				String id = req.queryParams("id");
				String pw = req.queryParams("pw");
				
				System.out.println(id + pw);
				
				if (id != null) {
					
					if (lm.containsKey(id)) {
						Lobby lobby = lm.get(id);
						
						if (lobby.hasPassword()) {
							if (pw == null || !lobby.isPassword(pw)) {
								attributes.put("message", "Bad Password");
								attributes.put("lobbies", lm);
								System.out.println("A");
								return new ModelAndView(attributes, "lobbyBrowse.ftl");
							}
						}
						
						attributes.put("lobby", lobby);
						System.out.println("B");
						return new ModelAndView(attributes, "play.ftl");
					}
					
					attributes.put("message", "Bad Request");
					attributes.put("lobbies", lm);
					System.out.println("C");
					return new ModelAndView(attributes, "lobbyBrowse.ftl");
				}
				
				attributes.put("lobbies", lm);
				System.out.println("C");
				return new ModelAndView(attributes, "lobbyBrowse.ftl");
				
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
		};
		
		get("/play", playRoute, new FreeMarkerEngine());
		
		Filter loginFilter = (req, res) -> {
			if (req.session().attribute("pname") == null) {
				res.redirect("/login?redirect=" + req.pathInfo() +
						((req.queryString() == null) ? "" : ("?" + req.queryString())) );
			}
			else if (req.pathInfo().endsWith("/")) {
				res.redirect(req.pathInfo().substring(0, req.pathInfo().length()-1));
			}
		};
		
		before("/play", loginFilter);
		before("/play/", loginFilter);
		
		
		get("/login", (req, res) -> {
			Map<String, Object> attributes = new HashMap<>();
			
			String query = req.queryString();
			System.out.println(query);
			
			if (query.contains("redirect="))
				req.session().attribute("redirect", query.substring(query.indexOf("redirect=") + 10));
			
			return new ModelAndView(attributes, "login.ftl");
		}, new FreeMarkerEngine());
		
		post("/login", (req, res) -> {
			
			String body = req.body();
			String[] split = body.split("&");
			
			Arrays.stream(split).forEach((str) -> {
				String[] entry = str.split("=");
				if (entry.length == 2) {
					System.out.println(Arrays.toString(entry));
					req.session().attribute(entry[0], entry[1]);
				}
			});
			
			if (req.session().attributes().contains("redirect"))
				res.redirect(req.session().attribute("redirect"));
			
			return "Redirecting...";
		});
		
		before("/login/", (req, res) -> res.redirect("/login"));
		
        Lobby lobby = new Lobby("The Testing Lobby!");
        lobby.getPlayers().add(new Player("PLAYER1"));
        lobby.getPlayers().add(new Player("PLAYER2"));
        lobby.getPlayers().add(new Player("PLAYER3"));
        lobby.deal();
        
        Lobby lobby2 = new Lobby("Super Private Lobby!");
        lobby2.getPlayers().add(new Player("Bill 'Binary Logic' Gates"));
        lobby2.getPlayers().add(new Player("Al Gore 'Rhythm'"));
        lobby2.getPlayers().add(new Player("Barack 'To the Future' Obama"));
        lobby2.setPassword("leet");
        lobby2.deal();
        
        lm.add(lobby);
        lm.add(lobby2);
//*/
	}

}
