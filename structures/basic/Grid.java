package structures.basic;

import commands.BasicCommands;
import utils.BasicObjectBuilders;
import java.util.ArrayList;
import java.util.List;
import akka.actor.ActorRef;


public class Grid {
    private int X = 9;
    private int Y = 5;
    private Tile[][] grid;
    private List<Tile> hlTiles = new ArrayList<>();
    private List<Unit> playerOneUnitsOnBoard = new ArrayList<>();
    private List<Unit> playerTwoUnitsOnBoard = new ArrayList<>();
    private Tile lastTile;

    private Tile[][] showTiles() {
        Tile[][] tiles = new Tile[X][Y];
        for(int x = 0; x < X; x++){
            for(int y = 0; y < Y; y++){
                Tile tile = BasicObjectBuilders.loadTile(x,y);
                tiles[x][y] = tile;
            }
        }
        return tiles;
    }

    public Grid() {
        grid = showTiles();
    }

    public int getX() {
        return X;
    }

    public void setX(int x) {
        X = x;
    }

    public int getY() {
        return Y;
    }

    public void setY(int y) {
        Y = y;
    }

    public Tile[][] getGrid() {
        return grid;
    }
    public Tile getTile(int x, int y){
        if((x<9 && x>-1)&&( y>-1 && y<5))
            return grid[x][y];
        // if(x>9 && y>5)
        //     return grid[x-1][y-1];
        // if(x<-1 && y<-1)
        //     return grid[x+1][y+1];
        return null;
    }

    public void setTiles(Tile[][] tiles) {
        this.grid = tiles;
    }

    // other methods to get the Grid lengths
    public int getNumRows() { return grid.length; }
    public int getNumCols() { return grid[0].length; }

    public List<Tile> getHighlightedTiles() {
        return hlTiles;
    }

    public void setHighlightedTiles(List<Tile> hlTiles) {
        this.hlTiles = hlTiles;
    }

    public void clearHighlightedTiles(){
        hlTiles.clear();
    }

    //show highlighted tiles on the front end
    public void displayHighlightedTiles(ActorRef out, int mode){
        for(Tile tile: hlTiles){
            BasicCommands.drawTile(out, tile, mode);
            tile.setMode(mode);
            try {Thread.sleep(10);} catch (InterruptedException e) {e.printStackTrace();}
        }
    }

    public List<Unit> getPlayerOneUnitsOnBoard() {
        return playerOneUnitsOnBoard;
    }

    public void setPlayerOneUnitsOnBoard(List<Unit> playerOneUnitsOnBoard) {
        this.playerOneUnitsOnBoard = playerOneUnitsOnBoard;
    }

    public List<Unit> getPlayerTwoUnitsOnBoard() {
        return playerTwoUnitsOnBoard;
    }

    public void setPlayerTwoUnitsOnBoard(List<Unit> playerTwoUnitsOnBoard) {
        this.playerTwoUnitsOnBoard = playerTwoUnitsOnBoard;
    }

    public List<Unit> getFriendlyUnits(Unit unit){
        if(playerOneUnitsOnBoard.contains(unit)){
            return playerOneUnitsOnBoard;
        } else if(playerTwoUnitsOnBoard.contains(unit)){
            return playerTwoUnitsOnBoard;
        }
        return null;
    }

    public List<Unit> getEnemyUnits(Unit unit){
        if(playerOneUnitsOnBoard.contains(unit)) {
            return playerTwoUnitsOnBoard;
        } else if(playerTwoUnitsOnBoard.contains(unit)) {
            return playerOneUnitsOnBoard;
        }
        return null;
    }

    public Tile getLastTile() {
        return lastTile;
    }

    public void setLastTile(Tile lastTile) {
        this.lastTile = lastTile;
    }
}