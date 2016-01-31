import java.sql.*;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Map;

import java.net.URI;
import java.net.URISyntaxException;

import static spark.Spark.*;
import spark.template.freemarker.FreeMarkerEngine;
import spark.ModelAndView;

import com.heroku.sdk.jdbc.DatabaseUrl;

import org.eclipse.jetty.websocket.api.*;
import org.json.*;
import java.text.*;
import java.util.*;
import static j2html.TagCreator.*;
import static spark.Spark.*;


public class Main {

	public static void main(String[] args) {
        
		port(Integer.valueOf(System.getenv("PORT")));

        staticFileLocation("/public"); //index.html is served at localhost:4567 (default port)
		webSocket("/chat", ChatWebSocketHandler.class);
        init();
        
		get("/hello", (req, res) -> "Hello World");

		get("/", (request, response) -> {
			Map<String, Object> attributes = new HashMap<>();
			attributes.put("message", "Hello World!");

			return new ModelAndView(attributes, "chattest.ftl");
		} , new FreeMarkerEngine());

		get("/db", (req, res) -> {
			Connection connection = null;
			Map<String, Object> attributes = new HashMap<>();
			try {
				connection = DatabaseUrl.extract().getConnection();

				Statement stmt = connection.createStatement();
				stmt.executeUpdate("CREATE TABLE IF NOT EXISTS ticks (tick timestamp)");
				stmt.executeUpdate("INSERT INTO ticks VALUES (now())");
				ResultSet rs = stmt.executeQuery("SELECT tick FROM ticks");

				ArrayList<String> output = new ArrayList<String>();
				while (rs.next()) {
					output.add("Read from DB: " + rs.getTimestamp("tick"));
				}

				attributes.put("results", output);
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

		// matches "GET /say/hello/to/world"
		// request.splat()[0] is 'hello' and request.splat()[1] 'world'
		get("/play", (req, res) -> {
			res.redirect("/play/aaa");
			return "";
		});
		
		get("/play/*", (req, res) -> {
			Connection connection = null;
			Map<String, Object> attributes = new HashMap<>();
			try {
				connection = DatabaseUrl.extract().getConnection();

				//Statement stmt = connection.createStatement();
				//stmt.executeUpdate("CREATE TABLE IF NOT EXISTS ticks (tick timestamp)");
				//stmt.executeUpdate("INSERT INTO ticks VALUES (now())");
				//ResultSet rs = stmt.executeQuery("SELECT tick FROM ticks");

				///ArrayList<String> output = new ArrayList<String>();
				//while (rs.next()) {
				//	output.add("Read from DB: " + rs.getTimestamp("tick"));
				//}

				//attributes.put("results", output);
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
		

		get("/", (request, response) -> {
			Map<String, Object> attributes = new HashMap<>();
			attributes.put("message", "Hello World!");
			
			return new ModelAndView(attributes, "chattest.ftl");
		} , new FreeMarkerEngine());
		
		webSocket("/chat", ChatWebSocketHandler.class);
        init();
//*/
	}

}
