package structures.units;

import akka.actor.ActorRef;
import structures.GameState;
import structures.basic.Grid;
import structures.basic.Player;
import structures.basic.Tile;
import structures.basic.Unit;

/**
 * Azurite Lion: can attack twice per turn.
 */
public class AzuriteLion extends Unit {
    //OIC
    int attack = 2;
    int current = 0;
    
    public void attackTwice(Unit unit, GameState gamestate, ActorRef out) {    
   	 super.attack(unit, gamestate, out);
   	 current++;
   	 resetAttack();
   	 
    }
    
    
    //method to reset current attack number when it is less than 2
    public void resetAttack() {    
   	 if(current < 2) {    
   		 hasAttacked = false;
   		 hasMoved = true;
   		 
   	 }
    }
    
    //method overwriting from Unit Class
    @Override
    public void summon(ActorRef out, Tile t, Player p, Grid g) {
   	 //set Attack to twice per turn
   	 super.summon(out, t, p, g);
   	 current = 2;
   	 
    }
}



