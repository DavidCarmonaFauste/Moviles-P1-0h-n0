package es.ucm.vm.androidengine;

import android.content.res.AssetManager;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.SurfaceView;

import es.ucm.vm.engine.AbstractGraphics;
import es.ucm.vm.engine.Color;

/**
 * Graphics class that implements the interface from the engine and uses android API to draw lines
 * and squares. It also draws text in screen. Extends AbstractGraphics to use scaling methods and
 * common variables.
 */
public class Graphics extends AbstractGraphics {
    //---------------------------------------------------------------
    //----------------------Private Atributes------------------------
    //---------------------------------------------------------------
    SurfaceView _sView;
    AssetManager _aMan;
    Paint _pnt;
    Canvas _cnv;
    public es.ucm.vm.engine.Color _color;
    Font _font;
    Image _image;

    /**
     * Constructor. Receives and saves an instance of the SurfaceView to paint there later. Also
     * receives and saves an instance of the AssetManager to load new Images and  create them .
     * Initializes Paint to use it to show images on the screen.
     *
     * @param sv (SurfaceView) SurfaceView instance
     * @param am (AssetManager) AssetManager instance
     */
    public Graphics(SurfaceView sv, AssetManager am){
        _sView = sv;
        _aMan = am;

        _pnt = new Paint();
        _color = new Color();
    } // Graphics

    /**
     * Initialize the frame. Receives a new canvas and sets it as the canvas it will use to Paint
     * later. Called once per frame.
     *
     * @param c (Canvas) New canvas.
     */
    public void startFrame(Canvas c)
    {
        _cnv = c;
    } // startFrame

    /**
     * Clears the screen with a specific color. Draws the hole screen with that color.
     *
     * @param color (int) Color to paint
     */
    @Override
    public void clear(Color color) {
        // Set color
        setColor(color);

        // Paint screen
        _cnv.drawRect(0, 0,
                _sView.getRight(), _sView.getBottom(),
                _pnt);
    }

    /**
     * Sets the color of the Paint component to the one provided
     * @param color (Color) Color to set for drawing
     */
    @Override
    public void setColor(Color color) {
        int c = android.graphics.Color.argb(color._a, color._r, color._g, color._b);
        _pnt.setColor(c);
    }

    /**
     * Draws a line with a given color.
     *
     * @param x1 (int) X position of the beginning point
     * @param y1 (int) Y position of the beginning point
     * @param x2 (int) X position of the ending point
     * @param y2 (int) Y position of the ending point
     */
    @Override
    public void drawLine(int x1, int y1, int x2, int y2, int thickness) {
        _cnv.setDensity(1);
        _pnt.setStrokeWidth(thickness);
        _cnv.drawLine(x1, y1, x2, y2, _pnt);
    }

    /**
     * Draws a rectangle with given coordinates. (Top left corner and bottom right corner).
     *
     * @param x1 (int) Top left X coordinate
     * @param y1 (int) Top left Y coordinate
     * @param x2 (int) Bottom right X coordinate
     * @param y2 (int) Bottom right Y coordinate
     */
    @Override
    public void fillRect(int x1, int y1, int x2, int y2) {
        _cnv.setDensity(1);
        android.graphics.Rect temp = new android.graphics.Rect(x1, y1, x2, y2);
        _cnv.drawRect(temp, _pnt);
    }

    /**
     * Android implementation of the fillCircle method. Uses the Android API to paint a circle
     * @param x (int) x position for rendering
     * @param y (int) y position for rendering
     * @param diameter (int) diameter of the circle
     */
    @Override
    public void fillCircle(int x, int y, int diameter) {
        _pnt.setStyle(Paint.Style.FILL);
        _cnv.drawCircle(x + (int)(diameter/2), y+(int)(diameter/2), (int)(diameter/2), _pnt);
    }

    /**
     * Android implementation of the drawText method. It should be called only after setting up the font.
     * It sets up a string of text and a position and renders the string
     * @param text (String) text we want to render
     * @param x (int) Position for rendering
     * @param y (int) Position for rendering
     */
    @Override
    public void drawText(String text, int x, int y) {
        _font.setContents(text);
        _font.setPosition(x - (_font._fontSize - _font._fontSize / 4), y - _font._fontSize / 2);
        _font.render();
    }

    /**
     * Android specific implementation of the method setUpFont. It uses the Android Font implementation
     * to load a font from the assets folder into a Font instance
     * @param filename (String) path of the assets folder font
     * @param size (int) Size of the font for rendering purposes
     * @param isBold (boolean) If the font will be rendered as bold or not
     * @return (Font) Android Font with all data set up
     */
    @Override
    public Font setUpFont(String filename, int size, boolean isBold) {
        _font = new Font();

        _font.setView(_sView);
        _font.setPaint(_pnt);
        _font.setCanvas(_cnv);
        _font.initializeFont(filename,size,_pnt.getColor(), isBold);

        return _font;
    }

    /**
     * Android specific implementation of the setUpImageMethod. Uses the Android Image class to
     * load an image from the assets folder and sets the size of the image
     * @param filename (String)  path of the assets folder image
     * @param sizeX (int)  size of the image (horizontal)
     * @param sizeY (int)  size of the image (vertical)
     * @return (Image) Android Image with all data set up
     */
    @Override
    public Image setUpImage(String filename, int sizeX, int sizeY) {
        _image = new Image();

        _image.setSize(sizeX, sizeY);
        _image.setAssetManager(_aMan);
        _image.initImage(filename);
        return _image;
    }

    /**
     * Android specific implementation of the drawImage method. It uses the Android Image class
     * and sends it the render instruction
     * @param x
     * @param y
     */
    @Override
    public void drawImage(int x, int y) {
        _image.setPosition(x, y);
        _image.render(this);
    }

    /**
     * Return width of the SurfaceView for calculations.
     *
     * @return (int) Width of the surface view.
     */
    @Override
    public int getWidth() {
        return _sView.getWidth();
    } // getWidth

    /**
     * Return width of the SurfaceView for calculations.
     *
     * @return (int) Height of the surface view.
     */
    @Override
    public int getHeight() {
        return _sView.getHeight();
    } // getHeight

    /**
     * Saves actual canvas state for restoring it later.
     */
    @Override
    public void save() {
        _cnv.save();
    } // save

    /**
     * Restores canvas using the last saved state
     */
    @Override
    public void restore() {
        _cnv.restore();
    } // restore

    @Override
    public void rotate(float angle) {

    }

    /**
     * Changes the origin coordinates of transformation matrix.
     *
     * @param x (int) X position to set as origin
     * @param y (int) Y position to set as origin
     */
    @Override
    public void translate(int x, int y) {
        x = _can.getX() + logicToScreenX(x);
        y = _can.getY() + logicToScreenY(y);

        _cnv.translate(x, y);
    } // translate
}
