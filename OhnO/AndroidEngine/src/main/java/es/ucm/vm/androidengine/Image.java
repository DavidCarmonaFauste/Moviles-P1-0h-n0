package es.ucm.vm.androidengine;

import android.graphics.Paint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import es.ucm.vm.engine.AbstractImage;
import es.ucm.vm.engine.Graphics;

public class Image extends AbstractImage {

    private Bitmap _bitmap = null;

    /**
     * Used to create a platform specific Image, in this case for Android platform
     * @param filename (string) location of the image to load
     */
    @Override
    public void initImage(String filename) {
        _bitmap = BitmapFactory.decodeFile(filename);

        if(_bitmap == null) {
            System.err.println("Error loading font");
            return;
        }
    }

    /**
     * Draw the image.
     */
    @Override
    public void render(Graphics g) {
        Rect r = new Rect();
        r.top = _x;
        r.bottom = _x + _sizeX;
        r.left = _y;
        r.right = _y + _sizeY;

        es.ucm.vm.androidengine.Graphics aG = (es.ucm.vm.androidengine.Graphics)g;
        aG._cnv.drawBitmap(_bitmap,null, r, aG._pnt);
    }
}
