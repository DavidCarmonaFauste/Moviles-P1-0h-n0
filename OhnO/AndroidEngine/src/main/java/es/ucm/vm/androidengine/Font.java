package es.ucm.vm.androidengine;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.View;

public class Font implements es.ucm.vm.engine.Font {
    /**
     * Font object
     */
    private Typeface _font = null;


    /**
     * Attributes for rendering purposes
     */
    private View _view = null;
    private Paint _paint = null;
    private Canvas _canvas = null;

    /**
     * Attributes that control the appearance of the text
     */
    String _contents = "";
    int _fontSize = 1;
    int _fontColor = 0xFFFFFFFF;
    int _x = 0, _y = 0;

    /**
     * Font initializer. Receives the path to the file containing the font
     * and initializes it with the different values provided after the path,
     * like the size, the color and whether is bold or not.
     *
     * @param filename (String) String containing the path of the font
     * @param fontSize (int) Size of the text
     * @param fontColor (int) Color of the text, in hex format
     * @param isBold (boolean) Whether is Font or not.
     * @return (boolean) True if everything went well, False if not.
     */
    @Override
    public boolean initializeFont(String filename, int fontSize, int fontColor, boolean isBold) {
        if (_view == null && _canvas == null && _paint == null) {
            System.err.println("Tried to load font before setting View, Canvas and Paint");
            return false;
        }

        _font = Typeface.createFromAsset(_view.getContext().getAssets(), filename);
        if(_font == null) {
            System.err.println("Error loading font");
            return false;
        }

        _fontSize = fontSize;
        _fontColor= fontColor;

        _paint.setTypeface(_font);
        _paint.setFakeBoldText(isBold);
        _paint.setColor(fontColor);
        _paint.setTextSize(fontSize);

        return true;
    } // initializeFont

    /**
     * Sets the contents that the font will print on screen.
     *
     * @param contents (String) text we want to display
     */
    @Override
    public void setContents(String contents) {
        _contents = contents;
    } // setContents

    /**
     * Displays the text.
     */
    @Override
    public void render() {
        _canvas.drawText(_contents, _x, _y, _paint);
    } // render

    /**
     * Sets the position of the Font.
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
