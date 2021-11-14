package es.ucm.vm.androidengine;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import java.io.IOException;
import java.io.InputStream;

import es.ucm.vm.engine.AbstractImage;
import es.ucm.vm.engine.Graphics;

/**
 * Android implementation of the Image class, uses Android API and our Android specific classes to
 * manage the loading and rendering of images from the assets folder
 */
public class Image extends AbstractImage {
    // Private attributes for image rendering
    private Bitmap _bitmap = null;
    private AssetManager _am = null;

    /**
     * Used to create a platform specific Image, in this case for Android platform. Uses the asset
     * manager to fetch files and saves the image to bitmap
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
     * Renders the stored bitmap through the Android canvas
     * @param g (Graphics) Android graphics instance, for rendering
     */
    @Override
    public void render(Graphics g) {
        Rect r = new Rect();
        r.top = _y;
        r.bottom = _y + _sizeY;
        r.left = _x;
        r.right = _x + _sizeX;

        es.ucm.vm.androidengine.Graphics aG = (es.ucm.vm.androidengine.Graphics)g;

        aG._cnv.drawBitmap(_bitmap, null, r, aG._pnt);
        aG._pnt.setAlpha(255);
    }

    /**
     * Sets the AssetManager required to open files from the assets folder
     * @param am (AssetManager) reference to the assets manager
     */
    public void setAssetManager(AssetManager am) {
        _am = am;
    }
}
