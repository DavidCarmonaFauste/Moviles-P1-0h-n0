package es.ucm.vm.logic;

import java.util.Stack;

import es.ucm.vm.engine.Color;
import es.ucm.vm.engine.Font;
import es.ucm.vm.engine.Graphics;
import es.ucm.vm.engine.Input;

public class Board {
    private BoardTile[][] _map;
    private int _mapSize = 0;

    Stack<BoardPosition> _lastMoves;

    BoardTile _quit, _resetMove, _hint;
    /*
        b.render(g);*/
    public Board(int size)
    {
        this._map = new BoardTile[size][size];
        this._mapSize = size;
        _lastMoves = new Stack<BoardPosition>();
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

    public BoardTile getButton(int i){
        return (i == 0) ? _quit : (i == 1) ? _resetMove : _hint;
    }
    public void setButton(int i, BoardTile bt){
        switch (i)
        {
            case 0:
                _quit = bt;
                break;
            case 1:
                _resetMove = bt;
                break;
            case 2:
                _hint = bt;
                break;
            default:
                break;
        }
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

    public void removeLastMove(){
        if(!_lastMoves.isEmpty()) {
            BoardPosition bp = _lastMoves.pop();
            _map[bp._x][bp._y].backCycleTileColor();
        }
    }

    public void render(Graphics g) {
        for (BoardTile row[]: _map) {
            for (BoardTile tile: row) {
                tile.render(g);
            }
        }
        _quit.render(g);
        _hint.render(g);
        _resetMove.render(g);
    }

    public void sendClickEvent(Input.TouchEvent te) {
        for (BoardTile row[]: _map) {
            for (BoardTile tile: row) {
                if (tile.isPressed(te.getX(), te.getY())) {
                    _lastMoves.push(tile._boardPos);
                    tile.cycleTileColor();
                    return;
                }
            }
        }

        if(_quit.isPressed(te.getX(), te.getY())) removeLastMove();//por hacer
        else if(_hint.isPressed(te.getX(), te.getY())) removeLastMove();//por hacer
        else if (_resetMove.isPressed(te.getX(), te.getY())) removeLastMove();
    }
}
