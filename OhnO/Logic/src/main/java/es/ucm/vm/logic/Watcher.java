package es.ucm.vm.logic;

public class Watcher {
    Board _board;

    public Watcher(Board b) {
        _board = b;
    }

    public int gimli(Coord pos, Coord dir, TileColor watching)
    {
        int counted = 0;
        Coord _newPos = Coord.add(pos, dir);

        if (!_board.offLimits(_newPos))
        {
            if(_board.getMap()[_newPos._x][_newPos._y]._Tile_color == watching)
            {
                counted++;
                counted += gimli(_newPos, dir, watching);
            }
        }
        return counted;
    }

    public int legolas(Coord pos, Coord dir, TileColor watching)
    {
        int _free = 0;
        Coord _newPos = Coord.add(pos, dir);

        if (!_board.offLimits(_newPos))
        {
            if(_board.getMap()[_newPos._x][_newPos._y]._Tile_color != watching)
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


enum TileColor {GREY, RED, BLUE}

class Coord {
    public static final Coord[] DIRECTIONS;
    static {
        DIRECTIONS = new Coord[]{new Coord(0, 1), new Coord(0, -1), new Coord(1, 0), new Coord(-1, 0)};
    }

    public int _x;
    public int _y;

    public Coord(int x, int y) {
        _x = x;
        _y = y;
    }

    public static Coord add(Coord a, Coord b) {
        return new Coord(a._x + b._x,a._y + b._y);
    }

    public static boolean compare(Coord a, Coord b) {
        return a._x == b._x && a._y == b._y;
    }
}

class Tile {
    public TileColor _Tile_color;
    public Coord _pos;
}

class CounterTile extends Tile {
    public CounterTile(TileColor c, Coord pos, int count){
        this._Tile_color = c;
        this._pos = pos;
        this._count = count;
    }

    public int _count;
}