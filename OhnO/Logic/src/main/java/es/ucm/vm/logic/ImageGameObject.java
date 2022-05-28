package es.ucm.vm.logic;

import es.ucm.vm.engine.Graphics;
import es.ucm.vm.engine.Image;

public class ImageGameObject extends GameObject{
    Image _image;
    String _f; // path to the image file
    int _sizeX, _sizeY;

    /**
     * Constructor of the GameObject. Creates a new GameObject and assigns the position, the color
     * and the rotation that the object will have. To make this generic, initializes _rot to 0.
     *
     * @param x (double) X coordinate.
     * @param y (double) Y coordinate.ç
     */
    public ImageGameObject(double x, double y, int sizeX, int sizeY, String filePath) {
        super(x, y);
        _f = filePath;
        _sizeX = sizeX;
        _sizeY = sizeY;
    }

    @Override
    public void render(Graphics g) {
        _image = g.setUpImage(_f, _sizeX, _sizeY);
        g.drawImage((int) _coordOrigin._x + (int) _pos._x, (int) _coordOrigin._y + ((int) _pos._y * (-1)));
    }
}
