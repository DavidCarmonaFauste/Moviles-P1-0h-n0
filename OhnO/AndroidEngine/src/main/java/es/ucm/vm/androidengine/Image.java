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
            System.err.println(ex.getMessage());
        }
    }

    public Bitmap getBitmap() {return _bitmap;}


    /**
     * Sets the AssetManager required to open files from the assets folder
     * @param am (AssetManager) reference to the assets manager
     */
    public void setAssetManager(AssetManager am) {
        _am = am;
    }
}
