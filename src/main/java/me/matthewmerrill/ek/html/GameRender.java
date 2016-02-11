package me.matthewmerrill.ek.html;
import static j2html.TagCreator.*;


import j2html.tags.Tag;
import me.matthewmerrill.ek.card.Card;
import me.matthewmerrill.ek.card.Deck;

public class GameRender {
	
	public static String render(Deck deck) {
		Tag[] tags = new Tag[deck.size()];
		
		for (int i = 0; i < deck.size(); i++) {
			tags[i] = renderCard(deck.get(i));
		}
		
		return article().with(tags).render();
	}
	
	public static Tag renderCard(Card card) {
		return article().with(img().attr("", card.get(Card.IMAGE_URL).toString()));
	}
	
}
