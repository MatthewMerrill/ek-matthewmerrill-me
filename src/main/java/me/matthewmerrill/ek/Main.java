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
			QueryMap map = new QueryMap(body);
			
			map.entrySet().forEach((entry) -> {
				req.session().attribute(entry.getKey(), entry.getValue());
				res.cookie(entry.getKey(), entry.getValue());
				
				if (entry.getKey().equals("pname")) {
					
					String ssid = Double.toHexString(Math.random() * 1024);
					
					res.cookie("ssid", ssid);
					UserData.addUser(entry.getValue(), ssid);
				}
			});

			System.out.println(body);
			System.out.println(map);
			System.out.println(req.session().attributes());
			
			if (req.session().attributes().contains("redirect"))
				res.redirect(req.session().attribute("redirect"));
			
			return "Redirecting...";
		});
		
		before("/login/", (req, res) -> res.redirect("/login"));
		
        Lobby lobby = new Lobby("The Testing Lobby!");
        lobby.getPlayers().add(new Player("PLAYER1", "a"));
        lobby.getPlayers().add(new Player("PLAYER2", "b"));
        lobby.getPlayers().add(new Player("PLAYER3", "c"));
        lobby.deal();
        
        Lobby lobby2 = new Lobby("Super Private Lobby!");
        lobby2.getPlayers().add(new Player("Bill 'Binary Logic' Gates", "d"));
        lobby2.getPlayers().add(new Player("Al Gore 'Rhythm'", "e"));
        lobby2.getPlayers().add(new Player("Barack 'To the Future' Obama", "f"));
        lobby2.setPassword("leet");
        lobby2.deal();
        
        lm.add(lobby);
        lm.add(lobby2);
//*/
        System.out.println(lm);
	}

}
