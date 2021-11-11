package es.ucm.vm.logic;

import es.ucm.vm.engine.Color;
import es.ucm.vm.engine.Graphics;


public class BoardTile extends GameObject{
    //---------------------------------------------------------------
    //---------------------Private Attributes------------------------
    //---------------------------------------------------------------
    private int _d; // diameter
    private double _cD; // Current diameter
    private Color _transpCol; // color change variable
    private boolean _taken; // Flag to control if the player has taken this item
    private float _distanceCenter; // Distance to the center point

    //---------------------------------------------------------------
    //----------------------Public Attributes------------------------
    //---------------------------------------------------------------
    public TileColor _tileColor;
    public int _count = -1; // -1 is the counter by default and the one given to red tiles
    BoardPosition _boardPos;

    /**
     * Constructor of the GameObject. Creates a new GameObject and assigns the position, the color
     * and the rotation that the object will have. To make this generic, initializes _rot to 0.
     *
     * @param x         (double) X coordinate.
     * @param y         (double) Y coordinate.
     */
    public BoardTile(double x, double y, int d, TileColor tileC, int count, BoardPosition bPos) {
        super(x, y);
        _d = d;
        _boardPos = new BoardPosition(bPos._x, bPos._y);
        _tileColor = tileC;
        if (tileC == TileColor.BLUE)
            _count = count;
        else if (tileC == TileColor.GREY)
            _count = 0;
    }

    @Override
    public void update(double t) {
        super.update(t);
    }

    @Override
    public void render(Graphics g) {
        g.fillCircle((int)_pos._x, (int)_pos._y, _d);
    }
}

enum TileColor {GREY, RED, BLUE}

class BoardPosition {
    public static final BoardPosition[] DIRECTIONS;
    static {
        DIRECTIONS = new BoardPosition[]{new BoardPosition(0, 1), new BoardPosition(0, -1), new BoardPosition(1, 0), new BoardPosition(-1, 0)};
    }

    public int _x;
    public int _y;

    public BoardPosition(int x, int y) {
        _x = x;
        _y = y;
    }

    public static BoardPosition add(BoardPosition a, BoardPosition b) {
        return new BoardPosition(a._x + b._x,a._y + b._y);
    }

    public static boolean compare(BoardPosition a, BoardPosition b) {
        return a._x == b._x && a._y == b._y;
    }
}

/*
class Tile {
    public TileColor _Tile_color;
    public Coord _pos;
}

class CounterTile extends Tile {
    public CounterTile(TileColor c, Coord pos, int count){
        this._Tile_color = c;
        this._pos = pos;
        this._count = count;
    }

    public int _count;
}*/
