package events;

import com.fasterxml.jackson.databind.JsonNode;

import akka.actor.ActorRef;
import structures.GameState;
import commands.BasicCommands;
import structures.basic.Player;
import structures.basic.Unit;
import structures.AI;

/**
 * Indicates that the user has clicked an object on the game canvas, in this case
 * the end-turn button.
 * 
 * { 
 *   messageType = “endTurnClicked”
 * }
 * 
 * @author Dr. Richard McCreadie
 *
 */
public class EndTurnClicked implements EventProcessor{

	@Override
	public void processEvent(ActorRef out, GameState gameState, JsonNode message) {
		if(gameState.isClickable && !gameState.gameOver){
			//after we implement AI, we need to uncomment this line, so the AI can run; after the end of AI turn, it should be changed back to true
			gameState.isClickable = false;

			Player playerOne = gameState.getPlayerOne();
			Player playerTwo = gameState.getPlayerTwo();

			//change mana and health value of player
			gameState.setPlayerWithManaAndHealth(out, playerOne, playerTwo);

			//show mana and health change on board
			gameState.displayGridBoard(out);

			//at the end of each turn, players would draw a card
			drawAndDisplayHandCard(out, gameState, playerOne, playerTwo);
			BasicCommands.addPlayer1Notification(out, "AI turn", 5);
			try {Thread.sleep(40);} catch (InterruptedException e) {}
			
			// Rhea Ajit John
			AI AI= gameState.getAI();
			AI.makeAIMove(out,gameState);

			// need a condition for whether player has played a card/ if health of AI is decreasing drastically
			if(gameState.cardExecuted || gameState.getGrid().getPlayerOneUnitsOnBoard().size()>=1)
				AI.executeAICard(out,gameState);

			for(Unit u : gameState.getGrid().getPlayerOneUnitsOnBoard()){
				u.setHasAttacked(false);
				u.setHasMoved(false);
				u.setHasCounterAttacked(false);
			}
			for(Unit u : gameState.getGrid().getPlayerTwoUnitsOnBoard()){
				u.setHasAttacked(false);
				u.setHasMoved(false);
				u.setHasCounterAttacked(false);
			}

			gameState.cardExecuted=false;
			gameState.isClickable=true;
		}
	}

	public void drawAndDisplayHandCard(ActorRef out, GameState gameState, Player playerOne, Player playerTwo) {
		playerOne.drawCard(out);
		playerTwo.drawCard(out);
		gameState.showHandCards(out, playerOne);
	}
}
