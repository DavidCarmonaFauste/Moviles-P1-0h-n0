package es.ucm.vm.logic;

public class Watcher {
    Board _board;

    public Watcher(Board b) {
        _board = b;
    }

    public int gimli(BoardPosition pos, BoardPosition dir, TileColor watching)
    {
        int counted = 0;
        BoardPosition _newPos = BoardPosition.add(pos, dir);

        if (!_board.offLimits(_newPos))
        {
            if(_board.getMap()[_newPos._x][_newPos._y]._tileColor == watching)
            {
                counted++;
                counted += gimli(_newPos, dir, watching);
            }
        }
        return counted;
    }

    public int legolas(BoardPosition pos, BoardPosition dir, TileColor watching)
    {
        int _free = 0;
        BoardPosition _newPos = BoardPosition.add(pos, dir);

        if (!_board.offLimits(_newPos))
        {
            if(_board.getMap()[_newPos._x][_newPos._y]._tileColor != watching)
            {
                _free++;
                int l = legolas(_newPos, dir, watching);
                if(l == -1) return -1;
                else _free += l;
            }
            return _free;
        }
        return  -1;
    }
}