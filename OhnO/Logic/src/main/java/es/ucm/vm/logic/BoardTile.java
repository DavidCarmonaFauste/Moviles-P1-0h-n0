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
    private Button _button = null;

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
            this._text = new Text(x, y, new Color(255,255,255,255), 30,
                    String.valueOf(count), false, Font.FONT_JOSEFIN_BOLD);
        }
        else if (_tileColor == TileColor.GREY) {
            this._button = new Button(x, y, d, d, new Color(0,0,0,100), 20,
                    new Text(x, y, new Color(50,50,50,255), 30,
                            "", false, Font.FONT_JOSEFIN_BOLD));
        }
    }

    /**
     * Getter for the tile diameter
     * @return (int) diameter of the tile
     */
    public int getSize(){return _d;}

    /**
     * Clone method for deep copy of the tile object
     * @return (BoardTile) a new board tile with the values initialized to the same things
     * @throws CloneNotSupportedException
     */
    protected Object clone() throws CloneNotSupportedException {
        BoardTile bt = new BoardTile(this._pos._x, this._pos._y, this._d, this._tileColor, this._count, new BoardPosition(this._boardPos._x, this._boardPos._y));
        return bt;
    }

    /**
     * Update function. In this case, it doesn't do anything
     * @param t (double) Time elapsed
     */
    @Override
    public void update(double t) {
        super.update(t);
    }

    /**
     * First it repositions and scales the object, then it draws it, restores the graphics component
     * to its state before the transformations and then draws the extra components if any: text in
     * case of a blue tile with counter, and calls the render of the grey tile's button
     * @param g (Graphics) Graphics instance to paint it
     */
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
            this._text.render(g);
        }
        else if (_button != null) {
            this._button.setCoordOrigin(_coordOrigin);
            _button.render(g);
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
        if (this._button == null) return false;

        return this._button.isPressed(x, y);
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

    /**
     * Updates the count value depending on the value of the tile color. Intended for use only on
     * constructors
     * @param count
     */
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

    /**
     * Used for interactable tiles. It changes the tile's color depending on its current color,
     * in a circular manner
     */
    public void cycleTileColor(){
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

    public void backCycleTileColor(){
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
    }// backCycleColor
}// BoardTile




/**
 * Enum class that represents the possible states of a tile
 */
enum TileColor {GREY, RED, BLUE}




/**
 * Simple position class intended for board positioning and movement
 */
class BoardPosition {
    /**
     * Used to navigate the board
     */
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
