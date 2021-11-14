package es.ucm.vm.logic;

import java.util.List;
import java.util.Random;

import es.ucm.vm.engine.Color;
import es.ucm.vm.engine.Font;
import es.ucm.vm.engine.Graphics;
import es.ucm.vm.engine.Image;
import es.ucm.vm.engine.Input;
import es.ucm.vm.engine.Rect;
import sun.rmi.runtime.Log;

public class PlayGameState implements GameState{
    Logic _l; // For changing gamestate
    Color _color;

    Board _board;
    Hints _hints;

    Button _closeButton, _undoButton, _hintsButton;
    Text _hintsTxt;
    Vector2 _coordOr; // Coord origin
    int _coordOrX; // Coord origin X value
    int _coordOrY; // Coord origin Y value

    public PlayGameState(Logic l, int mapSize) {
        _l = l;
        _color = new Color();

        _coordOrX = _l._cnv.width/2;
        _coordOrY = _l._cnv.height/2;

        _coordOr = new Vector2(_coordOrX, _coordOrY);

        newMap(mapSize);
    }

    public void newMap(int mapSize) {
        _board = new Board(mapSize);
        _hints = new Hints(_board);
        fillBoard(mapSize);
    }


    private void fillBoard(int mapSize) {
        // Calculate sizings used for tile placement
        double step = 0, smallSide = 0, ogSmallSide;
        if (_l._eng.getWinHeight() < _l._eng.getWinWidth())
            smallSide = _l._eng.getWinHeight() * 0.8;
        else
            smallSide = _l._eng.getWinWidth() * 0.8;
        smallSide = _l._eng.getGraphics().reverseRepositionX((int)smallSide);
        step = smallSide / (mapSize + 1.5);
        // calculate diameter for tiles
        int d = (int)(step);
        // set the probability of a filled tile
        float probabilityLimit = 0.25f;
        // extra variables used in tile creation
        int blueCount = 0;
        TileColor tileColor = TileColor.GREY;

        BoardTile[][] generatedMap;

        int tries = 0;
        System.out.println("Generating new level");
        do{
            tries = 0;
            generatedMap = new BoardTile[mapSize][mapSize];
            for (int i = 0; i < mapSize; ++i) {
                for (int j = 0; j < mapSize; ++j) {
                    Random rand = new Random();
                    float f = rand.nextFloat();

                    if (f >= probabilityLimit) { // grey
                        tileColor = TileColor.GREY;
                        blueCount = 0;
                    }
                    else if (f <= probabilityLimit/2) { // red
                        tileColor = TileColor.RED;
                        blueCount = -1;
                    }
                    else { // blue
                        rand = new Random();
                        blueCount = 1 + rand.nextInt(mapSize);
                        tileColor = TileColor.BLUE;
                    }

                    generatedMap[i][j] = new BoardTile(step*(i - ((double)(mapSize - 1)/2)), step * (j - ((double)(mapSize- 1)/2)) * -1, d,
                            tileColor, blueCount, new BoardPosition(i, j));
                }
            }

            _board.setMap(generatedMap);
            _hints.updateMap(_board);
            while(!_hints.solveMap() && tries < 25)
            {
                tries++;
                Random rand = new Random();
                float f = rand.nextFloat();
                if (f <= probabilityLimit/2) { // red
                    tileColor = TileColor.RED;
                    blueCount = -1;
                }
                else { // blue
                    rand = new Random();
                    blueCount = 1 + rand.nextInt(mapSize);
                    tileColor = TileColor.BLUE;
                }
                rand = new Random();
                int i = rand.nextInt(mapSize - 1);
                rand = new Random();
                int j = rand.nextInt(mapSize - 1);
                generatedMap[i][j] = new BoardTile(step*(i - ((double)(mapSize -1)/2)), step*(j - ((double)(mapSize- 1)/2)) * -1,
                        d, tileColor, blueCount, new BoardPosition(i, j));
                _board.setMap(generatedMap);
                _hints.updateMap(_board);
            }
        }
        while(tries >= 50);
        System.out.println("Finished generating level");
        for (BoardTile row[]:generatedMap) {
            for (BoardTile tile: row) {
                tile.setCoordOrigin(_coordOr);
            }
        }

        Color boundingBoxColor = new Color (50, 50,50, 100);
        double buttonY = -smallSide/2;
        int buttonSize = 30;

        ImageObject closeImage = new ImageObject(-40, buttonY, buttonSize, buttonSize, Image.IMAGE_CLOSE);
        closeImage.setCoordOrigin(_coordOr);
        _closeButton = new Button(-40, buttonY, buttonSize, buttonSize,
                boundingBoxColor, 10, null, closeImage);
        _closeButton.setCoordOrigin(_coordOr);

        ImageObject undoImage = new ImageObject(0, buttonY, buttonSize, buttonSize, Image.IMAGE_HISTORY);
        undoImage.setCoordOrigin(_coordOr);
        _undoButton = new Button(0, buttonY, buttonSize, buttonSize,
                boundingBoxColor, 10, null, undoImage);
        _undoButton.setCoordOrigin(_coordOr);

        ImageObject hintImage = new ImageObject(40, buttonY, buttonSize, buttonSize, Image.IMAGE_EYE);
        hintImage.setCoordOrigin(_coordOr);
        _hintsButton = new Button(40, buttonY, buttonSize, buttonSize,
                boundingBoxColor, 10, null,  hintImage);
        _hintsButton.setCoordOrigin(_coordOr);

        _hintsTxt = new Text(step/2 * (mapSize - 1) - (smallSide / 4), smallSide/2, new Color(50,50,50,255), d/2,
                            Integer.toString(mapSize) + "x" + Integer.toString(mapSize), false, Font.FONT_JOSEFIN_BOLD);
        _hintsTxt.setCoordOrigin(_coordOr);
    }

    @Override
    public void update(double t) {
        /*System.out.println(_resetMove._c._a);
        if(_hintButton._c._a < 255) {
            _hintButton._c._a += 250 * t;
            if(_hintButton._c._a > 255) _hintButton._c._a = 255;
        }
        if(_resetMove._c._a < 255) {
            _resetMove._c._a += 250 * t;
            if(_resetMove._c._a > 255) _resetMove._c._a = 255;
        }
        if(_quit._c._a < 255) {
            _quit._c._a += 250 * t;
            if(_quit._c._a > 255) _quit._c._a = 255;
        }*/
    }

    @Override
    public void render(Graphics g) {
        _color.setWhite();
        g.clear(_color);

        _board.render(g);
        _closeButton.render(g);
        _undoButton.render(g);
        _hintsButton.render(g);

        _hintsTxt.render(g);
    }

    /**
     * Process all input incoming form the Input class. Send mouse events to the buttons and exit
     * event if the esc key is pressed
     *
     * @param e (List<Input.TouchEvent>) Event list taken from the Input class
     */
    @Override
    public void processInput(List<Input.TouchEvent> e) {
        // int ptr = e.size() - 1; // Pointer to roam the list
        int ptr = 0;

        while(ptr < e.size()){ // While list is not empty...
            Input.TouchEvent te = e.get(ptr); // Get touch event at pointers position

            switch(te.getType()){
                case CLICKED:
                case PRESSED:
                    _board.sendClickEvent(te);
                    _hints.updateMap(_board);
                    if(_closeButton.isPressed(te.getX(), te.getY())) {
                        //_quit._c._a = 100;
                        _l.setGameState(Logic.GameStates.MENU);
                    }
                    else if(_hintsButton.isPressed(te.getX(), te.getY())){
                        //_hintButton._c._a = 100;
                        _hintsTxt.changeTxt(_hints.helpUser());
                    }
                    else if (_undoButton.isPressed(te.getX(), te.getY())){
                        //_resetMove._c._a = 100;
                        _board.removeLastMove();
                    }
                    if(_hints.isSolved()) _hintsTxt.changeTxt("YOU WIN!!!");
                    break;
                case KEY_RESTART:
                    _l.setMapSize(_board.getMapSize());
                    _l.setGameState(Logic.GameStates.PLAY);
                case KEY_EXIT:
                    _l.closeGame();
                    break;
                default:
                    // Ignore the rest
                    break;
            } // switch

            ptr++;
        } // while
    } // processInput
}
