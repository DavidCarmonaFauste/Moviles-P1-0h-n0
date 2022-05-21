package es.ucm.vm.logic;

import java.util.ArrayList;
import java.util.List;

import es.ucm.vm.engine.Color;
import es.ucm.vm.engine.Font;
import es.ucm.vm.engine.GameState;
import es.ucm.vm.engine.Graphics;
import es.ucm.vm.engine.Image;
import es.ucm.vm.engine.Input;
import es.ucm.vm.engine.Rect;

public class MainMenuState implements GameState {
    //---------------------------------------------------------------
    //----------------------Private Attributes-----------------------
    //---------------------------------------------------------------
    Logic _l; // For changing gamestate

    int _posOrX; // Pos of coord origin X
    int _posOrY; // Pos of coord origin Y
    Vector2 _coordOrigin;

    Text _header;
    Text _description;

    ArrayList<Button> _buttons; // Array list with the level buttons
    int _diameter = 90;
    Button _closeButton;

    //---------------------------------------------------------------
    //--------------------------Constants----------------------------
    //---------------------------------------------------------------
    final String FREE_PLAY = "Free play";
    final String FREE_PLAY_DESCRIPTION = "Select a size to play...";
    final Color _clearColor  = new Color(255,255,255,255);

    /**
     * Constructor of the MainMenuState. Creates the different texts and positions them in the
     * screen to show the info. It also creates the different buttons needed to begin the game.
     *
     * @param l logic instance, used for checks regarding common values like screen dimensions f.e.
     */
    public MainMenuState(Logic l) {
        _l = l;

        // set the coordinate origin to the center of the logical canvas
        _posOrY = _l._cnv.height/2;
        _posOrX = _l._cnv.width/2;
        _coordOrigin = new Vector2(_posOrX, _posOrY);

        // create header text
        _header = new Text(0, _l._cnv.height *((double)1/3), new Color(0,0,0,255),
                55, FREE_PLAY, true, FONT_JOSEFIN_BOLD);
        _header.setCoordOrigin(_coordOrigin);

        // create description text
        _description = new Text(30, _l._cnv.height *((double)1/6), new Color(0,0,0,255),
                35, FREE_PLAY_DESCRIPTION, false, FONT_JOSEFIN_BOLD);
        _description.setCoordOrigin(_coordOrigin);

        // create level buttons
        createLevelButtons();

        // create close button
        ImageObject imageObject = new ImageObject(0, -200, 25, 25, IMAGE_CLOSE);
        imageObject.setCoordOrigin(_coordOrigin);
        _closeButton = new Button(0, -200, 25, 25, new Color(50,50,50,100),
                10, null, imageObject);
        _closeButton.setCoordOrigin(_coordOrigin);
    } // Constructor

    /**
     * Auxiliary method that creates and places the level buttons
     */
    private void createLevelButtons() {
        // create level buttons
        _buttons = new ArrayList<>();
        int levels = 4;
        Color white = new Color(255, 255, 255, 255);
        Color blue = new Color(); blue.setBlue();
        Color red = new Color(); red.setRed();
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 3; j++) {
                Vector2 pos = new Vector2(j*100 - 100, - i*100);

                Text levelText = new Text(pos._x, pos._y, white, 20, String.valueOf(levels), false, FONT_JOSEFIN_BOLD);

                Color buttonColor;
                if (levels % 2 == 0) buttonColor = blue;
                else buttonColor = red;

                Button levelButton = new Button(pos._x, pos._y, _diameter, _diameter, buttonColor, 20, levelText,null);
                levelButton.setCoordOrigin(_coordOrigin);
                _buttons.add(levelButton);

                levels++;
            }
        }// create level buttons
    }

    /**
     * Gets a Rect with the logical canvas size
     *
     * @return (Rect) logic canvas dimensions
     */
    @Override
    public Rect getCanvasSize() {
        return _l.getCanvasSize();
    }


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
        g.clear(_clearColor);

        for (Button button: _buttons) {
            drawLevelButtons(g, button);
            button.render(g);
        }

        g.save();
        _closeButton.render(g);
        _header.render(g);
        _description.render(g);

        g.restore();
    } // render

    /**
     * Auxiliary function used to draw the "select map size" button circles. Since they don't
     * use a image, their graphics are drawn before the actual rendering of the button. In this
     * case that rendering only would draw the text
     *
     * @param g (Graphics) used for rendering purposes
     * @param b (Button) uses button data like color and position to draw the circle
     */
    private void drawLevelButtons(Graphics g, Button b) {
        Rect o = new Rect((int)(_diameter * ((double)3/4)), 0, 0, (int)(_diameter * ((double)3/4)));
        Rect n = g.scale(o, g.getCanvas());
        // Set the color to paint the button
        g.setColor(b._c);
        // Save the actual canvas transformation matrix
        g.save();

        g.translate((int) b._coordOrigin._x + (int) b._pos._x,
                (int) b._coordOrigin._y + ((int) b._pos._y * (-1)));

        g.fillCircle(n.getX() - n.getRight()/2, n.getY() - n.getBottom()/2, n.getWidth());

        // Reset canvas after drawing
        g.restore();
    }

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

            if (te.getType() == Input.TouchEvent.TouchType.CLICKED || te.getType() == Input.TouchEvent.TouchType.PRESSED) {
                if (_closeButton.isPressed(te.getX(), te.getY())) {
                    _l.closeGame();
                    return;
                }
                int levelCount = 4;
                for (Button button: _buttons) {
                    if(button.isPressed(te.getX(), te.getY())) {
                        System.out.println(levelCount);
                        _l.setMapSize(levelCount);
                        _l.setGameState(Logic.GameStates.PLAY);
                        break;
                    }
                    levelCount++;
                } // foreach _buttons
            } // if CLICKED || PRESSED
            else if(te.getType() == Input.TouchEvent.TouchType.KEY_EXIT) {
                _l.closeGame();
            } // else if

            ptr++;
        } // while
    } // processInput
}
