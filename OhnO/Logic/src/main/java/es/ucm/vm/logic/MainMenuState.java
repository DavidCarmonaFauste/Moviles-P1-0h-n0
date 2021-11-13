package es.ucm.vm.logic;

import java.util.ArrayList;
import java.util.List;

import es.ucm.vm.engine.Color;
import es.ucm.vm.engine.Font;
import es.ucm.vm.engine.Graphics;
import es.ucm.vm.engine.Input;
import es.ucm.vm.engine.Rect;

public class MainMenuState implements GameState {
    //---------------------------------------------------------------
    //----------------------Private Atributes------------------------
    //---------------------------------------------------------------
    Logic _l; // For changing gamestate
    int _posOrX; // Pos of coord origin X
    int _posOrY; // Pos of coord origin Y
    Text _header;
    Text _description;
    ArrayList<Button> _buttons; // Array list with the level buttons
    int _d = 90;
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

        // create level buttons
        _buttons = new ArrayList<>();
        int levels = 4;
        Color white = new Color(255, 255, 255, 255);
        Color blue = new Color(); blue.setBlue();
        Color red = new Color(); red.setRed();
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 3; j++) {
                Vector2 pos = new Vector2(j*100 - 100, - i*100);

                Text levelText = new Text(pos._x, pos._y, white, 20, String.valueOf(levels), false, Font.FONT_JOSEFIN_BOLD);

                Color buttonColor;
                if (levels % 2 == 0) buttonColor = blue;
                else buttonColor = red;

                Button levelButton = new Button(pos._x, pos._y, _d, _d, buttonColor, 20, levelText);
                levelButton.setCoordOrigin(ors);
                _buttons.add(levelButton);

                levels++;
            }
        }// create level buttons
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
        for (Button button: _buttons) {
            drawCircle(g, button);
            button.render(g);
        }

        g.save();

        _header.render(g);
        _description.render(g);

        g.restore();
    } // render

    private void drawCircle(Graphics g, Button b) {
        Rect o;
        Rect n = null;
        o = new Rect((int)(_d * ((double)3/4)), 0, 0, (int)(_d * ((double)3/4)));
        n = g.scale(o, g.getCanvas());
        // Set the color to paint the coin/item
        g.setColor(b._c);
        // Save the actual canvas transformation matrix
        g.save();

        g.translate((int) b._coordOrigin._x + (int) b._pos._x,
                (int) b._coordOrigin._y + ((int) b._pos._y * (-1)));

        g.fillCircle((int)n.getX() - n.getRight()/2, (int)n.getY() - n.getBottom()/2, n.getWidth());
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
                int levelCount = 4;
                for (Button button: _buttons) {
                    if(button.isPressed(te.getX(), te.getY())) {
                        System.out.println(levelCount);
                        _l.setMapSize(levelCount);
                        _l.setGameState(Logic.GameStates.PLAY);
                        break;
                    }
                    levelCount++;
                }
            } // if
            else if(te.getType() == Input.TouchEvent.TouchType.KEY_EXIT) {
                _l.closeGame();
            } // else if

            ptr++;
        } // while
    } // processInput
}
