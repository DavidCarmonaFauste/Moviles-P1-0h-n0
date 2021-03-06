package es.ucm.vm.logic;

import es.ucm.vm.engine.Color;
import es.ucm.vm.engine.Graphics;

/**
 * Text GameObject that stores the font with which to paint the text, the text to write and the
 * different parameters.
 */
public class TextGameObject extends GameObject {
    private final boolean _b; // if the text is bold or not
    private String _t; // text that will be displayed
    private final String _f; // path to the font file

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
    public TextGameObject(double x, double y, Color c, int thickness,
                          String text, boolean bold, String font) {
        super(x, y);
        _c = c;
        _thickness = thickness;
        _b = bold;
        _t = text;
        _f = font;
    } // Constructor

    /**
     * Changes the string that will be written
     *
     * @param newTxt (String) string to be written
     */
    public void changeTxt(String newTxt){
        _t = newTxt;
    }

    /**
     * Renders the text in the current position by using Graphics methods
     *
     * @param g (Graphics) Graphics instance to paint it.
     */
    @Override
    public void render(Graphics g) {
        g.setUpFont(_f, _thickness, _b);
        g.setColor(_c);
        g.drawText(_t, (int) _coordOrigin._x + (int) _pos._x - (_thickness/5)* _t.length() ,
                (int) _coordOrigin._y + ((int) _pos._y * (-1)) + (_thickness / 4));
    } // render
} // Text

