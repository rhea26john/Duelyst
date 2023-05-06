package events;


import com.fasterxml.jackson.databind.JsonNode;

import akka.actor.ActorRef;
import structures.GameState;
import structures.basic.Card;
import commands.BasicCommands;
import java.util.ArrayList;
import java.util.List;

/**
 * Indicates that the user has clicked an object on the game canvas, in this case a card.
 * The event returns the position in the player's hand the card resides within.
 * 
 * { 
 *   messageType = “cardClicked”
 *   position = <hand index position [1-6]>
 * }
 * 
 * @author Dr. Richard McCreadie
 *
 */
public class CardClicked implements EventProcessor{

	@Override
	public void processEvent(ActorRef out, GameState gameState, JsonNode message) {
		//the hand position ranges from 1 to 6
		int handPosition = message.get("position").asInt();
		ArrayList<Card> hand = gameState.getPlayerOne().getCardInhand();

		//when it's player 1's turn
		if(gameState.isClickable && !gameState.gameOver) {
			//every time we click one card, we need to first reset all the cards to default mode
			resetCardsMode(out, gameState, hand, 0);

			//highlight the clicked card
			highlightClickedCardAndShowTiles(out, gameState, hand, handPosition, 1);
		}
	}

	public void resetCardsMode(ActorRef out, GameState gameState, ArrayList<Card> hand, int mode) {
		for(int i = 0; i < hand.size(); i++) {
			Card cardShown = hand.get(i);
			BasicCommands.drawCard(out, cardShown, i+1, mode);
			try {Thread.sleep(15);} catch (InterruptedException e) {e.printStackTrace();}
		}
	}

	//Yao
	//this method is used to highlight tiles for clicked card, save clicked card and show available tiles for that card
	public void highlightClickedCardAndShowTiles(ActorRef out, GameState gameState, ArrayList<Card> hand, int handPosition, int mode) {
		Card clickedCard = hand.get(handPosition-1);
		BasicCommands.drawCard(out, clickedCard, handPosition, mode);
		try {Thread.sleep(15);} catch (InterruptedException e) {e.printStackTrace();}

		//save clicked card position to gameState
		gameState.setClickedHandPosition(handPosition);

		//save clicked card so that it could be used to differentiate unit and spell card
		gameState.setCardLastClicked(obtainClickedCard(gameState, clickedCard));

		//show highlighted tiles
		clickedCard.showTilesAvailable(out, gameState);
	}
	private Card obtainClickedCard(GameState gameState, Card clickedCard) {
		Card cardLastClicked = gameState.getCardLastClicked();
	    cardLastClicked = clickedCard;
		return cardLastClicked;

	}
}
