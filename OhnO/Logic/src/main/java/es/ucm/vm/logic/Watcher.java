package es.ucm.vm.logic;

/**
 * Class with functions that can count tiles and return useful information
 */
public class Watcher {
    Board _board;

    public Watcher(Board b) {
        _board = b;
    }

    /**
     * As long as the board contains the "watched" color in the direction given, it keeps counting
     * @param pos (BoardPosition) Starting position
     * @param dir (BoardPosition) Direction
     * @param watching (TileColor) one of the three tile colors
     * @return (int) how many tiles of that color are in a consecutive line
     */
    public int tilesInfFrontOfRecursive(BoardPosition pos, BoardPosition dir, TileColor watching)
    {
        int counted = 0;
        BoardPosition _newPos = BoardPosition.add(pos, dir);

        if (!_board.offLimits(_newPos))
        {
            if(_board.getMap()[_newPos._x][_newPos._y]._tileColor == watching)
            {
                counted++;
                counted += tilesInfFrontOfRecursive(_newPos, dir, watching);
            }
        }
        return counted;
    }

    public int tilesInfFrontOf(BoardPosition pos, BoardPosition dir, TileColor watching)
    {
        int counted = 0;
        BoardPosition _newPos = BoardPosition.add(pos, dir);

        while(!_board.offLimits(_newPos) && _board.getMap()[_newPos._x][_newPos._y]._tileColor == watching)
        {
            counted++;
            _newPos = BoardPosition.add(_newPos, dir);
        }
        return counted;
    }

    /**
     * It keeps counting in a direction as long as the tiles aren't of the "watched" color or the map
     * isn't over
     * @param pos (BoardPosition) Starting position
     * @param dir (BoardPosition) Direction
     * @param watching (TileColor) One of the three tile colors
     * @return (int) How many tiles there are between the starting position and the first "watched" tile
     */
    public int closerTileRecursive(BoardPosition pos, BoardPosition dir, TileColor watching)
    {
        int _free = 0;
        BoardPosition _newPos = BoardPosition.add(pos, dir);

        if (!_board.offLimits(_newPos))
        {
            if(_board.getMap()[_newPos._x][_newPos._y]._tileColor != watching)
            {
                _free++;
                int l = closerTileRecursive(_newPos, dir, watching);
                if(l == -1) return -1;
                else _free += l;
            }
            return _free;
        }
        return  -1;
    }

    public int closerTile(BoardPosition pos, BoardPosition dir, TileColor watching) {
        int _free = 0;
        BoardPosition _newPos = BoardPosition.add(pos, dir);

        while (!_board.offLimits(_newPos) && _board.getMap()[_newPos._x][_newPos._y]._tileColor != watching) {
            _free++;
            _newPos = BoardPosition.add(_newPos, dir);
        }
        if (_free == 0) return -1;
        return _free;
    }
}