using System;
using System.Collections;

namespace PruebaLogica
{

    public enum color { gris, azul, rojo };
    class coordenada
    {
        public coordenada(int x, int y)
        {
            _x = x;
            _y = y;
        }
        public int _x, _y;
        public static coordenada operator +(coordenada a, coordenada b) => new coordenada(a._x + b._x, a._y + b._y);
    }
    class celda
    {
        public color _c;
        public bool _close;
        public coordenada _pos;
    }

    class celda_Numerica : celda
    {
        public int _vista;
    }

    class Program
    {
        const int X = 4, Y = 4;
        public static celda[,] _mapa = new celda[4, 4];
        //Recusivamente mirar ahsa ared o hueco si hay algo

        //método util, devuevlve color del ultimo en ver (4 = pared) y la cantidad de lo que ve
        static bool legolas(celda_Numerica c, coordenada dir,ref int vistos)
        {
            coordenada _new_pos = c._pos + dir;

            if (_new_pos._x < 0 ||
                _new_pos._x > _mapa.GetLength(0) ||
                _new_pos._y < 0 ||
                _new_pos._y > _mapa.GetLength(1)) return false;

            if (_mapa[_new_pos._x, _new_pos._y]._c == 0) return true;
            else
            {
                vistos++;
                legolas(c, dir, ref vistos);
            }

            return false;
        }

        //Calcula si en alguna dirección ve otras celdas, si la cantidad de celdas que está viendo
        //es igual o mayor a la que debe, podemos decir que se puede cerrar
        static bool sePuedeCerrar(celda_Numerica c, coordenada dir)
        {
            int _vistos = 0;

            legolas(c, new coordenada(0, 1),  ref _vistos);
            legolas(c, new coordenada(1, 0),  ref _vistos);
            legolas(c, new coordenada(0, -1), ref _vistos);
            legolas(c, new coordenada(-1, 0), ref _vistos);

            return _vistos >= c._vista;
        }

        //Podemos poner un azul en una celda vacia sin cargarnos el rango de visión??
        static Stack cortoDeMiras(celda_Numerica c)
        {
            int _vistos = 0;
            Stack _posiciones = new Stack();

            legolas(c, new coordenada(0, 1), ref _vistos);
            legolas(c, new coordenada(1, 0), ref _vistos);
            legolas(c, new coordenada(0, -1), ref _vistos);
            legolas(c, new coordenada(-1, 0), ref _vistos);

            return c._vista == 1;
        }



        static void Main(string[] args)
        {
            Console.WriteLine("Hello World!");
        }
    }
}
