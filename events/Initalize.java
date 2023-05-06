package events;

import com.fasterxml.jackson.databind.JsonNode;

import akka.actor.ActorRef;
import structures.GameState;
import commands.BasicCommands;
import structures.basic.Player;
import structures.basic.Avatar;
import structures.basic.Tile;
import structures.basic.Unit;
// import structures.units.*;
import utils.BasicObjectBuilders;
import utils.StaticConfFiles;

/**
 * Indicates that both the core game loop in the browser is starting, meaning
 * that it is ready to recieve commands from the back-end.
 * 
 * { 
 *   messageType = “initalize”
 * }
 * 
 * @author Dr. Richard McCreadie
 *
 */
public class Initalize implements EventProcessor{
	@Override
	public void processEvent(ActorRef out, GameState gameState, JsonNode message) {
		gameState.gameInitalised = true;
		gameState.isClickable = true;

		Player playerOne = gameState.getPlayerOne();
		Player playerTwo = gameState.getPlayerTwo();

		//show game start notification
		BasicCommands.addPlayer1Notification(out, "Game Start!", 5);
		try {Thread.sleep(1000);} catch (InterruptedException e) {e.printStackTrace();}

		//player initialization
		gameState.setPlayerWithManaAndHealth(out, playerOne, playerTwo);

		//board tiles initialization
		gameState.displayGridBoard(out);

		//avatar initialization
		loadAndSummonAvatar(out, gameState, playerOne, playerTwo);

		//hand card initialization
		drawAndDisplayHandCard(out, gameState, playerOne, playerTwo);

		//show human player's turn notification
		BasicCommands.addPlayer1Notification(out, "Your turn", 5);
		try {Thread.sleep(40);} catch (InterruptedException e) {e.printStackTrace();}
	}

	//Yao
	//This method is used for loading and summoning avatar
	public void loadAndSummonAvatar(ActorRef out, GameState gameState, Player playerOne, Player playerTwo) {
		Avatar avatarOne = (Avatar) BasicObjectBuilders.loadUnit(StaticConfFiles.humanAvatar, gameState.getNewUnitID(), Avatar.class);
		try {Thread.sleep(100);} catch (InterruptedException e) {e.printStackTrace();}
		avatarOne.initiateAvatar(2, playerOne);

		Avatar avatarTwo = (Avatar) BasicObjectBuilders.loadUnit(StaticConfFiles.aiAvatar, gameState.getNewUnitID(), Avatar.class);
		try {Thread.sleep(250);} catch (InterruptedException e) {e.printStackTrace();}
		avatarTwo.initiateAvatar(2, playerTwo);

		Tile tileOfAvatarOne = gameState.getGrid().getTile(1,2);
		Tile tileOfAvatarTwo = gameState.getGrid().getTile(7,2);

		avatarOne.summon(out, tileOfAvatarOne, gameState.getPlayerOne(), gameState.getGrid());
		gameState.getGrid().getPlayerOneUnitsOnBoard().add(avatarOne);
		avatarTwo.summon(out, tileOfAvatarTwo, gameState.getPlayerTwo(), gameState.getGrid());
		gameState.getGrid().getPlayerTwoUnitsOnBoard().add(avatarTwo);

	}

	//Yao
	//It is used to draw hand card on back end and show hand card on front end
	public void drawAndDisplayHandCard(ActorRef out, GameState gameState, Player playerOne, Player playerTwo) {
		int startHandCardsNum = 3;
		//player1 & player2 both will draw cards but we only allow player 1's cards to be shown
		for(int i = 0; i < startHandCardsNum; i++){
			playerOne.drawCard(out);
			playerTwo.drawCard(out);
		}

		//the method is used to decide which player's hand to be shown;
		gameState.showHandCards(out, playerOne);
	}

}


