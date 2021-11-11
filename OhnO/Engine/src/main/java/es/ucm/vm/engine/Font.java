package es.ucm.vm.engine;

/**
 * Base class for the font functionality of the game. Contains shared attributes and functionality
 */
public interface Font {
    final String FONT_JOSEFIN_BOLD = "fonts/JosefinSans-Bold.ttf";
    final String FONT_MOLLE_REGULAR = "fonts/Molle-Regular.ttf";

    /**
     * Creates a platform specific instance of a font object with the supplied data
     * @param filename (String) string containing the path of the font
     * @param fontSize (int) size of the text
     * @param fontColor (int) color of the text, in hex format
     */
    public boolean initializeFont(String filename, int fontSize, int fontColor, boolean isBold);

    /**
     * Sets the content/text
     * @param contents (String) text we want to display
     */
    public void setContents(String contents);

    /**
     * Uses platform-appropriate methods to render the text
     */
    public void render();

    /**
     * Sets the screen position of the text
     * @param x (int) horizontal value
     * @param y (int) vertical value
     */
    public void setPosition(int x, int y);
}

