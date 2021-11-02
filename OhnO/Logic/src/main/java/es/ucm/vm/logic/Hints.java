package es.ucm.vm.logic;

public class Hints {
    public Watcher _rorschach = new Watcher();

    public Hints(int mapSize)
    {
        _rorschach.setMap(new CounterTile[mapSize][mapSize]);
        _rorschach.setMapSize(mapSize);
    }

    public void updateMap(CounterTile[][] map)
    {
        _rorschach.setMap(map);
    }
    public void solveMap()
    {
        for(CounterTile[] column : _rorschach.getMap())
        {
            for(CounterTile t : column)
            {
                //Para azules -> 1,2,3
                int util = 0;
                if(t._c == Color.BLUE && t._count > 0)
                {
                    //Pista 1

                    if(checkVisibleFulfilled(t))
                    {
                        if(t._pos._y < _rorschach.getMapSize() - 1){
                            util = _rorschach.gimli(t._pos, new Coord(0, 1), Color.BLUE);
                            if(t._pos._y + util + 1 < _rorschach.getMapSize()) {
                                if (_rorschach.getMap()[t._pos._x][t._pos._y + util + 1]._c == Color.GREY)
                                    _rorschach.getMap()[t._pos._x][t._pos._y + util + 1]._c = Color.RED;
                            }
                            //if(_rorschach.getMap()[t._pos._x][t._pos._y + 1]._c == Color.GREY) _rorschach.getMap()[t._pos._x][t._pos._y + 1]._c = Color.RED;
                        }
                        if(t._pos._y > 0){
                            util = _rorschach.gimli(t._pos, new Coord(0, -1), Color.BLUE);
                            if(t._pos._y - util - 1 >= 0) {
                                if (_rorschach.getMap()[t._pos._x][t._pos._y - util - 1]._c == Color.GREY)
                                    _rorschach.getMap()[t._pos._x][t._pos._y - util - 1]._c = Color.RED;
                            }
                            /*util = _rorschach.legolas(t._pos, new Coord(0, -1), Color.GREY);
                            if(util != -1) _rorschach.getMap()[t._pos._x][t._pos._y - util]._c = Color.RED;
                            */
                            //if(_rorschach.getMap()[t._pos._x][t._pos._y - 1]._c == Color.GREY) _rorschach.getMap()[t._pos._x][t._pos._y - 1]._c = Color.RED;
                        }
                        if(t._pos._x < _rorschach.getMapSize() - 1){
                            util = _rorschach.gimli(t._pos, new Coord(1, 0), Color.BLUE);
                            if(t._pos._x + util + 1 < _rorschach.getMapSize()) {
                                if (_rorschach.getMap()[t._pos._x + util + 1][t._pos._y]._c == Color.GREY)
                                    _rorschach.getMap()[t._pos._x + util + 1][t._pos._y]._c = Color.RED;
                            }
                            /*
                            util = _rorschach.legolas(t._pos, new Coord(1, 0), Color.GREY);
                            if(util != -1) _rorschach.getMap()[t._pos._x + util][t._pos._y]._c = Color.RED;
                            */
                            //if(_rorschach.getMap()[t._pos._x + 1][t._pos._y]._c == Color.GREY) _rorschach.getMap()[t._pos._x + 1][t._pos._y ]._c = Color.RED;
                        }
                        if(t._pos._x > 0){
                            util = _rorschach.gimli(t._pos, new Coord(-1, 0), Color.BLUE);
                            if(t._pos._x - util - 1 >= 0) {
                                if (_rorschach.getMap()[t._pos._x - util - 1][t._pos._y]._c == Color.GREY)
                                    _rorschach.getMap()[t._pos._x - util - 1][t._pos._y]._c = Color.RED;
                            }
                            /*
                            util = _rorschach.legolas(t._pos, new Coord(-1, 0), Color.GREY);
                            if(util != -1) _rorschach.getMap()[t._pos._x - util][t._pos._y]._c = Color.RED;
                            */
                            //if(_rorschach.getMap()[t._pos._x - 1][t._pos._y]._c == Color.GREY) _rorschach.getMap()[t._pos._x + 1][t._pos._y ]._c = Color.RED;
                        }
                    }
                    else {
                        //Pista 2
                        if (checkNoMoreBlue(t, new Coord(0, 1))) {
                            _rorschach.getMap()[t._pos._x][t._pos._y + 1]._c = Color.RED;
                        }
                        if (checkNoMoreBlue(t, new Coord(0, -1))) {
                            _rorschach.getMap()[t._pos._x][t._pos._y - 1]._c = Color.RED;
                        }
                        if (checkNoMoreBlue(t, new Coord(1, 0))) {
                            _rorschach.getMap()[t._pos._x + 1][t._pos._y]._c = Color.RED;
                        }
                        if (checkNoMoreBlue(t, new Coord(-1, 0))) {
                            _rorschach.getMap()[t._pos._x - 1][t._pos._y]._c = Color.RED;
                        }

                        //Pista 3 BUG
                        if (checkForcedBlue(t, new Coord(0, 1))) {
                            _rorschach.getMap()[t._pos._x][t._pos._y + 1]._c = Color.BLUE;
                        }
                        if (checkForcedBlue(t, new Coord(0, -1))) {
                            _rorschach.getMap()[t._pos._x][t._pos._y - 1]._c = Color.BLUE;
                        }
                        if (checkForcedBlue(t, new Coord(1, 0))) {
                            _rorschach.getMap()[t._pos._x + 1][t._pos._y]._c = Color.BLUE;
                        }
                        if (checkForcedBlue(t, new Coord(-1, 0))) {
                            _rorschach.getMap()[t._pos._x - 1][t._pos._y]._c = Color.BLUE;
                        }
                    }
                }
                //Para grises -> 5
                else if(t._c == Color.GREY)
                {
                    //Pista 5
                    if(checkIfRed(t)) {
                        t._c = Color.RED;
                    }
                }
                //System.out.println("X: " + t._pos._x + "   Y: " +  t._pos._y);
                //renderPrueba();
            }
        }
        if(!isSolved()) solveMap();
    }
    void renderPrueba()
    {
        for(int y = 0; y < 4; y++)
        {
            for(int x = 0; x < 4; x++)
            {
                if(_rorschach.getMap()[x][y]._c == Color.BLUE)      System.out.print(_rorschach.getMap()[x][y]._count);
                else if (_rorschach.getMap()[x][y]._c == Color.RED) System.out.print("X");
                else                                 System.out.print("#");
            }
            System.out.println("");
        }
        System.out.println("+---+---+---+---+");
    }
    public void reCountEmpty()
    {
        for(CounterTile[] column : _rorschach.getMap())
        {
            for(CounterTile t : column) {
                if(t._c == Color.BLUE && t._count == 0) {
                    t._count += _rorschach.gimli(t._pos, new Coord(0, 1), Color.BLUE);
                    t._count += _rorschach.gimli(t._pos, new Coord(1, 0), Color.BLUE);
                    t._count += _rorschach.gimli(t._pos, new Coord(0, -1), Color.BLUE);
                    t._count += _rorschach.gimli(t._pos, new Coord(-1, 0), Color.BLUE);
                }
            }
        }
    }
    public boolean isSolved()
    {
        for(CounterTile[] column : _rorschach.getMap())
        {
            for(CounterTile t : column) {
                if(t._c == Color.GREY) return false;
            }
        }
        return true;
    }
    //Si un número tiene ya visibles el número de celdas que dice, entonces se puede cerrar
    public boolean checkVisibleFulfilled(CounterTile c) {
        int _counted = 0;

        _counted += _rorschach.gimli(c._pos, new Coord(0, 1), Color.BLUE);
        _counted += _rorschach.gimli(c._pos, new Coord(1, 0), Color.BLUE);
        _counted += _rorschach.gimli(c._pos, new Coord(0, -1), Color.BLUE);
        _counted += _rorschach.gimli(c._pos, new Coord(-1, 0), Color.BLUE);
        return c._count == _counted;
    }

    // si ponemos otro punto, se pasa del numero?
    public boolean checkNoMoreBlue(CounterTile c, Coord dir) {
        int _counted = 0;
        Coord _newPos = Coord.add(c._pos, dir);

        if (_newPos._x < 0 ||
            _newPos._x >= _rorschach.getMapSize() ||
            _newPos._y < 0 ||
            _newPos._y >= _rorschach.getMapSize()) return false;

        Color last_c = _rorschach.getMap()[_newPos._x][_newPos._y]._c;
        if(last_c == Color.BLUE) return false;
        _rorschach.getMap()[_newPos._x][_newPos._y]._c = Color.BLUE;

        //_counted += _rorschach.gimli(c._pos, dir, Color.BLUE);


        _counted += _rorschach.gimli(c._pos, new Coord(0, 1), Color.BLUE);
        _counted += _rorschach.gimli(c._pos, new Coord(1, 0), Color.BLUE);
        _counted += _rorschach.gimli(c._pos, new Coord(0, -1), Color.BLUE);
        _counted += _rorschach.gimli(c._pos, new Coord(-1, 0), Color.BLUE);

        _rorschach.getMap()[_newPos._x][_newPos._y]._c = last_c;

        return  c._count < _counted;
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
        if (_newPos._x < 0 ||
                _newPos._x >= _rorschach.getMapSize() ||
                _newPos._y < 0 ||
                _newPos._y >= _rorschach.getMapSize()) return false;
        int leg = 0;
        if(!Coord.compare(dir, new Coord(0, 1))){
            leg = _rorschach.legolas(c._pos, new Coord(0, 1), Color.RED);
            _free += (leg == -1) ? _rorschach.getMapSize() - Coord.add(c._pos, new Coord(0, 1))._y : leg;

        }
        if(!Coord.compare(dir, new Coord(1, 0))){
            leg = _rorschach.legolas(c._pos, new Coord(1, 0), Color.RED);
            _free += (leg == -1) ? _rorschach.getMapSize() - Coord.add(c._pos, new Coord(1, 0))._x : leg;
        }
        if(!Coord.compare(dir, new Coord(0, -1))) {
            leg = _rorschach.legolas(c._pos, new Coord(0, -1), Color.RED);
            _free += (leg == -1) ? c._pos._y : leg;
        }
        if(!Coord.compare(dir, new Coord(-1, 0))){
            leg = _rorschach.legolas(c._pos, new Coord(-1, 0), Color.RED);
            _free += (leg == -1) ? c._pos._x : leg;
        }

        return _free < c._count;
    }

    //Un número tiene más casillas azules visibles de las que debería.
    public boolean checkTooMuchBlue(CounterTile c) {
        int _counted = 0;
        _counted += _rorschach.gimli(c._pos, new Coord(0, 1), Color.BLUE);
        _counted += _rorschach.gimli(c._pos, new Coord(1, 0), Color.BLUE);
        _counted += _rorschach.gimli(c._pos, new Coord(0, -1), Color.BLUE);
        _counted += _rorschach.gimli(c._pos, new Coord(-1, 0), Color.BLUE);
        return c._count >= _counted;
    }

    // Un número tiene una cantidad insuficiente de casillas azules visibles y sin embargo
    //ya está cerrada (no puede ampliarse más por culpa de paredes)
    public boolean checkTooMuchRed(CounterTile c) {
        int _free = 0;

        _free += _rorschach.legolas(c._pos, new Coord(0, 1), Color.RED);
        _free += _rorschach.legolas(c._pos, new Coord(1, 0), Color.RED);
        _free += _rorschach.legolas(c._pos, new Coord(0, -1), Color.RED);
        _free += _rorschach.legolas(c._pos, new Coord(-1, 0), Color.RED);

        return c._count < _free;
    }

    // Si una celda está vacía y cerrada y no ve ninguna celda azul, entonces es pared (todos
    //los puntos azules deben ver al menos a otro)
    public boolean checkIfRed(CounterTile c) {
        int _free = 0, lego = 0;

        lego = _rorschach.legolas(c._pos, new Coord(0, 1), Color.RED);
        _free += (lego == -1) ? _rorschach.getMapSize() - Coord.add(c._pos, new Coord(0, 1))._y : lego;
        lego = _rorschach.legolas(c._pos, new Coord(1, 0), Color.RED);
        _free += (lego == -1) ? _rorschach.getMapSize() - Coord.add(c._pos, new Coord(1, 0))._x : lego;
        lego = _rorschach.legolas(c._pos, new Coord(0, -1), Color.RED);
        _free += (lego == -1) ? Coord.add(c._pos, new Coord(0, -1))._y : lego;
        lego = _rorschach.legolas(c._pos, new Coord(-1, 0), Color.RED);
        _free += (lego == -1) ? Coord.add(c._pos, new Coord(-1, 0))._x : lego;

        return _free <= 0;
    }

    // En sentido opuesto, si hay una celda punto puesta por el usuario que está cerrada
    //y no ve a ninguna otra, entonces se trata de un error por el mismo motivo
    public boolean checkWrongBlue() {
        return false;
    }
}
