package structures.spells;

import akka.actor.ActorRef;
import commands.BasicCommands;
import structures.GameState;
import structures.basic.Avatar;
import structures.basic.Spell;
import structures.basic.Tile;
import utils.BasicObjectBuilders;
import utils.StaticConfFiles;

public class EntropicDecay implements Spell  {
	/**
	 * Entropic Decay: reduce a non-avatar unit to 0 health.
	 * @param out used to send messages to the front-end UI
	 * @param gameState holds gamestate information
	 * @param tile is target tile which contains the unit of non-avatar
	 */
	@Override
	public void spell(ActorRef out, GameState gameState, Tile tile) {
   	 
   	 //OIC
   	 //Entropic Decay: Reduce a non-Avatar unit to 0 health
   	 
    	// only if the Unit is not the Avatar
    	if(!(tile.getUnit() instanceof Avatar)){
   		 
   		 // reduce a non-avatar unit to 0 health
        	tile.getUnit().causeDamage(out, gameState, tile.getUnit().getHealth());
    	}
	}
}


