package es.ucm.vm.logic;

import java.util.ArrayList;
import java.util.List;

import es.ucm.vm.engine.Color;
import es.ucm.vm.engine.Font;
import es.ucm.vm.engine.Graphics;
import es.ucm.vm.engine.Input;

public class MainMenuState implements GameState {
    //---------------------------------------------------------------
    //----------------------Private Atributes------------------------
    //---------------------------------------------------------------
    Logic _l; // For changing gamestate
    int _posOrX; // Pos of coord origin X
    int _posOrY; // Pos of coord origin Y
    Text _header;
    Text _description;
    ArrayList<Text> _texts; // Array list with the different texts.

    //---------------------------------------------------------------
    //--------------------------Constants----------------------------
    //---------------------------------------------------------------
    final String FREE_PLAY = "Free play";
    final String FREE_PLAY_DESCRIPTION = "Select a size to play...";

    /**
     * Constructor of the MainMenuState. Creates the different texts and positions them in the
     * screen to show the info. It also creates the different buttons needed to begin the game.
     *
     * @param l
     */
    public MainMenuState(Logic l) {
        _l = l;

        _posOrY = _l._cnv.height/2;
        _posOrX = _l._cnv.width/2;

        Vector2 ors = new Vector2(_posOrX, _posOrY);

        //_texts = new ArrayList<Text>();

        // create header text
        _header = new Text(-100, _l.getCanvasSize().getHeight()*((double)1/3), new Color(0,0,0,255),
                55, FREE_PLAY, true, Font.FONT_JOSEFIN_BOLD);
        _header.setCoordOrigin(ors);

        // create description text
        _description = new Text(-130, _l.getCanvasSize().height*((double)1/6), new Color(0,0,0,255),
                35, FREE_PLAY_DESCRIPTION, false, Font.FONT_JOSEFIN_BOLD);
        _description.setCoordOrigin(ors);

        /*Text hdText =  new Text(-308, -116, _colorPicker.getWhite(),
                35, "HARD MODE", false, Font.FONT_BUNGEE_REGULAR);
        hdText.setCoordOrigin(ors);
        _hard = new Button(-196, -110, 224, 29,
                _colorPicker.getItemColor(), 10, hdText);
        _hard.setCoordOrigin(ors);*/
    } // Constructor

    /**
     * Update. In this screen is only to fit the Interface requirements.
     *
     * @param t (double) Time elapsed since the last frame.
     */
    @Override
    public void update(double t) {}

    /**
     * Renders all data from the scene.
     *
     * @param g (Graphics) Instance of Graphics
     */
    @Override
    public void render(Graphics g) {
        g.save();

        /*// Render game title
        for (int i = 0; i < _texts.size(); i++){
            _texts.get(i).render(g);
        } // for*/

        _header.render(g);
        _description.render(g);

        g.restore();
    } // render

    /**
     * Function to process the input received from the Engine.
     *
     * @param e (List<Input.TouchEvent>) Event list taken from the Input class
     */
    @Override
    public void processInput(List<Input.TouchEvent> e) {
        // int ptr = e.size() - 1; // Pointer to roam the list
        int ptr = 0;

        while(ptr < e.size()){ // While list is not empty...
            Input.TouchEvent te = e.get(ptr); // Get touch event at pointers position

            if (te.getType() == Input.TouchEvent.TouchType.CLICKED) {
                /*if (_easy.isPressed(te.getX(), te.getY())) {
                    _l.setGameState(Logic.GameStates.PLAY, 0);
                } // if
                else if(_hard.isPressed(te.getX(), te.getY())) {
                    _l.setGameState(Logic.GameStates.PLAY, 1);
                } // else if*/
            } // if
            else if(te.getType() == Input.TouchEvent.TouchType.KEY_EXIT) {
                _l.closeGame();
            } // else if

            ptr++;
        } // while
    } // processInput
}
