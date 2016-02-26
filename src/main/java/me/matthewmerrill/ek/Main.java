package me.matthewmerrill.ek;
import static spark.Spark.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

import me.matthewmerrill.ek.*;
import me.matthewmerrill.ek.websocket.ChatWebSocketHandler;
import spark.Filter;
import spark.ModelAndView;
import spark.TemplateViewRoute;
import spark.template.freemarker.FreeMarkerEngine;

public class Main {

	public static LobbyManager lm = new LobbyManager();
	
	public static void main(String[] args) {

        Lobby testLobby = new Lobby("The Testing Lobby!");
       // testLobby.getPlayers().add(new Player("PLAYER1", "a"));
       // testLobby.getPlayers().add(new Player("PLAYER2", "b"));
       // testLobby.getPlayers().add(new Player("PLAYER3", "c"));
       // testLobby.deal();
        
        Lobby lobby2 = new Lobby("Super Private Lobby!");
        lobby2.getPlayers().add(new Player("Bill 'Binary Logic' Gates", "d"));
        lobby2.getPlayers().add(new Player("Al Gore 'Rhythm'", "e"));
        lobby2.getPlayers().add(new Player("Barack 'To the Future' Obama", "f"));
        lobby2.setPassword("leet");
        lobby2.deal();
        
		port(Integer.valueOf(System.getenv("PORT")));

        staticFileLocation("/public"); //index.html is served at localhost:4567 (default port)
		webSocket("/chat", ChatWebSocketHandler.class);
        init();

		get("/", (request, response) -> {
			Map<String, Object> attributes = new HashMap<>();
			//attributes.put("message", "Hello World!");

			return new ModelAndView(attributes, "index.ftl");
		} , new FreeMarkerEngine());
		
		get("/hello", (req, res) -> "Hello World");
		
		get("/testdeck", (request, response) -> {
			Map<String, Object> attributes = new HashMap<>();
			
			attributes.put("playername", "TestDeck!");
			attributes.put("cards", testLobby.get(Lobby.DRAW_DECK));
			
			return new ModelAndView(attributes, "cardDeck.ftl");
		} , new FreeMarkerEngine());
		
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
								return new ModelAndView(attributes, "lobbyBrowse.ftl");
							}
						}
						
						attributes.put("lobby", lobby);
						return new ModelAndView(attributes, "play.ftl");
					}
					
					attributes.put("message", "Bad Request");
					attributes.put("lobbies", lm);
					return new ModelAndView(attributes, "lobbyBrowse.ftl");
				}
				
				String msg = req.queryParams("msg");
				if (msg != null && msg.length() > 0)
					attributes.put("message", msg);
						
				attributes.put("lobbies", lm);
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
				
				if (req.cookies().containsKey("pname") && req.cookies().containsKey("ssid") ) {
					req.session().attribute("pname", req.cookies().get("pname"));
					req.session().attribute("ssid", req.cookies().get("ssid"));
					
					UserData.addUser(req.session().attribute("pname"), req.session().attribute("ssid"));
				} else {
					res.redirect("/login?redirect=" + req.pathInfo() +
							((req.queryString() == null) ? "" : ("?" + req.queryString())) );
				}
			}
			else if (req.pathInfo().endsWith("/")) {
				res.redirect(req.pathInfo().substring(0, req.pathInfo().length()-1));
			}
		};
		
		before("/*", (req, res) -> {
			if ((!req.pathInfo().contains(".")) && req.pathInfo().matches(".*[A-Z]+.*"))
				res.redirect(req.pathInfo().toLowerCase());
		});
		
		before("/play", loginFilter);
		before("/play/", loginFilter);
		before("/newlobby", loginFilter);
		before("/newlobby/", loginFilter);
		
		get("/login", (req, res) -> {
			Map<String, Object> attributes = new HashMap<>();
			
			String query = req.queryString();
			System.out.println(query);
			
			QueryMap map = new QueryMap(query);
			
			if (query.contains("invalid"))
				attributes.put("invalid", true);
			
			if (map.containsKey("redirect"))
				req.session().attribute("redirect", map.get("redirect"));
			
			return new ModelAndView(attributes, "login.ftl");
		}, new FreeMarkerEngine());

		
		post("/login", (req, res) -> {
			
			String body = req.body();
			QueryMap map = new QueryMap(body);
			
			for (Map.Entry<String, String> entry : map.entrySet()) {
				
				if (entry.getKey().equals("pname")) {
					
					if (entry.getValue() == null || entry.getValue().equals("") || entry.getValue().matches(".*[^a-zA-Z0-9_].*")) {
						
						res.redirect("/login?invalid=true");
						
						return "Redirecting...";
					}
					
					String ssid = Double.toHexString(Math.random() * 1024);
					
					res.cookie("ssid", ssid);
					UserData.addUser(entry.getValue(), ssid);
				}
				
				req.session().attribute(entry.getKey(), entry.getValue());
				res.cookie(entry.getKey(), entry.getValue());
				
			}

			System.out.println(body);
			System.out.println(map);
			System.out.println(req.session().attributes());
			
			if (req.session().attributes().contains("redirect"))
				res.redirect(req.session().attribute("redirect"));
			else
				res.redirect("/");
			
			return "Redirecting...";
		});
		
		before("/login/", (req, res) -> res.redirect("/login"));
		
		get("/newlobby", (req, res) -> {
			Map<String, Object> attributes = new HashMap<>();
			
			String query = req.queryString();
			
			if (query != null) {
				QueryMap map = new QueryMap(query);
				
				if (query.contains("invalid"))
					attributes.put("invalid", true);
				
				if (map.containsKey("redirect"))
					req.session().attribute("redirect", map.get("redirect"));
			}
			
			return new ModelAndView(attributes, "newLobby.ftl");
		}, new FreeMarkerEngine());
		
		post("/newlobby", (req, res) -> {

			String body = req.body();
			QueryMap map = new QueryMap(body);
			
			String lname = null;
			String lpass = null;
			
			for (Map.Entry<String, String> entry : map.entrySet()) {
				
				if (entry.getKey().equals("lname")) {
					lname = entry.getValue();
				} else if (entry.getKey().equals("lpassword")) {
					lpass = entry.getValue();
				}
				
				req.session().attribute(entry.getKey(), entry.getValue());
				res.cookie(entry.getKey(), entry.getValue());
				
			}

			if (lname == null || lname.equals("") || lname.matches(".*[^a-zA-Z0-9_].*")) {
				res.redirect("/newlobby?invalid=true");
				return "Redirecting...";
			}
			
			Lobby lobby = new Lobby(lname);
			lm.add(lobby);
			
			if (lpass != null && lpass.length() > 0) { 
				lobby.setPassword(lpass);
				res.redirect("/play?id=" + lobby.getId() + "&pw=" + lpass);
			} else {
				res.redirect("/play?id=" + lobby.getId());
			}
			
			return "Redirecting...";
		});
		
        
        lm.add(testLobby);
        lm.add(lobby2);
//*/
        System.out.println(lm);
	}

}
