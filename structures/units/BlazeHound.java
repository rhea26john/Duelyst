package structures.units;

import java.util.ArrayList;
import java.util.List;

import akka.actor.ActorRef;
import structures.basic.Grid;
import structures.basic.Player;
import structures.basic.Tile;
import structures.basic.Unit;

/**
 * Blaze Hound: when this unit is summoned,
 * both players draw a card.
 * this Card bugs out the game when summoned so is commented.
 */
//@Override
//     public void summon(ActorRef out, Tile t, Player p, Grid g) {    
//    	 super.summon(out, t, p, g);
//    	 //get both Player and Ai units
//    	 List<Unit> allUnits = new ArrayList<>(g.getPlayerOneUnitsOnBoard());
//    	 allUnits.addAll(g.getPlayerTwoUnitsOnBoard());
//     }
    
// }

public class BlazeHound extends Unit {
    
    //now written in SummonUnit method in Card
}

