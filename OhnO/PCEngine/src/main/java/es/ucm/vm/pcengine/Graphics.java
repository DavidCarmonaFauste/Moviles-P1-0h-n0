package es.ucm.vm.pcengine;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.Stack;

import es.ucm.vm.engine.AbstractGraphics;
import es.ucm.vm.engine.Color;
import es.ucm.vm.engine.Rect;

public class Graphics extends AbstractGraphics {
    //---------------------------------------------------------------
    //----------------------Private Attributes-----------------------
    //---------------------------------------------------------------
    Window _win;
    Stack<AffineTransform> _saveStack; // Stack to save transformation matrix states
    Font _font;
    Image _image;

    /**
     * Graphics constructor.
     *
     * @param w (Window) Window instance
     */
    public Graphics(Window w) {
        _win = w;
        _can = new Rect(0, 0, 0, 0);
        _refCan = new Rect(0, 0, 0, 0);
        _saveStack = new Stack<AffineTransform>();
    } // Graphics

    /**
     * Translate canvas to its position in screen for painting correctly.
     *
     * @param x (int) X coordinate
     * @param y (int) Y coordinate
     */
    @Override
    public void setCanvasPos(int x, int y) {
        _can.setPosition(x, y);
        (_win.getJGraphics()).translate(x, y);
    } // setCanvasPos

    /**
     * This function receives a color and paints the hole screen with that color (white recommended)
     * to clean it from the last painting.
     *
     * @param color (int) Flat color received to paint the screen
     */
    @Override
    public void clear(Color color) {
        // Paint the whole screen with it.
        setColor(color);
        save();
        (_win.getJGraphics()).translate(0, 0);
        ((_win.getJGraphics())).fillRect(0, 0, _win.getWidth(), _win.getHeight());
        restore();
    } // clear

    /**
     * Sets color to paint on screen.
     *
     * @param color (int) Color to set for drawing
     */
    @Override
    public void setColor(Color color) {
        java.awt.Color c = new java.awt.Color(color._r, color._g, color._b, color._a);

        // Set color to paint in the Swing Graphics.
        ((_win.getJGraphics())).setColor(c);
    } // setColor

    /**
     * Draws a line between two given points.
     *
     * @param x1 (int) X position of the beginning point
     * @param y1 (int) Y position of the beginning point
     * @param x2 (int) X position of the ending point
     * @param y2 (int) Y position of the ending point
     */
    @Override
    public void drawLine(int x1, int y1, int x2, int y2, int thickness) {
        try{
            ((Graphics2D)(_win.getJGraphics())).setStroke(new BasicStroke(thickness));
            ((_win.getJGraphics())).drawLine(x1, y1, x2, y2);
        } // try
        catch (Exception e){
            e.printStackTrace();
        } // catch
    } // drawLine

    /**
     * Draws a rectangle with given coordinates and fills it with specified color.
     *
     * @param x1 (int) X position of top left corner
     * @param y1 (int) Y position of top left corner
     * @param x2 (int) Bottom right X coordinate
     * @param y2 (int) Bottom right Y coordinate
     */
    @Override
    public void fillRect(int x1, int y1, int x2, int y2) {
        try {
            // using fill polygon so it only uses position instead of position and dimensions like drawRect
            (_win.getJGraphics()).fillPolygon(new int[]{x1, x2, x2, x1}, new int[]{y1, y1, y2, y2}, 4);
        } // try
        catch(Exception e){
            e.printStackTrace();
        } // catch
    } // fillRect

    @Override
    public void fillCircle(int x, int y, int diameter) {
        (_win.getJGraphics()).fillOval(x, y, diameter, diameter);
    }

    @Override
    public void drawText(String text, int x, int y) {
        _font.setContents(text);
        _font.setPosition(x, y);
        _font.render();
    }

    @Override
    public Font setUpFont(String filename, int size, boolean isBold) {
        _font = new Font();

        _font.initializeFont(filename, size, ((Graphics2D)(_win.getJGraphics())).getColor().getRGB(), isBold);
        _font.setCanvas((Graphics2D)_win.getJGraphics());

        return _font;
    }

    @Override
    public Image setUpImage(String filename) {
        _image = new Image();

        _image.initImage(filename);

        return _image;
    }

    @Override
    public void drawImage(int x, int y) {
        _image.setPosition(x, y);
        _image.render(this);
    }

    /**
     * Return width of the window.
     *
     * @return (int) Window Width
     */
    @Override
    public int getWidth() {
        return _win.getWidth();
    } // getWidth

    /**
     * Return height of the window.
     *
     * @return (int) Window height
     */
    @Override
    public int getHeight() {
        return _win.getHeight();
    } // getHeight

    /**
     * Saves actual transformation matrix's state for restoring it later.
     */
    @Override
    public void save() {
        AffineTransform t = ((Graphics2D)_win.getJGraphics()).getTransform();
        _saveStack.push(t);
    } // save

    /**
     * Restores last saved Transformation matrix instance.
     */
    @Override
    public void restore() {
        ((Graphics2D)_win.getJGraphics()).setTransform(_saveStack.pop());
    } // restore

    /**
     * Rotates transformation matrix to paint objects rotated.
     *
     * @param angle (float) Angle to rotate object
     */
    @Override
    public void rotate(float angle) {
        ((Graphics2D)_win.getJGraphics()).rotate(Math.toRadians(angle));
    } // rotate

    /**
     * Change origin coordinates of transformation matrix to paint objects in
     * given position.
     *
     * @param x (int) X position to set as origin
     * @param y (int) Y position to set as origin
     */
    @Override
    public void translate(int x, int y) {
        try {
            x = _can.getX() + logicToScreenX(x);
            y = _can.getY() + logicToScreenY(y);

            (_win.getJGraphics()).translate(x, y);
        } // try
        catch(Exception e){
            e.printStackTrace();
        }
    } // translate
}
