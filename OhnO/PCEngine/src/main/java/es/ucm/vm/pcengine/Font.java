package es.ucm.vm.pcengine;

import java.awt.Color;
import java.awt.Graphics;
import java.io.FileInputStream;
import java.io.InputStream;

public class Font implements es.ucm.vm.engine.Font {
    /**
     * Font object
     */
    protected java.awt.Font _font = null;

    /**
     * Attributes that control the appearance of the text
     */
    String _contents = "";
    int _fontSize = 1;
    Color _fontColor = Color.white;
    int _x = 0, _y = 0;

    /**
     * Graphics component for rendering purposes
     */
    java.awt.Graphics _graphics = null;

    @Override
    public boolean initializeFont(String filename, int fontSize, int fontColor, boolean isBold) {
        // Loading the font from the .ttf file
        java.awt.Font baseFont;

        try (InputStream is = new FileInputStream("data/" + filename)) {
            baseFont = java.awt.Font.createFont(java.awt.Font.TRUETYPE_FONT, is);
        }
        catch (Exception e) {
            // Font could not be loaded
            System.err.println("Error loading the font file: " + e);
            return false;
        }

        if (isBold)
            _font = baseFont.deriveFont(java.awt.Font.BOLD, fontSize);
        else
            _font = baseFont.deriveFont(java.awt.Font.PLAIN, fontSize);

        _contents = filename;
        _fontSize = fontSize;
        int r = (fontColor & 0xFF0000) >> 16;
        int g = (fontColor & 0xFF00) >> 8;
        int b = (fontColor & 0xFF);
        _fontColor = new Color(r, g, b);

        return true;
    } // initializeFont

    /**
     * Set the contents to be displayed.
     *
     * @param contents (String) text we want to display
     */
    @Override
    public void setContents(String contents) {
        _contents = contents;
    } // setContents

    /**
     * Renders the text on screen.
     */
    @Override
    public void render() {
        // check for nulls before trying to render
        if (_graphics != null && _font != null) {
            //_graphics.setColor(_fontColor);
            _graphics.setFont(_font);
            _graphics.drawString(_contents, _x, _y);
        } // if
    } // render

    /**
     * Set position of the text.
     *
     * @param x (int) horizontal value
     * @param y (int) vertical value
     */
    @Override
    public void setPosition(int x, int y) {
        _x = x;
        _y = y;
    } // setPosition

    /**
     * Sets the Graphics attribute used for rendering
     * @param g (Graphics) Graphics component for rendering purposes
     */
    public void setCanvas(Graphics g) { _graphics = g;} // setCanvas
}
