package structures.spells;

import akka.actor.ActorRef;
import commands.BasicCommands;
import structures.GameState;
import structures.basic.Avatar;
import structures.basic.Spell;
import structures.basic.Tile;
import structures.basic.Unit;

public class StaffofYKir implements Spell {
	/**
	 * Staff of Y’kir: add +2 attack to your avatar.
	 * @param out used to send messages to the front-end UI
	 * @param gameState holds gamestate information
	 * @param tile is target tile which contains the unit we want to attack
	 */
	@Override
	public void spell(ActorRef out, GameState gameState, Tile tile) {
   	 //OIC
   	 //Staff of Y'Kir: Add +2 attack to your Avatar
   	 
    	//Add +2 attack to your avatar, which could be any side's avatar
    	Unit unitOnTile = tile.getUnit();
   	 
    	//Unit has to be the Avatar to implement
    	if(unitOnTile instanceof Avatar) {
        	//back end avatar change setting
        	unitOnTile.setAttack((unitOnTile.getAttack()) + 2);
        	//front end display avatar attack change
        	BasicCommands.setUnitAttack(out, unitOnTile, unitOnTile.getAttack());
    	}
	}
}


