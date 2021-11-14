package es.ucm.vm.engine;

public abstract class AbstractImage implements Image {
    /**
     * Attributes that control the location and size of the image
     */
    protected int _x = 0, _y = 0;
    protected int _sizeX = 0, _sizeY = 0;

    /**
     * Set position of the image.
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
     * Set size of the image.
     *
     * @param x (int) horizontal value
     * @param y (int) vertical value
     */
    @Override
    public void setSize(int x, int y) {
        _sizeX = x;
        _sizeY = y;
    }
}
