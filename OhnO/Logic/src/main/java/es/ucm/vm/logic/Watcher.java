package es.ucm.vm.logic;

import static es.ucm.vm.logic.Color.BLUE;

public class Watcher {
    public int gimli(CounterTile tile, Coord dir, Color watching) {
        return 0;
    }

    public int legolas(CounterTile tile, Coord dir, Color watching) {
        return 0;
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
}

class Tile {
    public Color _c;
    public Coord _pos;
}

class CounterTile extends Tile {
    public int _count;
}