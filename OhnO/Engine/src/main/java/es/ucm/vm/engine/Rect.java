package es.ucm.vm.engine;

public class Rect {
    // Pixel where it begins
    int _right;
    int _left;
    int _top;
    int _bottom;

    // Positions
    int _x = 0;
    int _y = 0;

    // Width and Height
    public int width;
    public int height;

    /**
     * Constructor. Sets all values from the Rectangle. Right, Left, Bottom and Top.
     *
     * @param r Right value
     * @param l Left value
     * @param t Top value
     * @param b Bottom value
     */
    public Rect(int r, int l, int t, int b){
        _right = r;
        _left = l;
        _top = t;
        _bottom = b;

        width = _right - _left;
        height = _bottom - _top;
    } // Rect

    /**
     * Returns width of the Rectangle.
     *
     * @return width
     */
    public int getWidth(){
        return width;
    } // getWidth

    /**
     * Returns height of the rectangle.
     *
     * @return height
     */
    public int getHeight(){
        return height;
    } // getHeight

    /**
     * Set the position of the Rectangle (in canvas or screen).
     *
     * @param x X Coordinate
     * @param y Y Coordinate
     */
    public void setPosition(int x, int y) {
        _x = x;
        _y = y;
    } // setPosition

    /**
     * Get left value of the rectangle.
     *
     * @return left
     */
    public int getLeft(){
        return _left;
    }

    /**
     * Get right value of the rectangle.
     *
     * @return right
     */
    public int getRight(){
        return _right;
    } // getRight

    /**
     * Get top value of the rectangle.
     *
     * @return top
     */
    public int getTop(){
        return _top;
    } // getTop

    /**
     * Get bottom value of the rectangle.
     *
     * @return bottom
     */
    public int getBottom(){
        return _bottom;
    } // getBottom

    /**
     * Get x value of the rectangle.
     *
     * @return x
     */
    public int getX(){ return _x; } // getX

    /**
     * Get Y value of the rectangle.
     *
     * @return Y
     */
    public int getY() { return _y; } // getY
}
