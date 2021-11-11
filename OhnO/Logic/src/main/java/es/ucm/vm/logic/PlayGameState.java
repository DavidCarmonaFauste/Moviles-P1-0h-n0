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

    private CounterTile[][] fillBoard(int mapSize) {
        // TODO: ACTUALLY GENERATE BOARD HERE

        CounterTile[][] mapaPruebas = new CounterTile[4][4];
        /*----------------------------------------------------*///y -> 0
        mapaPruebas[0][0] = new CounterTile(TileColor.GREY, new Coord(0,0), 0);
        mapaPruebas[1][0] = new CounterTile(TileColor.RED,  new Coord(1,0), 0);
        mapaPruebas[2][0] = new CounterTile(TileColor.GREY, new Coord(2,0), 0);
        mapaPruebas[3][0] = new CounterTile(TileColor.GREY, new Coord(3,0), 0);
        /*----------------------------------------------------*///y -> 1
        mapaPruebas[0][1] = new CounterTile(TileColor.RED,  new Coord(0,1), 0);
        mapaPruebas[1][1] = new CounterTile(TileColor.GREY, new Coord(1,1), 0);
        mapaPruebas[2][1] = new CounterTile(TileColor.BLUE, new Coord(2,1), 2);
        mapaPruebas[3][1] = new CounterTile(TileColor.GREY, new Coord(3,1), 0);
        /*----------------------------------------------------*///y -> 2
        mapaPruebas[0][2] = new CounterTile(TileColor.GREY, new Coord(0,2), 0);
        mapaPruebas[1][2] = new CounterTile(TileColor.BLUE, new Coord(1,2), 1);
        mapaPruebas[2][2] = new CounterTile(TileColor.GREY, new Coord(2,2), 0);
        mapaPruebas[3][2] = new CounterTile(TileColor.GREY, new Coord(3,2), 0);
        /*----------------------------------------------------*///y -> 3
        mapaPruebas[0][3] = new CounterTile(TileColor.GREY, new Coord(0,3), 0);
        mapaPruebas[1][3] = new CounterTile(TileColor.GREY, new Coord(1,3), 0);
        mapaPruebas[2][3] = new CounterTile(TileColor.BLUE, new Coord(2,3), 2);
        mapaPruebas[3][3] = new CounterTile(TileColor.BLUE, new Coord(3,3), 4);
        /*----------------------------------------------------*/

        return mapaPruebas;
    }

    @Override
    public void update(double t) {
        if (!_hints._mapSolved)
            _hints.solveMap();
    }

    @Override
    public void render(Graphics g) {
        if (!_hints._mapSolved) {
            for(int y = 0; y < 4; y++)
            {
                System.out.println("+---+---+---+---+");
                for(int x = 0; x < 4; x++)
                {
                    if(_board.getMap()[x][y]._Tile_color == TileColor.BLUE)      System.out.print("| " + _board.getMap()[x][y]._count + " ");
                    else if (_board.getMap()[x][y]._Tile_color == TileColor.RED) System.out.print("| X ");
                    else                                 System.out.print("|   ");
                }
                System.out.println("|");
            }
            System.out.println("+---+---+---+---+");
        }
    }

    @Override
    public void processInput(List<Input.TouchEvent> e) {

    }
}
