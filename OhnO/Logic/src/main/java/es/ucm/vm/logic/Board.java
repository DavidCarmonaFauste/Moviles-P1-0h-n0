package es.ucm.vm.logic;

import java.util.Stack;

import es.ucm.vm.engine.Graphics;
import es.ucm.vm.engine.Input;

public class Board {
    private BoardTile[][] _map;
    private int _mapSize = 0;

    Stack<BoardPosition> _lastMoves;

    public Board(int size)
    {
        this._map = new BoardTile[size][size];
        this._mapSize = size;
    }

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

    public void setMapSize(int size)
    {
        _mapSize = size;
    }

    public void setMap(BoardTile[][] map)
    {
        _map  = map;
    }

    public BoardTile[][] getMap()
    {
        return _map;
    }

    public int getMapSize()
    {
        return _mapSize;
    }

    public boolean offLimits(BoardPosition pos)
    {
        return (pos._x < 0 ||
                pos._x >= _mapSize ||
                pos._y < 0 ||
                pos._y >= _mapSize);

    }

    public boolean clickTile(int x, int y){
        for(BoardTile[] column : this.getMap()) {
            for (BoardTile t : column) {
                /*
                int xTile = (int) t._coordOrigin._x + (int) t._pos._x,
                    yTile = (int) t._coordOrigin._y + (int) t._pos._y * (-1);
                */
                if(/*(xTile + t.getSize()/2) <= x &&
                    (xTile - t.getSize()/2) >= x &&
                    (yTile + t.getSize()/2) <= y &&
                    (yTile - t.getSize()/2) >= y*/
                    t.isPressed(x, y)) {
                    TileColor colorNow = _map[x][y]._tileColor;
                    _lastMoves.push(_map[x][y]._boardPos);
                    _map[x][y]._tileColor = (colorNow == TileColor.GREY) ? TileColor.BLUE :
                                            (colorNow == TileColor.BLUE) ? TileColor.RED : TileColor.GREY;
                    return true;
                }
            }
        }
        return false;
    }

    public void removeLastMove(){
        BoardPosition bp =  _lastMoves.pop();
        TileColor colorNow = _map[bp._x][bp._y]._tileColor;
        _map[bp._x][bp._y]._tileColor = (colorNow == TileColor.GREY) ? TileColor.RED :
                                        (colorNow == TileColor.BLUE) ? TileColor.GREY : TileColor.BLUE;
    }

    public void render(Graphics g) {
        for (BoardTile row[]: _map) {
            for (BoardTile tile: row) {
                tile.render(g);
            }
        }
    }

    public void sendClickEvent(Input.TouchEvent te) {
        /*for (BoardTile row[]: _map) {
            for (BoardTile tile: row) {
                if (tile.isPressed(te.getX(), te.getY())) {
                    tile.circleTileColor();
                    return;
                }
            }
        }*/

        for (int i = 0; i < _mapSize; i++) {
            for (int j = 0; j < _mapSize; j++) {
                if (_map[i][j].isPressed(te.getX(), te.getY())) {
                    _map[i][j].circleTileColor();

                    for (int jj = 0; jj < _mapSize; jj++) {
                        if(_map[i][jj]._tileColor == TileColor.BLUE && _map[i][jj]._count != 0){
                            //actualiza visibles de _map[i][jj]
                        }
                    }
                    for (int ii = 0; ii < _mapSize; ii++) {
                        if(_map[ii][j]._tileColor == TileColor.BLUE && _map[ii][j]._count != 0){
                            //actualiza visibles de _map[ii][j]
                        }
                    }
                    return;
                }
            }
        }
    }
}
