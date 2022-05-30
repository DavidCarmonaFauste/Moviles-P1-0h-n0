package es.ucm.vm.logic;

import java.util.List;

import es.ucm.vm.engine.Color;
import es.ucm.vm.engine.GameState;
import es.ucm.vm.engine.Graphics;
import es.ucm.vm.engine.Input;
import es.ucm.vm.engine.Rect;

public class IntroMenuState implements GameState {
    //---------------------------------------------------------------
    //--------------------------Constants----------------------------
    //---------------------------------------------------------------
    final String FONT_JOSEFIN_BOLD = "fonts/JosefinSans-Bold.ttf";
    final String FONT_MOLLE_REGULAR = "fonts/Molle-Regular.ttf";

    //---------------------------------------------------------------
    //----------------------Private Attributes-----------------------
    //---------------------------------------------------------------
    Logic _l; // For changing gamestate

    int _posOrX; // Pos of coord origin X
    int _posOrY; // Pos of coord origin Y
    Vector2 _coordOrigin;

    TextGameObject _header;
    TextGameObject _description;

    int _diameter = 150;
    Color _blue = new Color();
    Color _red = new Color();

    //---------------------------------------------------------------
    //--------------------------Constants----------------------------
    //---------------------------------------------------------------
    final String HEADER = "Oh no";
    final String DESCRIPTION = "Original game by Q42, Martin Kool";
    final Color _clearColor  = new Color(255,255,255,255);

    public IntroMenuState(Logic l) {
        _l = l;

        // set the coordinate origin to the center of the logical canvas
        _posOrY = _l._cnv.height/2;
        _posOrX = _l._cnv.width/2;
        _coordOrigin = new Vector2(_posOrX, _posOrY);

        // create header text
        _header = new TextGameObject(-30, _l._cnv.height *((double)1/3), new Color(0,0,0,255),
                55, HEADER, true, FONT_MOLLE_REGULAR);
        _header.setCoordOrigin(_coordOrigin);

        // create description text
        _description = new TextGameObject(0, -_l._cnv.height *((double)1/4), new Color(150,150,150,255),
                20, DESCRIPTION, false, FONT_JOSEFIN_BOLD);
        _description.setCoordOrigin(_coordOrigin);

        // init colors
        _blue.setBlue();
        _red.setRed();
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

    @Override
    public void update(double t) {

    }

    @Override
    public void render(Graphics g) {
        g.clear(_clearColor);

        // draw text
        g.save();
        _header.render(g);
        _description.render(g);
        g.restore();

        // draw circles
        drawCircles(g);
    }

    @Override
    public void processInput(List<Input.TouchEvent> e) {
        int ptr = 0;

        while(ptr < e.size()){ // While list is not empty...
            Input.TouchEvent te = e.get(ptr); // Get touch event at pointers position

            if (te.getType() == Input.TouchEvent.TouchType.CLICKED || te.getType() == Input.TouchEvent.TouchType.PRESSED) {
                _l.setGameState(Logic.GameStates.MENU);
            } // if CLICKED || PRESSED
            else if(te.getType() == Input.TouchEvent.TouchType.KEY_EXIT) {
                _l.closeGame();
            } // else if

            ptr++;
        } // while
    }

    private void drawCircles(Graphics g) {
        Rect o = new Rect((int)(_diameter * ((double)3/4)), 0, 0, (int)(_diameter * ((double)3/4)));
        Rect n = g.scale(o, g.getCanvas());

        // draw blue circle
        g.setColor(_blue);
        g.save();

        g.translate((int) _coordOrigin._x - 70,
                (int) _coordOrigin._y - 40);

        g.fillCircle(n.getX() - n.getRight()/2, n.getY() - n.getBottom()/2, n.getWidth());
        g.restore();

        // draw red circle
        g.setColor(_red);
        g.save();

        g.translate((int) _coordOrigin._x + 70,
                (int) _coordOrigin._y - 40);

        g.fillCircle(n.getX() - n.getRight()/2, n.getY() - n.getBottom()/2, n.getWidth());
        g.restore();
    }
}
