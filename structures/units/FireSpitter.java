package structures.units;

import java.util.List;

import akka.actor.ActorRef;
import commands.BasicCommands;
import structures.GameState;
import structures.basic.EffectAnimation;
import structures.basic.Tile;
import structures.basic.Unit;
import structures.basic.UnitAnimationType;
import structures.basic.Position;
import utils.BasicObjectBuilders;
import utils.StaticConfFiles;

public class FireSpitter extends Unit {
    
	//create firespitter and enemy's position
	private Position firespitter;
	private Position enemy;
    
	//OIC: bugs out on screen so i commented everything to make it work, need to Override most highlighting attack methods in Unit...
	//Fire Spitter highlights only enemy units

	/**
	 * Fire Spitter: ranged: can attack any enemy on the board.
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
  	  firespitter = this.getPosition();
  	  enemy = unit.getPosition();
  	 
  	  Tile fireT = gamestate.getGrid().getTile(firespitter.getTilex(), firespitter.getTiley());
  	  Tile enemyT= gamestate.getGrid().getTile(enemy.getTilex(), enemy.getTiley());
  	 
  	  //then play animations
  	  BasicCommands.playUnitAnimation(out, this, UnitAnimationType.attack);
  	  BasicCommands.playProjectileAnimation(out, animation, 0, fireT, enemyT);
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
    
//	@Override
//	public void moveAndAttackUnit(Tile tile, ActorRef out, GameState gameState) {    
//  	  //clear the highlight in front-end
//  	  gameState.displayGridBoard(out);
//  	  //moveAttackTile is the tile we need to move to before attack enemy unit
//  	  Tile moveAttackTile = null;
//  	  //get the coordinate of the target tile
//  	  int x = tile.getUnit().getPosition().getTilex();
//  	  int y = tile.getUnit().getPosition().getTiley();
//  	  //check the enemy unit's near tiles, if it is empty, it means we can move there and then attack.
//  			  for (int i = x - 1; i <= x + 1; i++) {
//  				  for (int j = y - 1; j <= y + 1; j++) {
//  					  if (i >= 0 && i < gameState.getGrid().getX() && j >= 0 && j < gameState.getGrid().getY()
//  							  && (gameState.getGrid().getTile(i, j) == null)){
//  						  moveAttackTile = gameState.getGrid().getTile(i, j);
//  					  }
//  				  }
//  			  }
//  			  //move this unit to moveAttackTile
//  			  this.move(moveAttackTile, gameState, out);
//  			  //hasMoved = true;  //use it before submit, it can help test now  为了测试 最后这行要用的
//  			  //This unit will attack after moving
//  			  this.attack(tile.getUnit(),gameState, out);
//  			  //Clear all highlighted tiles in the back-end
//  			  clearWhiteHighlighted();
//  			  clearRedHighlighted();
//	}
    

//	@Override
//	public void highlightAttackableUnits(Tile tile, GameState gameState, ActorRef out) {
//  	  int x = tile.getUnit().getPosition().getTilex();
//  	  int y = tile.getUnit().getPosition().getTiley();
//  	  List<Unit> enemyUnits = gameState.getGrid().getEnemyUnits(tile.getUnit());
//  	 
//  	  for (int height = 0; height < gameState.getGrid().getX(); height++) {
//  		  for (int width = 0; width < gameState.getGrid().getY(); width++) {
//  			 
//  			  //exclude tile itself
//  			  if(!(height==x && width==y)) {    
//  				 
//  				  Unit unitToCheck = gameState.getGrid().getTile(height, width).getUnit();
//  				  //if a tile is not null and has an enemy unit, it should be red highlighted.
//  				  if (unitToCheck != null && enemyUnits.contains(unitToCheck)) {
//  					  Tile shouldRedHighlight = gameState.getGrid().getTile(height, width);
//  					  redHighlighted.add(shouldRedHighlight);
//  				  }
//  			  }
//  		  }
//  			 
//  	  }
//  	  //Display tiles should be highlighted
//  			  for (Tile t : redHighlighted) {
//  				  BasicCommands.drawTile(out, t, 2);
//  				  t.setMode(2);
//  				  try {
//  					  Thread.sleep(20);
//  				  } catch (InterruptedException e) {
//  					  e.printStackTrace();
//  				  }
//  			  }
//	}
}
    





