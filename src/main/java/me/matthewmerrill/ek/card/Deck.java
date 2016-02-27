package me.matthewmerrill.ek.card;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import me.matthewmerrill.ek.card.admin.JoinCard;
import me.matthewmerrill.ek.card.admin.KickCard;
import me.matthewmerrill.ek.card.admin.LeaveCard;
import me.matthewmerrill.ek.card.admin.StartCard;
import me.matthewmerrill.ek.card.admin.StopCard;

public class Deck extends ArrayList<Card> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5493968985161180117L;	
	
	private static Random rand = new Random();
	
	public static final Deck ADMIN_DECK = new Deck() {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 3373329728097890666L;

	{
		add(new StartCard());
		add(new KickCard());
		add(new StopCard());
		add(new JoinCard());
		add(new LeaveCard());
	}};

	public static final Deck EAST_DECK = new Deck() {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = -3494342248094771754L;

	{
		add(new JoinCard());
		add(new LeaveCard());
	}};
	
	public Card draw() {
		return remove(0);
	}
	
	public Card[] seeFuture() {
		int count = 3;
		
		if (size() < 3)
			count = size();
		
		Card[] arr = new Card[count];
		for (int i = 0; i < count; ++i)
			arr[i] = get(i);
		
		return arr;
	}
	
	public void shuffle() {
		List<Card> list = new ArrayList<Card>();
		list.addAll(this);
		
		this.clear();
		
		while (!list.isEmpty())
			add(list.remove(rand.nextInt(list.size())));
	}
	
}
