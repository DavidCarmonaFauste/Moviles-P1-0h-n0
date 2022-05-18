package es.ucm.vm.logic;

import java.util.List;
import java.util.Random;

import es.ucm.vm.engine.Color;
//import es.ucm.vm.engine.Font;
import es.ucm.vm.engine.GameState;
import es.ucm.vm.engine.Graphics;
//import es.ucm.vm.engine.Image;
import es.ucm.vm.engine.Input;
import es.ucm.vm.engine.Rect;

public class PlayGameState implements GameState {
    //---------------------------------------------------------------
    //----------------------Private Attributes-----------------------
    //---------------------------------------------------------------
    Logic _l; // For changing gamestate

    Board _board;
    Hints _hints;
    Button _closeButton, _undoButton, _hintsButton;
    Text _hintsTxt;
    Vector2 _coordOr; // Coord origin
    int _coordOrX; // Coord origin X value
    int _coordOrY; // Coord origin Y value

    //---------------------------------------------------------------
    //--------------------------Constants----------------------------
    //---------------------------------------------------------------
    final Color _clearColor = new Color(255, 255, 255, 255);

    /**
     * Starts up the play state by setting up the logic sizes of the canvas
     * and creating a new map to play
     *
     * @param l       (Logic) used to set logical canvas sizes
     * @param mapSize (int) how big the board will be
     */
    public PlayGameState(Logic l, int mapSize) {
        _l = l;

        _coordOrX = _l._cnv.width / 2;
        _coordOrY = _l._cnv.height / 2;

        _coordOr = new Vector2(_coordOrX, _coordOrY);

        newMap(mapSize);
    }

    /**
     * Creates new board and hint objects, and populates the board
     *
     * @param mapSize (int) how many tiles big is the map
     */
    private void newMap(int mapSize) {
        _board = new Board(mapSize);
        _hints = new Hints(_board);
        fillBoard(mapSize);
    }

    /**
     * Creates the actual map tiles (red, blue or grey) by randomly placing them and keeping
     * a map that can be solved (using the hint mechanics for those checks). When it's done
     * it sets up that tile data into the board for later use
     *
     * @param mapSize          (int) how many tiles wide is the map
     * @param probabilityLimit (float) probability of a filled tile
     * @param step             (double) diameter of a tile, used for tile placement and sizing
     */
    private void generateLevel(int mapSize, float probabilityLimit, double step) {
        BoardTile[][] generatedMap;
        TileColor tileColor;
        //int tries = 0;
        int blueCount;
        int blueInMap = 0, redInMap = 0, greyInMap = 0;
        System.out.println("Generating new level");
        do{
        //tries = 0;
        generatedMap = new BoardTile[mapSize][mapSize];
        for (int i = 0; i < mapSize; ++i) {
            for (int j = 0; j < mapSize; ++j) {
                generatedMap[i][j] = new BoardTile(step * (i - ((double) (mapSize - 1) / 2)),
                        step * (j - ((double) (mapSize - 1) / 2)) * -1, (int) (step),
                        TileColor.GREY, 0, new BoardPosition(i, j));
            }
        }
        for (int i = 0; i < mapSize; ++i) {
            for (int j = 0; j < mapSize; ++j) {
                Random rand = new Random();
                float f = rand.nextFloat();

                if (f >= probabilityLimit && greyInMap <= blueInMap / 1.5) { // grey
                    tileColor = TileColor.GREY;
                    blueCount = 0;
                    greyInMap++;
                } else if ((f <= probabilityLimit / 4 || greyInMap > blueInMap) &&  redInMap <= blueInMap/2) { // red
                    tileColor = TileColor.RED;
                    blueCount = -1;
                    redInMap++;
                } else { // blue
                    rand = new Random();
                    blueCount = 1 + rand.nextInt(mapSize);
                    tileColor = TileColor.BLUE;
                    blueInMap++;
                }
                generatedMap[i][j] = new BoardTile(step * (i - ((double) (mapSize - 1) / 2)),
                        step * (j - ((double) (mapSize - 1) / 2)) * -1, (int) (step),
                        tileColor, blueCount, new BoardPosition(i, j));

                _board.setMap(generatedMap);
                _hints.updateMap(_board);
                boolean triying = true, changeDone = false;
                while (triying) {
                    if (_hints.colorTileIsValid(generatedMap[i][j])) {
                        //generatedMap[i][j] = new_Tile;
                        triying = false;
                    } else {
                        if (generatedMap[i][j]._tileColor == TileColor.RED && !changeDone) {
                            tileColor = TileColor.BLUE;
                            rand = new Random();
                            blueCount = 1 + rand.nextInt(mapSize);
                            redInMap--;
                            blueInMap++;
                            generatedMap[i][j] = new BoardTile(step * (i - ((double) (mapSize - 1) / 2)),
                                    step * (j - ((double) (mapSize - 1) / 2)) * -1, (int) (step),
                                    tileColor, blueCount, new BoardPosition(i, j));
                            changeDone = true;
                        }
                        else if (generatedMap[i][j]._tileColor == TileColor.BLUE && !changeDone) {
                            tileColor = TileColor.RED;
                            blueCount = -1;
                            blueInMap--;
                            redInMap++;
                            generatedMap[i][j] = new BoardTile(step * (i - ((double) (mapSize - 1) / 2)),
                                    step * (j - ((double) (mapSize - 1) / 2)) * -1, (int) (step),
                                    tileColor, blueCount, new BoardPosition(i, j));
                            changeDone = true;
                        }
                        else {
                            //si no puede ser de ningún color reconstruimos
                            i = 0;
                            j = 0;
                            blueInMap = 0; redInMap = 0; greyInMap = 0;
                            for (int x = 0; x < mapSize; ++x) {
                                for (int y = 0; y < mapSize; ++y) {
                                    generatedMap[x][y] = new BoardTile(step * (x - ((double) (mapSize - 1) / 2)),
                                            step * (y - ((double) (mapSize - 1) / 2)) * -1, (int) (step),
                                            TileColor.GREY, 0, new BoardPosition(x, y));
                                }
                            }
                            triying = false;
                        }

                        _board.setMap(generatedMap);
                        _hints.updateMap(_board);
                    }
                }


            }
        }
        _board.setMap(generatedMap);
        _hints.updateMap(_board);
    }while(!_hints.solveMap());
        System.out.println("Finished generating level");
        for (BoardTile[] row : generatedMap) {
            for (BoardTile tile : row) {
                tile.setCoordOrigin(_coordOr);
            }
        }
    }

    /**
     * Creates and sets all the buttons on the play scene
     *
     * @param mapSize   (int) how many tiles wide is the map
     * @param step      (double) diameter of a tile, used for placement and sizing
     * @param smallSide (double) shortened screen width or length, depending which one is smaller
     */
    private void generateButtons(int mapSize, double step, double smallSide) {
        Color boundingBoxColor = new Color(50, 50, 50, 100);
        double buttonY = -smallSide / 2;
        int buttonSize = 30;

        ImageObject closeImage = new ImageObject(-40, buttonY, buttonSize, buttonSize, IMAGE_CLOSE);
        closeImage.setCoordOrigin(_coordOr);
        _closeButton = new Button(-40, buttonY, buttonSize, buttonSize,
                boundingBoxColor, 10, null, closeImage);
        _closeButton.setCoordOrigin(_coordOr);

        ImageObject undoImage = new ImageObject(0, buttonY, buttonSize, buttonSize, IMAGE_HISTORY);
        undoImage.setCoordOrigin(_coordOr);
        _undoButton = new Button(0, buttonY, buttonSize, buttonSize,
                boundingBoxColor, 10, null, undoImage);
        _undoButton.setCoordOrigin(_coordOr);

        ImageObject hintImage = new ImageObject(40, buttonY, buttonSize, buttonSize, IMAGE_EYE);
        hintImage.setCoordOrigin(_coordOr);
        _hintsButton = new Button(40, buttonY, buttonSize, buttonSize,
                boundingBoxColor, 10, null, hintImage);
        _hintsButton.setCoordOrigin(_coordOr);

        _hintsTxt = new Text(step / 2 * (mapSize - 1) - (smallSide / 4), smallSide / 2,
                new Color(50, 50, 50, 255), (int) (step) / 2,
                (mapSize) + "x" + (mapSize), false, FONT_JOSEFIN_BOLD);

        _hintsTxt.setCoordOrigin(_coordOr);
    }

    /**
     * Populates the board and the buttons for the play scene, depending on the size of the map
     *
     * @param mapSize (int) how many tiles wide is the map
     */
    private void fillBoard(int mapSize) {
        // Calculate size, used for tile placement
        double step, smallSide;

        if (_l._eng.getWinHeight() < _l._eng.getWinWidth())
            smallSide = _l._eng.getWinHeight() * 0.8;
        else
            smallSide = _l._eng.getWinWidth() * 0.8;
        smallSide = _l._eng.getGraphics().screenToLogicX((int) smallSide);

        // calculate diameter for tiles
        step = smallSide / (mapSize + 1.5);
        // set the probability of a filled tile
        float probabilityLimit = 0.25f;

        // generate and assign the tiles to the map
        generateLevel(mapSize, probabilityLimit, step);

        // generate play scene buttons
        generateButtons(mapSize, step, smallSide);
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
    public void update(double t) {
    }

    /**
     * Renders all GameObjects in their specific locations. Receives an instance of Graphics
     * to call the drawing methods.
     *
     * @param g (Graphics) Instance of Graphics
     */
    @Override
    public void render(Graphics g) {
        g.clear(_clearColor);

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

        while (ptr < e.size()) { // While list is not empty...
            Input.TouchEvent te = e.get(ptr); // Get touch event at pointers position

            switch (te.getType()) {
                case CLICKED:
                case PRESSED:
                    int x = te.getX();
                    int y = te.getY();
                    _board.sendClickEvent(te);
                    _hints.updateMap(_board);
                    if (_closeButton.isPressed(x, y)) {
                        _l.setGameState(Logic.GameStates.MENU);
                    } else if (_hintsButton.isPressed(x, y)) {
                        _hintsTxt.changeTxt(_hints.helpUser());
                    } else if (_undoButton.isPressed(x, y)) {
                        _board.removeLastMove();
                    }
                    if (_hints.isSolved()) _hintsTxt.changeTxt("YOU WIN!!!");
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
