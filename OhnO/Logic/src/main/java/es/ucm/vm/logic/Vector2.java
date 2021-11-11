package es.ucm.vm.logic;

/**
 * 2 dimensions vector. Used for calculations and positions. Uses double as type of the positions
 * for avoiding loss of information.
 */
public class Vector2 {
    //---------------------------------------------------------------
    //---------------------------Atributes---------------------------
    //---------------------------------------------------------------
    public double _x; // X value of the vector
    public double _y; // Y value of the vector
    public double _magnitude = 0; // Magnitude of the vector
    public double _squareMagnitude = 0; // Square magnitude of the vector
    public Vector2 _unit; // Unit vector of this vector
    //---------------------------------------------------------------
    //---------------------------Atributes---------------------------
    //---------------------------------------------------------------

    /**
     * Constructor.
     *
     * @param x (double) X value
     * @param y (double) Y value
     */
    public Vector2(double x, double y){
        _x = x;
        _y = y;
    } // Constructor

    /**
     * Calculates the magnitude (distance) of the vector
     */
    public double magnitude(){
        _magnitude = Math.sqrt(Math.pow(_x, 2) + Math.pow(_y, 2));
        return _magnitude;
    } // magnitude

    /**
     * Calculates
     */
    public double sqMagnitude(){
        _squareMagnitude = (Math.pow(_x, 2) + Math.pow(_y, 2));
        return _squareMagnitude;
    } // sqMagnitude

    /**
     * Function used to convert the values of the Vector2 into values between 0 and 1
     */
    public void normalize(){
        if(_magnitude == 0){
            magnitude();
        }
        _unit = new Vector2(_x / _magnitude, _y / _magnitude);
    } // normalize
} // Vector2

