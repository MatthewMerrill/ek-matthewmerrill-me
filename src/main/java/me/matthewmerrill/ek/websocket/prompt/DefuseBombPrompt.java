package me.matthewmerrill.ek.websocket.prompt;

import me.matthewmerrill.ek.Lobby;
import me.matthewmerrill.ek.Player;
import me.matthewmerrill.ek.card.BombCard;
import me.matthewmerrill.ek.card.BombCard.BombType;
import me.matthewmerrill.ek.html.GameRender;
import me.matthewmerrill.ek.websocket.Chat;
import me.matthewmerrill.ek.websocket.UserPrompt;

public class DefuseBombPrompt extends UserPrompt {

	public DefuseBombPrompt(Lobby lobby, Player player) {
		super(lobby, player, "integerPrompt", "How many cards deep do you want the bomb?", (l, p, r) -> {
			try {
				int index = Integer.parseInt(r);
				lobby.getDrawDeck().set(index, new BombCard(BombType.DEFAULT));
				lobby.nextTurn();
				
				System.err.println("Defused " + index + " deep.");
				
				return true;
			} catch (Exception e) {
				return false;
			}
		});
	}

	@Override
	public boolean validResponse(String response) {
		try {
			Integer.parseInt(response);
			return true;
		} catch (Exception ignored) {}
		return false;
	}
	
	public void send() {
		Chat.sendSandbox(
				GameRender.renderIntPrompt(lobby, 0, lobby.getDrawDeck().size(), "How many cards do you want on top of the bomb? 0-" + (lobby.getDrawDeck().size()-1)).render(),
				"south", player.getSsid());
	}

}
