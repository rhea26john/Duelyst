package structures.units;

import java.util.List;

import akka.actor.ActorRef;
import commands.BasicCommands;
import structures.GameState;
import structures.basic.Unit;

/**
 * Silverguard Knight: provoke:
 * if an enemy unit can attack and is adjacent to any unit with provoke,
 * then it can only choose to attack the provoke units.
 * enemy units cannot move when provoked.
 * if player avatar is dealt damage this unit gains +2 attack.
 * attack +2 method is written in unit class.
 */
public class SilverguardKnight extends Unit {
    
    public SilverguardKnight(ActorRef out, GameState gamestate) {    
   	 //has the provoke ability
      	  super.canProvoke = true;
      	 
   	//silverguardCondition method for when avatar is dealt damage, +2 attack, written in Unit
         if(avatarIsAttacked = true) {    
        	 
        	 List<Unit> playerOneUnits = null;
           	  Unit silverguard = null;
           	  //get Player One's units
           	  playerOneUnits = gamestate.getGrid().getPlayerOneUnitsOnBoard();
           	  //check if silverguard knight is on the board
           	  for(Unit unit : playerOneUnits) {    
           		  if(unit instanceof SilverguardKnight) {    
           			  silverguard = unit;
           			  assert silverguard != null;
           			  silverguard.setAttack(attack + 2);;
           			  BasicCommands.setUnitAttack(out, silverguard, silverguard.getAttack());
           			  try {
           				  Thread.sleep(2000);
           			  } catch (InterruptedException e) {
           				  e.printStackTrace();
           			  }
           			  //reset boolean condition of dealt damage
           			  resetAvatarIsAttacked();
           		  }
           	  }  	 
         }
    }
}


