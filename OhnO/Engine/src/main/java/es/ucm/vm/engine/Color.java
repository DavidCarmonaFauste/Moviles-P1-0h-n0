package es.ucm.vm.engine;

/**
 * Utility class used as a multiplatform container for color
 */
public class Color {
    // internal color values
    public int _r, _g, _b, _a;

    /**
     * Empty constructor
     */
    public Color() {
        _r = _g = _b = _a = 255;
    }

    /**
     * Class constructor with parameters
     * @param r (int) red value between 0 and 255
     * @param g (int) green value between 0 and 255
     * @param b (int) blue value between 0 and 255
     * @param a (int) alpha value between 0 and 255
     */
   public Color(int r, int g, int b, int a) {
       _r = r;
       _g = g;
       _b = b;
       _a = a;
   }

   public void setWhite() {
       _r = _g = _b = _a = 255;
   }

   public void setBlack() {
       _r = _g = _b = 0;
       _a = 255;
   }

   public void setRed() {
       _r = _a = 255;
       _g = 56;
       _b = 75;
   }

   public void setBlue() {
       _r = 28;
       _g = 192;
       _b = 224;
       _a = 255;
   }

   public void setDarkGrey(){
       _r = _g = _b = 51;
       _a = 255;
   }

   public void setMediumGrey() {
        _r = _g = _b = 153;
        _a = 255;
   }

   public void setLightGrey() {
       _r = _g = _b = 238;
       _a = 255;
   }
}
