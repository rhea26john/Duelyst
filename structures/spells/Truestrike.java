package structures.spells;

import akka.actor.ActorRef;
import commands.BasicCommands;
import structures.GameState;
import structures.basic.Spell;
import structures.basic.Tile;

public class Truestrike implements Spell {
	/**
	 *
	 * @param out used to send messages to the front-end UI
	 * @param gameState holds gamestate information
	 * @param tile is target tile which contains the unit we want to attack
	 */
	@Override
	public void spell(ActorRef out, GameState gameState, Tile tile) {
   	 
   	 //OIC
   	 //Truestrike: deal 2 damage to an enemy unit
    	tile.getUnit().causeDamage(out, gameState, 2);
	}
}



