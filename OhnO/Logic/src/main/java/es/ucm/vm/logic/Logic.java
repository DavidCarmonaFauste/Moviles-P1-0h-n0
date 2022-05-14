package es.ucm.vm.logic;

import es.ucm.vm.engine.Engine;
import es.ucm.vm.engine.GameState;
import es.ucm.vm.engine.Rect;

/**
 * Controls the basic game core such as delegating the render/update methods to the game states
 */
public class Logic implements es.ucm.vm.engine.Logic {
    //---------------------------------------------------------------
    //---------------------Private Attributes------------------------
    //---------------------------------------------------------------
    Engine _eng; // Instance of Engine for loading levels and resources.
    Rect _cnv; // Surface to paint current GameState.
    GameState _currentGameState; // Current GameState instance
    PlayGameState _pgs = null; // PlayGameState to use its different functions

    protected enum GameStates {PLAY, MENU} // Enum with the different type of GameStates.

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
     * Sets the size of the board (ending with a size*size board)
     * @param size (int) dimension of the board
     */
    public void setMapSize(int size) {
        _mapSize = size;
    }

    /**
     * Used to change gamestate
     * @param gs (GameState) next game state we want to run
     */
    public void setGameState(GameStates gs) {
        if (gs == GameStates.PLAY) {
            _currentGameState = new PlayGameState(this, _mapSize);
            _pgs = (PlayGameState)_currentGameState;

            _eng.saveGameState(_currentGameState);
        } // if
        else if (gs == GameStates.MENU) {
            _currentGameState = new MainMenuState(this);

            _eng.saveGameState(_currentGameState);
        } // else if
    } // setGameState

    public GameState getGameState() {
        return _currentGameState;
    }

    /**
     * Function to close the game.
     */
    public void closeGame(){
        _eng.closeGame();
    } // closeGame
}