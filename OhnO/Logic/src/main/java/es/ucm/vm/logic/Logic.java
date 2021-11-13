package es.ucm.vm.logic;

import es.ucm.vm.engine.Color;
import es.ucm.vm.engine.Engine;
import es.ucm.vm.engine.Rect;

public class Logic implements es.ucm.vm.engine.Logic {
    //---------------------------------------------------------------
    //----------------------Private Atributes------------------------
    //---------------------------------------------------------------
    Engine _eng; // Instance of Engine for loading levels and resources.
    Rect _cnv; // Surface to paint current GameState.
    Color _clearColor; // Black color to clear the render.
    GameState _currentGameState; // Current GameState instance
    PlayGameState _pgs = null; // PlayGameState to use its different functions

    protected enum GameStates {PLAY, MENU, RESET} // Enum with the different type of GameStates.

    int _mapSize;

    /**
     * Logic constructor, creates a new instance of Logic with a new Engine.
     *
     * @param e (Engine) New engine instance.
     */
    public Logic(Engine e){
        // Save instance of engine for updates and rendering
        _eng = e;

        // Init everything
        _cnv = new Rect (640, 0, 0, 480);
        _clearColor = new Color(255,255,255,255);
    } // Logic

    @Override
    public void initLogic() {
        setGameState(GameStates.MENU);
    }

    /**
     * Returns the actual canvas of the logic established here.
     *
     * @return (Rect) Logic canvas
     */
    @Override
    public Rect getCanvasSize() {
        return _cnv;
    } // getCanvasSize

    /**
     * Updates the game variables and data for the next frame render.
     *
     * @param t (double) Time elapsed between frames.
     */
    @Override
    public void update(double t) {
        // Process actual input and update GameState
        _currentGameState.processInput((_eng.getInput().getTouchEvents()));
        _currentGameState.update(t);
    } // update

    /**
     * Renders the actual state of the game.
     */
    @Override
    public void render() {
        // Clear buffer with black
        _eng.getGraphics().clear(_clearColor);

        //_pgs.render(_eng.getGraphics());
        _currentGameState.render(_eng.getGraphics());
    } // render

    public void setMapSize(int size) {
        _mapSize = size;
    }

    public void setGameState(GameStates gs) {
        if (gs == GameStates.PLAY) {
            _currentGameState = new PlayGameState(this, _mapSize);
            _pgs = (PlayGameState)_currentGameState;
        } // if
        else if (gs == GameStates.MENU) {
            _currentGameState = new MainMenuState(this);
        } // else if
    } // setGameState

    /**
     * Function to close the game.
     */
    public void closeGame(){
        _eng.closeGame();
    } // closeGame
}