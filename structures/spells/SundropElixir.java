package structures.spells;

import akka.actor.ActorRef;
import commands.BasicCommands;
import structures.GameState;
import structures.basic.Avatar;
import structures.basic.Spell;
import structures.basic.Tile;
import structures.basic.Unit;

public class SundropElixir implements Spell {
	/**
	 * Sundrop Elixir: add +5 health to a unit.
	 * this cannot take a unit over its starting health value.
	 * @param out used to send messages to the front-end UI
	 * @param gameState holds gamestate information
	 * @param tile is target tile which contains the unit we want to heal
	 */
	@Override
	public void spell(ActorRef out, GameState gameState, Tile tile) {
   	 
    	//OIC
   	 //Sundrop Elixir: add +5 health to a Unit. This cannot take a unit over its starting health value.
    	Unit unitOnTile = tile.getUnit();
    	unitOnTile.heal(out, gameState,5);
	}
}
