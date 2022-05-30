package es.ucm.vm.pcengine;

import java.awt.Color;
import java.awt.Graphics;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 * PC implementation of the Font class for file loading and other platform specific tasks
 */
public class Font implements es.ucm.vm.engine.Font {
    /**
     * Font object
     */
    protected java.awt.Font _font = null;

    /**
     * Attributes that control the appearance of the text
     */
    int _fontSize = 1;
    Color _fontColor = Color.white;

    /**
     * Initializes the PC font object, loading the actual font and preparing files
     *
     * @param filename (String) string containing the path of the font
     * @param fontSize (int) size of the text
     * @param fontColor (int) color of the text, in hex format
     * @param isBold (boolean) is the font bold?
     * @return (boolean) true if everything went well
     */
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

        _fontSize = fontSize;
        int r = (fontColor & 0xFF0000) >> 16;
        int g = (fontColor & 0xFF00) >> 8;
        int b = (fontColor & 0xFF);
        _fontColor = new Color(r, g, b);

        return true;
    } // initializeFont


    /**
     * Getter for the java font object
     *
     * @return (java.awt.Font) _font
     */
    public java.awt.Font getFont() {
        return _font;
    }
}
