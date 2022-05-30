package es.ucm.vm.logic;

import es.ucm.vm.engine.Color;
import es.ucm.vm.engine.Graphics;

/**
 * GameObject abstract class. Abstract some aspects of the objects of the game that
 * are common to all of them.
 */
public abstract class GameObject {
    //---------------------------------------------------------------
    //----------------------Private Atributes------------------------
    //---------------------------------------------------------------
    protected Vector2 _pos;
    protected Vector2 _initPos;
    protected Color _c; // Color for painting player
    protected boolean _act; // Check if GO is active or not
    protected Vector2 _coordOrigin; // Origen de coordenadas

    /**
     * Constructor of the GameObject. Creates a new GameObject and assigns the position, the color
     * and the rotation that the object will have. To make this generic, initializes _rot to 0.
     *
     * @param x (double) X coordinate.
     * @param y (double) Y coordinate.ç
     */
    public GameObject(double x, double y){
        _pos = _initPos = new Vector2(x, y);
        setColor(new Color());
        _act = true;
    } // GameObject

    // Functions and methods to access GO position

    /**
     * Function that returns the actual position of the GameObject as a Vector2.
     *
     * @return (Vector2) Actual position.
     */
    public Vector2 getPos() {
        return _pos;
    } // get_pos

    /**
     * Check if GO is active or not.
     *
     * @return (boolean) Actual active state.
     */
    public boolean isActive(){
        return _act;
    } // isActive

    // Functions and methods to place the GO

    /**
     * Sets a new position for this GameObject.
     *
     * @param nPos (Vector2) New position to set the player.
     */
    public void setPos(Vector2 nPos){
        _pos._x = nPos._x;
        _pos._y = nPos._y;
    } // set_pos

    /**
     * Change actual GO state to provided active state (true or false)
     *
     * @param chng (boolean) New active state
     */
    public void setActive(boolean chng){
        _act = chng;
    } // setActive

    /**
     * Set the color to draw the GO.
     *
     * @param c (int) Color to draw.
     */
    public void setColor(Color c) {
        _c = c;
    } // setColor
    /**
     * Set the color to draw the GO.
     *
     * @param o (int) Alpha.
     */
    public void setAlphaColor(int o) { _c._a = o; } // setAlpha

    public void setCoordOrigin(Vector2 or){
        _coordOrigin = or;
    } // set_coordOrigin

    // Functions to print GO and update its state
    /**
     * Updates the state of the gameobject with the time elapsed between frames. Update is specific
     * from every GO, there is no default update.
     *
     * @param t (double) Time elapsed
     */
    public void update(double t){ }

    /**
     * Renders the GO with the characteristics set on this object. Render is specific from every
     * GO, there is no default render.
     *
     * @param g (Graphics) Graphics instance to paint it
     */
    public void render(Graphics g){ }
} // GameObject
