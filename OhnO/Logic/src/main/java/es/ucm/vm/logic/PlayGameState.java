package es.ucm.vm.logic;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Stack;

import es.ucm.vm.engine.Color;
import es.ucm.vm.engine.GameState;
import es.ucm.vm.engine.Graphics;
import es.ucm.vm.engine.Input;
import es.ucm.vm.engine.Rect;

public class PlayGameState implements GameState {
    //---------------------------------------------------------------
    //--------------------------Constants----------------------------
    //---------------------------------------------------------------
    final String IMAGE_CLOSE = "sprites/close.png";
    final String IMAGE_EYE = "sprites/eye.png";
    final String IMAGE_HISTORY = "sprites/history.png";
    final String FONT_JOSEFIN_BOLD = "fonts/JosefinSans-Bold.ttf";

    //---------------------------------------------------------------
    //----------------------Private Attributes-----------------------
    //---------------------------------------------------------------
    Logic _l; // For changing gamestate

    Board _board;
    Hints _hints;
    Button _closeButton, _undoButton, _hintsButton;
    TextGameObject _hintsTxt;
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
     * @param step             (double) diameter of a tile, used for tile placement and sizing
     */

    private void generateLevel(int mapSize, double step) {
        BoardTile[][] generatedMap;
        System.out.println("Generating new level");

        // fill the map with all blues ----------------------------------------------
        generatedMap = new BoardTile[mapSize][mapSize];
        for (int i = 0; i < mapSize; ++i) {
            for (int j = 0; j < mapSize; ++j) {
                generatedMap[i][j] = new BoardTile(step * (i - ((double) (mapSize - 1) / 2)),
                        step * (j - ((double) (mapSize - 1) / 2)) * -1, (int) (step),
                        TileColor.BLUE, 0, new BoardPosition(i, j));
            }
        }

        _board.setMap(generatedMap);
        _hints.updateMap(_board);
        _hints.newSolveMap(_board, false);
        _hints.updateMap(_board);

        // adds reds and re-counts the number of blues each tile sees -------------
        addRedsAndRecountBlues(generatedMap, mapSize, step);

        // count reds and shuffle the tile pool
        int walls = 0;
        Stack<BoardTile> pool = new Stack<BoardTile>();
        for(BoardTile[] column : _board.getMap()) {
            for(BoardTile t : column) {
                pool.push(t);
                if (t._tileColor == TileColor.RED)
                    walls++;
            }
        }
        Collections.shuffle(pool, new Random());

        _hints.updateMap(_board);

        // try to place grey tiles ------------------------------------------------
        tryPlacingGreys(pool, walls);

        //System.out.println("tries: " + attempts + " of " + minEmptyTile*8);
        //System.out.println("empties: "  + emptyTiles);

        System.out.println("Finished generating level");
        for (BoardTile[] row : generatedMap) {
            for (BoardTile t : row) {
                t.setCoordOrigin(_coordOr);
            }
        }
    }

    /**
     * Checks that the tiles stack doesn't have any tile that sees more than it should. If one
     * exists, it places a wall and recounts the visible blues, all while making sure the map
     * stays resolvable
     *
     * @param generatedMap (BoardTile[][]) map of tiles
     * @param mapSize (int) width of the map in tiles
     * @param step (size of a tile)
     */
    private void addRedsAndRecountBlues(BoardTile[][] generatedMap, int mapSize, double step) {
        boolean tryAgain = true;
        int attempts = 0;
        BoardTile tile;
        int maxAllowed = _board.getMapSize();

        while (tryAgain && attempts++ < 99) {
            tryAgain = false;
            Stack<BoardTile> maxTiles = new Stack<BoardTile>();

            for(BoardTile[] column : _board.getMap())
            {
                for(BoardTile t : column)
                {
                    if (t._count > maxAllowed) {
                        maxTiles.push(t);
                    }
                }
            }
            Collections.shuffle(maxTiles, new Random());

            for (int i = 0; i < maxTiles.size(); i++) {
                tile = maxTiles.pop();
                if (tile._count > maxAllowed) {
                    int min = 1, max = maxAllowed;
                    Stack<BoardTile> cuts = new Stack<BoardTile>();
                    BoardTile cut = null, firstCut = null;

                    for (BoardPosition dir : BoardPosition.DIRECTIONS) {
                        int distance = 0;
                        if(_board.getTileInDir(tile, dir) != null) {
                            for (BoardTile nextTile = _board.getTileInDir(tile, dir); nextTile._tileColor != TileColor.RED; nextTile = _board.getTileInDir(nextTile, dir)) {
                                distance++;
                                if (distance >= min && distance <= max)
                                    cuts.push(tile);
                                if (_board.getTileInDir(nextTile, dir) == null)
                                    break;
                            }
                        }
                    }
                    Collections.shuffle(cuts, new Random());

                    while (cut == null && cuts.size() > 0) {
                        cut = cuts.pop();
                        if (firstCut == null)
                            firstCut = cut;
                    }
                    if (cut == null)
                        cut = firstCut;
                    if (cut != null) {
                        generatedMap[cut._boardPos._x][cut._boardPos._y] = new BoardTile(step * (cut._boardPos._x - ((double) (mapSize - 1) / 2)),
                                step * (cut._boardPos._y - ((double) (mapSize - 1) / 2)) * -1, (int) (step),
                                TileColor.RED, -1, new BoardPosition(cut._boardPos._x, cut._boardPos._y));
                        for (int z = 0; z < mapSize; ++z) {
                            for (int j = 0; j < mapSize; ++j) {
                                if(generatedMap[z][j]._tileColor != TileColor.RED) {
                                    generatedMap[z][j] = new BoardTile(step * (z - ((double) (mapSize - 1) / 2)),
                                            step * (j - ((double) (mapSize - 1) / 2)) * -1, (int) (step),
                                            TileColor.BLUE, 0, new BoardPosition(z, j));
                                }
                            }
                        }
                        _board.setMap(generatedMap);
                        _hints.updateMap(_board);
                        _hints.newSolveMap(_board, false);
                        _hints.updateMap(_board);
                        tryAgain = true;
                    }
                    break;
                }
            }
        }
    } // recountBluesAddReds

    /**
     * Given a map with a possible solution, removes a singular tile and turns it into
     * a grey tile, and checks if the change is correct or needs to be undone
     *
     * @param pool (Stack(BoardTIle)) stack with shuffled tiles
     * @param walls (int) number of red tiles
     * @return (boolean) returns a "try again" value
     */
    private void tryPlacingGreys(Stack<BoardTile> pool, int walls) {
        boolean tryAgain = true;
        int attempts = 0;
        BoardTile tile = null;
        int minWalls = 3, emptyTiles = 0, minEmptyTile = pool.size()/2;

        while (tryAgain  && attempts++ < (minEmptyTile*8)) {
            tryAgain = emptyTiles >= minEmptyTile;

            if(pool.isEmpty()) {
                walls = 0;

                for(BoardTile[] column : _board.getMap()) {
                    for(BoardTile t : column) {
                        pool.push(t);
                        if (t._tileColor == TileColor.RED)
                            walls++;
                    }
                }
            } // if (pool.isEmpty())

            // only use the pool for x,y coordinates, but retrieve the tile again because it has been rebuilt
            tile = pool.pop();
            boolean isWall = tile._tileColor == TileColor.RED;

            // make sure there is a minimum of walls
            if (isWall && walls <= minWalls)
                continue;

            TileColor lastColor = TileColor.GREY;
            int lastValue = 0;
            switch (tile._tileColor)
            {
                case BLUE : lastColor = TileColor.BLUE; lastValue = tile._count; break;
                case RED : lastColor = TileColor.RED; lastValue = -1; break;
            }
            tile.updateTileColor(TileColor.GREY);
            tile.updateCount(0);
            tile._tileInfo = null;

            _hints.updateMap(_board);
            if (_hints.newSolveMap(null, true)) {
                tile.activateButton();
                if(lastValue != 0)emptyTiles++;
                if (isWall)
                    walls--;
            } else {
                tile.updateTileColor(lastColor);
                tile.updateCount(lastValue);
            }

            tryAgain = true;
        }
    } // tryPlacingGreys

    /**
     * Creates and sets all the buttons on the play scene
     *
     * @param mapSize   (int) how many tiles wide is the map
     * @param step      (double) diameter of a tile, used for placement and sizing
     */
    private void generateButtons(int mapSize, double step) {
        Color boundingBoxColor = new Color(50, 50, 50, 100);
        double buttonY = -240; // a bit less than half the height of the logical canvas height
        int buttonSize = 30;

        ImageGameObject closeImage = new ImageGameObject(-40, buttonY, buttonSize, buttonSize, IMAGE_CLOSE);
        closeImage.setCoordOrigin(_coordOr);
        _closeButton = new Button(-40, buttonY, buttonSize, buttonSize,
                boundingBoxColor, 10, null, closeImage);
        _closeButton.setCoordOrigin(_coordOr);

        ImageGameObject undoImage = new ImageGameObject(0, buttonY, buttonSize, buttonSize, IMAGE_HISTORY);
        undoImage.setCoordOrigin(_coordOr);
        _undoButton = new Button(0, buttonY, buttonSize, buttonSize,
                boundingBoxColor, 10, null, undoImage);
        _undoButton.setCoordOrigin(_coordOr);

        ImageGameObject hintImage = new ImageGameObject(40, buttonY, buttonSize, buttonSize, IMAGE_EYE);
        hintImage.setCoordOrigin(_coordOr);
        _hintsButton = new Button(40, buttonY, buttonSize, buttonSize,
                boundingBoxColor, 10, null, hintImage);
        _hintsButton.setCoordOrigin(_coordOr);

        _hintsTxt = new TextGameObject(step / 2 * (mapSize - 1) + (buttonY / 2), -buttonY,
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

        // calculate diameter for tiles
        step = _l.getCanvasSize().width / (mapSize + 1.5);

        // generate and assign the tiles to the map
        generateLevel(mapSize, step);

        // generate play scene buttons
        generateButtons(mapSize, step);
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
