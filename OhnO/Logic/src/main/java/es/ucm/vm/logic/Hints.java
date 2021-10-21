package es.ucm.vm.logic;

public class Hints {
    //Si un número tiene ya visibles el número de celdas que dice, entonces se puede
    // cerrar,
    public boolean checkVisibleFulfilled() {
        return false;
    }

    // si ponemos otro punto, se pasa del numero?
    public boolean checkNoMoreBlue() {
        return false;
    }

    //Si no ponemos un punto azul en alguna celda vacía, entonces es imposible alcanzar el
    //número
    public boolean checkForcedBlue() {
        return false;
    }

    //Un número tiene más casillas azules visibles de las que debería.
    public boolean checkTooMuchBlue() {
        return false;
    }

    // Un número tiene una cantidad insuficiente de casillas azules visibles y sin embargo
    //ya está cerrada (no puede ampliarse más por culpa de paredes)
    public boolean checkTooMuchRed() {
        return false;
    }

    // Si una celda está vacía y cerrada y no ve ninguna celda azul, entonces es pared (todos
    //los puntos azules deben ver al menos a otro)
    public boolean checkIfRed() {
        return false;
    }

    // En sentido opuesto, si hay una celda punto puesta por el usuario que está cerrada
    //y no ve a ninguna otra, entonces se trata de un error por el mismo motivo
    public boolean checkWrongBlue() {
        return false;
    }
}
