package structures.basic;

import structures.GameState;
import akka.actor.ActorRef;

public interface Spell {
    public void spell(ActorRef out, GameState gameState, Tile tile);
}
