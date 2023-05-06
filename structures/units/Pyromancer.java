package structures.units;

import akka.actor.ActorRef;
import commands.BasicCommands;
import structures.GameState;
import structures.basic.EffectAnimation;
import structures.basic.Position;
import structures.basic.Tile;
import structures.basic.Unit;
import structures.basic.UnitAnimationType;
import utils.BasicObjectBuilders;
import utils.StaticConfFiles;

public class Pyromancer extends Unit {

	private Position pyromancer;
	private Position enemy;
    
	//OIC: bugs out on screen so i commented everything to make it work, need to Override most highlighting attack methods in Unit...
	//Pyromancer highlights only enemy units

	/**
	 * Pyromancer: ranged: can attack any enemy on the board.
	 * @param unit is the unit that this unit want to attack
	 * @param gamestate hold game state information
	 * @param out used to send messages to the front-end UI
	 */
	@Override
	public void attack(Unit unit, GameState gamestate, ActorRef out) {    
  	  //clear the highlight in front-end
  	  gamestate.displayGridBoard(out);
  	  EffectAnimation animation = BasicObjectBuilders.loadEffect(StaticConfFiles.f1_projectiles);
  	 
  	  //get both unit and enemy tile positions
  	 pyromancer = this.getPosition();
  	  enemy = unit.getPosition();
  	 
  	  Tile pyroT = gamestate.getGrid().getTile(pyromancer.getTilex(), pyromancer.getTiley());
  	  Tile enemyT= gamestate.getGrid().getTile(enemy.getTilex(), enemy.getTiley());
  	 
  	  //then play animations
  	  BasicCommands.playUnitAnimation(out, this, UnitAnimationType.attack);
  	  BasicCommands.playProjectileAnimation(out, animation, 0, pyroT, enemyT);
  	  try {
  		  Thread.sleep(2000);
  	  } catch (InterruptedException e) {
  		  e.printStackTrace();
  	  }
  	  //the unit that be attacked will lose health
  			  unit.beAttacked(attack, gamestate, out);
  			  //check if the unit that be attacked is dead
  			  unit.death(gamestate,out);
  			  hasAttacked = true; //use it before submit, it can help test now
  			  hasMoved = true;  //use it before submit, it can help test now
  			  // Clear all highlighted tiles
  			  clearRedHighlighted();
	}
}


