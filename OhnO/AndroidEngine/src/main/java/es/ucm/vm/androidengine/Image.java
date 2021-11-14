package es.ucm.vm.androidengine;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import es.ucm.vm.engine.AbstractImage;
import es.ucm.vm.engine.Graphics;

public class Image extends AbstractImage {

    private Bitmap _bitmap = null;
    private AssetManager _am = null;

    /**
     * Used to create a platform specific Image, in this case for Android platform
     * @param filename (string) location of the image to load
     */
    @Override
    public void initImage(String filename) {
        // load image
        try {
            // get input stream
            InputStream ims = _am.open(filename);
            _bitmap = BitmapFactory.decodeStream(ims);
            ims.close();
        }
        catch(IOException ex) {
            return;
        }
    }

    /**
     * Draw the image.
     */
    @Override
    public void render(Graphics g) {
        Rect r = new Rect();
        r.top = _y;
        r.bottom = _y + _sizeY;
        r.left = _x;
        r.right = _x + _sizeX;

        es.ucm.vm.androidengine.Graphics aG = (es.ucm.vm.androidengine.Graphics)g;
        if(_fadeOut) {
            aG._pnt.setAlpha((_alpha / 100) * 255);
            if(_alpha > 0)
                _alpha--;
            else _fadeOut = false;
        }
        aG._cnv.drawBitmap(_bitmap, null, r, aG._pnt);
        aG._pnt.setAlpha(255);
    }

    public void setAssetManager(AssetManager am) {
        _am = am;
    }
}
