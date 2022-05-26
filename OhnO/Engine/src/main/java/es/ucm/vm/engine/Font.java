package es.ucm.vm.engine;

/**
 * Base class for the font functionality of the game. Contains shared functionality
 */
public interface Font {
    /**
     * Creates a platform specific instance of a font object with the supplied data
     * @param filename (String) string containing the path of the font
     * @param fontSize (int) size of the text
     * @param fontColor (int) color of the text, in hex format
     */
    public boolean initializeFont(String filename, int fontSize, int fontColor, boolean isBold);
}

