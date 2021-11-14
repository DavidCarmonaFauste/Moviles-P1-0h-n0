package es.ucm.vm.engine;

/**
 * Abstract Engine with all common methods and variables that both platforms will use
 */
public abstract class AbstractEngine implements Engine {
    // ENGINE VARS
    protected Graphics _g;
    protected Input _ip;
    protected Logic _l;
    protected Logic _tempLogic;

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
        if(_l != null) {
            Rect temp;
            Rect temp2;

            // RESIZE
            // Get window size (as a rectangle)
            temp2 = new Rect(getWinWidth(), 0, 0, getWinHeight());

            // Get Logic's canvas
            temp = _l.getCanvasSize();

            // Resize the Logic's canvas with that reference
            _g.setCanvasSize(temp, temp2);

            _g.setCanvasPos(((getWinWidth() / 2) - (_g.getCanvas().getWidth() / 2)),
                    ((getWinHeight() / 2) - (_g.getCanvas().getHeight() / 2)));
        } // if
    } // resize

    /**
     * Function to save an instance of the logic and call all it's functions (update, render, handle
     * Input, etc.)
     *
     * @param l (Logic) Instance of Logic
     */
    @Override
    public void setLogic(Logic l) {
        _tempLogic = l;
    } // setLogic

    /**
     * Function called when a change in Logic has happened. Resets everything to meet the Logic's
     * conditions.
     */
    @Override
    public void resetLogic(){
        // Checking that _tempLogic is truly null to avoid callings from other objects.
        if(_tempLogic != null) {
            _l = _tempLogic;

            _g.setReferenceCanvas(_l.getCanvasSize());

            _l.initLogic();

            resize();

            _tempLogic = null;
        } // if
    } // resetLogic

    /**
     * Returns the instance of Graphics when needed to draw or making calculations.
     *
     * @return (Graphics) Graphics instance saved here.
     */
    @Override
    public Graphics getGraphics() {
        return (Graphics)_g;
    } // getGraphics

    /**
     * Return Input Instance when needed for processing Input and etc.
     *
     * @return (Input) Input instance saved here.
     */
    @Override
    public Input getInput() {
        return (Input)_ip;
    } // getInput

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
        if(_l != null) {
            _l.update(_elapsedTime);
        } // if
    } // update
}
