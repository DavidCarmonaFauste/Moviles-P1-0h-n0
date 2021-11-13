package es.ucm.vm.logic;

import java.util.List;
import java.util.Random;

import es.ucm.vm.engine.Color;
import es.ucm.vm.engine.Font;
import es.ucm.vm.engine.Graphics;
import es.ucm.vm.engine.Input;
import es.ucm.vm.engine.Rect;
import sun.rmi.runtime.Log;

public class PlayGameState implements GameState{
    Logic _l; // For changing gamestate
    Color _color;

    Board _board;
    Hints _hints;

    BoardTile _quit, _resetMove, _hintButton;
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



    /*----------------------------------------------------*//*//y -> 0
    mapaPruebas[0][0] = new BoardTile(100,100, d, TileColor.GREY, 0, new BoardPosition(0,0));
    mapaPruebas[1][0] = new BoardTile(200, 100, d, TileColor.RED, 0, new BoardPosition(1,0));
    mapaPruebas[2][0] = new BoardTile(300, 100, d, TileColor.GREY, 0, new BoardPosition(2,0));
    mapaPruebas[3][0] = new BoardTile(400, 100, d, TileColor.GREY, 0, new BoardPosition(3,0));
    *//*----------------------------------------------------*//*//y -> 1
    mapaPruebas[0][1] = new BoardTile(100,200, d, TileColor.RED, 0, new BoardPosition(0,1));
    mapaPruebas[1][1] = new BoardTile(200, 200, d, TileColor.GREY, 0, new BoardPosition(1,1));
    mapaPruebas[2][1] = new BoardTile(300, 200, d, TileColor.BLUE, 2, new BoardPosition(2,1));
    mapaPruebas[3][1] = new BoardTile(400, 200, d, TileColor.GREY, 0, new BoardPosition(3,1));
    *//*----------------------------------------------------*//*//y -> 2
    mapaPruebas[0][2] = new BoardTile(100, 300, d, TileColor.GREY, 0, new BoardPosition(0,2));
    mapaPruebas[1][2] = new BoardTile(200, 300, d, TileColor.BLUE, 1, new BoardPosition(1,2));
    mapaPruebas[2][2] = new BoardTile(300, 300, d, TileColor.GREY, 0, new BoardPosition(2,2));
    mapaPruebas[3][2] = new BoardTile(400, 300, d, TileColor.GREY, 0, new BoardPosition(3,2));
    *//*----------------------------------------------------*//*//y -> 3
    mapaPruebas[0][3] = new BoardTile(100, 400, d, TileColor.GREY, 0, new BoardPosition(0,3));
    mapaPruebas[1][3] = new BoardTile(200, 400, d, TileColor.GREY, 0, new BoardPosition(1,3));
    mapaPruebas[2][3] = new BoardTile(300, 400, d, TileColor.BLUE, 2, new BoardPosition(2,3));
    mapaPruebas[3][3] = new BoardTile(400, 400, d, TileColor.BLUE, 4, new BoardPosition(3,3));
    *//*----------------------------------------------------*/

    private void fillBoard(int mapSize) {
        // Calculate sizings used for tile placement
        double step = 0, smallSide = 0;
        Rect canvasSize = _l.getCanvasSize();
        if (canvasSize.getHeight() < canvasSize.getWidth())
            smallSide = canvasSize.getHeight() * 0.8;
        else
            smallSide = canvasSize.getWidth();
        step = smallSide / (mapSize + 3);
        // calculate diameter for tiles
        int d = (int)(step);
        // set the probability of a filled tile
        float probabilityLimit = 0.25f;
        // extra variables used in tile creation
        int blueCount = 0;
        TileColor tileColor = TileColor.GREY;

        BoardTile[][] generatedMap;

        int tries = 0;
        System.out.println("Cargando Nuevo nivel");
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
            while(!_hints.solveMap() && tries < 50)
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
        System.out.println("nivel cargado");
        for (BoardTile row[]:generatedMap) {
            for (BoardTile tile: row) {
                tile.setCoordOrigin(_coordOr);
            }
        }
        _quit = (new BoardTile(step/2 - (smallSide / 4),                     -190, d, TileColor.GREY, 0, new BoardPosition(0, 0)));
        _quit.setTxt("Q");
        _quit.setCoordOrigin(_coordOr);
        _resetMove = new BoardTile(step/2 * (mapSize - 1) - (smallSide / 4),     -190, d, TileColor.GREY, 0, new BoardPosition(0, 0));
        _resetMove.setTxt("R");
        _resetMove.setCoordOrigin(_coordOr);
        _hintButton = new BoardTile(step * ((mapSize-1) - 0.5) - (smallSide / 4), -190, d, TileColor.GREY, 0, new BoardPosition(0, 0));
        _hintButton.setTxt("H");
        _hintButton.setCoordOrigin(_coordOr);

        _hintsTxt = new Text(step/2 * (mapSize - 1) - (smallSide / 4), 200, new Color(50,50,50,255), d/2,
                            Integer.toString(mapSize) + "x" + Integer.toString(mapSize), false, Font.FONT_JOSEFIN_BOLD);
        _hintsTxt.setCoordOrigin(_coordOr);
    }

    @Override
    public void update(double t) {
        //if (!_hints._sameMap)

        /*
        for(int y = 0; y < 4; y++)
        {
            System.out.println("+---+---+---+---+");
            for(int x = 0; x < 4; x++)
            {
                if(_board.getMap()[x][y]._tileColor == TileColor.BLUE)      System.out.print("| " + _board.getMap()[x][y]._count + " ");
                else if (_board.getMap()[x][y]._tileColor == TileColor.RED) System.out.print("| X ");
                else                                 System.out.print("|   ");
            }
            System.out.println("|");
        }
        System.out.println("+---+---+---+---+");*/
    }

    @Override
    public void render(Graphics g) {
        _color.setWhite();
        g.clear(_color);

        _board.render(g);
        _quit.render(g);
        _hintButton.render(g);
        _resetMove.render(g);

        _hintsTxt.render(g);
    }

    /**
     * Process all input incoming form the Input class. If mouse clicked or screen touched, throw
     * player to his right until it hits a new line or leaves the play zone.
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
                    if(_quit.isPressed(te.getX(), te.getY())) _board.removeLastMove();//por hacer
                    else if(_hintButton.isPressed(te.getX(), te.getY())) _hintsTxt.changeTxt(_hints.helpUser());
                    else if (_resetMove.isPressed(te.getX(), te.getY())) _board.removeLastMove();
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
