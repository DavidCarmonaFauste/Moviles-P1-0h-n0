package es.ucm.vm.engine;

import java.io.InputStream;

/**
 * Interface for the Engine methods of both platforms
 */
public interface Engine {
    Graphics getGraphics();

    void saveGameState(GameState gs);

    void closeGame();

    /**
     * To be implemented inside the platform specific classes. It returns
     * the width of the screen canvas
     *
     * @return (int) size of the screen window width
     */
    int getWinWidth();

    /**
     * To be implemented inside the platform specific classes. It returns
     * the height of the screen canvas
     *
     * @return (int) size of the screen window height
     */
    int getWinHeight();
}