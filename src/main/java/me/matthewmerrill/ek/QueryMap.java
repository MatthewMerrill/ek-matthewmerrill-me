package me.matthewmerrill.ek;

import java.util.Arrays;
import java.util.HashMap;

public class QueryMap extends HashMap<String, String> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7557462154673362068L;

	public QueryMap(String parse) {
		super();
		
		parse(parse);
	}
	
	private void parse(String parse) {
		String[] split = parse.split("&");
		Arrays.stream(split).forEach((str) -> {
			String[] entry = str.split("=");
			
			if (entry.length == 2)
				put(entry[0], entry[1]);
			else
				System.out.println("Couldn't parse " + str + " of " + parse + "!");
			
		});
	}
}
