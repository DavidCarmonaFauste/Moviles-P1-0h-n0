package es.ucm.vm.logic;

import java.util.Stack;

import es.ucm.vm.engine.Graphics;
import es.ucm.vm.engine.Input;

/**
 * Class that stores the board tiles, and the one that iterates through them on render and input calls
 */
public class Board {
    private BoardTile[][] _map;
    private int _mapSize;

    Stack<BoardPosition> _lastMoves;

    /**
     * Constructor. Takes in the size and creates an empty container for the BoardTiles
     * @param size (int) size of the board
     */
    public Board(int size)
    {
        this._map = new BoardTile[size][size];
        this._mapSize = size;
        _lastMoves = new Stack<>();
    }

    /**
     * Clone method for deep copy of Board class
     * @return (Board) new deep copy of the object
     * @throws CloneNotSupportedException (Exception) will be throw if the object cannot be cloned
     */
    protected Object clone() throws CloneNotSupportedException {
        Board b = new Board(this.getMapSize());

        int x = 0, y = 0;
        for(BoardTile[] column : this.getMap()) {
            for (BoardTile t : column) {
                b.getMap()[x][y] = new BoardTile(t._pos._x, t._pos._y, t._d, t._tileColor, t._count,
                        new BoardPosition(t._boardPos._x, t._boardPos._y));
                y++;
            }
            y = 0;
            x++;
        }

        return b;
    }
    public BoardTile getTileInDir(BoardTile t, BoardPosition dir){
        BoardPosition _newPos = BoardPosition.add(t._boardPos, dir);
        return (offLimits(_newPos)) ? null : _map[t._boardPos._x + dir._x][t._boardPos._y + dir._y];
    }
    /**
     * Setter for the map size
     * @param size (int) new map size to store
     */
    public void setMapSize(int size)
    {
        _mapSize = size;
    }

    /**
     * Sets the stored map to the one provided
     * @param map (BoardTile[][]) new map to store
     */
    public void setMap(BoardTile[][] map)
    {
        _map  = map;
    }
    /**
     * Performs a deep copy of the board to update the internal _board
     * @param b (Board) new board we want to copy
     */
    public void updateMap(Board b)
    {
        try {
            int x = 0, y = 0;
            for (BoardTile[] column : b.getMap()) {
                for (BoardTile t : column) {
                    getMap()[x][y] = (BoardTile) t.clone();
                    y++;
                }
                y = 0;
                x++;
            }
        } catch (Exception e){
            // todo handle exception
        }
    }
    /**
     * Getter for the whole tile matrix
     * @return (BoardTile[][]) _map
     */
    public BoardTile[][] getMap()
    {
        return _map;
    }

    /**
     * Getter for the map size
     * @return (int) _mapSize
     */
    public int getMapSize()
    {
        return _mapSize;
    }

    /**
     * Checks if a board position is off limits or not
     * @param pos (BoardPosition) position to check
     * @return (boolean) true if the position is invalid, false if the position is valid
     */
    public boolean offLimits(BoardPosition pos)
    {
        return (pos._x < 0 ||
                pos._x >= _mapSize ||
                pos._y < 0 ||
                pos._y >= _mapSize);

    }

    /**
     * Used to undo board changes
     */
    public void removeLastMove(){
        if(!_lastMoves.isEmpty()) {
            BoardPosition bp = _lastMoves.pop();
            _map[bp._x][bp._y].backCycleTileColor();
        }
    }

    /**
     * Iterates through the board tiles and calls their render method
     * @param g (Graphics) Graphics instance to perform rendering
     */
    public void render(Graphics g) {
        for (BoardTile[] row : _map) {
            for (BoardTile tile: row) {
                tile.render(g);
            }
        }
    }

    /**
     * Sends the click event to the tiles in the board and stops when one of them returns true or
     * when there are no more tiles to iterate
     * @param te (TouchEvent) event with the click information
     */
    public void sendClickEvent(Input.TouchEvent te) {
        for (BoardTile[] row : _map) {
            for (BoardTile tile: row) {
                if (tile.isPressed(te.getX(), te.getY())) {
                    _lastMoves.push(tile._boardPos);
                    tile.cycleTileColor();
                    return;
                }
            }
        }
    }
}
