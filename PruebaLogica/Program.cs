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

        static void gimli(celda_Numerica c, coordenada dir, ref int vistos, color vigilada = color.azul)
        {
            coordenada _new_pos = c._pos + dir;

            if (!(  _new_pos._x < 0 ||
                    _new_pos._x > _mapa.GetLength(0) ||
                    _new_pos._y < 0 ||
                    _new_pos._y > _mapa.GetLength(1) &&
                    _mapa[_new_pos._x, _new_pos._y]._c != vigilada))
            {
                vistos++;
                gimli(c, dir, ref vistos);
            }
        }
        static void legolas(celda_Numerica c, coordenada dir, ref int vistos, color vigilada = color.azul)
        {
            coordenada _new_pos = c._pos + dir;

            if (!(_new_pos._x < 0 ||
                    _new_pos._x > _mapa.GetLength(0) ||
                    _new_pos._y < 0 ||
                    _new_pos._y > _mapa.GetLength(1) &&
                    _mapa[_new_pos._x, _new_pos._y]._c == vigilada))
            {
                vistos++;
                gimli(c, dir, ref vistos);
            }
        }

        static bool pista01(celda_Numerica c)
        {
            int _vistos = 0;
            gimli(c, new coordenada(0, 1), ref _vistos);
            gimli(c, new coordenada(1, 0), ref _vistos);
            gimli(c, new coordenada(0, -1), ref _vistos);
            gimli(c, new coordenada(-1, 0), ref _vistos);
            return c._vista == _vistos;
        }

        static bool pista02(celda_Numerica c, coordenada dir)
        {
            int _vistos = 0;

            if (dir._x < 0 ||
                dir._x > _mapa.GetLength(0) ||
                dir._y < 0 ||
                dir._y > _mapa.GetLength(1)) return false;

            coordenada next_pos = c._pos + dir;
            color last_c = _mapa[next_pos._x, next_pos._y]._c;
            _mapa[next_pos._x, next_pos._y]._c = color.azul;

            gimli(c, dir, ref _vistos, color.gris);

            _mapa[next_pos._x, next_pos._y]._c = last_c;

            return c._vista > _vistos;
        }
        static bool pista03(celda_Numerica c, coordenada dir)
        {
            if(dir._x == 0)
            {
                if (dir._y == -1)   return c._vista > c._pos._y;
                else                return c._vista > (_mapa.GetLength(1) - c._pos._y);
            }
            else
            {
                if (dir._x == -1)   return c._vista > c._pos._x;
                else                return c._vista > (_mapa.GetLength(0) - c._pos._x);
            }

            return false;
        }

        static bool pista04(celda_Numerica c)
        {
            int _vistos = 0;
            gimli(c, new coordenada(0, 1), ref _vistos);
            gimli(c, new coordenada(1, 0), ref _vistos);
            gimli(c, new coordenada(0, -1), ref _vistos);
            gimli(c, new coordenada(-1, 0), ref _vistos);
            return c._vista < _vistos;
        }
        static bool pista05(celda_Numerica c)
        {
            int _huecos_Libres = 0;
            legolas(c, new coordenada(0, 1), ref _huecos_Libres, color.rojo);
            legolas(c, new coordenada(1, 0), ref _huecos_Libres, color.rojo);
            legolas(c, new coordenada(0, -1), ref _huecos_Libres, color.rojo);
            legolas(c, new coordenada(-1, 0), ref _huecos_Libres, color.rojo);

            return c._vista < _huecos_Libres;
        }

        //Tambien responde a la pista 7....
        static bool pista06(celda_Numerica c)
        {
            int _huecos_Libres = 0;
            legolas(c, new coordenada(0, 1), ref _huecos_Libres, color.rojo);
            legolas(c, new coordenada(1, 0), ref _huecos_Libres, color.rojo);
            legolas(c, new coordenada(0, -1), ref _huecos_Libres, color.rojo);
            legolas(c, new coordenada(-1, 0), ref _huecos_Libres, color.rojo);

            return _huecos_Libres == 0;
        }
        static void Main(string[] args)
        {
            Console.WriteLine("Hello World!");
        }
    }
}
