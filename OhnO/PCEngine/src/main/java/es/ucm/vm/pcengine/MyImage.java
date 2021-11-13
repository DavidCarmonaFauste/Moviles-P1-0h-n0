package es.ucm.vm.pcengine;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

public class MyImage implements es.ucm.vm.engine.MyImage{
    /**
     * Image object
     */
    Image _image;
    /**
     * Graphics component for rendering purposes
     */
    java.awt.Graphics _graphics = null;
    /**
     * Attributes that control the location and size of the image
     */
    int _x = 0, _y = 0;
    int _sizeX = 0, _sizeY = 0;

    @Override
    public void readImage(String filename) {
        _image = Toolkit.getDefaultToolkit().getImage(filename);
    }
    /**
     * Renders the image on screen.
     */
    @Override
    public void render() {
        // check for nulls before trying to render
        if (_graphics != null && _image != null) {
            _graphics.drawImage(_image,_x,_y,_x + _sizeX, _y + _sizeY,
                                0,0,_image.getWidth(null), _image.getHeight(null), null);
        }
    }
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

    /**
     * Sets the Graphics attribute used for rendering
     * @param g (Graphics) Graphics component for rendering purposes
     */
    public void setCanvas(Graphics g) { _graphics = g;} // setCanvas

}
