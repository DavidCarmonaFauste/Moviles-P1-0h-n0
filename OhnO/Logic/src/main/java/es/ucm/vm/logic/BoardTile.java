package es.ucm.vm.logic;

import es.ucm.vm.engine.Color;
import es.ucm.vm.engine.Graphics;
import es.ucm.vm.engine.Rect;

/**
 * Class used to represent a board tile. Manages its own rendering and input
 */
public class BoardTile extends GameObject{
    //---------------------------------------------------------------
    //---------------------Private Attributes------------------------
    //---------------------------------------------------------------
    private final String FONT_JOSEFIN_BOLD = "fonts/JosefinSans-Bold.ttf";
    private double _cD; // Current diameter
    private boolean _taken; // Flag to control if the player has taken this item
    private float _distanceCenter; // Distance to the center point
    private TextGameObject _textGameObject = null;
    private Button _button = null;

    //---------------------------------------------------------------
    //----------------------Public Attributes------------------------
    //---------------------------------------------------------------
    public int _d; // diameter
    public TileColor _tileColor;
    public TileInfo _tileInfo;
    public int _count = -1; // -1 is the counter by default and the one given to red tiles
    BoardPosition _boardPos;

    /**
     * Constructor of the GameObject. Creates a new GameObject and assigns the position, the color
     * and the rotation that the object will have. To make this generic, initializes _rot to 0.
     * @param x (double) X coordinate.
     * @param y (double) Y coordinate.
     * @param d (int) diameter of the tile
     * @param tileC (TileColor) color of the tile (blue, red or grey)
     * @param count (int) in case of a blue counter tile, how many blues it sees
     * @param bPos (BoardPosition) Position inside the Board class map
     */
    public BoardTile(double x, double y, int d, TileColor tileC, int count, BoardPosition bPos) {
        super(x, y);
        this._d = d;
        this._boardPos = new BoardPosition(bPos._x, bPos._y);
        updateTileColor(tileC);
        updateCount(count);
        if (_tileColor == TileColor.BLUE) {
            this._textGameObject = new TextGameObject(x, y, new Color(255,255,255,255), d/3,
                    String.valueOf(count), false, FONT_JOSEFIN_BOLD);
        }
        else if (_tileColor == TileColor.GREY) {
            this._button = new Button(x, y, d, d, new Color(0,0,0,100), 20,
                    null, null);
        }
    }

    /**
     * Sets the text of the tile to something different
     * @param newTxt (String) string that we want to set
     */
    public void setTxt(String newTxt){
        if(_textGameObject == null) this._textGameObject = new TextGameObject(_pos._x, _pos._y, new Color(50,50,50,255), this._d/3, "", false, FONT_JOSEFIN_BOLD);
        this._textGameObject.changeTxt(newTxt);
    }

    /**
     * Getter for the tile diameter
     * @return (int) _d
     */
    public int getSize(){return _d;}

    /**
     * Clone method for deep copy of the tile object
     * @return (BoardTile) a new board tile with the values initialized to the same things
     * @throws CloneNotSupportedException
     */
    protected Object clone() throws CloneNotSupportedException {
        return new BoardTile(this._pos._x, this._pos._y, this._d, this._tileColor, this._count, new BoardPosition(this._boardPos._x, this._boardPos._y));
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
        Rect n;
        o = new Rect((int)(_d * ((double)3/4)), 0, 0, (int)(_d * ((double)3/4)));
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
        if (_textGameObject != null) {
            this._textGameObject.setCoordOrigin(_coordOrigin);
            this._textGameObject.render(g);
        }
        if (_button != null) {
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
                if(_count > 0)this.setTxt(Integer.toString(_count));
                break;
            case RED:
                _c.setRed();
                this.setTxt("");
                break;
            default:
            case GREY:
                _c.setLightGrey();
                this.setTxt("");
                break;
        }
    }
    public void activateButton(){
        this._button = new Button(_pos._x, _pos._y, _d, _d, new Color(0,0,0,100), 20, null, null);
    }
    public void deactivateButton(){
        this._button = null;
    }
    /**
     * Updates the count value depending on the value of the tile color. Intended for use only on
     * constructors
     * @param count number of tiles that should see (by color)
     */
    public void updateCount(int count) {
        this._count = count;
        if (_tileColor == TileColor.BLUE) {
            this.setTxt(Integer.toString(count));
        }
        else if (_tileColor == TileColor.GREY) {
            this.setTxt("");
        }
        else {
            this.setTxt("");
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

    /**
     * Cycles through the colors of the tile in the opposite order, used to undo
     */
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

class TileInfo{
    public static class TileInfoInDir {
        public int unknownCount;
        public int numberCountAfterUnknown; // how many numbers after an unknown were found
        public int maxPossibleCount; // what would optionally be the highest count?
        public boolean wouldBeTooMuch; // would filling an unknown with a number be too much
        public int maxPossibleCountInOtherDirections;
        public int numberWhenDottingFirstUnknown;
    }
    public int unknownsAround; // are there still any unknowns around
    public int numberCount; // how many numbers/dots are seen in all directions
    public boolean numberReached; // if the current tile is a number and it has that many numbers/dots around
    public  boolean canBeCompletedWithUnknowns; // if the number can be reached by exactly its amount of unknowns
    public  boolean completedNumbersAround; // if the current tile has one or more numberReached tiles around (second pass only)
    public  BoardPosition singlePossibleDirection; // if there's only one way to expand, set this to that direction

    public TileInfoInDir[] directionInfo;

    public boolean haveInfo = false;

    public void init(){
        haveInfo = true;
        unknownsAround = 0;
        numberCount = 0;
        numberReached = false;
        canBeCompletedWithUnknowns = false;
        completedNumbersAround = false;
        singlePossibleDirection = null;
        directionInfo = new TileInfoInDir[4];
        for(int i = 0; i < 4; i++){
            directionInfo[i] = new TileInfoInDir();
            directionInfo[i].unknownCount = 0;
            directionInfo[i].numberCountAfterUnknown = 0;
            directionInfo[i].maxPossibleCount = 0;
            directionInfo[i].wouldBeTooMuch = false;
            directionInfo[i].maxPossibleCountInOtherDirections = 0;
            directionInfo[i].numberWhenDottingFirstUnknown = 0;
        }
      }
      public void catchInfo(){

      }
}