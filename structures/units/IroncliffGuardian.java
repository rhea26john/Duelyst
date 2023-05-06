package structures.units;

import structures.basic.Unit;

/**
 * Ironcliff Guardian: can be summoned anywhere on the board.
 * provoke: if an enemy unit can attack and is adjacent to any unit with provoke,
 * then it can only choose to attack the provoke units.
 * enemy units cannot move when provoked.
 */
public class IroncliffGuardian extends Unit {


    public IroncliffGuardian() {
        
        //called into Unit method for Provoke ability
        super.canProvoke = true;
    }
}


