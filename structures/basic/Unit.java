package structures.basic;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import akka.actor.ActorRef;
import commands.BasicCommands;
import structures.GameState;
import structures.basic.Avatar;
import structures.units.SilverguardKnight;

import java.util.List;
import java.util.ArrayList;
import static structures.basic.UnitAnimationType.death;

/**
 * This is a representation of a Unit on the game board.
 * A unit has a unique id (this is used by the front-end.
 * Each unit has a current UnitAnimationType, e.g. move,
 * or attack. The position is the physical position on the
 * board. UnitAnimationSet contains the underlying information
 * about the animation frames, while ImageCorrection has
 * information for centering the unit on the tile.
 *
 * @author Dr. Richard McCreadie
 *
 */
public class Unit {

    @JsonIgnore
    protected static ObjectMapper mapper = new ObjectMapper(); // Jackson Java Object Serializer, is used to read java objects from a file
    
    private int id;
    private UnitAnimationType animation;
    private Position position;
    private UnitAnimationSet animations;
    private ImageCorrection correction;
    private int maxHealth;
    private int health;
    protected int attack;
    protected boolean hasAttacked = false;
    protected boolean hasMoved = false;
    private boolean hasCounterAttacked = false;


    public Unit() {}
    
    public Unit(int id, UnitAnimationSet animations, ImageCorrection correction) {
   	 super();
   	 this.id = id;
   	 this.animation = UnitAnimationType.idle;
   	 
   	 position = new Position(0,0,0,0);
   	 this.correction = correction;
   	 this.animations = animations;
    }
    
    public Unit(int id, UnitAnimationSet animations, ImageCorrection correction, Tile currentTile) {
   	 super();
   	 this.id = id;
   	 this.animation = UnitAnimationType.idle;
   	 
   	 position = new Position(currentTile.getXpos(),currentTile.getYpos(),currentTile.getTilex(),currentTile.getTiley());
   	 this.correction = correction;
   	 this.animations = animations;
    }

    public Unit(int id, UnitAnimationType animation, Position position, UnitAnimationSet animations,
   		 ImageCorrection correction) {
   	 super();
   	 this.id = id;
   	 this.animation = animation;
   	 this.position = position;
   	 this.animations = animations;
   	 this.correction = correction;
    }

    public int getId() {
   	 return id;
    }
    public void setId(int id) {
   	 this.id = id;
    }
    public UnitAnimationType getAnimation() {
   	 return animation;
    }
    public void setAnimation(UnitAnimationType animation) {
   	 this.animation = animation;
    }

    public ImageCorrection getCorrection() {
   	 return correction;
    }

    public void setCorrection(ImageCorrection correction) {
   	 this.correction = correction;
    }

    public Position getPosition() {
   	 return position;
    }

    public void setPosition(Position position) {
   	 this.position = position;
    }

    public UnitAnimationSet getAnimations() {
   	 return animations;
    }

    public void setAnimations(UnitAnimationSet animations) {
   	 this.animations = animations;
    }

    public int getMaxHealth() {
   	 return maxHealth;
    }

    public void setMaxHealth(int maxHealth) {
   	 this.maxHealth = maxHealth;
   	 health = maxHealth;
    }

    public int getHealth() {
   	 return health;
    }

    public void setHealth(int health) {
   	 this.health = health;
    }

    public int getAttack() {
   	 return attack;
    }

    public void setAttack(int attack) {
   	 this.attack = attack;
    }

    public boolean isHasAttacked() {
   	 return hasAttacked;
    }

    public void setHasAttacked(boolean hasAttacked) {
   	 this.hasAttacked = hasAttacked;
    }

    public boolean isHasMoved() {
   	 return hasMoved;
    }

    public void setHasMoved(boolean hasMoved) {
   	 this.hasMoved = hasMoved;
    }

    public boolean isHasCounterAttacked(){
   	 return hasCounterAttacked;
    }
    public void setHasCounterAttacked(boolean hasCounterAttacked){
   	 this.hasCounterAttacked = hasCounterAttacked;
    }
	
	//OIC
    //to reset the avatar dealt damage condition
	public void resetAvatarIsAttacked() {    
 		  avatarIsAttacked = false;
   	}

	//OIC: for Provoke Units
	protected boolean canProvoke = false;
	//OIC: for Silverguard Knight
	protected boolean avatarIsAttacked = false;
   


    @JsonIgnore
    public void setPositionByTile(Tile tile) {
   	 position = new Position(tile.getXpos(),tile.getYpos(),tile.getTilex(),tile.getTiley());
    }


	/**this method is used for unit summon purpose; it will be used in Card class's summonUnit method
	 * the summoned unit will be added to related tile, playerUnits, and will be shown on front end.
	 * @param out
	 * @param tile: the tile that the summmoned unit will be placed on
	 * @param player: the player that should summon the unit
	 * @param grid
	 */
    public void summon(ActorRef out, Tile tile, Player player, Grid grid){
   	 if(!(this instanceof Avatar)){
   		 hasAttacked = true;
   		 hasMoved = true;
   	 }
   	 this.setPositionByTile(tile);
   	 tile.addUnit(this);
   	 if(player.getPlayerNumber() == 1 && !(this instanceof Avatar)){
   		 grid.getPlayerOneUnitsOnBoard().add(this);
   	 } else if (player.getPlayerNumber() == 2 && !(this instanceof Avatar)){
   		 grid.getPlayerTwoUnitsOnBoard().add(this);
   	 }
   	 BasicCommands.drawUnit(out, this, tile);
   	 try {Thread.sleep(30);} catch (InterruptedException e) {e.printStackTrace();}
   	 BasicCommands.setUnitHealth(out, this, health);
   	 try {Thread.sleep(30);} catch (InterruptedException e) {e.printStackTrace();}
   	 BasicCommands.setUnitAttack(out, this, attack);
   	 try {Thread.sleep(30);} catch (InterruptedException e) {e.printStackTrace();}

    }

    private List<Tile> whiteHighlighted = new ArrayList<>();

    public List<Tile> getWhiteHighlighted() {
   	 return whiteHighlighted;
    }

    public void clearWhiteHighlighted() {
   	 for (Tile t : whiteHighlighted) {
   		 t.setMode(0);
    }
   	 whiteHighlighted.clear();
    }
    private List<Tile> redHighlighted = new ArrayList<>();

    public void clearRedHighlighted() {
   	 for (Tile t : redHighlighted) {
   		 t.setMode(0);
   	 }
   	 redHighlighted.clear();
    }
    public List<Tile> getRedHighlighted() {
   	 return redHighlighted;
    }
    private List<Tile> tilesToRemove = new ArrayList<>();
    private Tile tileToRemove = null;
	/**
	 * this method is a helper method, it is used to get the tile near tileE, in the direction away from tile,but
	 * in the same line with tile, eg.tile(0,0),tileE(1,0) this method will get a tile whose coordinate is (2,0),
	 * it could help when enemy units is close to player's unit, the movable tiles will be different under this circumstance
	 * @param tile is tile that this player want to check
	 * @param tileE is the neighbor tile of tile
	 * @param gameState hold game state information
	 * @return a tile that should not be white high lighted
	 */
    public Tile getTheNearOne(Tile tile,Tile tileE, GameState gameState){
   	 //get the coordinate of the tile
   	 int x = tile.getTilex();
   	 int y = tile.getTiley();
   	 //get the coordinate of the tileE
   	 int xtileE = tileE.getTilex();
   	 int ytileE = tileE.getTiley();
   	 //when tileE is in different direction with tile, the tile we want to get will have different coordinate
   	 if(xtileE-x==1){
   		 tileToRemove = gameState.getGrid().getTile(x+2,y);
   	 }else if(x-xtileE==1){
   		 tileToRemove = gameState.getGrid().getTile(x-2,y);
   	 }else if(ytileE-y==1){
   		 tileToRemove = gameState.getGrid().getTile(x,y+2);
   	 }else if(y-ytileE==1){
   		 tileToRemove = gameState.getGrid().getTile(x,y-2);
   	 }
   	 return tileToRemove;
    }
	/**
	 * this method is a helper method, it is used to get the tiles near tile1 and tile2, in the direction away from tile,
	 * but in the same line with tile，and also get the tile encircled by tile1 and tile2, eg.tile(0,0),tile1(1,0),tile2(0,1), this
	 * method will get three tiles whose coordinate are (1,1),(2,0),(0,2),it could help when enemy units is close to player's unit,
	 * the movable tiles will be different under this circumstance
	 * @param tile is tile that this player want to check
	 * @param tile1 is one neighbor tile of the tile
	 * @param tile2 is another neighbor tile of the tile
	 * @param gameState hold game state information
	 */
    public void addNearAndDiagonalOne(Tile tile,Tile tile1,Tile tile2,GameState gameState){
   	 //get the coordinate of the tile
   	 int x = tile.getTilex();
   	 int y = tile.getTiley();
   	 //get the coordinate of the tile1
   	 int xtile1 = tile1.getTilex();
   	 int ytile1 = tile1.getTiley();
   	 //get the coordinate of the tile2
   	 int xtile2 = tile2.getTilex();
   	 int ytile2 = tile2.getTiley();
   	 //if those two tiles confront each other, add two tiles near those two tiles into tilesToRemove
   	 if (xtile1 + xtile2 == x + x) {
   		 tilesToRemove.add(getTheNearOne(tile, tile1, gameState));
   		 tilesToRemove.add(getTheNearOne(tile, tile2, gameState));
   	 }//if those two tiles are near each other, add two tiles near those two tiles into tilesToRemove,
   	 //and add the tile encircled by those two tiles into tilesToRemove
   	 else {
   		 tilesToRemove.add(getTheNearOne(tile, tile1, gameState));
   		 tilesToRemove.add(getTheNearOne(tile, tile2, gameState));
   		 //there would be two tiles encircled by tile1 and tile2, they are tile and the tile we want to get,
   		 //if statements will exclude the original tile, the left is the tile need to be obtained
   		 if (x == xtile1 && y == ytile2) {
   			 tilesToRemove.add(gameState.getGrid().getTile(xtile2, ytile1));
   		 } else if (x == xtile2 && y == ytile1) {
   			 tilesToRemove.add(gameState.getGrid().getTile(xtile1, ytile2));
   		 }
   	 }
    }
	/**
	 * click a unit, this method will highlight the tiles where this unit can move to in white,
	 * and highlight the tiles where this unit can attack in red
	 * This method is used when the unit haven't moved or attacked
	 * @param tile is the tile which contains the clicked unit
	 * @param gameState hold game state information
	 * @param out used to send messages to the front-end UI
	 */
    public void highlightMoveAndAttackableUnits(Tile tile, GameState gameState, ActorRef out) {
   	 //x,y is the coordinate of the tile which player want to attack
   	 int x = tile.getTilex();
   	 int y = tile.getTiley();
   	// Rhea Ajit John
	List<Tile> highlightedTiles = gameState.getGrid().getHighlightedTiles();
	//get the tiles in the range. （Diagonal 1 Horizontal 2）
   	 for (int i = x - 2; i <= x + 2; i++) {
   		 for (int j = y - 2; j <= y + 2; j++) {
   			 if(i >= 0 && i < gameState.getGrid().getX() && j >= 0 && j < gameState.getGrid().getY()){
   				 int a = Math.abs(x - i);
   				 int b = Math.abs(y - j);
   				 //if the tile in the range and the tile has no unit, it's a tile where the unit can move to
   				 if (a + b <= 2 && gameState.getGrid().getTile(i, j).getUnit() == null) {
   					 Tile shouldWhiteHighlight = gameState.getGrid().getTile(i, j);
   					 whiteHighlighted.add(shouldWhiteHighlight);
   				 }
   			 }
   		 }
   	 }
   	 //check whether the enemy unit will block the path and some tiles will become not movable due to this
   	 //if the near four tiles(top,bottom,left,right) contains one enemy unit
   	 //the tile near the enemy tile can't be moved to, so the tile should be removed from white highlighted
   	 //use the helper method getTheNearOne
   	 if(gameState.getNearFourEnemyTiles(tile,gameState).size()==1){
   		 Tile tile1 = gameState.getNearFourEnemyTiles(tile,gameState).get(0);
   		 tilesToRemove.add(getTheNearOne(tile,tile1,gameState));
   	 } //if the near four tiles(top,bottom,left,right) contains two enemy units
   	 //use the helper method addNearAndDiagonalOne
   	 else if (gameState.getNearFourEnemyTiles(tile,gameState).size()==2) {
   		 Tile tile1 = gameState.getNearFourEnemyTiles(tile, gameState).get(0);
   		 Tile tile2 = gameState.getNearFourEnemyTiles(tile, gameState).get(1);
   		 addNearAndDiagonalOne(tile,tile1,tile2,gameState);
   	 }//if the near four tiles(top,bottom,left,right) contains three enemy units
   	 //use the helper method addNearAndDiagonalOne
   	 else if (gameState.getNearFourEnemyTiles(tile,gameState).size()==3) {
   		 Tile tile1 = gameState.getNearFourEnemyTiles(tile, gameState).get(0);
   		 Tile tile2 = gameState.getNearFourEnemyTiles(tile, gameState).get(1);
   		 Tile tile3 = gameState.getNearFourEnemyTiles(tile, gameState).get(2);
   		 addNearAndDiagonalOne(tile,tile1,tile2,gameState);
   		 addNearAndDiagonalOne(tile,tile1,tile3,gameState);
   		 addNearAndDiagonalOne(tile,tile2,tile3,gameState);
   	 } //if the near four tiles(top,bottom,left,right) contains four enemy units
   	 //this unit can't move to anywhere, check if it has enemy units around
   	 else if (gameState.getNearFourEnemyTiles(tile,gameState).size()==4) {
   		 whiteHighlighted.clear();
   		 for(Tile t : gameState.getNearTiles(tile,gameState)){
   			 if(t.getUnit()!=null && gameState.getGrid().getEnemyUnits(tile.getUnit()).contains(t.getUnit())){
   				 redHighlighted.add(t);
   			 }
   		 }
   	 }
   	 //remove unmovable tiles from whiteHighlighted
   	 for(Tile t: tilesToRemove) {
   		 if (whiteHighlighted.contains(t)) {
   			 whiteHighlighted.remove(t);
   		 }
   	 }
   	 //check if movable tiles have enemy units near them
   	 for (int i= 0; i < whiteHighlighted.size(); i++) {
   		 int hx = whiteHighlighted.get(i).getTilex();
   		 int hy = whiteHighlighted.get(i).getTiley();
   		 for (int a = hx - 1; a <= hx + 1; a++) {
   			 for (int b = hy - 1; b <= hy + 1; b++) {
   				 //exclude the tile itself
   				 if(!(a==hx && b==hy)){
   					 if(a >= 0 && a < gameState.getGrid().getX() && b >= 0 && b < gameState.getGrid().getY()){
   						 Tile t = gameState.getGrid().getTile(a, b);
   						 //if a tile is not null and has an enemy unit, it should be red highlighted, means it is attackable.
   						 if (t.getUnit() != null && gameState.getGrid().getEnemyUnits(tile.getUnit()).contains(t.getUnit())) {
   							 redHighlighted.add(t);
   						 }
   					 }
   				 }
   			 }
   		 }
   	 }
   	 //Display tiles should be highlighted

   	 for (Tile t : redHighlighted) {
			if(gameState.getGrid().getPlayerTwoUnitsOnBoard().contains(this)) {
				BasicCommands.drawTile(out, t, 0);
			}else{
				BasicCommands.drawTile(out, t, 2);
			}
			highlightedTiles.add(t);
   		 t.setMode(2);
   		 try {
   			 Thread.sleep(20);
   		 } catch (InterruptedException e) {
   			 e.printStackTrace();
   		 }
   	 }

   	 for (Tile t : whiteHighlighted) {
		 if(gameState.getGrid().getPlayerTwoUnitsOnBoard().contains(this)) {
			 BasicCommands.drawTile(out, t, 0);
		 }else{
			 BasicCommands.drawTile(out, t, 1);
		 }
		 highlightedTiles.add(t);
   		 t.setMode(1);
   		 try {
   			 Thread.sleep(20);
   		 } catch (InterruptedException e) {
   			 e.printStackTrace();
   		 }
   	 }
    }

    //Yao
    //the method is used when unit ability can lead to damage to this unit (dealing health)
    public void causeDamage(ActorRef out, GameState gameState, int damage) {
		if(health > 0) {
			health = health - damage;
		} else {
			health = 0;
		}

   	 Tile curTile = gameState.getGrid().getTile(position.tilex, position.tiley);
   	 if(health > 0) {
   		 BasicCommands.setUnitHealth(out, this, health);
   		 if(this instanceof Avatar) {
   			 gameState.displayPlayerChange(out, this, this.getHealth());
   		 }
   	 } else {
		 // part2:player change
		 if(this instanceof Avatar) {
			 gameState.displayPlayerChange(out, this,health);
		 }

   		 //part1:unit change
   		 //back end unit deletion (player, tile)
   		 List<Unit> playerOneUnitsOnBoard = gameState.getGrid().getPlayerOneUnitsOnBoard();
   		 List<Unit> playerTwoUnitsOnBoard = gameState.getGrid().getPlayerTwoUnitsOnBoard();
   		 if(playerOneUnitsOnBoard.contains(this)) {
   			 playerOneUnitsOnBoard.remove(this);
   		 } else {
   			 playerTwoUnitsOnBoard.remove(this);
   		 }
   		 curTile.removeUnit();

   		 //front end display for unit, and board
   		 BasicCommands.setUnitHealth(out, this, 0);
   		 try {Thread.sleep(30);} catch (InterruptedException e) {e.printStackTrace();}
   		 BasicCommands.playUnitAnimation(out,this, UnitAnimationType.death);
   		 try {Thread.sleep(30);} catch (InterruptedException e) {e.printStackTrace();}
   		 BasicCommands.deleteUnit(out, this);
   		 try {Thread.sleep(30);} catch (InterruptedException e) {e.printStackTrace();}

   		 gameState.getGrid().clearHighlightedTiles();
   		 gameState.displayGridBoard(out);
   	 }
    }
    
    //the method is used when unit ability can lead to health gain to this unit (dealing health)
    public void heal(ActorRef out, GameState gameState, int h) {
   	 if(health + h > maxHealth) {
   		 health = maxHealth;
   	 } else {
   		 health += h;
   	 }
   	 //front end display health change
   	 BasicCommands.setUnitHealth(out, this, health);
   	 //if this unit is an avatar, we also need to change player's health and mana on the front end
   	 if(this instanceof Avatar) {
   		 gameState.displayPlayerChange(out, this, health);
   	 }
    }

	/**
	 * click a unit, this method will highlight the near tiles with enemy units in red.
	 * This method is used when the unit hasn't attacked but has moved
	 * @param tile is the tile which contains the clicked unit
	 * @param gameState hold game state information
	 * @param out used to send messages to the front-end UI
	 */
    public void highlightAttackableUnits(Tile tile, GameState gameState, ActorRef out) {
   	 int x = tile.getUnit().getPosition().getTilex();
   	 int y = tile.getUnit().getPosition().getTiley();
   	 List<Unit> enemyUnits = gameState.getGrid().getEnemyUnits(tile.getUnit());
   	 for (int i = x - 1; i <= x + 1; i++) {
   		 for (int j = y - 1; j <= y + 1; j++) {
   			 //exclude the tile itself
   			 if(!(i==x && j==y)){
   				 if (i >= 0 && i < gameState.getGrid().getX() && j >= 0 && j < gameState.getGrid().getY()) {
   					 Unit unitToCheck = gameState.getGrid().getTile(i, j).getUnit();
   					 //if a tile is not null and has an enemy unit, it should be red highlighted.
   					 if (unitToCheck != null && enemyUnits.contains(unitToCheck)) {
   						 Tile shouldRedHighlight = gameState.getGrid().getTile(i, j);
   						 redHighlighted.add(shouldRedHighlight);
   					 }
   				 }
   			 }
   		 }
   	 }
   	 //Display tiles should be highlighted
   	 for (Tile t : redHighlighted) {
   		 BasicCommands.drawTile(out, t, 2);
   		 t.setMode(2);
   		 try {
   			 Thread.sleep(20);
   		 } catch (InterruptedException e) {
   			 e.printStackTrace();
   		 }
   	 }
    }
	/**
	 * This method moves this unit to the selected tile.
	 * @param tile is target tile where we want to move to
	 * @param gameState hold game state information
	 * @param out used to send messages to the front-end UI
	 */
    public void move(Tile tile, GameState gameState, ActorRef out) {
   	 //clear the highlight in front-end
   	 gameState.displayGridBoard(out);
   	 if (whiteHighlighted.contains(tile)) {
   		 Tile transfertile = gameState.getGrid().getTile(tile.getTilex(), position.tiley);
   		 //Checks whether the path to the unit is blocked. If so it takes an alternate path.
   		 if (transfertile.getUnit() == null || (transfertile.getUnit() != null &&
   				 gameState.getGrid().getFriendlyUnits(this).contains(transfertile.getUnit()))) {
   			 BasicCommands.moveUnitToTile(out, this, tile);
   			 try {
   				 Thread.sleep(2000);
   			 } catch (InterruptedException e) {
   				 e.printStackTrace();
   			 }
   		 }
   		 // the alternative path
   		 else {
   			 boolean yfirst = true;
   			 BasicCommands.moveUnitToTile(out, this, tile, yfirst);
   			 try {
   				 Thread.sleep(2000);
   			 } catch (InterruptedException e) {
   				 e.printStackTrace();
   			 }
   		 }
   		 //reset the unit's position
   		 gameState.getGrid().getTile(position.tilex, position.tiley).removeUnit();
   		 this.setPositionByTile(tile);
   		 tile.addUnit(this);
   		 hasMoved = true;
   		 //clear the highlight in the back-end
   		 clearWhiteHighlighted();
   		 clearRedHighlighted();
   	 }
    }

	/**
	 * If a unit is able to counterattack, this method will be called.
	 * @param unit is the unit that attacked this unit
	 * @param gameState hold game state information
	 * @param out used to send messages to the front-end UI
	 */
    public void counterAttack(Unit unit, GameState gameState, ActorRef out) {
   	 //if this unit is alive and has not counterattacked in this turn
   	 if (health >= 1 && !hasCounterAttacked) {
   		 //mytile is the tile contains this unit
   		 Tile mytile = gameState.getGrid().getTile(this.position.tilex,this.position.tiley);
   		 //unitTile is the tile contains the unit that attacked this unit
   		 Tile unitTile = gameState.getGrid().getTile(unit.position.tilex,unit.position.tiley);
   		 //if unitTile near mytile, mytile can counterattack, and it will counterattack
   		 if(gameState.getNearTiles(mytile,gameState).contains(unitTile)){
   			 BasicCommands.playUnitAnimation(out, this, UnitAnimationType.attack);
   			 try {
   				 Thread.sleep(1000);
   			 } catch (InterruptedException e) {
   				 e.printStackTrace();
   			 }
//   			 BasicCommands.addPlayer1Notification(out, "enemy is counterattacking", 2);
//   			 try {
//   				 Thread.sleep(1000);
//   			 } catch (InterruptedException e) {
//   				 e.printStackTrace();
//   			 }
   			 //the unit that be counterattacked will lose health
   			 unit.beAttacked(attack, gameState, out);
   			 hasCounterAttacked = true;
   			 //check if the unit that be counterattacked is dead
   			 unit.death(gameState,out);
   		 }
   	 }
    }
	/**
	 * This method is used to attack another unit.
	 * Make sure the unit haven't attacked before.
	 * @param unit is the unit that this unit want to attack
	 * @param gameState hold game state information
	 * @param out used to send messages to the front-end UI
	 */
    public void attack(Unit unit, GameState gameState, ActorRef out) {
   	 //clear the highlight in front-end
   	 gameState.displayGridBoard(out);
   	 BasicCommands.playUnitAnimation(out, this, UnitAnimationType.attack);
   	 try {
   		 Thread.sleep(2000);
   	 } catch (InterruptedException e) {
   		 e.printStackTrace();
   	 }
//   	 BasicCommands.addPlayer1Notification(out, "I'm attacking", 2);
//   	 try {
//   		 Thread.sleep(1000);
//   	 } catch (InterruptedException e) {
//   		 e.printStackTrace();
//   	 }
   	 //the unit that be attacked will lose health
   	 unit.beAttacked(attack, gameState, out);
   	 //check if the unit that be attacked is dead
   	 unit.death(gameState,out);
   	 hasAttacked = true;
   	 hasMoved = true;
   	 // Clear all highlighted tiles
   	 clearRedHighlighted();
    }

	/**
	 * If this unit hasn't moved, and the player clicked an enemy unit,
	 * this method will move this unit to a tile close to the enemy unit and attack.
	 * @param tile is target tile which contains the unit we want to attack
	 * @param out used to send messages to the front-end UI
	 * @param gameState hold game state information
	 */
    public void moveAndAttackUnit(Tile tile, ActorRef out, GameState gameState) {
   	 //clear the highlight in front-end
   	 gameState.displayGridBoard(out);
   	 //moveAttackTile is the tile we need to move to before attack enemy unit
   	 Tile moveAttackTile = null;
		List<Tile> findTheClosestTile = new ArrayList<>();
   	 //get the coordinate of the target tile
   	 int x = tile.getUnit().getPosition().getTilex();
   	 int y = tile.getUnit().getPosition().getTiley();
   	 //check the enemy unit's near tiles, if it is white highlighted, it means we can move there and then attack.
   	 for (int i = x - 1; i <= x + 1; i++) {
   		 for (int j = y - 1; j <= y + 1; j++) {
   			 if (i >= 0 && i < gameState.getGrid().getX() && j >= 0 && j < gameState.getGrid().getY()
   					 && whiteHighlighted.contains(gameState.getGrid().getTile(i, j))){
   				 moveAttackTile = gameState.getGrid().getTile(i, j);
				 findTheClosestTile.add(moveAttackTile);
   			 }
   		 }
   	 }
		int min = 100;
		int nearestTileX = 0;
		int nearestTileY = 0;
		//fine the closest tile
		for(Tile t : findTheClosestTile) {
			int currtileX = t.getTilex();
			int currtileY = t.getTiley();
			int a = Math.abs(x - currtileX);
			int b = Math.abs(y - currtileY);
			int distance = a + b;
			if (distance <= min) {
				min = distance;
				nearestTileX = currtileX;
				nearestTileY = currtileY;
			}
		}
		//theClosestTile is the tile we need to move to before attack enemy unit
		Tile theClosestTile = gameState.getGrid().getTile(nearestTileX,nearestTileY);
   	 //move this unit to theClosestTile
   	 this.move(theClosestTile, gameState, out);
   	 hasMoved = true;
   	 //This unit will attack after moving
   	 this.attack(tile.getUnit(),gameState, out);
	 hasAttacked = true;
   	 //Clear all highlighted tiles in the back-end
   	 clearWhiteHighlighted();
   	 clearRedHighlighted();
    }


	/**
	 * This method decreases the health.
	 * @param reduceHealth is the attack power of the unit which attacked this unit
	 * @param gameState hold game state information
	 * @param out used to send messages to the front-end UI
	 */
    public void beAttacked(int reduceHealth, GameState gameState, ActorRef out) {
   	 health -= reduceHealth;

   	 //check if the unit is dead.
		int flag=0;
		if(gameState.getGrid().getPlayerOneUnitsOnBoard().contains(this))
			flag=1;
		else if(gameState.getGrid().getPlayerTwoUnitsOnBoard().contains(this))
			flag=2;
   	 death(gameState, out);

   	 if(!death){
   		 BasicCommands.setUnitHealth(out, this, health);
   		 try {
   			 Thread.sleep(1000);
   		 } catch (InterruptedException e) {
   			 e.printStackTrace();
   		 }
   		 if(this instanceof Avatar) {
   			 avatarIsAttacked = true;
   			 gameState.displayPlayerChange(out, this, this.getHealth());
   			 //check if silverguard knight's ability can be applied
   			 silverguardCondition(gameState, out, 2);
   		 }
   	 } else
		{
			if(flag==1)//(gameState.getGrid().getPlayerOneUnitsOnBoard().contains(this))
			{
				if(this instanceof Avatar)
				{
					// gameState.displayPlayerChange(out, this, this.getHealth());
					gameState.gameOver=true;
					gameState.isClickable=false;
					BasicCommands.addPlayer1Notification(out," AI Wins",10);
				}
			}

			else if(flag==2)//(gameState.getGrid().getPlayerTwoUnitsOnBoard().contains(this))
			{
				if(this instanceof Avatar)
				{
					// gameState.displayPlayerChange(out, this, this.getHealth());
					gameState.gameOver=true;
					gameState.isClickable=false;
					BasicCommands.addPlayer1Notification(out," Player Wins",10);
				}
			}

		}
    }

	/**
	 * This is for the Card silverguard knight's condition on
	 * if Player avatar is dealt damage, this unit gains +2 attack
	 * @param gamestate: holds gamestate information
	 * @param out: used to send messages to the front-end UI
	 * @param num: +2 attack condition
	 */
	public void silverguardCondition(GameState gamestate, ActorRef out, int num) {    
  	  List<Unit> playerOneUnits = null;
  	  Unit silverguard = null;
  	  //get Player One's units
  	  playerOneUnits = gamestate.getGrid().getPlayerOneUnitsOnBoard();
  	  if(avatarIsAttacked = true) {    
   	 //check if silverguard knight is on the board
  	   	  for(Unit unit : playerOneUnits) {    
  	   		  if(unit instanceof SilverguardKnight) {    
  	   			  silverguard = unit;
//  	   			  assert silverguard != null;
  	   			  silverguard.setAttack(getAttack()+2);
  	   			  BasicCommands.setUnitAttack(out, silverguard, silverguard.getAttack());
  	   			  try {
  	   				  Thread.sleep(2000);
  	   			  } catch (InterruptedException e) {
  	   				  e.printStackTrace();
  	   			  }
  	  }
  			  //reset boolean condition of dealt damage
  			  resetAvatarIsAttacked();
  		  }
  	   }  	 
	}

	/**
	 * Any Units with the Provoke ability:
	 * if an enemy unit can attack and is adjacent to any unit with provoke,
	 * then it can only choose to attack the provoke units.
	 * enemy units cannot move when provoked
	 * @param tile is target tile which contains the unit we want to attack
	 * @param gameState holds gamestate information
	 * @param out used to send messages to the front-end UI
	 * @return
	 */
    public boolean hasbeenProvoked(Tile tile, GameState gameState, ActorRef out) {
   	 if(canProvoke == true) {    
   	  	  int x = position.tilex;
   	   	  int y = position.tiley;
   	   	  List<Unit> enemyUnits = gameState.getGrid().getEnemyUnits(this);
   	   	  for (int i = x - 1; i <= x + 1; i++) {
   	   		  for (int j = y - 1; j <= y - 1; j++) {
   	   			  if (i >= 0 && i < gameState.getGrid().getX() && j >= 0 && j < gameState.getGrid().getY()) {
   	   				  Unit unitToCheck = gameState.getGrid().getTile(i, j).getUnit();
   	   				  if (unitToCheck != null && enemyUnits.contains(unitToCheck) && unitToCheck.canProvoke) {
   	   					  BasicCommands.drawTile(out, gameState.getGrid().getTile(i, j), 2);
   	   					  try {
   	   						  Thread.sleep(100);
   	   					  } catch (InterruptedException e) {
   	   						  e.printStackTrace();
   	   					  }
   	   					  return true;
   	   				  }
   	   			  }
   	   		  }
   	   	  }
   	   	  return false;
   	 }
   	 //reset canProvoke condition
   	 return canProvoke = false;
	}

    //CUI
    private boolean death;
	/**
	 * this method is used to check if the unit is dead.
	 * If the unit health reaches 0, the unit is destroyed and will be removed from the board.
	 * @param gameState hold game state information
	 * @param out used to send messages to the front-end UI
	 */
    public void death(GameState gameState, ActorRef out) {
   	 //if the unit's health is below 1, it is dead
		// int tempPHealth=gameState.getPlayerOne().getHealth();
		// int tempAIhealth=gameState.getPlayerTwo().getHealth();

		int tempHealth= this.getHealth();
		if(this instanceof Avatar && tempHealth<1)
			gameState.displayPlayerChange(out, this, 0);
		if(this instanceof Avatar && tempHealth>=1)
			gameState.displayPlayerChange(out,this,this.getHealth());
		// if(this instanceof Avatar && temp<1)
		// 	gameState.displayPlayerChange(out, this, 0);

   	 if (health < 1) {
   		 BasicCommands.setUnitHealth(out, this, 0);
   		 try {
   			 Thread.sleep(1000);
   		 } catch (InterruptedException e) {
   			 e.printStackTrace();
   		 }
   		 //play the death animation, delete the unit from the front-end
   		 death = true;
   		 BasicCommands.playUnitAnimation(out, this, UnitAnimationType.death);
   		 try {
   			 Thread.sleep(1000);
   		 } catch (InterruptedException e) {
   			 e.printStackTrace();
   		 }
   		 BasicCommands.deleteUnit(out, this);
   		 try {
   			 Thread.sleep(1000);
   		 } catch (InterruptedException e) {
   			 e.printStackTrace();
   		 }
   		 //remove this unit from the tile
   		 gameState.getGrid().getTile(position.getTilex(), position.getTiley()).removeUnit();
   		 //remove this unit from its team
   		 if(gameState.getGrid().getPlayerOneUnitsOnBoard().contains(this)){
   			 gameState.getGrid().getPlayerOneUnitsOnBoard().remove(this);
   		 }else if(gameState.getGrid().getPlayerTwoUnitsOnBoard().contains(this)){
   			 gameState.getGrid().getPlayerTwoUnitsOnBoard().remove(this);
   		 }
   		 if(this instanceof Avatar) {
   			 gameState.displayPlayerChange(out, this,0);
   		 }
		// Rhea John
		// if(gameState.getPlayerOne().getHealth()<1)
		// 	BasicCommands.addPlayer1Notification(out, "AI wins", 5);
		// if(gameState.getPlayerTwo().getHealth()<1)
		// 	BasicCommands.addPlayer1Notification(out, "Player wins", 5);
   	 }
    }
}


