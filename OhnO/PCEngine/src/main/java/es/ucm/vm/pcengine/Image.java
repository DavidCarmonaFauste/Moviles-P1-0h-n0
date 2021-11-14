package es.ucm.vm.pcengine;

import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import es.ucm.vm.engine.AbstractImage;

public class Image extends AbstractImage {
    /**
     * Image object
     */
    java.awt.Image _image;
    /**
     * Buffered image (alpha uses)
     */
    BufferedImage _buffImage;


    @Override
    public void initImage(String filename) {
        _image = Toolkit.getDefaultToolkit().getImage("data/" + filename);
        try {
            _buffImage = ImageIO.read(new File("data/" + filename));
        }
        catch (IOException e) {
            return;
        }
    }

    @Override
    public void render(es.ucm.vm.engine.Graphics g) {
        es.ucm.vm.pcengine.Graphics pcG = (es.ucm.vm.pcengine.Graphics)g;
        // check for nulls before trying to render
        if (_image != null) {
            pcG._win.getJGraphics().drawImage(_image, _x, _y, _x + _sizeX, _y + _sizeY,
                    0, 0, _image.getWidth(null), _image.getHeight(null), null);
        }
    }

}
