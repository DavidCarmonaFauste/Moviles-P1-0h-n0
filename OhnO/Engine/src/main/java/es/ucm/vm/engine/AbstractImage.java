package es.ucm.vm.engine;

/**
 * Abstract Image class with all the common methods and variables that both platforms will use
 */
public abstract class AbstractImage implements Image {
    /**
     * Attributes that control the location and size of the image
     */
    protected int _x = 0, _y = 0;
    protected int _sizeX = 0, _sizeY = 0;
    protected int _alpha = 100; // 0 (transparent) to 100 (opaque)

    /**
     * Set position of the image.
     *
     * @param x (int) horizontal value
     * @param y (int) vertical value
     */
    public void setPosition(int x, int y) {
        _x = x;
        _y = y;
    }

    /**
     * Set size of the image.
     *
     * @param x (int) horizontal value
     * @param y (int) vertical value
     */
    public void setSize(int x, int y) {
        _sizeX = x;
        _sizeY = y;
    }

    public void initImage(String filename){}
    public void render(Graphics g){}

}
