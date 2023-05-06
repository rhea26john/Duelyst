package structures.units;

import structures.basic.Avatar;
import structures.basic.Grid;
import structures.basic.Player;
import structures.basic.Tile;
import structures.basic.Unit;
import structures.GameState;
import java.util.List;

// import akka.actor.ActorRef;
// import commands.BasicCommands;

// public class AzureHerald extends Unit {
//     int heal = 3;
    
//     //OIC
//     //Azure Herald: When summoned, gives Player's avatar +3 health (max. 20) //not sure why this doesn't work
//     List<Unit> playerUnits = null;
//     Avatar player = null;
//     Player p1;
//     Unit avatar;
    
//     public AzureHerald(ActorRef out, GameState gamestate, Tile t, Player p, Grid g) {    
//    	 summon(out, t, p, g);
//    	 heal(out, gamestate, this.heal);
//     }
    
//     @Override
//     public void summon(ActorRef out, Tile t, Player p, Grid g) {    
//    	 super.summon(out, t, p, g);
//    	 summonToHeal(out, p, g);
//     }
    
//     public void summonToHeal(ActorRef out, Player p, Grid g) {    
//    	 //ensure it is the Player's turn
//    	 if(p.getPlayerNumber() == 1) {    
//    		 this.playerUnits = g.getPlayerOneUnitsOnBoard();
//    	 }
//    	 //then get the Player Avatar
//    	 for(Unit u : playerUnits) {    
//    		 if(u instanceof Avatar) {    
//    			 this.player = (Avatar)u;
//    			 this.avatar = u;
//    		 }
//    	 }
//     }
    
//     //heal +3 to Avatar after ensuring the Player Avatar has been selected
//     @Override
//     public void heal(ActorRef out, GameState gamestate, int heal) {    
//    	 assert this.player != null;
//    	 this.heal = heal;
//    	 //heal Avatar
//    	 this.player.heal(out, gamestate, this.heal);
//    	 //i added this to see if it works
//    	 this.avatar.heal(out, gamestate, this.heal);
//    	 //set the health in front end and back end
//    	 BasicCommands.setUnitHealth(out, this.player, this.player.getHealth());
//    	 BasicCommands.setPlayer1Health(out, this.p1);
//     }
// }

public class AzureHerald extends Unit {
    
    //method now written in Card
}





