package es.ucm.vm.engine;

import java.io.InputStream;

/**
 * Interface for the Engine methods of both platforms
 */
public interface Engine {
    Graphics getGraphics();

    InputStream openInputStream(String filename);

    void saveGameState(GameState gs);

    void HandleException(Exception e);

    void closeGame();

    int getWinWidth();

    int getWinHeight();
}