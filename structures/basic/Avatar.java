package structures.basic;

import java.util.*;

import akka.actor.ActorRef;
import commands.BasicCommands;
import structures.GameState;
import structures.basic.*;
import structures.basic.Player;

public class Avatar extends Unit {
    private Player player;

    public void initiateAvatar(int attackValue, Player player) {
        super.setAttack(attackValue);
        super.setMaxHealth(player.getHealth());
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    // public void causeDamage(ActorRef out, GameState gameState, int damage) {
    //     super.causeDamage(out, gameState, damage);
    //     if(this.getHealth() < 1){
    //         player.setHealth(0);
    //         if(player.getPlayerNumber() == 1){
    //             BasicCommands.setPlayer1Health(out, player);
    //             BasicCommands.addPlayer1Notification(out, "YOU LOSE!", 10000);
    //         }
    //         else {
    //             BasicCommands.setPlayer2Health(out, player);
    //             BasicCommands.addPlayer1Notification(out, "YOU WIN!", 10000);
    //         }
    //         //game ends and thus should no longer be clickable
    //         gameState.gameOver = true;
    //         gameState.isClickable = false;
    //         //stop all units on board
    //         gameState.stopAllUnit();
    //     }
    //     else{
    //         player.setHealth(this.getHealth());
    //         if(player.getPlayerNumber() == 1){
    //             BasicCommands.setPlayer1Health(out, player);
    //         }
    //         else {
    //             BasicCommands.setPlayer2Health(out, player);
    //         }
    //     }
    // }
}