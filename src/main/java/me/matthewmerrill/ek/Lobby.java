package me.matthewmerrill.ek;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.matthewmerrill.ek.card.BombCard;
import me.matthewmerrill.ek.card.Card;
import me.matthewmerrill.ek.card.Deck;
import me.matthewmerrill.ek.card.DefuseCard;
import me.matthewmerrill.ek.card.SkipCard;

public class Lobby {
	
	public final Deck deck;
	
	public final Map<String, Player> map = new HashMap<String, Player>();
	public final List<Player> players = new ArrayList<Player>();
	
	private int playerIndex = 0;
	public final int direction = 1;
	
	public Lobby() {
		deck = new Deck();
	}
	
	public void deal() {
		
		deck.clear();
		players.forEach((Player player) -> player.playerDeck.clear());
		
		// Deal bulk of player cards
		{
			for (Card card : SkipCard.startingCards())
				deck.add(card);
			
			deck.shuffle();
			
			players.forEach((Player player) -> {
				while (player.playerDeck.size() < 4)
					player.giveCard(deck.draw());
			});
		}

		// Handle defuse cards
		{
			final Deck defuseDeck = new Deck();
			
			for (Card card : DefuseCard.startingCards())
				defuseDeck.add(card);
			
			defuseDeck.shuffle();
			
			players.forEach((Player player) -> player.giveCard(defuseDeck.draw()));
			deck.addAll(defuseDeck);
			
			defuseDeck.clear();
		}
		
		// Add bomb cards, shuffle once more and we're done!
		{
			for (Card card : BombCard.startingCards())
				deck.add(card);
			
			deck.shuffle();
		}
		
	}
	
	public int getPlayerIndex() {
		return playerIndex;
	}
	public void setPlayerIndex(int index) {
		this.playerIndex = index %= players.size();
	}

}
