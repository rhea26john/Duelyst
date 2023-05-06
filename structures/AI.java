package structures;

import akka.actor.ActorRef;
import commands.BasicCommands;
import structures.basic.Grid;
import structures.basic.Card;
import structures.basic.Position;
import structures.basic.Tile;
import structures.basic.Unit;
import structures.basic.Avatar;
import java.util.Random;
import structures.units.Serpenti;
import java.util.Collections;
// Rhea Ajit John
import java.util.*;

public class AI
{
    static int count=0;
    public static int getCount()
    {
        return count;
    }

    public static void setCount()
    {
        count++;
    }
    



// =======================================================================================================================================================
// ========================================================Movement Functions ==============================================================================================
// =======================================================================================================================================================
    // this function will make the AI units move
    /**
     * The function creates a list all the player2 units which is AI units present on the board
     * It also creates a new list called HighlightedTiles which is used to determine where we can place AI card units on the board
     * and which tiles are available for attack.
     * We are using synchronised function because in later the chances are the units may get deleted from the list even if their functionality hasn't been
     * over yet, so to avoid any concurrent errors.
     * We traverse through every unit on the board, so that every unit in a turn gets a chance to either move or attack.
     * subAImove is called to determine which tiles are the best for every unit movement in board.
     @param out , holds actor ref information
     @param gameState, hold gameState information
  */



    public static void makeAIMove(ActorRef out, GameState gameState) 
    {
        Random rand = new Random(); 
        //BasicCommands.addPlayer1Notification(out, "Inside AI move function", 5);
        try {Thread.sleep(10);} catch (InterruptedException e) {e.printStackTrace();}
        Grid grid = gameState.getGrid();
        //List<Unit> AIunits = grid.getPlayerTwoUnitsOnBoard();
        List<Unit> AIunits = new ArrayList<>(grid.getPlayerTwoUnitsOnBoard());
        //BasicCommands.addPlayer1Notification(out, "Before if ", 5);
        gameState.displayGridBoard(out);

        List<Tile> highlightedTiles = new ArrayList<>(grid.getHighlightedTiles());
        synchronized(highlightedTiles) {
            highlightedTiles.clear();
        }

        //grid.getHighlightedTiles().clear();
        setCount();
        for (Unit AIunit : AIunits) {
            subAImove(grid,AIunit,out,gameState);
            if(AIunit instanceof Serpenti)
                subAImove(grid,AIunit,out,gameState);
            try {Thread.sleep(20);} catch (InterruptedException e) {e.printStackTrace();}
        }
        try {Thread.sleep(200);} catch (InterruptedException e) {e.printStackTrace();}
        gameState.displayGridBoard(out);
    }



    /**
     * again we take into account the AI unit which is passed from it parent function
     * and then we determine the tile at which that AI unit is placed.
     * then we check the condition for whether unit has moved or attack, and accordingly we keep a check on highlighting list
     * After we get the highlighted tiles on board, we created a Tile variable called selectedTile.
     * We create a maxScore and score variables as well.
     * For every highlighted tile we will check what type of unit is place on the board. If its PLayer avatar then it will have more points as compared to units of player.
     * After performing the mathematical computation, we will select the tile which has the highest score.
     * After selecting the tile which will check whether the tile is empty, contains unit
     * If it contains unit we will call the  attack function, else we will move towards that tile.
     * @param grid instance of grid
     * @param AIunit holds AIunit
     * @param out hold actor ref instances
     * @param gameState hold game state information
     *

     */

    public static void subAImove(Grid grid, Unit AIunit,ActorRef out,GameState gameState){
    //private static void startMove(ActorRef out, Unit AIunit, GameState gameState){
    //Board board = gameState.getBoard();
    List<Unit> AIunits = grid.getPlayerTwoUnitsOnBoard();

    gameState.displayGridBoard(out); //gameState.drawDefaultTilesGrid(out);
    grid.getHighlightedTiles().clear(); // board.getHighlightedTiles().clear();
    Tile unitTile = grid.getTile(AIunit.getPosition().getTilex(), AIunit.getPosition().getTiley());
    grid.setLastTile(unitTile);

    if(!AIunit.isHasMoved() && !AIunit.isHasAttacked()){
        //AIunit.displayMovementTiles(out, unitTile, gameState);
        //BasicCommands.addPlayer1Notification(out, "Check 1", 5);
        try {Thread.sleep(40);} catch (InterruptedException e) {e.printStackTrace();}
        AIunit.highlightMoveAndAttackableUnits(unitTile,gameState,out);
    }
    if(AIunit.isHasMoved() && !AIunit.isHasAttacked()){
        //AIunit.displayInRangeAttackTiles(out, unitTile, gameState.getBoard());
        //BasicCommands.addPlayer1Notification(out, "Check 2", 5);
        try {Thread.sleep(40);} catch (InterruptedException e) {e.printStackTrace();}
        //AIunit.highlightAttackableUnits(unitTile,gameState,out);
        AIunit.highlightMoveAndAttackableUnits(unitTile,gameState,out);
    }

    List<Tile> highlightedTiles = grid.getHighlightedTiles();
    Tile selectedTile = null;
    double maxScore = 0;
    double score=0;
    for(Tile move : highlightedTiles){
        //BasicCommands.addPlayer1Notification(out, "Check 3", 5);
        try {Thread.sleep(40);} catch (InterruptedException e) {e.printStackTrace();}
        if((AIunit instanceof Unit||AIunit instanceof Avatar) && (move.getUnit()!=null))  // AI player
        {
            //BasicCommands.addPlayer1Notification(out, "Check 4", 5);
            try {Thread.sleep(40);} catch (InterruptedException e) {e.printStackTrace();}
             //player unit/ avatar is there on board
            
                if(move.getUnit() instanceof Avatar)
                {
                    score= (move.getUnit().getHealth()-AIunit.getAttack())*4;
                    if(score<0)
                        score=score*(-1);
                    try {Thread.sleep(40);} catch (InterruptedException e) {e.printStackTrace();}
                }
                else if(move.getUnit() instanceof Unit)
                {
                    score= (move.getUnit().getHealth()-AIunit.getAttack())*2;
                    if(score<0)
                        score=score*(-1);
                    try {Thread.sleep(40);} catch (InterruptedException e) {e.printStackTrace();}
                }

            
        }
        // AI card units
        else if(move.getUnit()==null)
        {
            //BasicCommands.addPlayer1Notification(out, "Check 5", 5);
            score=1;
            try {Thread.sleep(40);} catch (InterruptedException e) {e.printStackTrace();}
        }

        if(score>maxScore) //selecting that tile which has the highest score
        {
            //BasicCommands.addPlayer1Notification(out, "Check 6", 5);
            maxScore=score;
            selectedTile=move;
            try {Thread.sleep(40);} catch (InterruptedException e) {e.printStackTrace();}
        }
    }

    if (selectedTile != null && selectedTile.getUnit()!=null) {
        //BasicCommands.addPlayer1Notification(out, "INside selected loop", 5);
        try {Thread.sleep(40);} catch (InterruptedException e) {e.printStackTrace();}
        if(gameState.getNearTiles(unitTile,gameState).contains(selectedTile)){
            if(grid.getPlayerOneUnitsOnBoard().contains(selectedTile.getUnit())){
                AIunit.attack(selectedTile.getUnit(), gameState, out);
                if(selectedTile.getUnit()!=null)
                    selectedTile.getUnit().counterAttack(AIunit,gameState,out);
                try {Thread.sleep(40);} catch (InterruptedException e) {e.printStackTrace();}
            }
        } else {
            // AIunit.moveUnit(selectedTile, out, gameState);
            //BasicCommands.addPlayer1Notification(out, "INside else", 5);
            AIunit.moveAndAttackUnit(selectedTile, out, gameState);
            if(selectedTile.getUnit()!=null)
                selectedTile.getUnit().counterAttack(AIunit,gameState,out);
            try {Thread.sleep(40);} catch (InterruptedException e) {e.printStackTrace();}
        }
    }
    else if (selectedTile!=null)
    {
            //BasicCommands.addPlayer1Notification(out, "Inside else", 5);
            
            try {
                Thread.sleep(40);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            AIunit.move(selectedTile, gameState, out);
    }

    try {Thread.sleep(200);} catch (InterruptedException e) {e.printStackTrace();}

    //gameState.drawDefaultTilesGrid(out);
    gameState.displayGridBoard(out);
    try {Thread.sleep(40);} catch (InterruptedException e) {e.printStackTrace();}
}


// =======================================================================================================================================================
// ========================================================Card Functions ==============================================================================================
// =======================================================================================================================================================

    /**
     * We create a card list from getCardInhand(). We also locally store the mana of AI avatar.
     * We traverse through the loop to see which cards can actually be played for the respective mana.
     * Spell card are considered separately as they don't produce unit, for them different conditions are written below.
     * We then call the helper function to implement the cards.
     * @param out
     * @param gameState hold game state information
     */

    public static void executeAICard(ActorRef out, GameState gameState)
    { 
        //BasicCommands.addPlayer1Notification(out, "Inside execute card", 5);
        ArrayList<Card> AIcards = gameState.getPlayerTwo().getCardInhand();
        //Collections.shuffle(AIcards);
        ArrayList<Card> eligibleCardsUnits= new ArrayList<>();
        Tile edTile=null  ;
        Tile syTile=null  ;
        // calling the function based on the mana availabe to AI player
        int mana= gameState.getPlayerTwo().getMana();
        for(Card c: AIcards)
        {
            //checking the mana, and making sure that cards are not spell cards
            if(c.getManacost()<=mana && (!c.getCardname().equals("Entropic Decay")) && (!c.getCardname().equals("Staff of Y'Kir'")))
            {
                eligibleCardsUnits.add(c);  // this is making sure that only unit cards are added
            }

            // need a condition
            if(c.getManacost()<=mana)
            {
                if(gameState.getPlayerOne().getHealth()<=8 && gameState.getPlayerOne().getHealth()>=5)
                {
                    if((c.getCardname().equals("Entropic Decay")))
                    {
                        edTile= executeED(out,gameState);
                        if(edTile!=null)
                            c.summonUnitFromCard(out, gameState,edTile);
                    }
                }
                
                if(gameState.getPlayerOne().getHealth()<=10 && gameState.getPlayerOne().getHealth()>=7)
                {
                    if((c.getCardname().equals("Staff of Y'Kir'")))
                    {
                        //BasicCommands.addPlayer1Notification(out, "Staff of Y'Kir", 5);
                        try {Thread.sleep(40);} catch (InterruptedException e) {e.printStackTrace();}

                        syTile=executeSY(out,gameState);
                        if(syTile!=null)
                            c.summonUnitFromCard(out,gameState,syTile);
                    }
                }
            }

        }

        //Card bestCard=
        helperExecutingCards(out,eligibleCardsUnits,gameState);    
    }

    /**
     * The functions takes a list of eligible cards which can be used to play. After that we will pick out one card which has the most attack power
     * The that card is sent to exectute best card.
     * After executing that card, we reduce the specific mana from the AI mana
     * @param eligibleCardsUnits takes into account the cards which can be played for the available mana
     * @param out
     * @param gameState hold game state information
     */
    public static void helperExecutingCards(ActorRef out,ArrayList<Card> eligibleCardsUnits, GameState gameState)
    {
        //BasicCommands.addPlayer1Notification(out, "Inside helper executing card function", 5);
        try {Thread.sleep(40);} catch (InterruptedException e) {e.printStackTrace();}
        Card bestCard = null; //storing the appropriate card which will be used to execute
        int maxAttack = -1;
        int maxHealth = -1;
        maxHealth=eligibleCardsUnits.get(0).getBigCard().getHealth(); // storing the first card health
        maxAttack=eligibleCardsUnits.get(0).getBigCard().getAttack();
        for (Card card : eligibleCardsUnits) {
            
            if ((card.getBigCard().getAttack() > maxAttack) || (card.getBigCard().getHealth()>maxHealth)) {
                bestCard = card;
                maxAttack = card.getBigCard().getAttack();
                maxHealth = card.getBigCard().getHealth();
            }
        }    
        // after getting the best card we will determine the tile in which this card unit can be placed
        // the tile selection will depends on the player units which is enemy and AI units which is friendly unit
        // we will use the tiles near the AI avatar to place the units, planar scout will be placed near to player unit
        // because the main agenda from the perspective of AI is the defeat of player avatar
        
        executeBestCard(out,bestCard,gameState);
        gameState.getPlayerTwo().getCardInhand().remove(bestCard);

        gameState.getPlayerTwo().setMana(gameState.getPlayerTwo().getMana()-bestCard.getManacost());
        //BasicCommands.setPlayer2Mana(out, gameState.getPlayerTwo());
   	    try {Thread.sleep(30);} catch (InterruptedException e) {e.printStackTrace();}
        //return bestCard;
    }

    /**
     * This function will help in executing the card function. First we determine the card name and match it with names we have
     * Accordingly we call the functions to determine the tile in which card units can be placed
     * We will check whether card can be placed on the tile. If yes. we call the function summonUNitFromCard to invoke the functionality of card
     *
     * @param bestCard the card which was selected based on attack power
     * @param out holds actor ref information
     * @param gameState hold game state information
     */
    public static void executeBestCard(ActorRef out,Card bestCard, GameState gameState)
    {
        //BasicCommands.addPlayer1Notification(out, "Inside executebestcard ", 5);
        try {Thread.sleep(40);} catch (InterruptedException e) {e.printStackTrace();}
        ArrayList<Unit> enemyUnits = new ArrayList<>(gameState.getGrid().getPlayerOneUnitsOnBoard());
        ArrayList<Unit> friendlyUnits = new ArrayList<>(gameState.getGrid().getPlayerTwoUnitsOnBoard());

        Tile cardUnitToBePlaced=null;
        //Planar Scout
        if(bestCard.getCardname().equals("Planar Scout"))
        {
            //BasicCommands.addPlayer1Notification(out, "Inside if PS ", 5);
        try {Thread.sleep(40);} catch (InterruptedException e) {e.printStackTrace();}
            cardUnitToBePlaced=TileforPlanarScout(out,bestCard,gameState,enemyUnits,friendlyUnits);
            if(cardUnitToBePlaced!=null)
                bestCard.summonUnitFromCard(out,gameState,cardUnitToBePlaced);
        }

        //Rock Pulveriser
        if(bestCard.getCardname().equals("Rock Pulveriser"))
        {
        //BasicCommands.addPlayer1Notification(out, "Inside if RP ", 5);
        try {Thread.sleep(40);} catch (InterruptedException e) {e.printStackTrace();}
            cardUnitToBePlaced=TileforRockP(out,bestCard,gameState,enemyUnits,friendlyUnits);
            if(cardUnitToBePlaced!=null)
                bestCard.summonUnitFromCard(out,gameState,cardUnitToBePlaced);
        }

        //TileforPyromancer
        if(bestCard.getCardname().equals("Pyromancer"))
        {
            //BasicCommands.addPlayer1Notification(out, "Inside if P ", 5);
            try {Thread.sleep(40);} catch (InterruptedException e) {e.printStackTrace();}
            cardUnitToBePlaced=TileforPyromancer(out,bestCard,gameState,enemyUnits,friendlyUnits);
            if(cardUnitToBePlaced!=null)
                bestCard.summonUnitFromCard(out,gameState,cardUnitToBePlaced);
        }

        //Tile for Bloodshard Golem 
        if(bestCard.getCardname().equals("Bloodshard Golem"))
        {
            //BasicCommands.addPlayer1Notification(out, "Inside if PS ", 5);
            try {Thread.sleep(40);} catch (InterruptedException e) {e.printStackTrace();}
            cardUnitToBePlaced=TileforBG(out,bestCard,gameState,enemyUnits,friendlyUnits);
            if(cardUnitToBePlaced!=null)
                bestCard.summonUnitFromCard(out,gameState,cardUnitToBePlaced);
        }
        //Tile for Hailstone Golem
        if(bestCard.getCardname().equals("Hailstone Golem"))
        {
        //BasicCommands.addPlayer1Notification(out, "Inside if HG ", 5);
        try {Thread.sleep(40);} catch (InterruptedException e) {e.printStackTrace();}
            cardUnitToBePlaced=TileforHG(out,bestCard,gameState,enemyUnits,friendlyUnits);
            if(cardUnitToBePlaced!=null)
                bestCard.summonUnitFromCard(out,gameState,cardUnitToBePlaced);
        }

        //Tile for Blaze Hound
        if(bestCard.getCardname().equals("Blaze Hound"))
        {
            //BasicCommands.addPlayer1Notification(out, "Inside if BH ", 5);
        try {Thread.sleep(40);} catch (InterruptedException e) {e.printStackTrace();}
            cardUnitToBePlaced=TileforBH(out,bestCard,gameState,enemyUnits,friendlyUnits);
            if(cardUnitToBePlaced!=null)
                bestCard.summonUnitFromCard(out,gameState,cardUnitToBePlaced);
        }

        //Tile for WindShrike
        if(bestCard.getCardname().equals("Windshrike"))
        {
        //BasicCommands.addPlayer1Notification(out, "Inside if WS ", 5);
        try {Thread.sleep(40);} catch (InterruptedException e) {e.printStackTrace();}
            cardUnitToBePlaced=TileforWS(out,bestCard,gameState,enemyUnits,friendlyUnits);
            if(cardUnitToBePlaced!=null)
                bestCard.summonUnitFromCard(out,gameState,cardUnitToBePlaced);
        }

        //Tile for Serpenti
        if(bestCard.getCardname().equals("Serpenti"))
        {
        //BasicCommands.addPlayer1Notification(out, "Inside if S ", 5);
        try {Thread.sleep(40);} catch (InterruptedException e) {e.printStackTrace();}
            cardUnitToBePlaced=TileforPyromancer(out,bestCard,gameState,enemyUnits,friendlyUnits);
            if(cardUnitToBePlaced!=null)
                bestCard.summonUnitFromCard(out,gameState,cardUnitToBePlaced);
        }
        
    }

    /*
    * =============================================Selecting tiles for respective cards =============================
    * Below are the functions for every card which will be placed according to their requirement
    * */

    /**
     * The card needs to be placed near player units that's why enemy units are considered
     * We will traverse through unit and get the near by tiles which are available
     * one of those tiles is selected and used to summon planar scout unit
     * THe rest cards need to be summoned near AI avatar that’s why we considered the friendly units
     * @param out actor ref info
     * @param bestCard the card which was selected
     * @param gameState gamestate info
     * @param enemyUnits enemy units which is player 1 units
     * @param friendlyUnits AI units
     * @return
     */
    public static Tile TileforPlanarScout( ActorRef out,Card bestCard,GameState gameState, ArrayList<Unit> enemyUnits, ArrayList<Unit> friendlyUnits)
    {
        //BasicCommands.addPlayer1Notification(out, "Inside Planar scout function", 5);
        try {Thread.sleep(40);} catch (InterruptedException e) {e.printStackTrace();}
        List<Tile> nearByTiles= null;
        //List<Unit> enemyUnits= gameState.getGrid().getPlayerOneUnitsOnBoard();
        //List<Unit> friendlyUnits= gameState.getGrid().getPlayerTwoUnitsOnBoard();
        Position avatarPos = null;
        Tile cardUnitToBePlaced=null;
        Tile selected=null;
        for(Unit e:enemyUnits)
        {
            if(e instanceof Avatar)
            {
                avatarPos=e.getPosition();
                selected= gameState.getGrid().getTile(avatarPos.getTilex(),avatarPos.getTiley());
            }
        }

        nearByTiles=gameState.getNearTiles(selected,gameState);
        for(Tile t: nearByTiles)
        {
            if(t.getUnit()==null)
                return t;
        }
        return null;
    }
    /**
     *
     * @param out actor ref info
     * @param bestCard the card which was selected
     * @param gameState gamestate info
     * @param enemyUnits enemy units which is player 1 units
     * @param friendlyUnits AI units
     * @return
     */
    public static Tile TileforRockP(ActorRef out,Card bestCard,GameState gameState, ArrayList<Unit> enemyUnits, ArrayList<Unit> friendlyUnits)
    {
        //BasicCommands.addPlayer1Notification(out, "RockP function", 5);
        try {Thread.sleep(40);} catch (InterruptedException e) {e.printStackTrace();}
        List<Tile> nearByTiles= null;
        //List<Unit> enemyUnits= gameState.getGrid().getPlayerOneUnitsOnBoard();
        //List<Unit> friendlyUnits= gameState.getGrid().getPlayerTwoUnitsOnBoard();
        Position avatarPos = null;
        Tile cardUnitToBePlaced=null;
        Tile selected=null;
        for(Unit f: friendlyUnits)
        {
            // if(f instanceof Avatar)
            // {
                avatarPos=f.getPosition();
                selected= gameState.getGrid().getTile(avatarPos.getTilex(),avatarPos.getTiley());
                for(Tile t: nearByTiles)
                {
                    if(t.getUnit()!=null)
                        continue;
                    else
                        return t;
                }
            //}
        }

        // nearByTiles=gameState.getNearTiles(selected,gameState);
        // for(Tile t: nearByTiles)
        // {
        //     if(t.getUnit()==null)
        //         return t;
        // }

        return null;
    }

    /**
     *
     * @param out actor ref info
     * @param bestCard the card which was selected
     * @param gameState gamestate info
     * @param enemyUnits enemy units which is player 1 units
     * @param friendlyUnits AI units
     * @return
     */
    //focusing to attack on player avatar, and waiting for the card to be created
    // if time persist, we can change the functionality
    public static Tile TileforPyromancer(ActorRef out,Card bestCard,GameState gameState, ArrayList<Unit> enemyUnits, ArrayList<Unit> friendlyUnits)
    {
        //BasicCommands.addPlayer1Notification(out, "TileforPyromancer function/ Serpenti", 5);
        try {Thread.sleep(40);} catch (InterruptedException e) {e.printStackTrace();}
        List<Tile> nearByTiles= null;
        //List<Unit> enemyUnits= gameState.getGrid().getPlayerOneUnitsOnBoard();
        //List<Unit> friendlyUnits= gameState.getGrid().getPlayerTwoUnitsOnBoard();
        Position avatarPos = null;
        Tile cardUnitToBePlaced=null;
        Tile selected=null;
        for(Unit e: enemyUnits)
        {
            // if(f instanceof Avatar)
            // {
                avatarPos=e.getPosition();
                selected= gameState.getGrid().getTile(avatarPos.getTilex(),avatarPos.getTiley());
                nearByTiles=gameState.getNearTiles(selected,gameState);
                for(Tile t: nearByTiles)
                {
                    if(t.getUnit()!=null)
                        continue;
                    else
                        return t;
                }
            // }
        }

        // nearByTiles=gameState.getNearTiles(selected,gameState);
        // for(Tile t: nearByTiles)
        // {
        //     if(t.getUnit()==null)
        //         return t;
        // }

        return null;
    }

    /**
     *
     * @param out actor ref info
     * @param bestCard the card which was selected
     * @param gameState gamestate info
     * @param enemyUnits enemy units which is player 1 units
     * @param friendlyUnits AI units
     * @return
     */
    public static Tile TileforBH(ActorRef out,Card bestCard,GameState gameState,ArrayList<Unit> enemyUnits,ArrayList<Unit> friendlyUnits)
    {
        //BasicCommands.addPlayer1Notification(out, "TileforBH", 5);
        try {Thread.sleep(40);} catch (InterruptedException e) {e.printStackTrace();}
        List<Tile> nearByTiles= null;
        //List<Unit> enemyUnits= gameState.getGrid().getPlayerOneUnitsOnBoard();
        //List<Unit> friendlyUnits= gameState.getGrid().getPlayerTwoUnitsOnBoard();
        Position avatarPos = null;
        Tile cardUnitToBePlaced=null;
        Tile selected=null;
        for(Unit f: friendlyUnits)
        {
            avatarPos=f.getPosition();
            selected= gameState.getGrid().getTile(avatarPos.getTilex(),avatarPos.getTiley());
            nearByTiles=gameState.getNearTiles(selected,gameState);
                for(Tile t: nearByTiles)
                {
                    if(t.getUnit()!=null)
                        continue;
                    else
                        return t;
                }
        }

        // nearByTiles=gameState.getNearTiles(selected,gameState);
        // for(Tile t: nearByTiles)
        // {
        //     if(t.getUnit()==null)
        //         return t;
        // }

        return null;
    }

    /**
     *
     * @param out actor ref info
     * @param bestCard the card which was selected
     * @param gameState gamestate info
     * @param enemyUnits enemy units which is player 1 units
     * @param friendlyUnits AI units
     * @return
     */
    public static Tile TileforBG(ActorRef out,Card bestCard,GameState gameState,ArrayList<Unit> enemyUnits,ArrayList<Unit> friendlyUnits)
    {
        //BasicCommands.addPlayer1Notification(out, "TileforBG", 5);
        try {Thread.sleep(40);} catch (InterruptedException e) {e.printStackTrace();}
        List<Tile> nearByTiles= null;
        //List<Unit> enemyUnits= gameState.getGrid().getPlayerOneUnitsOnBoard();
        //List<Unit> friendlyUnits= gameState.getGrid().getPlayerTwoUnitsOnBoard();
        Position avatarPos = null;
        Tile cardUnitToBePlaced=null;
        Tile selected=null;
        int flag=0;
        for(Unit f: friendlyUnits)
        {
            // if(f instanceof Avatar)
            // {
                avatarPos=f.getPosition();
                selected= gameState.getGrid().getTile(avatarPos.getTilex(),avatarPos.getTiley());
                nearByTiles=gameState.getNearTiles(selected,gameState);
                for(Tile t: nearByTiles)
                {
                    if(t.getUnit()!=null)
                        continue;
                    else
                        return t;
                }
            // }
            // if(flag==1)
            //     continue;
        }

        // nearByTiles=gameState.getNearTiles(selected,gameState);
        // for(Tile t: nearByTiles)
        // {
        //     if(t.getUnit()==null)
        //         return t;
        // }

        return null;
    }

    /**
     *
     * @param out actor ref info
     * @param bestCard the card which was selected
     * @param gameState gamestate info
     * @param enemyUnits enemy units which is player 1 units
     * @param friendlyUnits AI units
     * @return
     */
    public static Tile TileforHG(ActorRef out,Card bestCard,GameState gameState,ArrayList<Unit> enemyUnits,ArrayList<Unit> friendlyUnits)
    {
        //BasicCommands.addPlayer1Notification(out, "TileforHG", 5);
        try {Thread.sleep(40);} catch (InterruptedException e) {e.printStackTrace();}
        List<Tile> nearByTiles= null;
        //List<Unit> enemyUnits= gameState.getGrid().getPlayerOneUnitsOnBoard();
        //List<Unit> friendlyUnits= gameState.getGrid().getPlayerTwoUnitsOnBoard();
        Position avatarPos = null;
        Tile cardUnitToBePlaced=null;
        Tile selected=null;
        for(Unit f: friendlyUnits)
        {
            // if(f instanceof Unit)
            // {
                avatarPos=f.getPosition();
                selected= gameState.getGrid().getTile(avatarPos.getTilex(),avatarPos.getTiley());
            // }
            nearByTiles=gameState.getNearTiles(selected,gameState);
                for(Tile t: nearByTiles)
                {
                    if(t.getUnit()!=null)
                        continue;
                    else
                        return t;
                }
        }

        // nearByTiles=gameState.getNearTiles(selected,gameState);
        // for(Tile t: nearByTiles)
        // {
        //     if(t.getUnit()==null)
        //         return t;
        // }

        return null;
    }

    /**
     *
     * @param out actor ref info
     * @param bestCard the card which was selected
     * @param gameState gamestate info
     * @param enemyUnits enemy units which is player 1 units
     * @param friendlyUnits AI units
     * @return
     */
    //Windshrike
    public static Tile TileforWS(ActorRef out,Card bestCard,GameState gameState,ArrayList<Unit> enemyUnits,ArrayList<Unit> friendlyUnits)
    {
        //BasicCommands.addPlayer1Notification(out, "TileforWS", 5);
        try {Thread.sleep(40);} catch (InterruptedException e) {e.printStackTrace();}
        List<Tile> nearByTiles= null;
        //List<Unit> enemyUnits= gameState.getGrid().getPlayerOneUnitsOnBoard();
        //List<Unit> friendlyUnits= gameState.getGrid().getPlayerTwoUnitsOnBoard();
        Position avatarPos = null;
        Tile cardUnitToBePlaced=null;
        Tile selected=null;
        for(Unit e: enemyUnits)
        {
            // if(e instanceof Unit)
            // {
                avatarPos=e.getPosition();
                selected= gameState.getGrid().getTile(avatarPos.getTilex(),avatarPos.getTiley());
            //}
            nearByTiles=gameState.getNearTiles(selected,gameState);
                for(Tile t: nearByTiles)
                {
                    if(t.getUnit()!=null)
                        continue;
                    else
                        return t;
                }
        }

        // nearByTiles=gameState.getNearTiles(selected,gameState);
        // for(Tile t: nearByTiles)
        // {
        //     if(t.getUnit()==null)
        //         return t;
        // }

        return null;
    }

    // these are the functions to execute the spell card
    /**
     *
     * @param out actor ref info
     * @param gameState gamestate info
     * @return
     */
    public static Tile executeED(ActorRef out, GameState gameState)
    {
        List<Unit> enemyUnits= gameState.getGrid().getPlayerOneUnitsOnBoard();
        
        for(Unit e: enemyUnits)
        {
            if(!(e instanceof Avatar) && e.getHealth()>10)
            {
                return gameState.getGrid().getTile(e.getPosition().getTilex(), e.getPosition().getTiley());
            }
        }

        return null;

    }

    /**
     *
     * @param out actor ref info
     * @param gameState gamestate info
     * @return
     */
    public static Tile executeSY(ActorRef out, GameState gameState)
    {
        List<Unit> enemyUnits = gameState.getGrid().getPlayerOneUnitsOnBoard();
        List<Unit> friendlyUnits = gameState.getGrid().getPlayerTwoUnitsOnBoard();
        //friendlyUnits = gameState.getBoard().getPlayer2Units();

        for(Unit f: friendlyUnits)
        {
            if(f instanceof Avatar)
            {
                if(enemyUnits.size()<6)
                    return gameState.getGrid().getTile(f.getPosition().getTilex(),f.getPosition().getTiley());
            }
        }
        return null; 
    }
    
}