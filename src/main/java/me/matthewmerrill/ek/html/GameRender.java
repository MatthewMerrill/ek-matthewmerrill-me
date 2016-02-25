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
		return render(deck, null);
	}
	
	public static String render(Deck deck, String message) {
		
		if (deck == null || deck.isEmpty()) {
			return h3("Your Hand is Empty.").render();
		}
		
		Map<String, Object> attributes = new HashMap<String, Object>();
		attributes.put("message", message);
		attributes.put("cards", deck);
		
		FreeMarkerEngine engine = new FreeMarkerEngine();
		ModelAndView westMV = engine.modelAndView(attributes, "cardDeck.ftl");
		return engine.render(westMV);
	}
	
	public static Tag renderCard(Card card) {
		return article().with(img().attr("", card.get(Card.IMAGE_URL).toString()));
	}
	
}
