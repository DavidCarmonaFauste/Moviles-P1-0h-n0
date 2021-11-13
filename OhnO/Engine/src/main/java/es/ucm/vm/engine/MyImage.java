package es.ucm.vm.engine;

public interface MyImage {
    public void readImage(String filename);
    public void render();
    public void setPosition(int x, int y);
    public void setSize(int x, int y);
}
