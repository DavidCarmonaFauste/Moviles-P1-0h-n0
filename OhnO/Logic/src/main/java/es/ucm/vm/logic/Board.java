package es.ucm.vm.logic;

public class Board {
    private CounterTile[][] _map;
    private int _mapSize = 0;

    public Board(int size)
    {
        _map = new CounterTile[size][size];
        _mapSize = size;
    }

    public void setMapSize(int size)
    {
        _mapSize = size;
    }

    public void setMap(CounterTile[][] map)
    {
        _map  = map;
    }

    public CounterTile[][] getMap()
    {
        return _map;
    }

    public int getMapSize()
    {
        return _mapSize;
    }

    public boolean offLimits(Coord pos)
    {
        return (pos._x < 0 ||
                pos._x >= _mapSize ||
                pos._y < 0 ||
                pos._y >= _mapSize);

    }
}
