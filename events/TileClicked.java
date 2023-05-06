package events;


import com.fasterxml.jackson.databind.JsonNode;

import akka.actor.ActorRef;
import com.fasterxml.jackson.module.scala.ser.SymbolSerializer;
import structures.GameState;
import structures.basic.Card;
import structures.basic.Unit;
import structures.basic.Tile;
import java.util.ArrayList;
import java.util.List;

/**
 * Indicates that the user has clicked an object on the game canvas, in this case a tile.
 * The event returns the x (horizontal) and y (vertical) indices of the tile that was
 * clicked. Tile indices start at 1.
 *
 * {
 *   messageType = “tileClicked”
 *   tilex = <x index of the tile>
 *   tiley = <y index of the tile>
 * }
 *
 * @author Dr. Richard McCreadie
 *
 */
public class TileClicked implements EventProcessor {
	// 123
	@Override
	public void processEvent(ActorRef out, GameState gameState, JsonNode message) {
    	int tilex = message.get("tilex").asInt();
    	int tiley = message.get("tiley").asInt();

    	//CUI
    	//get the clicked tile
    	if (gameState.gameInitalised && gameState.isClickable && !gameState.gameOver) {
        	Tile tile = gameState.getGrid().getTile(tilex, tiley);
        	//available tiles when card is clicked
        	List<Tile> hlTiles = gameState.getGrid().getHighlightedTiles();
        	Unit u = tile.getUnit();
        	//if clicked a non highlighted tile
        	if (tile.getMode() == 0) {
				if (gameState.getTileLastClicked() == null) {
					//the player can only click playerone's unit
					//if the player clicked their unit
					if (u != null && gameState.getGrid().getPlayerOneUnitsOnBoard().contains(u)) {
						//if the unit has not moved and attacked
						if (!u.isHasMoved() && !u.isHasAttacked()) {
							//the tiles where the unit can move to should be white highlighted
							// and the unit can attack should be red highlighted
							u.highlightMoveAndAttackableUnits(tile, gameState, out);
							//save the tile last clicked, prepare for the next step
							gameState.setTileLastClicked(tile);
							gameState.setUnitLastclicked(u);
						} else if (u.isHasMoved() && !u.isHasAttacked()) {
							//if the unit has moved but has not attacked, should highlight the tiles it can attack
							u.highlightAttackableUnits(tile, gameState, out);
							gameState.setTileLastClicked(tile);
							gameState.setUnitLastclicked(u);
						}
					}
//            	} else {
//                	//Mode0 and clicked before.
//                	//it's used to make the flow more reasonable, but don't have time to polish code
//            	}
				}
			}
        	//if the color of highlighted tile is white
        	else if (tile.getMode() == 1) {
       		 int clickedHandPosition = gameState.getClickedHandPosition();
            	//when there's no tile clicked before and card has been clicked
            	if (gameState.getTileLastClicked() == null && gameState.getCardLastClicked() != null) {
           		 String lastCardName = gameState.getCardLastClicked().getCardname();
                	if ( lastCardName.equals("Sundrop Elixir") || lastCardName.equals("Staff of Y'Kir")) {
                    	//here should write logic to distinguish which spell card it is and highlight tiles according
                    	//to different spell cards, then implement spell cards
               		 //only Sundrop Elixir and Staff of Y'Kir are spells on friendly units
               		 Card clickedCard = gameState.getPlayerOne().getCardInhand().get(clickedHandPosition - 1);
               		 clickedCard.summonUnitFromCard(out, gameState, tile);
               		 
                	} else {
                    	//clicked normal cards, should summon a unit to the board
                    	if (clickedHandPosition != -1) {
                        	Card clickedCard = gameState.getPlayerOne().getCardInhand().get(clickedHandPosition - 1);
                        	//when the clicked tile is also within the range of highlighted tiles
                        	if (hlTiles.contains(tile)) {
                            	clickedCard.summonUnitFromCard(out, gameState, tile);
                            	//set to default mode in both board and handcard when a non-highlighted tile is clicked
                        	} else {
                            	gameState.getGrid().clearHighlightedTiles();
                            	gameState.displayGridBoard(out);
                            	gameState.setClickedHandPosition(-1);
                        	}
                    	}
                	}
                	//when a tile with unit is clicked
            	} else if (gameState.getTileLastClicked() != null) {
                	//if clicked the player's unit before, it means the player want to move the unit to this tile
                	if (gameState.getUnitLastClicked() != null
                        	&& gameState.getGrid().getPlayerOneUnitsOnBoard().contains(gameState.getUnitLastClicked())) {
                    	//make sure it's a tile without a unit, so it can be moved to
                    	if (tile.getUnit() == null) {
                        	//the first clicked unit should move
                        	gameState.getUnitLastClicked().move(tile, gameState, out);
                        	//reset the tileLastClicked and unitLastClicked
                        	gameState.setTileLastClicked(null);
                        	gameState.setUnitLastclicked(null);
                    	}
                	}
            	}
        	}
        	//if clicked a red highlighted tile
        	else if (tile.getMode() == 2) {
            	//when there's a mode2 tile,it's impossible that nothing was clicked before
            	if (gameState.getTileLastClicked() != null) {
                	//if clicked the player's unit before, it means the player want to move and attack/attack the unit in this tile
                	if (gameState.getUnitLastClicked() != null
                        	&& gameState.getGrid().getPlayerOneUnitsOnBoard().contains(gameState.getUnitLastClicked())) {
                    	// if tile2 has an enemy unit, the first clicked unit should attack the second unit.
                    	if (u != null && gameState.getGrid().getEnemyUnits(gameState.getUnitLastClicked()).contains(u)) {
                        	//if those two tiles are adjacent
                        	if (gameState.getNearTiles(gameState.getTileLastClicked(), gameState).contains(tile)) {
                            	//attack
                            	gameState.getUnitLastClicked().attack(u, gameState, out);
                            	u.counterAttack(gameState.getUnitLastClicked(), gameState, out);
                            	gameState.setTileLastClicked(null);
                            	gameState.setUnitLastclicked(null);
                        	} //if those two are not adjacent, the unit will move and then attack the enemy
                        	else {
                            	gameState.getUnitLastClicked().moveAndAttackUnit(tile, out, gameState);
                            	u.counterAttack(gameState.getUnitLastClicked(), gameState, out);
                            	gameState.setTileLastClicked(null);
                            	gameState.setUnitLastclicked(null);
                        	}
                    	}
                	}
            	}
            	else if (gameState.getTileLastClicked() == null && gameState.getCardLastClicked() != null) {
            	//the only appropriate situation is cilcked a spell card whose target is enemy
            	//that's why the tile is mode2.
           		 //this only applies for Spells cards TrueStrike and Entropic Decay whose target is to the enemy
           		 String lastCardName = gameState.getCardLastClicked().getCardname();
                	if ( lastCardName.equals("Truestrike") || lastCardName.equals("Entropic Decay") ) {    
               		 int clickedHandPosition = gameState.getClickedHandPosition();
               		 Card clickedCard = gameState.getPlayerOne().getCardInhand().get(clickedHandPosition - 1);
               		 clickedCard.summonUnitFromCard(out, gameState, tile); }
            	}
        	}
    	}
	}
}











//        	Tile clickedTile = gameState.getGrid().getTile(tilex, tiley);
//        	List<Tile> hlTiles = gameState.getGrid().getHighlightedTiles();
//
//        	int clickedHandPosition = gameState.getClickedHandPosition();
//        	//when there's any card in hand that has been selected
//        	if(clickedHandPosition >= 1) {
//            	Card clickedCard = gameState.getPlayerOne().getCardInhand().get(clickedHandPosition - 1);
//            	//when the clicked tile is also within the range of highlighted tiles
//            	if(hlTiles.contains(clickedTile)) {
//                	clickedCard.summonUnitFromCard(out, gameState, clickedTile);
//                	//set to default mode in both board and handcard when a non-highlighted tile is clicked
//            	} else {
//                	gameState.getGrid().clearHighlightedTiles();
//                	gameState.displayGridBoard(out);
//                	gameState.setClickedHandPosition(-1);
//            	}
//            	//when no card has been selected before, there are several situations
//            	//1.tile with unit is clicked, we need to highlight its available move tiles
//            	//2.highlighted tile is clicked, we need to move the unit to this tile's position
//            	//3.empty tile is clicked, no effect should be triggered
//        	} else {
//
//        	}




