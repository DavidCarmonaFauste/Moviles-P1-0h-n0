package es.ucm.vm.engine;

import java.io.InputStream;

/**
 * Interface for the Engine methods of both platforms
 */
public interface Engine {
    Graphics getGraphics();

    Input getInput();

    InputStream openInputStream(String filename);

    void setLogic(Logic l);

    void resetLogic();

    void HandleException(Exception e);

    void closeGame();

    int getWinWidth();

    int getWinHeight();
}