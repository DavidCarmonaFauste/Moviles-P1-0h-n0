package es.ucm.vm.logic;

import es.ucm.vm.engine.Graphics;
import es.ucm.vm.engine.Image;

public class ImageObject extends GameObject{
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
    public ImageObject(double x, double y, int sizeX, int sizeY, String filePath) {
        super(x, y);
        _f = filePath;
        _sizeX = sizeX;
        _sizeY = sizeY;
    }

    @Override
    public void render(Graphics g) {
        int x = g.logicToScreenX((int) _coordOrigin._x + (int) _pos._x);
        int y = g.logicToScreenY((int) _coordOrigin._y + ((int) _pos._y * (-1)));
        int sizeX = g.logicToScreenX(_sizeX);
        int sizeY = g.logicToScreenX(_sizeY);

        _image = g.setUpImage(_f);
        _image.setSize(sizeX, sizeY);
        g.drawImage(x + g.getCanvas().getX() - sizeX/2, y + g.getCanvas().getY() - sizeY/2);
    }
}
