package es.ucm.vm.logic;

public class Logic {

    public void prueba()
    {
        CounterTile[][] mapaPrueba = pruebas();
        Hints pistas = new Hints(5);
        pistas.updateMap(mapaPrueba);
        renderPrueba(mapaPrueba);
        String respuesta;
        //PISTA 01
        //respuesta = (pistas.checkVisibleFulfilled(mapaPrueba[4][0])) ? "Se puede cerrar" : "hmmm falla algo...";
        //PISTA 02
        //respuesta = (pistas.checkNoMoreBlue(mapaPrueba[2][3], new Coord(0, -1))) ? "Puedes avanzar en esa direccion" : "Por ahí ya hay demasiados azules";
        //PISTA 03
        respuesta = (pistas.checkForcedBlue(mapaPrueba[3][4], new Coord(-1, 0))) ? "Tienes que avanzar por ahí" : "Hay mucho campo por explorar";
        //PISTA 04
        //respuesta = (pistas.checkTooMuchBlue(mapaPrueba[2][3])) ? "Vas bien" : "Te has pasao amigo";
        //PISTA 05
        //respuesta = (pistas.checkTooMuchRed(mapaPrueba[2][3])) ? "Vas bien de rojo" : "Te has pasao amigo";
        //PISTA 06 - 07
        //respuesta = (pistas.checkIfRed(mapaPrueba[4][2])) ? "Eso es una pared" : "Puede ser azul";
        System.out.print(respuesta);
    }
    public void renderPrueba(CounterTile[][] mapa)
    {
        for(int y = 0; y < 5; y++)
        {
            System.out.println("+---+---+---+---+---+");
            for(int x = 0; x < 5; x++)
            {
                if(mapa[x][y]._c == Color.BLUE)      System.out.print("| " + mapa[x][y]._count + " ");
                else if (mapa[x][y]._c == Color.RED) System.out.print("| X ");
                else                                 System.out.print("|   ");
            }
            System.out.println("|");
        }
        System.out.println("+---+---+---+---+---+");
    }
    /*
    +---+---+---+---+---+
    | 3 | 2 | x | 3 | 1 |
    +---+---+---+---+---+
    | 5 | 4 | 3 | 5 | x |
    +---+---+---+---+---+
    | 2 | x | x | 2 | x |
    +---+---+---+---+---+
    | x | 2 | 2 | x | x |
    +---+---+---+---+---+
    | 4 | 5 | 5 | 4 | 4 |
    +---+---+---+---+---+
    esta es la funcional, puede estar cambiada para probar pistas
    */
    private CounterTile[][] pruebas()
    {
        CounterTile[][] mapaPruebas = new CounterTile[5][5];
        /*----------------------------------------------------*///y -> 0
        mapaPruebas[0][0] = new CounterTile(Color.BLUE, new Coord(0,0), 3);
        mapaPruebas[1][0] = new CounterTile(Color.GREY, new Coord(1,0), 2);
        mapaPruebas[2][0] = new CounterTile(Color.GREY,  new Coord(2,0), 0);
        mapaPruebas[3][0] = new CounterTile(Color.BLUE, new Coord(3,0), 3);
        mapaPruebas[4][0] = new CounterTile(Color.BLUE, new Coord(4,0), 1);
        /*----------------------------------------------------*///y -> 1
        mapaPruebas[0][1] = new CounterTile(Color.GREY, new Coord(0,1), 5);
        mapaPruebas[1][1] = new CounterTile(Color.GREY, new Coord(1,1), 4);
        mapaPruebas[2][1] = new CounterTile(Color.BLUE, new Coord(2,1), 3);
        mapaPruebas[3][1] = new CounterTile(Color.RED, new Coord(3,1), 5);
        mapaPruebas[4][1] = new CounterTile(Color.RED,  new Coord(4,1), 0);
        /*----------------------------------------------------*///y -> 2
        mapaPruebas[0][2] = new CounterTile(Color.BLUE, new Coord(0,2), 2);
        mapaPruebas[1][2] = new CounterTile(Color.GREY,  new Coord(1,2), 0);
        mapaPruebas[2][2] = new CounterTile(Color.GREY,  new Coord(2,2), 0);
        mapaPruebas[3][2] = new CounterTile(Color.RED, new Coord(3,2), 2);
        mapaPruebas[4][2] = new CounterTile(Color.BLUE,  new Coord(4,2), 0);
        /*----------------------------------------------------*///y -> 3
        mapaPruebas[0][3] = new CounterTile(Color.RED,  new Coord(0,3), 0);
        mapaPruebas[1][3] = new CounterTile(Color.GREY, new Coord(1,3), 2);
        mapaPruebas[2][3] = new CounterTile(Color.BLUE, new Coord(2,3), 2);
        mapaPruebas[3][3] = new CounterTile(Color.RED,  new Coord(3,3), 0);
        mapaPruebas[4][3] = new CounterTile(Color.RED,  new Coord(4,3), 0);
        /*----------------------------------------------------*///y -> 4
        mapaPruebas[0][4] = new CounterTile(Color.GREY, new Coord(0,4), 4);
        mapaPruebas[1][4] = new CounterTile(Color.GREY, new Coord(1,4), 5);
        mapaPruebas[2][4] = new CounterTile(Color.BLUE, new Coord(2,4), 5);
        mapaPruebas[3][4] = new CounterTile(Color.BLUE, new Coord(3,4), 4);
        mapaPruebas[4][4] = new CounterTile(Color.GREY, new Coord(4,4), 4);
        /*----------------------------------------------------*/

        return mapaPruebas;
    }

}