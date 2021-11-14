package es.ucm.vm.engine;

/**
 * Interface for the Image methods of both platforms and the constants for some image paths
 */
public interface Image {
    String IMAGE_CLOSE = "sprites/close.png";
    String IMAGE_EYE = "sprites/eye.png";
    String IMAGE_HISTORY = "sprites/history.png";
    String IMAGE_LOCK = "sprites/lock.png";
    String IMAGE_Q42 = "sprites/q42.png";

    public void initImage(String filename);
    public void render(Graphics g);
    public void setPosition(int x, int y);
    public void setSize(int x, int y);
}
