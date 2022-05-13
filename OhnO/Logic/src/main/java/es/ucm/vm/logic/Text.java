package es.ucm.vm.logic;

import es.ucm.vm.engine.Color;
import es.ucm.vm.engine.Graphics;

/**
 * Text GameObject that stores the font with which to paint the text, the text to write and the
 * different parameters.
 */
public class Text extends GameObject {
    private boolean _b; // if the text is bold or not
    private String _t; // text that will be displayed
    private String _f; // path to the font file

    int _thickness; // thickness of the text line
    Color _c; // color in which the text will be drawn

    /**
     * Constructor of Text GameObject. Creates a new Text object with all the parameters provided
     * to apply it for the text.
     *
     * @param x (double) X position of the text.
     * @param y (double) Y position of the text.
     * @param c (VDMColor) Color to paint the text.
     * @param thickness (int) Thickness of the text. (Font size)
     * @param text (String) Text to write
     * @param bold (boolean) Boolean to check if text is bold or not.
     * @param font (String) Font path that will use to paint itself.
     */
    public Text(double x, double y, Color c, int thickness,
                String text, boolean bold, String font) {
        super(x, y);
        _c = c;
        _thickness = thickness;
        _b = bold;
        _t = text;
        _f = font;
    } // Constructor

    public void changeTxt(String newTxt){
        _t = newTxt;
    }

    /**
     * Renders the text in the current position.
     *
     * @param g (Graphics) Graphics instance to paint it.
     */
    @Override
    public void render(Graphics g) {

        int x = g.logicToScreenX((int) _coordOrigin._x + (int) _pos._x - (_thickness/4)*_t.length());
        int y = g.logicToScreenY((int) _coordOrigin._y + ((int) _pos._y * (-1)) + (_thickness / 4));

        g.setUpFont(_f, g.logicToScreenX(_thickness), _b);
        g.setColor(_c);
        g.drawText(_t, g.getCanvas().getX() + x , g.getCanvas().getY() + y);
    } // render
} // Text

