package es.ucm.vm.logic;

import es.ucm.vm.engine.Color;
import es.ucm.vm.engine.Graphics;
import es.ucm.vm.engine.Rect;

/**
 * Button object to create an interactable object in the scene.
 */
public class Button extends GameObject{
    //---------------------------------------------------------------
    //----------------------Private Atributes------------------------
    //---------------------------------------------------------------
    private int _w, _h; // Width and Height
    private boolean _debug;
    private Text _t;
    private int _lineThickness;

    /**
     * Button constructor. Creates the new button and sets it's parameters.
     *
     * @param x (double) X Position.
     * @param y (double) Y Position.
     * @param width (int) Width of the button Rect.
     * @param height (int) Height of the button Rect.
     * @param c (VDMColor) Color to set for debugging rectangle.
     * @param thickness (int) Thickness.
     * @param t (Text) Text associated to the Button.
     */
    public Button(double x, double y, int width, int height, Color c, int thickness, Text t) {
        super(x, y);
        _w = width;
        _h = height;
        _t = t;
        _c = c;
        _lineThickness = thickness;
        _debug = false;
    } // Constructor

    /**
     * Render the text associated with this button. If debug is activated, it also
     * renders the dimensions of the button to test if Input is working correctly.
     *
     * @param g (Graphics) Graphics instance to paint it
     */
    @Override
    public void render(Graphics g) {
        // for debug only
        // This is just for debugging the dimensions of the button.
        if(_debug) {
            Rect o = new Rect(_w, 0, 0, _h);
            Rect n = g.scale(o, g.getCanvas());

            g.setColor(_c);
            // Save actual canvas Transformation matrix
            g.save();

            Vector2 v = new Vector2((int) _coordOrigin._x + (int) _pos._x,
                    (int) _coordOrigin._y + ((int) _pos._y * (-1)));

            // Change transformation matrix
            g.translate((int) _coordOrigin._x + (int) _pos._x,
                    (int) _coordOrigin._y + ((int) _pos._y * (-1)));

            // Draw square
            g.drawLine(-n.width / 2, -n.height / 2,
                    n.width / 2, -n.height / 2, _lineThickness);
            g.drawLine(-n.width / 2, -n.height / 2,
                    -n.width / 2, n.height / 2, _lineThickness);
            g.drawLine(n.width / 2, -n.height / 2,
                    n.width / 2, n.height / 2, _lineThickness);
            g.drawLine(-n.width / 2, n.height / 2,
                    n.width / 2, n.height / 2, _lineThickness);

            // Reset canvas after drawing
            g.restore();
        } // if

        if (_t._coordOrigin == null) {
            _t.setCoordOrigin(_coordOrigin);
        }
        _t.render(g);
    } // render

    /**
     * Use this to debug the button dimensions.
     *
     * @param debug (boolean) New debug status.
     */
    public void toggleDebug(boolean debug) {
        _debug = debug;
    } // toggleDebug

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

        leftX = _pos._x - (_w / 2);
        leftY = _pos._y - (_h / 2);
        rightX = _pos._x + (_w / 2);
        rightY = _pos._y + (_h / 2);

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
} // Button

