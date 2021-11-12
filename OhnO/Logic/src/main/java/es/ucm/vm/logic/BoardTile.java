package es.ucm.vm.logic;

import es.ucm.vm.engine.Color;
import es.ucm.vm.engine.Font;
import es.ucm.vm.engine.Graphics;
import es.ucm.vm.engine.Rect;


public class BoardTile extends GameObject{
    //---------------------------------------------------------------
    //---------------------Private Attributes------------------------
    //---------------------------------------------------------------
    private double _cD; // Current diameter
    private boolean _taken; // Flag to control if the player has taken this item
    private float _distanceCenter; // Distance to the center point
    private Text _text = null;

    //---------------------------------------------------------------
    //----------------------Public Attributes------------------------
    //---------------------------------------------------------------
    public int _d; // diameter
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
        this._d = d;
        this._boardPos = new BoardPosition(bPos._x, bPos._y);
        updateTileColor(tileC);
        updateCount(count);
        if (_tileColor == TileColor.BLUE) {
            this._text = new Text(x, y, new Color(50,50,50,255), 30,
                    String.valueOf(count), false, Font.FONT_JOSEFIN_BOLD);
        }
    }

    public int getSize(){return _d;}

    protected Object clone() throws CloneNotSupportedException {
        BoardTile bt = new BoardTile(this._pos._x, this._pos._y, this._d, this._tileColor, this._count, new BoardPosition(this._boardPos._x, this._boardPos._y));
        return bt;
    }

    @Override
    public void update(double t) {
        super.update(t);
    }

    @Override
    public void render(Graphics g) {
        Rect o;
        Rect n = null;
        o = new Rect(_d/2, 0, 0, _d/2);
        n = g.scale(o, g.getCanvas());
        // Set the color to paint the coin/item
        g.setColor(_c);
        // Save the actual canvas transformation matrix
        g.save();

        g.translate((int) _coordOrigin._x + (int) _pos._x,
                (int) _coordOrigin._y + ((int) _pos._y * (-1)));

        g.fillCircle((int)n.getX() - n.getRight()/2, (int)n.getY() - n.getBottom()/2, n.getWidth());

        // Reset canvas after drawing
        g.restore();
        if (_text != null) {
            this._text.setCoordOrigin(_coordOrigin);
            _text.render(g);
        }
    }

    /**
     * Function that checks if a button is pressed. Returns true when that happens, false if not.
     *
     * @param x X position of the pointer
     * @param y Y position of the pointer
     * @return Returns true if button is pressed, false if not
     */
    public boolean isPressed(int x, int y){
        // If the cursor is inside the rectangle of the sprite.
        double leftX, leftY;
        double rightX, rightY;

        leftX = _pos._x - (_d / 2);
        leftY = _pos._y - (_d / 2);
        rightX = _pos._x + (_d / 2);
        rightY = _pos._y + (_d / 2);

        // Translate to coordOriginPos

        // x
        if(x < _coordOrigin._x) {
            x = (((int)_coordOrigin._x - x) * -1);
        } // if
        else {
            x = (((int)_coordOrigin._x -((2 * (int)_coordOrigin._x) - x)));
        } // else

        // y
        if(y < _coordOrigin._y) {
            y = (((int)_coordOrigin._y - y));
        } // if
        else {
            y = (((int)_coordOrigin._y -((2 * (int)_coordOrigin._y) - y)) * -1);
        } // else

        // Check inside button
        if( ((x >= leftX) && (x < rightX))
                && ((y >= leftY) && (y < rightY))) {
            return true;
        } // if
        else{ // If not, return Button not Pressed
            return false;
        } // else
    } // isPressed

    /**
     * Used to change the drawing color of the tile as well as the internal TileColor
     * @param newColor (TileColor) color of tile we want to set (either red, blue or grey)
     */
    public void updateTileColor(TileColor newColor) {
        _tileColor = newColor;
        switch (newColor){
            case BLUE:
                _c.setBlue();
                break;
            case RED:
                _c.setRed();
                break;
            default:
            case GREY:
                _c.setMediumGrey();
                break;
        }
    }

    public void updateCount(int count) {
        if (_tileColor == TileColor.BLUE) {
            this._count = count;
        }
        else if (_tileColor == TileColor.GREY) {
            this._count = 0;
        }
        else {
            this._count = -1;
        }
    }

    public void circleTileColor(){
        if (this._count == 0) {
            switch (_tileColor) {
                case GREY:
                    updateTileColor(TileColor.BLUE);
                    break;
                case BLUE:
                    updateTileColor(TileColor.RED);
                    break;
                case RED:
                    updateTileColor(TileColor.GREY);
                    break;
            }
        }
    }

    public void backCicleTileColor(){
        if (this._count == 0) {
            switch (_tileColor) {
                case GREY:
                    updateTileColor(TileColor.RED);
                    break;
                case BLUE:
                    updateTileColor(TileColor.GREY);
                    break;
                case RED:
                    updateTileColor(TileColor.BLUE);
                    break;
            }
        }
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
