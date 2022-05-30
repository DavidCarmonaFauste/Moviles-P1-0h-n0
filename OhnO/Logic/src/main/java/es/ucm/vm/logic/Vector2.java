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


    public void add(Vector2 other) {
        _x += other._x;
        _y += other._y;
    }
} // Vector2
