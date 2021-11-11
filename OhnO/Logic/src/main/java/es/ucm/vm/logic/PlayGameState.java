package es.ucm.vm.logic;

import java.util.List;

import es.ucm.vm.engine.Color;
import es.ucm.vm.engine.Graphics;
import es.ucm.vm.engine.Input;

public class PlayGameState implements GameState{
    Logic _l; // For changing gamestate
    Color _color;

    Board _board;
    Hints _hints;

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
        _board.setMap(fillBoard(mapSize));

        _hints = new Hints(_board);
    }

    private BoardTile[][] fillBoard(int mapSize) {
        // TODO: ACTUALLY GENERATE BOARD HERE
        int d = 30; // temp diameter for tiles
        BoardTile[][] mapaPruebas = new BoardTile[4][4];
        /*----------------------------------------------------*///y -> 0
        mapaPruebas[0][0] = new BoardTile(100,100, d, TileColor.GREY, 0, new BoardPosition(0,0));
        mapaPruebas[1][0] = new BoardTile(200, 100, d, TileColor.RED, 0, new BoardPosition(1,0));
        mapaPruebas[2][0] = new BoardTile(300, 100, d, TileColor.GREY, 0, new BoardPosition(2,0));
        mapaPruebas[3][0] = new BoardTile(400, 100, d, TileColor.GREY, 0, new BoardPosition(3,0));
        /*----------------------------------------------------*///y -> 1
        mapaPruebas[0][1] = new BoardTile(100,200, d, TileColor.RED, 0, new BoardPosition(0,1));
        mapaPruebas[1][1] = new BoardTile(200, 200, d, TileColor.GREY, 0, new BoardPosition(1,1));
        mapaPruebas[2][1] = new BoardTile(300, 200, d, TileColor.BLUE, 2, new BoardPosition(2,1));
        mapaPruebas[3][1] = new BoardTile(400, 200, d, TileColor.GREY, 0, new BoardPosition(3,1));
        /*----------------------------------------------------*///y -> 2
        mapaPruebas[0][2] = new BoardTile(100, 300, d, TileColor.GREY, 0, new BoardPosition(0,2));
        mapaPruebas[1][2] = new BoardTile(200, 300, d, TileColor.BLUE, 1, new BoardPosition(1,2));
        mapaPruebas[2][2] = new BoardTile(300, 300, d, TileColor.GREY, 0, new BoardPosition(2,2));
        mapaPruebas[3][2] = new BoardTile(400, 300, d, TileColor.GREY, 0, new BoardPosition(3,2));
        /*----------------------------------------------------*///y -> 3
        mapaPruebas[0][3] = new BoardTile(100, 400, d, TileColor.GREY, 0, new BoardPosition(0,3));
        mapaPruebas[1][3] = new BoardTile(200, 400, d, TileColor.GREY, 0, new BoardPosition(1,3));
        mapaPruebas[2][3] = new BoardTile(300, 400, d, TileColor.BLUE, 2, new BoardPosition(2,3));
        mapaPruebas[3][3] = new BoardTile(400, 400, d, TileColor.BLUE, 4, new BoardPosition(3,3));
        /*----------------------------------------------------*/

        for (BoardTile row[]:mapaPruebas) {
            for (BoardTile tile: row) {
                tile.setCoordOrigin(_coordOr);
            }
        }

        return mapaPruebas;
    }

    @Override
    public void update(double t) {
        if (!_hints._mapSolved)
            _hints.solveMap();
    }

    @Override
    public void render(Graphics g) {
        _color.setWhite();
        g.setColor(_color);
        _board.render(g);
        if (!_hints._mapSolved) {
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
            System.out.println("+---+---+---+---+");
        }
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