package me.matthewmerrill.ek.card.admin;

import java.util.Iterator;

import me.matthewmerrill.ek.Lobby;
import me.matthewmerrill.ek.Player;
import me.matthewmerrill.ek.card.Card;
import me.matthewmerrill.ek.card.Deck;
import me.matthewmerrill.ek.websocket.Chat;
import me.matthewmerrill.ek.websocket.ChatWebSocketHandler;
import me.matthewmerrill.ek.websocket.LobbyCallback;
import me.matthewmerrill.ek.websocket.PromptCallbackManager;
import me.matthewmerrill.ek.websocket.UserPrompt;

public class KickCard extends Card {


	/**
	 * 
	 */
	private static final long serialVersionUID = 5660836405532350239L;
	
	public KickCard() {
		super("kick", "kick", "Kick");
	}


	@Override
	public void played(Lobby lobby, Deck deck, Player player) {
		if (lobby.getAdmin().get(Player.SESSION_ID).equals(player.get(Player.SESSION_ID))) {
			PromptCallbackManager.sendPrompt(new KickPrompt(lobby, player));
		}
	}
	
	public static class KickPrompt extends UserPrompt {
	
		public KickPrompt(Lobby lobby, Player player) {
			super(lobby, player, "kickPlayer", "Type a player to kick.",
				new LobbyCallback() {
				public boolean callback(Lobby l, Player sender, String m) {
					Iterator<Player> itr = l.getPlayers().iterator();
					while (itr.hasNext()) {
						Player target = itr.next();
						if (m.equalsIgnoreCase((String)target.get(Player.NAME))) {
							itr.remove();
							
							Chat.kickPlayer(target);
							Chat.broadcastMessage("Server", sender.get(Player.NAME) + " kicked player " + target.get(Player.NAME), lobby);
							
							return true;
						}
					}
					return false;
				}});
		}

		public static KickPrompt kickPrompt(Lobby lobby, Player player) {
			return new KickPrompt(lobby, player);
		}

		@Override
		public boolean validResponse(String response) {
			// TODO Auto-generated method stub
			return false;
		}
		
	}

}
