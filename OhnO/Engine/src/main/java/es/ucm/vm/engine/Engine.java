package es.ucm.vm.engine;

import java.io.InputStream;

/**
 * Interface for the Engine methods of both platforms
 */
public interface Engine {
    Graphics getGraphics();

    void saveGameState(GameState gs);

    void closeGame();

    int getWinWidth();

    int getWinHeight();
}