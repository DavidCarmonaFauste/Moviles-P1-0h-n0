package es.ucm.vm.logic;

import java.util.List;
import java.util.Random;

import es.ucm.vm.engine.Color;
import es.ucm.vm.engine.Font;
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

    Text _text;

    public PlayGameState(Logic l, int mapSize) {
        _l = l;
        _color = new Color();

        _coordOrX = _l._cnv.width/2;
        _coordOrY = _l._cnv.height/2;

        _coordOr = new Vector2(_coordOrX, _coordOrY);

        newMap(mapSize);
        _text = new Text(0,0, new Color(100, 100, 100, 255), 30, "Hola", false, Font.FONT_JOSEFIN_BOLD);
        _text.setCoordOrigin(_coordOr);
    }

    public void newMap(int mapSize) {
        _board = new Board(mapSize);
        _hints = new Hints(_board);
        _board.setMap(fillBoard(mapSize));
        _hints.updateMap(_board);

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

    private BoardTile[][] fillBoard(int mapSize) {
        // TODO: ACTUALLY GENERATE BOARD HERE
        int d = 30; // temp diameter for tiles
        float probabilityLimit = 0.1f;
        int blueCount;

        BoardTile[][] generatedMap = new BoardTile[mapSize][mapSize];

        for (int i = 0; i < mapSize; ++i) {
            for (int j = 0; j < mapSize; ++j) {
                Random rand = new Random();
                float f = rand.nextFloat();

                if (f >= probabilityLimit) { // grey
                    generatedMap[i][j] = new BoardTile(-200 + i * 100, -200 + j * 100, d,
                            TileColor.GREY, 0, new BoardPosition(i, j));
                }
                else {
                    rand = new Random();
                    blueCount = 1 + rand.nextInt(mapSize);
                    generatedMap[i][j] = new BoardTile(-200 + i * 100, -200 + j * 100, d,
                            TileColor.BLUE, blueCount, new BoardPosition(i, j));
                }
            }
        }

        for (BoardTile row[]:generatedMap) {
            for (BoardTile tile: row) {
                tile.setCoordOrigin(_coordOr);
            }
        }
        return generatedMap;






        /*BoardTile[][] mapaPruebas = new BoardTile[mapSize][mapSize];
        // Vector de bool para saber las casillas que relleno aleatoriamente de azules, y depués recorrer el resto para ponerlas en gris
        boolean rellenas [][] = new boolean[mapSize][mapSize];

        for (int i = 0; i < rellenas.length; i++) {
            for (int j = 0; j < rellenas[i].length; j++) {
                rellenas[i][j] = false;
            }
        }

        // Generación de 'mpaSize' casillas aleatorias azules con 'maxSize' números aleatorios
        for (int i = 0; i < mapSize; i++){
            int x = (int)(Math.random()*(mapSize));
            int y = (int)(Math.random()*(mapSize));

            rellenas[x][y] = true;

            int count = (int)(Math.random()*(mapSize+1));

            mapaPruebas[x][y] = new BoardTile((x +1)*100,(y+1)*100, d, TileColor.BLUE, count, new BoardPosition(x,y));
        }

        for (int i = 0; i < mapSize; i++) {
            for (int j = 0; j < mapSize; j++) {
                if(!rellenas[i][j])
                    mapaPruebas[i][j] = new BoardTile((i +1)*100,(j+1)*100, d, TileColor.GREY, 0, new BoardPosition(i,j));
            }
        }*/



        /*_board.setMap(mapaPruebas);
        _hints.updateMap(_board);
        while (!_hints.solveMap()){
            int x = (int)(Math.random()*(mapSize));
            int y = (int)(Math.random()*(mapSize));

            rellenas[x-1][y-1] = true;

            int count = (int)(Math.random()*(mapSize+1));

            mapaPruebas[x][y] = new BoardTile((x +1)*100,(y+1)*100, d, TileColor.BLUE, count, new BoardPosition(x,y));
        }


        for (BoardTile row[]:mapaPruebas) {
            for (BoardTile tile: row) {
                tile.setCoordOrigin(_coordOr);
            }
        }

        return mapaPruebas;*/
    }

    @Override
    public void update(double t) {
        if (!_hints._sameMap)
            _hints.solveMap();
    }

    @Override
    public void render(Graphics g) {
        _color.setWhite();
        g.setColor(_color);
        _board.render(g);
        if (!_hints._sameMap) {
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

        _text.render(g);
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
