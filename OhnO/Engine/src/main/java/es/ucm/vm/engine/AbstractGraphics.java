package es.ucm.vm.engine;

/**
 * Abstract Graphics class with all the common methods and variables that both platforms will use
 */
public abstract class AbstractGraphics implements Graphics {
    // Canvas
    public Rect _canvas;
    public Rect _refCanvas;

    /**
     * Set the logic canvas for reference to scale every image that wil be drawn.
     *
     * @param c Logic canvas
     */
    public void setReferenceCanvas(Rect c) {
        _refCanvas = c;
    } // setReferenceCanvas

    /**
     * Set a size for the canvas to place objects in the menus and UI
     *
     * @param c Size of canvas
     * @param dim Reference rectangle to resize canvas
     */
    public void setCanvasSize(Rect c, Rect dim) {
        Rect temp; // Temporal rectangle for calculations

        int width = c.getWidth();
        int height = c.getHeight();

        width = dim.getWidth();

        height = (width * c.getHeight()) / c.getWidth();

        if(height > dim.getHeight()){
            height = dim.getHeight();

            width = (height * c.getWidth()) / c.getHeight();
        } // if

        temp = new Rect (width, 0, 0, height);

        _canvas = temp;
    } // setCanvasSize

    /**
     * Set the position of the physical canvas in the screen (this will be used later to calculate
     * the position of different images, etc.)
     *
     * @param x X coordinate
     * @param y Y coordinate
     */
    public void setCanvasPos(int x, int y) {
        _canvas.setPosition(x, y);
    } // setCanvasPos


    /**
     * Change the size of a Rectangle using another rectangle as a reference. Maintains the aspect
     * ratio of the Rectangle (proportions) and returns the new rectangle.
     *
     * @param src Rectangle to be resized
     * @param dim Rectangle to use as a reference.
     * @return Resized src rectangle maintaining the aspect ratio of it.
     */
    public Rect scale(Rect src, Rect dim){
        Rect temp; // Temporal rectangle for calculations

        int width = src.getWidth(); // Save the src width
        int height = src.getHeight(); // Save the src height

        // Set the new width but resized proportionally
        width = (width * _canvas.getWidth()) / _refCanvas.getWidth();
        // Change height keeping proportions
        height = (width * src.getHeight()) / src.getWidth();

        // If the src height (or the changed height) is bigger than the reference one
        if(height > dim.getHeight()){
            // Set the new height but resized proportionally
            height = (height * _canvas.getHeight()) / _refCanvas.getHeight();
            // Change width proportionally
            width = (height * src.getWidth()) / src.getHeight();
        } // if

        // Save the changes to the new Rectangle
        temp = new Rect (width, 0, 0, height);

        // Set the original position in canvas of the source Rectangle
        temp.setPosition(src.getX(), src.getY());

        // Return result
        return temp;
    } // dimensions

    /**
     * Method that checks if the given coordinates are inside the Canvas rectangle.
     *
     * @param x X coordinate
     * @param y Y coordinate
     * @return True if the position is inside Canvas, false if not
     */
    public boolean isInCanvas(int x, int y) {
        // Checking if coordinates are inside the canvas Rectangle.
        return ((x >= _canvas.getX()) && (x < (_canvas.getX() + _canvas.getWidth())))
                && ((y >= _canvas.getY()) && (y < (_canvas.getY() + _canvas.getHeight())));
    } // isInCanvas

    /**
     * Receives a X coordinate in logic reference and converts it to physical reference.
     *
     * @param x X coordinate in logic reference.
     * @return X coordinate in physical reference.
     */
    public int logicToScreenX(int x) {
        return (x * _canvas.getWidth()) / _refCanvas.getWidth();
    } // repositionX

    /**
     * Receives a Y coordinate in logic reference and converts it to physical reference.
     *
     * @param y Y coordinate in logic reference.
     * @return Y coordinate in physical reference.
     */
    public int logicToScreenY(int y) {
        return (y * _canvas.getHeight()) / _refCanvas.getHeight();
    } // repositionY

    /**
     * Receives a X coordinate in screen axis system an converts it to logic canvas coordinates.
     *
     * @param x X coordinate in physical reference
     * @return X coordinate in logic reference.
     */
    public int screenToLogicX(int x) {
        return (x * _refCanvas.getWidth()) / _canvas.getWidth();
    } // reverseRepositionX

    /**
     * Receives a Y coordinate in screen axis system an converts it to logic canvas coordinates.
     *
     * @param y Y coordinate in physical reference.
     * @return Y coordinate in logic reference
     */
    public int screenToLogicY(int y) {
        return (y * _refCanvas.getHeight()) / _canvas.getHeight();
    } // reverseRepositionY

    /**
     * Return the physic canvas stored here in this object.
     *
     * @return _can
     */
    public Rect getCanvas() {
        return _canvas;
    } // getCanvas

}
