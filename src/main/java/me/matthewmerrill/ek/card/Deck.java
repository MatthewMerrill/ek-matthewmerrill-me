package me.matthewmerrill.ek.card;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Deck extends ArrayList<Card> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5493968985161180117L;
	
	private Random rand = new Random();
	
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