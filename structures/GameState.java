package structures;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import commands.BasicCommands;
import structures.basic.*;
import akka.actor.ActorRef;
import utils.BasicObjectBuilders;

import java.lang.reflect.Field;
import java.util.*;
import java.util.List;
import structures.basic.Player;
import utils.StaticConfFiles;

/**
 * This class can be used to hold information about the on-going game.
 * Its created with the GameActor.
 * 
 * @author Dr. Richard McCreadie
 *
 */
public class GameState {
	private Grid grid;
	private Player playerOne;
	private Player playerTwo;
	private AI AI;
	private int turn;
	private int clickedHandPosition;
	private int unitID = 0;
	public boolean gameOver = false;
	public boolean gameInitalised = false;
	public boolean isClickable = false;

	//Rhea
	public boolean cardExecuted=false;
	private Card cardLastClicked;
	private Tile tileLastClicked = null;
	private Unit unitLastClicked = null;

	public GameState() {
		grid = new Grid();
		playerOne = new Player(20,0,1);
		playerTwo = new Player(20,0,2);
		AI= new AI();
		turn = 0;
	}

	public Grid getGrid() {
		return grid;
	}
	// Rhea Ajit John
	public AI getAI() {
		return AI;
	}

	public void setAI(AI tempAI)
	{
		this.AI= tempAI;
	}

	public Player getPlayerOne() {
		return playerOne;
	}

	public void setPlayerOne(Player playerOne) {
		this.playerOne = playerOne;
	}

	public Player getPlayerTwo() {
		return playerTwo;
	}

	public void setPlayerTwo(Player playerTwo) {
		this.playerTwo = playerTwo;
	}

	public void setTurn() {
		turn++;
	}
	public void increaseTurn() {
		turn++;
	}

	public int getClickedHandPosition() {
		return clickedHandPosition;
	}

	public void setClickedHandPosition(int clickedHandPosition) {
		this.clickedHandPosition = clickedHandPosition;
	}

	public Card getCardLastClicked() {
		return cardLastClicked;
	}

	public void setCardLastClicked(Card cardLastClicked) {
		this.cardLastClicked = cardLastClicked;
	}
	public void clearCardLastClicked() {
		cardLastClicked = null;
	}

	public int getNewUnitID() {
		return ++unitID;
	}
	public Unit getUnitLastClicked() {
		return unitLastClicked;
	}
	public void setUnitLastclicked(Unit unitLastClicked) {
		this.unitLastClicked = unitLastClicked;
	}

	public Tile getTileLastClicked() {
		return tileLastClicked;
	}

	public void setTileLastClicked(Tile tileLastClicked) {
		this.tileLastClicked = tileLastClicked;
	}

	//this method could be used to show default board and clear highlighted tiles.
	public void displayGridBoard(ActorRef out){
		for(int x = 0; x < getGrid().getX(); x++){
			for(int y = 0; y < getGrid().getY(); y++){
				BasicCommands.drawTile(out, getGrid().getTile(x, y), 0);
				getGrid().getTile(x,y).setMode(0);
				try { Thread.sleep(10); } catch (InterruptedException e) { e.printStackTrace();}
		    }
		}
	}

	//Yao
	//It's used when there's a need to increase player's mana; when it reaches 9, increase should stop.
	public void increasePlayerMana(Player player) {
		if(player.getMana() < 9) {
			//we could not change the round number cause it's fixed in each round, ++round is different from round+1
			player.setMana(turn+1);
		} else {
			player.setMana(9);
		}
	}


	//this method is used to display hand card in front end
	public void showHandCards(ActorRef out, Player player) {
		ArrayList<Card> hand = player.getCardInhand();
		for(int i = 0; i < hand.size(); i++){
			BasicCommands.drawCard(out, hand.get(i),i+1, 0);
		}
	}

	//this method is used to clear hand card in front end
	public void clearHandCards(ActorRef out, Player player) {
		ArrayList<Card> hand = player.getCardInhand();
		for(int i = 0; i < hand.size(); i++){
			BasicCommands.deleteCard(out,i+1);
		}
	}

	//Yao
	//this method is used to load unit
	public Unit loadUnitByCard(Card card) {
		String configFileOfUnit = getCardUnitRelation().get(card.getCardConf());
		unitID = getNewUnitID();

		Unit unit = BasicObjectBuilders.loadUnit(configFileOfUnit, unitID, Unit.class);
		unit.setMaxHealth(card.getBigCard().getHealth());
		unit.setHealth(card.getBigCard().getHealth());
		unit.setAttack(card.getBigCard().getAttack());
		return unit;
	}

	//Yao
	//we store card and unit confFile in a hashmap for corresponding search
	public Map<String, String> getCardUnitRelation() {
		Map<String, String> cuRelationMap = new HashMap<>();
		String[] cards_player1 = {
				StaticConfFiles.c_comodo_charger,
				StaticConfFiles.c_pureblade_enforcer,
				StaticConfFiles.c_fire_spitter,
				StaticConfFiles.c_silverguard_knight,
				StaticConfFiles.c_ironcliff_guardian,
				StaticConfFiles.c_azure_herald,
				StaticConfFiles.c_azurite_lion,
				StaticConfFiles.c_hailstone_golem
		};
		String[] units_player1 = {
				StaticConfFiles.u_comodo_charger,
				StaticConfFiles.u_pureblade_enforcer,
				StaticConfFiles.u_fire_spitter,
				StaticConfFiles.u_silverguard_knight,
				StaticConfFiles.u_ironcliff_guardian,
				StaticConfFiles.u_azure_herald,
				StaticConfFiles.u_azurite_lion,
				StaticConfFiles.u_hailstone_golem
		};
		String[] cards_player2 = {
				StaticConfFiles.c_rock_pulveriser,
				StaticConfFiles.c_bloodshard_golem,
				StaticConfFiles.c_blaze_hound,
				StaticConfFiles.c_windshrike,
				StaticConfFiles.c_pyromancer,
				StaticConfFiles.c_serpenti,
				StaticConfFiles.c_planar_scout,
				StaticConfFiles.c_hailstone_golem
		};
		String[] units_player2 = {
				StaticConfFiles.u_rock_pulveriser,
				StaticConfFiles.u_bloodshard_golem,
				StaticConfFiles.u_blaze_hound,
				StaticConfFiles.u_windshrike,
				StaticConfFiles.u_pyromancer,
				StaticConfFiles.u_serpenti,
				StaticConfFiles.u_planar_scout,
				StaticConfFiles.u_hailstone_golemR
		};
		for(int i = 0; i < 8; i++) {
			if(isClickable) {
				cuRelationMap.put(cards_player1[i], units_player1[i]);
			} else {
				cuRelationMap.put(cards_player2[i], units_player2[i]);
			}
		}
		return cuRelationMap;
	}

	//Yao
	//this method is used to load spell
	public Spell loadSpellByCard(Card card){
		Spell spell = null;
		String spellname = "structures.spells." + card.getCardname();
		spellname = spellname.replace("'", "").replace(" ","");
		try {
			spell = new ObjectMapper().readValue("{}", Class.forName(spellname).asSubclass(Spell.class));
		} catch (ClassNotFoundException | JsonProcessingException e) {
			e.printStackTrace();
		}
		return spell;
	}

	/**
	 * Avatar healing/damage & game win/loss
	 * this method is for player;
	 * it's used when avatar's health has been changed, player's should also be changed accordingly
	 * @param out
	 * @param u: it's the unit that has been attacked
	 * @param healthValue: it's the unit's health
	 */

	public void displayPlayerChange(ActorRef out, Unit u, int healthValue) {
		List<Unit> playerOneUnitsOnBoard = grid.getPlayerOneUnitsOnBoard();
		List<Unit> playerTwoUnitsOnBoard = grid.getPlayerTwoUnitsOnBoard();
		//playerOneUnitsOnBoard.contains(u)
		if (playerOneUnitsOnBoard.contains(u)) {
			playerOne.setHealth(healthValue);
			BasicCommands.setPlayer1Health(out, playerOne);
			try {
				Thread.sleep(30);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			//u instanceof Avatar && healthValue <1
			if (playerOne.getHealth() < 1) {
				gameOver = true;
				isClickable=false;
				stopAllUnit();
				BasicCommands.addPlayer1Notification(out, "you lose", 10);
			}
		} else if (playerTwoUnitsOnBoard.contains(u)) {
			playerTwo.setHealth(healthValue);
			BasicCommands.setPlayer2Health(out, playerTwo);
			try {
				Thread.sleep(30);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (playerTwo.getHealth() < 1) {
				gameOver = true;
				isClickable=false;
				BasicCommands.addPlayer1Notification(out, "you win", 3);
			}
		}
	}

	//CUI
	/**
	 * this method is used to get near 8 tiles
	 * @param tile the center tile, the method will get 8 tiles around it
	 * @param gameState hold game state information
	 * @return an arraylist of tiles
	 */
	public List<Tile> getNearTiles(Tile tile, GameState gameState) {
		//get the coordinate of the tile that we want to get its near tiles
		int x = tile.getUnit().getPosition().getTilex();
		int y = tile.getUnit().getPosition().getTiley();
		ArrayList<Tile> nearTiles = new ArrayList<>();
		for (int i = x - 1; i <= x + 1; i++) {
			for (int j = y - 1; j <= y + 1; j++) {
				if (i >= 0 && i < gameState.getGrid().getX() && j >= 0 && j < gameState.getGrid().getY()) {
					//exclude the tile itself
					if (!(i == x && j == y)) {
						Tile t = gameState.getGrid().getTile(i, j);
						nearTiles.add(t);
					}
				}
			}
		}
		return nearTiles;
	}


	//CUI
	/**
	 * this method is used to check near four tiles(top,bottom,left,right), if they have enemy unit.
	 * @param tile is the tile should be checked if its near four tiles have enemies
	 * @param gameState hold game state information
	 * @return an arraylist named nearFourEnemyTiles, which contain tiles with enemies in those four tiles
	 */
	public List<Tile> getNearFourEnemyTiles(Tile tile, GameState gameState){
		//get the coordinate of the tile we want to check
		int x = tile.getUnit().getPosition().getTilex();
		int y = tile.getUnit().getPosition().getTiley();
		ArrayList<Tile> nearFourEnemyTiles = new ArrayList<>();
		for (int i = x - 1; i <= x + 1; i++) {
			for (int j = y - 1; j <= y + 1; j++) {
				if (i >= 0 && i < gameState.getGrid().getX() && j >= 0 && j < gameState.getGrid().getY()) {
					int a = Math.abs(x - i);
					int b = Math.abs(y - j);
					//check the four tiles,if it's not null and the unit is an enemy unit
					if (a + b == 1 && gameState.getGrid().getTile(i, j).getUnit() != null
							&& gameState.getGrid().getEnemyUnits(tile.getUnit()).contains(gameState.getGrid().getTile(i, j).getUnit())) {
						Tile t = gameState.getGrid().getTile(i, j);
						nearFourEnemyTiles.add(t);
					}
				}
			}
		}
		return nearFourEnemyTiles;
	}

	//Yao
	//it's used in initalize and endturn event to initialize player
	public void setPlayerWithManaAndHealth(ActorRef out, Player playerOne, Player playerTwo) {
		//change backend data
		increaseTurn();
		increasePlayerMana(playerOne);
		increasePlayerMana(playerTwo);

		//show the data change to the frontend using BasicCommands
		BasicCommands.setPlayer1Mana(out, playerOne);
		BasicCommands.setPlayer2Mana(out, playerTwo);
		BasicCommands.setPlayer1Health(out, playerOne);
		BasicCommands.setPlayer2Health(out, playerTwo);
		try {Thread.sleep(30);} catch (InterruptedException e) {e.printStackTrace();}
	}

	//Yao
	//this method is used when gameOver is true
	public void stopAllUnit(){
		for(Unit unit: grid.getPlayerOneUnitsOnBoard()){
			unit.setHasMoved(true);
			unit.setHasAttacked(true);
		}
		for(Unit unit: grid.getPlayerTwoUnitsOnBoard()){
			unit.setHasMoved(true);
			unit.setHasAttacked(true);
		}
	}

}
