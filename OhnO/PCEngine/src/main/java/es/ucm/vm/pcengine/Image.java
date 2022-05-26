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
            System.err.println(e.getMessage());
        }
    }


    public java.awt.Image getAwtImage() {return _image;}
}
