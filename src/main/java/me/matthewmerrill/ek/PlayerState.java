package me.matthewmerrill.ek;

import me.matthewmerrill.ek.card.Card;

public enum PlayerState {

	PLAYING, WAITING, DEFUSING, GIVING_CARD, SELECTING_CARD;

	PlayerState() {

	}

	public boolean cardActive(Card card, Lobby lobby) {
		String typeId = card.get(Card.ID).toString();

		if (typeId.equals("nope") && lobby.getState().canNope())
			return true;

		if (this == PLAYING) {
			switch (typeId) {
			case ("defuse"):
				return false;
			default:
				return true;
			}
		}

		if (this == WAITING) {
			switch (card.get(Card.ID).toString()) {
			default:
				return false;
			}
		}

		if (this == DEFUSING) {
			switch (card.get(Card.ID).toString()) {
			case ("defuse"):
				return true;
			default:
				return false;
			}
		}

		if (this == GIVING_CARD) {
			switch (card.get(Card.ID).toString()) {
			default:
				return false;
			}
		}

		return false;
	}
}
