package es.ucm.vm.logic;

public class Hints {
    public Watcher _watcher;
    Board _board;
    public boolean _mapSolved = false;

    public Hints(Board b)
    {
        _board = b;
        _watcher = new Watcher(_board);
    }

    public void updateMap(Board b)
    {
        _board = b;
    }

    public void solveMap()
    {
        for(CounterTile[] column : _board.getMap())
        {
            for(CounterTile t : column)
            {
                switch (t._Tile_color)
                {
                    case BLUE:
                        tryHintsOnBlue(t);
                        break;
                    case GREY:
                        tryHintsOnGrey(t);
                        break;
                }
            }
        }
        if(!isSolved())
            solveMap();
        else
            reCountEmpty();
    }


    public void renderPrueba()
    {
        for(int y = 0; y < 4; y++)
        {
            System.out.println("+---+---+---+---+");
            for(int x = 0; x < 4; x++)
            {
                switch (_board.getMap()[x][y]._Tile_color){
                    case RED:
                        System.out.print("| X ");
                        break;
                    case BLUE:
                        System.out.print("| " + _board.getMap()[x][y]._count + " ");
                        break;
                    default:
                        System.out.print("|   ");
                }
            }
            System.out.println("|");
        }
        System.out.println("+---+---+---+---+");
    }

    /**
     * Adds the "views"/counter info to blue tiles that currently don't have it
     */
    public void reCountEmpty()
    {
        for(CounterTile[] column : _board.getMap())
        {
            for(CounterTile t : column) {
                if(t._Tile_color == TileColor.BLUE && t._count == 0) {
                    for (Coord dir: Coord.DIRECTIONS)
                    {
                        t._count += _watcher.gimli(t._pos, dir, TileColor.BLUE);
                    }
                }
            }
        }
        _mapSolved = true;
    }

    /**
     * Checks for puzzle completion by looking for grey tiles. If no grey tiles remain, it's a win
     * @return (boolean) true if no grey tiles, false if one or more grey tiles remaining
     */
    public boolean isSolved()
    {
        for(CounterTile[] column : _board.getMap())
        {
            for(CounterTile t : column) {
                if(t._Tile_color == TileColor.GREY) return false;
            }
        }
        return true;
    }

    void tryHintsOnGrey(CounterTile t)
    {
        // HINT 5   --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --
        if(checkIfRed(t)) {
            t._Tile_color = TileColor.RED;
            renderPrueba();
        }
    }

    /**
     * Runs hints that involve a blue tile
     * @param t (CounterTile) blue tile we want to check
     */
    void tryHintsOnBlue(CounterTile t)
    {
        // HINT 1   --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --
        if(checkVisibleFulfilled(t))
        {
            closeWithRed(t);
            renderPrueba();
        }
        else {
            // HINT 2   --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --
            for (Coord dir: Coord.DIRECTIONS)
            {
                if (checkNoMoreBlue(t, dir)) {
                    _board.getMap()[t._pos._x + dir._x][t._pos._y + dir._y]._Tile_color = TileColor.RED;
                    renderPrueba();
                }
            }

            // HINT 3 (BUGGY)   --  --  --  --  --  --  --  --  --  --  --  --  --  --
            for (Coord dir: Coord.DIRECTIONS)
            {
                if (checkForcedBlue(t, dir)) {
                    _board.getMap()[t._pos._x + dir._x][t._pos._y + dir._y]._Tile_color = TileColor.BLUE;
                    renderPrueba();
                }
            }
        }
    }

    /**
     * Given a tile, it checks around it and adds red tiles on the surrounding empty spaces.
     * @param t (CounterTile) tile we want to close. it has passed a checkVisibleFulfilled test
     */
    void closeWithRed(CounterTile t)
    {
        if (!_board.offLimits(t._pos))
        {
            int blueCount = 0;
            for (Coord dir: Coord.DIRECTIONS)
            {
                Coord auxPos = new Coord(t._pos._x, t._pos._y);
                blueCount = _watcher.gimli(t._pos, dir, TileColor.BLUE);
                auxPos._x += (dir._x * blueCount) + dir._x;
                auxPos._y += (dir._y * blueCount) + dir._y;
                if(!_board.offLimits(auxPos) && _board.getMap()[auxPos._x][auxPos._y]._Tile_color == TileColor.GREY)
                {
                    _board.getMap()[auxPos._x][auxPos._y]._Tile_color = TileColor.RED;
                }
            }
        }
    }

    /**
     * Counts all visible blue tiles surrounding the given tile
     * @param t (CounterTile) blue tile we want to count for
     * @return (int) number of visible blue tiles
     */
    int countVisibleBlue(CounterTile t)
    {
        int counted = 0;
        for (Coord dir: Coord.DIRECTIONS)
        {
            counted += _watcher.gimli(t._pos, dir, TileColor.BLUE);
        }

        return counted;
    }

    /*boolean validMapPosition(Coord point)
    {
        return  (point._y < _board.getMapSize()) &&
                (point._y >= 0) &&
                (point._x < _board.getMapSize()) &&
                (point._x >= 0);
    }*/

    // ---------------------------------------------------------------------------------------------
    //                                H I N T    M E T H O D S
    // ---------------------------------------------------------------------------------------------

    /**
     * Checks if a blue tile already sees all the necessary blue tiles. Then it can be closed off
     * with red tiles
     * @param c (CounterTile) blue tile to be checked
     * @return (boolean) true if the seen tiles are the same as the blue tiles it neeeds to see
     */
    public boolean checkVisibleFulfilled(CounterTile c) {
        int counted = countVisibleBlue(c);

        return c._count == counted;
    }

    // si ponemos otro punto, se pasa del numero?

    /**
     * Checks if a tile doesn't allow more blue visible tiles in a given direction
     * @param c
     * @param dir
     * @return
     */
    public boolean checkNoMoreBlue(CounterTile c, Coord dir) {
        int counted = 0;
        Coord _newPos = Coord.add(c._pos, dir);

        if (_board.offLimits(_newPos)) return false;

        TileColor last_c = _board.getMap()[_newPos._x][_newPos._y]._Tile_color;
        if(last_c == TileColor.BLUE) return false;
        _board.getMap()[_newPos._x][_newPos._y]._Tile_color = TileColor.BLUE;

        counted = countVisibleBlue(c);

        _board.getMap()[_newPos._x][_newPos._y]._Tile_color = last_c;

        return  c._count < counted;
    }

    //Si no ponemos un punto azul en alguna celda vacía, entonces es imposible alcanzar el número
    public boolean checkForcedBlue(CounterTile c, Coord dir) {
        /*
        if(dir._x == 0)
        {
            if (dir._y == -1)   return c._count > c._pos._y;
            else                return c._count > (_rorschach.getMapSize() - c._pos._y);
        }
        else
        {
            if (dir._x == -1)   return c._count > c._pos._x;
            else                return c._count > (_rorschach.getMapSize() - c._pos._x);
        }*/
        int _free = 0;
        Coord _newPos = Coord.add(c._pos, dir);
        if (_board.offLimits(_newPos)) return false;

        int leg = 0;
        if(!Coord.compare(dir, new Coord(0, 1))){
            leg = _watcher.legolas(c._pos, new Coord(0, 1), TileColor.RED);
            _free += (leg == -1) ? _board.getMapSize() - Coord.add(c._pos, new Coord(0, 1))._y : leg;

        }
        if(!Coord.compare(dir, new Coord(1, 0))){
            leg = _watcher.legolas(c._pos, new Coord(1, 0), TileColor.RED);
            _free += (leg == -1) ? _board.getMapSize() - Coord.add(c._pos, new Coord(1, 0))._x : leg;
        }
        if(!Coord.compare(dir, new Coord(0, -1))) {
            leg = _watcher.legolas(c._pos, new Coord(0, -1), TileColor.RED);
            _free += (leg == -1) ? c._pos._y : leg;
        }
        if(!Coord.compare(dir, new Coord(-1, 0))){
            leg = _watcher.legolas(c._pos, new Coord(-1, 0), TileColor.RED);
            _free += (leg == -1) ? c._pos._x : leg;
        }

        return _free < c._count;
    }

    //Un número tiene más casillas azules visibles de las que debería.
    public boolean checkTooMuchBlue(CounterTile c) {
        int counted = countVisibleBlue(c);

        return c._count >= counted;
    }

    // Un número tiene una cantidad insuficiente de casillas azules visibles y sin embargo
    //ya está cerrada (no puede ampliarse más por culpa de paredes)
    public boolean checkTooMuchRed(CounterTile c) {
        int free = 0;

        for (Coord dir: Coord.DIRECTIONS)
        {
            free += _watcher.legolas(c._pos, dir, TileColor.RED);
        }

        return c._count < free;
    }

    // Si una celda está vacía y cerrada y no ve ninguna celda azul, entonces es pared (todos
    //los puntos azules deben ver al menos a otro)
    public boolean checkIfRed(CounterTile c) {
        int free = 0, lego = 0;

        /*for (Coord dir: Coord.DIRECTIONS)
        {
            lego = _rorschach.legolas(c._pos, dir, Color.RED);
            free += (lego == -1)? _rorschach.getMapSize() - Coord.add(c._pos, dir) : lego;
        }*/
        lego = _watcher.legolas(c._pos, new Coord(0, 1), TileColor.RED);
        free += (lego == -1) ? _board.getMapSize() - Coord.add(c._pos, new Coord(0, 1))._y : lego;
        lego = _watcher.legolas(c._pos, new Coord(1, 0), TileColor.RED);
        free += (lego == -1) ? _board.getMapSize() - Coord.add(c._pos, new Coord(1, 0))._x : lego;
        lego = _watcher.legolas(c._pos, new Coord(0, -1), TileColor.RED);
        free += (lego == -1) ? Coord.add(c._pos, new Coord(0, -1))._y : lego;
        lego = _watcher.legolas(c._pos, new Coord(-1, 0), TileColor.RED);
        free += (lego == -1) ? Coord.add(c._pos, new Coord(-1, 0))._x : lego;

        return free <= 0;
    }

    // En sentido opuesto, si hay una celda punto puesta por el usuario que está cerrada
    //y no ve a ninguna otra, entonces se trata de un error por el mismo motivo
    public boolean checkWrongBlue() {
        return false;
    }
}
