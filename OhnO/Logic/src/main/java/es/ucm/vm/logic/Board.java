package es.ucm.vm.logic;

import es.ucm.vm.engine.Graphics;

public class Board {
    private BoardTile[][] _map;
    private int _mapSize = 0;

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
                b.getMap()[x][y] = t;
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

    public void render(Graphics g) {
        for (BoardTile row[]: _map) {
            for (BoardTile tile: row) {
                tile.render(g);
            }
        }
    }
}
