package es.ucm.vm.logic;

import static es.ucm.vm.logic.Color.BLUE;

public class Watcher {
    private CounterTile[][] _map;
    private int _mapSize = 0;

    public void setMapSize(int size) { _mapSize = size;}
    public void setMap(CounterTile[][] map) { _map  = map; }

    public CounterTile[][] getMap(){ return _map; }
    public int getMapSize() { return _mapSize; }

    public int gimli(Coord pos, Coord dir, Color watching)
    {
        int counted = 0;
        Coord _newPos = Coord.add(pos, dir);

        if (!offLimits(_newPos))
        {
            if(_map[_newPos._x][_newPos._y]._c == watching)
            {
                counted++;
                counted += gimli(_newPos, dir, watching);
            }
        }
        return counted;
    }

    public int legolas(Coord pos, Coord dir, Color watching)
    {
        int _free = 0;
        Coord _newPos = Coord.add(pos, dir);

        if (!offLimits(_newPos))
        {
            if(_map[_newPos._x][_newPos._y]._c != watching)
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
    private boolean offLimits(Coord pos)
    {
        return (pos._x < 0 ||
                pos._x >= _mapSize ||
                pos._y < 0 ||
                pos._y >= _mapSize);

    }
}


enum Color {GREY, RED, BLUE};

class Coord {
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
    public Color _c;
    public Coord _pos;
}

class CounterTile extends Tile {
    public CounterTile(Color c, Coord pos, int count){
        _c = c;
        _pos = pos;
        _count = count;
    }
    public int _count;
}