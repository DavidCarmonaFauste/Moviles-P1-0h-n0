package es.ucm.vm.engine;

import java.util.List;

import es.ucm.vm.engine.Graphics;
import es.ucm.vm.engine.Input;

/**
 * GameState Interface. Represents a GameState with it's basic methods, like processing
 * Input, update and render.
 */
public interface GameState {
    /**
     * Gets a Rect with the logical canvas size
     *
     * @return (Rect) logic canvas dimensions
     */
    Rect getCanvasSize();

    /**
     * Updates all GameObjects in this State with the time passed since the las update.
     *
     * @param t (double) Time elapsed since the last frame.
     */
    void update(double t);// update

    /**
     * Renders all GameObjects in their specific locations. Receives an instance of Graphics
     * to call the drawing methods.
     *
     * @param g (Graphics) Instance of Graphics
     */
    void render(Graphics g); // render

    /**
     * Method that processes the Input received from the Logic.
     *
     * @param e (List<Input.TouchEvent>) Event list taken from the Input class
     */
    void processInput (List<Input.TouchEvent> e); // processInput
} // GameState
