package es.ucm.vm.engine;

/**
 * Abstract Image class with all the common methods and variables that both platforms will use
 */
public abstract class AbstractImage implements Image {
    /**
     * Attributes that control the location and size of the image
     */
    protected int _sizeX = 0, _sizeY = 0;
    protected int _alpha = 100; // 0 (transparent) to 100 (opaque)

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

    public int getSizeX() {return _sizeX;}
    public int getSizeY() {return _sizeY;}

    public int getAlpha() {return _alpha;}

    public void initImage(String filename){}
}
