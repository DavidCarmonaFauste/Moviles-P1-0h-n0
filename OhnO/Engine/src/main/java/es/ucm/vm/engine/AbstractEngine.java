package es.ucm.vm.engine;

/**
 * Abstract Engine with all common methods and variables that both platforms will use
 */
public abstract class AbstractEngine implements Engine {
    // ENGINE VARS
    protected Graphics _g;
    protected Input _ip;
    protected GameState _gs;
    protected GameState _tempGS;

    // TIME AND FRAMES
    protected long _lastFrameTime;
    protected long _currentTime, _nanoElapsedTime;
    protected double _elapsedTime;
    protected int _frames;
    protected long _info;
    protected volatile boolean _running;

    /**
     * Resize canvas to fit the screen. Only called when the window is resized. (Fullscreen, or
     * anything else.
     */
    public void resize(){
        if(_gs != null) {
            Rect temp;
            Rect temp2;

            // RESIZE
            // Get window size (as a rectangle)
            temp2 = new Rect(getWinWidth(), 0, 0, getWinHeight());

            // Get Logic's canvas
            temp = _gs.getCanvasSize();

            // Resize the Logic's canvas with that reference
            _g.setCanvasSize(temp, temp2);

            _g.setCanvasPos(((getWinWidth() / 2) - (_g.getCanvas().getWidth() / 2)),
                    ((getWinHeight() / 2) - (_g.getCanvas().getHeight() / 2)));
        } // if
    } // resize

    /**
     * Saves the given game state, to be properly set up next time the engine
     * does a loop
     *
     * @param gs current logic's game state
     */
    @Override
    public void saveGameState(GameState gs) {
        _tempGS = gs;
    }

    protected void setGameState() {
        if (_tempGS != null) {
            _gs = _tempGS;

            _g.setReferenceCanvas(_gs.getCanvasSize());

            resize();

            _tempGS = null;
        }
    }


    /**
     * Returns the instance of Graphics when needed to draw or making calculations.
     *
     * @return (Graphics) Graphics instance saved here.
     */
    @Override
    public Graphics getGraphics() {
        return _g;
    } // getGraphics

    /**
     * Function to close and terminate the game.
     */
    @Override
    public void closeGame(){
        _running = false;
    } // closeGame

    /**
     * Update method. Is called once per frame and updates the logic with the elapsedTime value.
     */
    protected void update(){
        if(_gs != null) {
            _gs.update(_elapsedTime);
            _gs.processInput(_ip.getTouchEvents());
        } // if
    } // update
}
