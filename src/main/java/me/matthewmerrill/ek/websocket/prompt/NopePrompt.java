package me.matthewmerrill.ek.websocket.prompt;

import me.matthewmerrill.ek.Lobby;
import me.matthewmerrill.ek.LobbyState.Nope;
import me.matthewmerrill.ek.Player;
import me.matthewmerrill.ek.card.Card;
import me.matthewmerrill.ek.html.GameRender;
import me.matthewmerrill.ek.websocket.Chat;
import me.matthewmerrill.ek.websocket.PromptCallbackManager;
import me.matthewmerrill.ek.websocket.UserPrompt;

public class NopePrompt extends UserPrompt {

	private final Nope nope;
	
	public NopePrompt(Lobby lobby, Player player, Nope nope, String action) {
		super(lobby, player, "promptNope", "Would you like to Nope " + player.get(Player.NAME) + "'s " + action, (l, p, r) -> {
			
			System.out.println("Received Nope response: " + action + " : " + r);
			
			if ("true".equalsIgnoreCase(r) || "yes".equalsIgnoreCase(r))
				nope.nope(p);
			
			return true;
		});
		
		this.nope = nope;
	}

	@Override
	public boolean validResponse(String response) {
		return true;
	}
	
	public void send() {
		lobby.getPlaying().stream().filter((p) -> !p.equals(player)).forEach((p) -> {
			
			if (!p.getDeck().stream().anyMatch((c) -> "nope".equals(c.get(Card.ID))))
				return;
			
			Chat.sendSandbox(
					GameRender.renderYesNoPrompt(lobby, "Would you like to Nope " +player.getName() + "'s " + nope.getAction() + "?").render(),
					"south", p.getSsid());
			PromptCallbackManager.promptSent(this, p.getSsid(), 10000L, "false");
		});
	}
	
	public void cancel() {
		lobby.getPlayers().stream().filter((p) -> !p.equals(player)).forEach((p) -> {
			Chat.sendSandbox("", "south", p.getSsid());
		});
	}

}
