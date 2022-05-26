package es.ucm.vm.androidengine;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.View;

/**
 * Platform specific instance of the utility class Font. It's in charge of the platform specific
 * actions such as loading from a file
 */
public class Font implements es.ucm.vm.engine.Font {
    /**
     * Font object
     */
    private Typeface _font = null;


    /**
     * Attributes for painting purposes
     */
    private View _view = null;
    private Paint _paint = null;

    /**
     * Attributes that control the appearance of the text
     */
    int _fontSize = 1;
    int _fontColor = 0xFFFFFFFF;

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
        if (_view == null && _paint == null) {
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
}
