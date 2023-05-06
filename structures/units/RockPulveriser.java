package structures.units;

import structures.basic.Unit;

/**
 * Rock Pulveriser: provoke:
 * if an enemy unit can attack and is adjacent to any unit with provoke,
 * then it can only choose to attack the provoke units.
 * enemy units cannot move when provoked.
 */
public class RockPulveriser extends Unit {
    
    //OIC
    //Rock Pulveriser: with provoke ability, called to Unit's method
    public RockPulveriser() {    
   	 super.canProvoke = true;
    }
}


