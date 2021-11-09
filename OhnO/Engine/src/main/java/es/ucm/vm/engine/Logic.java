package es.ucm.vm.engine;

public interface Logic {
    Rect getCanvasSize();

    void initLogic();

    void update(double t);

    void render();
}
