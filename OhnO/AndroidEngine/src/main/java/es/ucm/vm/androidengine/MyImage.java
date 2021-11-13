package es.ucm.vm.androidengine;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.view.View;

import android.widget.ImageView;

public class MyImage implements es.ucm.vm.engine.MyImage {

    private Bitmap _bitmap = null;
    /**
     * Attributes for rendering purposes
     */
    private View _view = null;
    private Paint _paint = null;
    private Canvas _canvas = null;

    /**
     * Attributes that control the location and size of the image
     */
    int _x = 0, _y = 0;
    int _sizeX = 0, _sizeY = 0;

    @Override
    public void readImage(String filename) {
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
    public void render() {
        Rect r = new Rect();
        r.top = _x;
        r.bottom = _x + _sizeX;
        r.left = _y;
        r.right = _y + _sizeY;
        _canvas.drawBitmap(_bitmap,null,r, _paint);
    }
    /**
     * Sets the position of the Image.
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
     * Sets the View component used for retrieving the font asset
     * @param view (View) View component from Graphics
     */
    public void setView(View view) {
        _view = view;
    } // setView

    /**
     * Sets the Paint component used for rendering/typesetting purposes
     * @param paint (Paint) Paint component from Graphics
     */
    public void setPaint(Paint paint) {
        _paint = paint;
    } // setPaint

    /**
     * Sets the Canvas for rendering purposes
     * @param canvas (Canvas) Canvas component from Graphics
     */
    public void setCanvas(Canvas canvas) {
        _canvas = canvas;
    } // setCanvas
}
