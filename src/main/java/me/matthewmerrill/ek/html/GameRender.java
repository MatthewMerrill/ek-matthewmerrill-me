package me.matthewmerrill.ek.html;

import static j2html.TagCreator.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import j2html.tags.ContainerTag;
import j2html.tags.Tag;
import me.matthewmerrill.ek.Lobby;
import me.matthewmerrill.ek.Player;
import me.matthewmerrill.ek.card.Card;
import me.matthewmerrill.ek.card.Deck;

public class GameRender {
	
	public static String render(Deck deck, Lobby lobby, Player holder) {
		return render(deck, lobby, holder, null);
	}
	
	/*
	 * <div class="deck">
	 * 	<#if message??>
	 * 		<h4>${message}</h4>
	 * 	</#if>
	 * 	<#list cards as card>
	 * 		<#include "card.ftl">
	 * 	</#list>
	 * </div>
	 */
	public static String render(Deck deck, Lobby lobby, Player holder, String message) {
		
		if (deck == null || deck.isEmpty()) {
			if ((boolean) holder.get(Player.KILLED))
				return h3("You Exploded!").render();
			else if (!lobby.getPlaying().contains(holder)) {
				return h3("Not currently playing.").render();
			}

			return h3("Your Hand is Empty.").render();
		}
		
		Set<String> processedIds = new HashSet<>();
		List<Tag> children = new ArrayList<Tag>();
		
		for (Card c : deck) {
			String typeId = c.get(Card.ID).toString();
			
			if (processedIds.contains(typeId)) {
				//System.out.println("Already has "  + typeId);
				continue;	
			}
			
			int count = (int) deck.stream().filter(o -> {return o.get(Card.ID).toString().equals(typeId);}).count();
			
			//System.out.println(typeId + ":" + count);
			
			boolean active = true;
			
			if (lobby != null && holder != null)
				active = lobby.getState().isActive(c, holder);
			
			children.add(cardTag(c, count, active));
			//System.out.println(children);
			processedIds.add(typeId);
		}
		
		String render =  div().with(
				h3(message),
				div().withClass("deck").with(children)).render();
		//System.out.println(render);
		return render;
		/*
		*/
	}
	
	/*
	<a href="javascript:void(0);" <#if card.id??>onclick="cardClicked('${card.id}');</#if>"<div class="deck-card<#if card.active??>-active</#if>">
	<h4>${card.name}</h4>
	<#if card.description??><h6>${card.description}</h6></#if>

	<!--<img src="/card/${card.imageUrl}.png"/>-->
	</div></a>
	*/
	public static Tag cardTag(Card card, int count, boolean active) {
		
		if (card == null)
			return null;
		
		List<Tag> children = new ArrayList<Tag>();
		children.add(h4(card.get(Card.NAME).toString()));
		
		if (count > 1) {
			children.add(p("x" + count));
		}
		
		try {
			children.add(h6(card.get(Card.DESCRIPTION).toString()));
		} catch (Exception ignored) {}
		
		ContainerTag a =  a()
				.withHref("javascript:void(0);")
				.condAttr(active, "onclick", "cardClicked('" + card.get(Card.ID) + "');")
				.with(div().withId("deck-card")
						.withCondClass(active, "active")
						.with(children));
		
		//try {
			a  = a.withAlt("ALT!");
		//} 
		return a;
	}
	
	/*
	public static String renderCard(Card card, int count) {
		Map<String, Object> attributes = new HashMap<String, Object>();
		attributes.put("count", count);
		attributes.put("card", card);
		
		FreeMarkerEngine engine = new FreeMarkerEngine();
		ModelAndView mv = engine.modelAndView(attributes, "card.ftl");
		String ret =  engine.render(mv);
		//System.out.println(ret);
		return ret;
	}
	*/
	

	public static Tag renderOptionPrompt(Lobby lobby, String msg, List<Tag> options) {
		ContainerTag tag = div().withId("promptDiv").with(
				h3(msg),
				select().withId("inputSelect")
						.with(options),
				button().withText("Submit")
					.attr("onclick", "promptUserResponse('"+lobby.getId()+"', 'inputSelect')")
				);
		
		return tag;
	}
	
	public static Tag renderUserPrompt(Lobby lobby, String sender, boolean includeSelf, String msg) {
		List<Tag> userOptions = new ArrayList<Tag>();
		
		for (Player player : lobby.getPlayers()) {
			if (!includeSelf && sender.equals(player.get(Player.SESSION_ID)))
				continue;
			
			userOptions.add(option()
					.withValue((String)player.get(Player.SESSION_ID))
					.withText((String)player.get(Player.NAME)));
		}
		
		return renderOptionPrompt(lobby, msg, userOptions);
	}

	public static Tag renderYesNoPrompt(Lobby lobby, String msg) {
		List<Tag> options = new ArrayList<Tag>();
		
		options.add(option().withValue("true").withText("Yes"));
		options.add(option().withValue("false").withText("No"));
		
		
		return renderOptionPrompt(lobby, msg, options);
	}

	public static Tag renderIntPrompt(Lobby lobby, int min, int max, String msg) {
		ContainerTag tag = div().withId("promptDiv").with(
				h3(msg),
				input().withId("integerInput")
					.withType("number")
					.attr("min", min + "")
					.attr("max", max + ""),
				button().withText("Submit")
					.attr("onclick", "promptIntegerResponse('"+lobby.getId()+"', 'integerPrompt')")
				);
		
		return tag;
	}

	public static String playerList(Lobby lobby) {
		List<Tag> li = new ArrayList<Tag>();
		li.add(h4("Playing:"));
		for (Player p : lobby.getPlaying()) {
			li.add(li(p.getName()));
		}
		return ul().with(li).render();
	}
	
}
