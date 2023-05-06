package structures.units;

import akka.actor.ActorRef;
import commands.BasicCommands;
import structures.basic.Unit;

/**
 * Pureblade Enforcer: if enemy casts a spell,
 * this minion gains +1 attack and +1 health.
 */
public class PurebladeEnforcer extends Unit {
    
	public void causeEffect(ActorRef out) {
   	 //OIC
   	 //Pureblade Enforcer: when the enemy player casts a spell, it gains +1 attack and +1 health
    	this.setAttack(this.getAttack()+1);
    	this.setHealth(this.getHealth()+1);
   	 
    	BasicCommands.setUnitAttack(out, this, this.getAttack());
    	try {Thread.sleep(30);} catch (InterruptedException e) {e.printStackTrace();}
    	BasicCommands.setUnitHealth(out, this, this.getHealth());
    	try {Thread.sleep(30);} catch (InterruptedException e) {e.printStackTrace();}
	}
}


