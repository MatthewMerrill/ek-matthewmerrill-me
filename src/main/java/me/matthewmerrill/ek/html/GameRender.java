package me.matthewmerrill.ek.html;
import static j2html.TagCreator.*;

import java.util.HashMap;
import java.util.Map;

import j2html.tags.Tag;
import me.matthewmerrill.ek.card.Card;
import me.matthewmerrill.ek.card.Deck;
import spark.ModelAndView;
import spark.template.freemarker.FreeMarkerEngine;

public class GameRender {
	
	public static String render(Deck deck) {
		Map<String, Object> attributes = new HashMap<String, Object>();
		attributes.put("message", "Your Cards:");
		attributes.put("cards", deck);
		
		FreeMarkerEngine engine = new FreeMarkerEngine();
		ModelAndView westMV = engine.modelAndView(attributes, "cardDeck.ftl");
		return engine.render(westMV);
	}
	
	public static Tag renderCard(Card card) {
		return article().with(img().attr("", card.get(Card.IMAGE_URL).toString()));
	}
	
}
