package structures.basic;

import java.util.List;
import java.util.ArrayList;

import commands.BasicCommands;
import structures.GameState;
import akka.actor.ActorRef;
import structures.units.PurebladeEnforcer;
import utils.BasicObjectBuilders;
import utils.StaticConfFiles;


/**
 * This is the base representation of a Card which is rendered in the player's hand.
 * A card has an id, a name (cardname) and a manacost. A card then has a large and mini
 * version. The mini version is what is rendered at the bottom of the screen. The big
 * version is what is rendered when the player clicks on a card in their hand.
 *
 * @author Dr. Richard McCreadie
 *
 */
public class Card {
    
    int id;
    
    String cardname;
    int manacost;
    
    MiniCard miniCard;
    BigCard bigCard;
    

    public Card() {};
    
    public Card(int id, String cardname, int manacost, MiniCard miniCard, BigCard bigCard) {
   	 super();
   	 this.id = id;
   	 this.cardname = cardname;
   	 this.manacost = manacost;
   	 this.miniCard = miniCard;
   	 this.bigCard = bigCard;
    }
    
    public int getId() {
   	 return id;
    }
    public void setId(int id) {
   	 this.id = id;
    }
    public String getCardname() {
   	 return cardname;
    }
    public void setCardname(String cardname) {
   	 this.cardname = cardname;
    }
    public int getManacost() {
   	 return manacost;
    }
    public void setManacost(int manacost) {
   	 this.manacost = manacost;
    }
    public MiniCard getMiniCard() {
   	 return miniCard;
    }
    public void setMiniCard(MiniCard miniCard) {
   	 this.miniCard = miniCard;
    }
    public BigCard getBigCard() {
   	 return bigCard;
    }
    public void setBigCard(BigCard bigCard) {
   	 this.bigCard = bigCard;
    }


    //Yao

	/**
	 * This method is the main method we use to summon unit from card; mana will be checked as condition;
	 * also after summoning the card, mana, hand card, and tile highlight will also be changed on front end and back end
	 * @param out
	 * @param gameState
	 * @param tile: here tile refers to the tile that can place unit
	 */
    public void summonUnitFromCard(ActorRef out, GameState gameState, Tile tile) {
   	 //summon the unit only when players have enough mana
   	 if((gameState.isClickable && gameState.getPlayerOne().getMana() >= manacost) ||
   			 (!gameState.isClickable && gameState.getPlayerTwo().getMana() >= manacost)){
   		 //part 1:summon card
   		 //if it's a unit card
   		 if(bigCard.getHealth() != -1) {
   			 summonUnit(out, gameState, tile);
   		 }
   		 // if it's a spell card
        	else {
   			 castSpell(out, gameState, tile);
   		 }
   		 //part 2:
   		 //change mana,card in hand and tile value accordingly for player one
   		 if(gameState.isClickable){
   			 // changePlayerMana
   			 changePlayerMana(out, gameState);
			 gameState.cardExecuted=true;
   			 //delete card and re-display hand cards
   			 changeHandCard(out, gameState);

   			 //clear last clicked card
   			 gameState.clearCardLastClicked();
   		 }
			
   		 //clear highlighted effect for clicked card and tiles on board
   		 clearHighlightedTiles(out, gameState);
   		 gameState.setClickedHandPosition(-1);
   	 } else {
   		 //when there's no enough mana for player 1
   		 if(gameState.isClickable){
   			 BasicCommands.addPlayer1Notification(out, "Insufficient mana", 3);
   			 try {Thread.sleep(30);} catch (InterruptedException e) {e.printStackTrace();}
   		 }
   	 }
    }

    //Yao
    //This (sub)method is used to summon unit card; when clicking a card's related unit; the related unit will be loaded and summoned
    private void summonUnit(ActorRef out, GameState gameState, Tile tile) {
   	 //display effect animation when summoning a unit
   	 BasicCommands.playEffectAnimation(out, BasicObjectBuilders.loadEffect(StaticConfFiles.f1_summon), tile);
   	 try {Thread.sleep(30);} catch (InterruptedException e) {e.printStackTrace();}
   	 try {
   		 Unit newUnit = gameState.loadUnitByCard(this);
   		 // when it's playerOne's turn
   		 if (gameState.isClickable) {
   			 newUnit.summon(out, tile, gameState.getPlayerOne(), gameState.getGrid());
				//check if unit is azureherald and use ability
   			 azureHerald(out, gameState);
   			 // when it's playerTwo's turn
   		 } else {
   			 newUnit.summon(out, tile, gameState.getPlayerTwo(), gameState.getGrid());
				//check if unit is blazehound and use ability
   			 blazeHound(out, gameState);
   		 }
   	 } catch (Exception e) {
   		 e.printStackTrace();
   	 }
    }

	/**
	 * This method is called and check if the summoned Unit is azure herald,
	 * if so directly cast its ability to heal Player avatar +3
	 * @param out used to send messages to the front-end UI
	 * @param gameState holds gamestate information
	 */
    private void azureHerald(ActorRef out, GameState gameState) {    
   		  //Azure Herald's method when summoned, avatar heals +3
   		  if(cardname.equals("Azure Herald")) {
   			  List<Unit> playerUnits = null;
   			  Unit avatar = null;
   			 
   			  //get Player One's units
   			  playerUnits = gameState.getGrid().getPlayerOneUnitsOnBoard();
   			  //check for Avatar
   			  for(Unit unit : playerUnits) {    
   				  if(unit instanceof Avatar) {    
   					  avatar = unit;
   				  }
   			  }
   			  assert avatar != null;
   			  //heal avatar
   			  avatar.heal(out, gameState, 3); }
    }

	/**
	 * This method is called and check if the summoned Unit is blaze hound,
	 * if so directly make both players draw a card
	 * @param out used to send messages to the front-end UI
	 * @param gameState holds gamestate information
	 */
	private void blazeHound(ActorRef out, GameState gameState) {
   	 
   		  //Blaze Hound's method when summoned, both players draw a card
   		  if(cardname.equals("Blaze Hound")) {
				 /*
   			  List<Unit> playerUnits = null;
   			  Player player = null;
   			  Unit avatar = null;
   			  //get Player One and Two's units
   			  playerUnits = gameState.getGrid().getPlayerOneUnitsOnBoard();
   			  playerUnits.addAll(gameState.getGrid().getPlayerTwoUnitsOnBoard());
   			  for(Unit unit : playerUnits) {    
   				  if(unit instanceof Avatar) {    
   					  avatar = unit;
   				  }
   			  }
   			  //player One
   			  player = ((Avatar) avatar).getPlayer();
   			  player.drawCard(out);
   			  if(player.getPlayerNumber() == 1) {    
   				  gameState.showHandCards(out, player);
   			  }
				  */
			  gameState.getPlayerOne().drawCard(out);
			  gameState.getPlayerTwo().drawCard(out);
			  gameState.showHandCards(out, gameState.getPlayerOne());
   		  }
    }



    //Yao
    //it is a (sub)method for summonUnitFromCard; it's used to cast spell directly but in updated tileClicked event logic, you need to make change to it
    private void castSpell(ActorRef out, GameState gameState, Tile tile) {
   	 //if the AI player casts a spell, the unit(Pureblade Enforcer) will be triggered
   	 if(!gameState.isClickable) {
   		 List<Unit> units = gameState.getGrid().getPlayerOneUnitsOnBoard();
   		 for(int i = 0; i < units.size(); i++) {
   			 if (units.get(i) instanceof PurebladeEnforcer) {
   				 ((PurebladeEnforcer) units.get(i)).causeEffect(out);
   			 }
   		 }
   	 }
   	 //for both player1 and player2, we need to load spell card
   	 //first of all, we need to use effect animation
   	 String spellName = getCardname();
   	 if(spellName.equals("Truestrike")) {
   		 BasicCommands.playEffectAnimation(out, BasicObjectBuilders.loadEffect(StaticConfFiles.f1_inmolation), tile);
   		 try {Thread.sleep(250);} catch (InterruptedException e) {e.printStackTrace();}
   	 } else if (spellName.equals("Sundrop Elixir") || spellName.equals("Staff of Y'Kir")) {
   		 BasicCommands.playEffectAnimation(out, BasicObjectBuilders.loadEffect(StaticConfFiles.f1_buff), tile);
   		 try {Thread.sleep(250);} catch (InterruptedException e) {e.printStackTrace();}
   	 } else if (spellName.equals("Entropic Decay")) {
   		 BasicCommands.playEffectAnimation(out, BasicObjectBuilders.loadEffect(StaticConfFiles.f1_martyrdom), tile);
   		 try {Thread.sleep(250);} catch (InterruptedException e) {e.printStackTrace();}
   	 }
   	 //then we can load the spell
   	 gameState.loadSpellByCard(this).spell(out, gameState, tile);
    }

    //Yao
    //it is a (sub)method for summonUnitFromCard;
    private void changePlayerMana(ActorRef out, GameState gameState) {
   	 gameState.getPlayerOne().setMana(gameState.getPlayerOne().getMana() - manacost);
   	 BasicCommands.setPlayer1Mana(out, gameState.getPlayerOne());
   	 try {Thread.sleep(30);} catch (InterruptedException e) {e.printStackTrace();}
    }

	//Rhea
	//it is a (sub)method for summonUnitFromCard;
    private void changeAIMana(ActorRef out, GameState gameState) {
   	 gameState.getPlayerTwo().setMana(gameState.getPlayerTwo().getMana() - manacost);
   	 BasicCommands.setPlayer2Mana(out, gameState.getPlayerTwo());
   	 try {Thread.sleep(30);} catch (InterruptedException e) {e.printStackTrace();}
    }

    //Yao
    //it is a (sub)method for summonUnitFromCard;
    private void changeHandCard(ActorRef out, GameState gameState) {
   	 BasicCommands.deleteCard(out, gameState.getClickedHandPosition());
   	 try {Thread.sleep(30);} catch (InterruptedException e) {e.printStackTrace();}

   	 gameState.clearHandCards(out,gameState.getPlayerOne());
   	 gameState.getPlayerOne().getCardInhand().remove(this);
   	 gameState.showHandCards(out,gameState.getPlayerOne());
    }
    //Yao
    //it is a (sub)method for summonUnitFromCard;
    private void clearHighlightedTiles(ActorRef out, GameState gameState) {
   	 gameState.getGrid().clearHighlightedTiles();
   	 gameState.displayGridBoard(out);
    }


	/**
	 * when clicking a card we need to see what tiles it can be placed
	 * it has considered two cases: one is for units and spell cards that only affect friendly units,
	 * one is for spell cards that only affect enemy units;
	 * and they will show white and red valid tiles respectively;
	 * the airdrop function has been considered
	 * @param out
	 * @param gameState
	 */

    public void showTilesAvailable(ActorRef out, GameState gameState) {
   	 //clean highlighted tiles on board
   	 gameState.getGrid().clearHighlightedTiles();
   	 gameState.displayGridBoard(out);

   	 List<Unit> friendlyUnits = new ArrayList<>();
   	 List<Unit> enemyUnits = new ArrayList<>();
   	 if (gameState.isClickable == true) {
   		 friendlyUnits = gameState.getGrid().getPlayerOneUnitsOnBoard();
   		 enemyUnits = gameState.getGrid().getPlayerTwoUnitsOnBoard();
   	 } else {
   		 friendlyUnits = gameState.getGrid().getPlayerTwoUnitsOnBoard();
   		 enemyUnits = gameState.getGrid().getPlayerOneUnitsOnBoard();
   	 }

   	 List<Tile> hlTiles = gameState.getGrid().getHighlightedTiles();
   	 //this is for airdrop cards, spell cards and unit cards that only affect friendly units
   	 if (!cardname.equals("Truestrike") && !cardname.equals("Entropic Decay")) {
   		 for (int x = 0; x < gameState.getGrid().getX(); x++) {
   			 for (int y = 0; y < gameState.getGrid().getY(); y++) {
   				 Tile singleTl = gameState.getGrid().getTile(x, y);
   				 //highlight available tiles for units with airdrop ability
   				 if (singleTl.getUnit() == null) {
   					 if (cardname.equals("Ironcliff Guardian") || cardname.equals("Planar Scout")) {
   						 hlTiles.add(singleTl);
   					 }//highlight tiles that only affect friendly units
   				 } else if (friendlyUnits.contains(singleTl.getUnit())) {
   					 //make all tiles with friendly units highlighted so that we can choose one by Sundrop Elixir
   					 if (cardname.equals(("Sundrop Elixir"))) {
   						 hlTiles.add(singleTl);
   						 //Staff of Y'Kir only affects avatar
   					 } else if (cardname.equals("Staff of Y'Kir'") && singleTl.getUnit() instanceof Avatar) {
   						 hlTiles.add(singleTl);
   						 //highlight available tiles for common unit summon, different from above spell card
   					 } else {
   						 int[][] tilesAround = new int[][]{{-1, -1}, {-1, 0}, {-1, 1}, {0, -1}, {0, 1}, {1, -1}, {1, 0}, {1, 1}};
   						 for (int[] tl : tilesAround) {
   							 int posX = x + tl[0];
   							 int posY = y + tl[1];
   							 //ensure we get tiles within the border of board
   							 if (posX >= 0 && posX < gameState.getGrid().getX() && posY >= 0 && posY < gameState.getGrid().getY()) {
   								 // we only highlight empty tile
   								 if (gameState.getGrid().getTile(posX, posY).getUnit() == null) {
   									 hlTiles.add(gameState.getGrid().getTile(posX, posY));
   								 }
   							 }
   						 }
   					 }
   				 }
   			 }
   		 }
   		 if (gameState.isClickable == true) gameState.getGrid().displayHighlightedTiles(out, 1);
   		 //this is for spell cards that only affect enemy units
   	 } else {
   		 for (int x = 0; x < gameState.getGrid().getX(); x++) {
   			 for (int y = 0; y < gameState.getGrid().getY(); y++) {
   				 Tile singleTl = gameState.getGrid().getTile(x,y);
   				 if (enemyUnits.contains(singleTl.getUnit())) {
   					 //make all tiles with enemy units highlighted so that we can choose one by Truestrike
   					 if (cardname.equals("Truestrike")) {
   						 hlTiles.add(gameState.getGrid().getTile(x, y));
   						 //Entropic Decay couldn't be used for avatar
   					 } else if (cardname.equals("Entropic Decay") && gameState.getGrid().getTile(x, y).getUnit() instanceof Avatar == false) {
   						 hlTiles.add(gameState.getGrid().getTile(x, y));
   					 }
   				 }
   			 }
   		 }
   		 if (gameState.isClickable == true) gameState.getGrid().displayHighlightedTiles(out, 2);
   	 }
    }

    //Yao
    //this method is used to get card confFiles using cardname
    public String getCardConf() {
   	 if(cardname.equals("Comodo Charger")) {
   		 return StaticConfFiles.c_comodo_charger;
   	 } else if (cardname.equals("Azure Herald")) {
   		 return StaticConfFiles.c_azure_herald;
   	 } else if (cardname.equals("Azurite Lion")) {
   		 return StaticConfFiles.c_azurite_lion;
   	 } else if (cardname.equals("Fire Spitter")){
   		 return StaticConfFiles.c_fire_spitter;
   	 } else if (cardname.equals("Hailstone Golem")) {
   		 return StaticConfFiles.c_hailstone_golem;
   	 } else if (cardname.equals("Ironcliff Guardian")) {
   		 return StaticConfFiles.c_ironcliff_guardian;
   	 } else if (cardname.equals("Pureblade Enforcer")) {
   		 return StaticConfFiles.c_pureblade_enforcer;
   	 } else if (cardname.equals("Silverguard Knight")) {
   		 return StaticConfFiles.c_silverguard_knight;
   	 } else if (cardname.equals("Blaze Hound")) {
   		 return StaticConfFiles.c_blaze_hound;
   	 } else if (cardname.equals("Bloodshard Golem")) {
   		 return StaticConfFiles.c_bloodshard_golem;
   	 } else if (cardname.equals("Planar Scout")) {
   		 return StaticConfFiles.c_planar_scout;
   	 } else if (cardname.equals("Pyromancer")) {
   		 return StaticConfFiles.c_pyromancer;
   	 } else if (cardname.equals("Rock Pulveriser")) {
   		 return StaticConfFiles.c_rock_pulveriser;
   	 } else if (cardname.equals("Serpenti")) {
   		 return StaticConfFiles.c_serpenti;
   	 } else if (cardname.equals("Windshrike")) {
   		 return StaticConfFiles.c_windshrike;
   	 }
   	 return null;
    }
}


