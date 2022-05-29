package es.ucm.vm.engine;

/**
 * Interface for the Graphic methods of both platforms
 */
public interface Graphics {
    //-----------------------------Canvas-------------------------------------
    /**
     * Gets reference to the canvas.
     *
     * @return (Rect) Canvas of the game
     */
    Rect getCanvas();


    //---------------------------Drawing-------------------------------------

    /**
     * Clears the screen filling it with a specific color.
     *
     * @param color (Color) Color to fill screen
     */
    void clear(Color color);

    /**
     * Sets a color to draw things in screen.
     *
     * @param color (Color) Color to set for drawing
     */
    void setColor(Color color);

    /**
     * Draws a line between to points specified with some color provided.
     *
     * @param y1 (int) Y position of the beginning point
     * @param x1 (int) X position of the beginning point
     * @param y2 (int) Y position of the ending point
     * @param x2 (int) X position of the ending point
     */
    void drawLine(int x1, int y1, int x2, int y2, int thickness);

    void fillRect(int x1, int y1, int x2, int y2);

    void fillCircle(int x, int y, int diameter);

    Font setUpFont(String filename, int size, boolean isBold);

    void drawText(String text, int x, int y);

    Image setUpImage(String filename, int x,int y);

    void drawImage(int x, int y);


    //-----------------------------Scaling------------------------------------

    /**
     * Scale a rectangle to fit another dimensions provided.
     *
     * @param src (Rect) Rectangle to scale.
     * @param dim (Rect) Rectangle to fit source Rect.
     * @return (Rect) New scaled rectangle.
     */
    Rect scale(Rect src, Rect dim);

    void save();

    void restore();

    /**
     * Rotates an object using angles given.
     *
     * @param angle (float) Angle to rotate object
     */
    void rotate(float angle);

    /**
     * Translates the origin coordinates to the specified position.
     *
     * @param x (int) X position to set as origin
     * @param y (int) Y position to set as origin
     */
    void translate(int x, int y);
}
