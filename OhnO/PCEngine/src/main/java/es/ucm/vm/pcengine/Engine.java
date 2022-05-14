package es.ucm.vm.pcengine;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import es.ucm.vm.engine.AbstractEngine;
import es.ucm.vm.engine.Color;

/**
 * PC Engine. Manages all neccessary information related to the time and
 * FPS. It also provides a way to open files and retrieve information.
 */
public class Engine extends AbstractEngine implements Runnable, ComponentListener, WindowStateListener {
    //---------------------------------------------------------------
    //----------------------Private Atributes------------------------
    //---------------------------------------------------------------
    // Initializing
    Window _win;

    // Time and Calculations
    int _width;
    int _height;
    int FPS;

    // Used for the render method
    Color _color;

    /**
     * Class constructor. Creates a new Engine instance and sets everything for game to run
     * correctly. Sets all window values needed, creates the new window, creates a new graphics
     * instance, initializes all data related to time controlling and calculations and sets
     * itself as listener of window events.
     */
    public Engine(){
        //Window generation
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();

        // Window values
        _width = gd.getDisplayMode().getWidth();
        _height = gd.getDisplayMode().getHeight();
        String name = "Oh-nO!";

        // Create the Window
        _win = new Window(_width, _height, name, this);

        // Create Graphics instance
        _g = new Graphics(_win);
        _color = new Color();

        // Create input
        _ip = new Input(_win, (Graphics)_g);

        // Initialize some time values
        _lastFrameTime = System.nanoTime(); // System time in ms
        _info = _lastFrameTime; // Information about the fps (debug)
        _frames = 0; // Number of frames passed
        _running = true;

        _win.addComponentListener(this);
        _win.addWindowStateListener(this);
    } // Engine

    /**
     * Resize canvas to fit screen after window is resized.
     *
     * @param componentEvent (ComponentEvent) ComponentEvent
     */
    @Override
    public void componentResized(ComponentEvent componentEvent) {
        windowResized();
        resize();
    } // componentResized

    /**
     * Function that manages the window resizing and updates
     * everything for it to make sense and scale things right.
     */
    public void windowResized(){
        _width = _win.getWidth();
        _height = _win.getHeight();
    } // windowResized


    /**
     * Function that manages when the window state has changed.
     *
     * @param windowEvent (WindowEvent) Window change event.
     */
    @Override
    public void windowStateChanged(WindowEvent windowEvent) {
        windowResized();
    } // windowStateChanged


    /**
     * Input stream function to receive the levels.
     *
     * @param filename (String) file path
     * @return (InputStream) File content.
     */
    @Override
    public InputStream openInputStream(String filename) {
        InputStream data = null;
        try {
            data = new FileInputStream("./Data/" + filename);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return data;
    } // openInputStream

    /**
     * Sets the max frame rate to keep all running at the same velocity
     *
     * @param fps (int) Max frame rate.
     */
    public void setFPS(int fps) {
        FPS = fps;
    } // setFPS

    /**
     * Function to get the width of the screen or the window.
     *
     * @return (int) Window/Screen width
     */
    @Override
    public int getWinWidth() {
        return _width;
    } // getWidth

    /**
     * Function to get the height of the screen or the window
     *
     * @return (int) Window/Screen height
     */
    @Override
    public int getWinHeight() {
        return _height;
    } // getHeight

    /**
     * Render function.
     * Clears the buffer to paint again.
     * Calls the render from the logic to paint the updated version of the screen.
     * If it fails, it will still paint the screen.  It has 2 loops because it will try to paint
     * the buffer and then show the buffer. Ideally, this won't fail and it will do only 1 iteration
     * per loop.
     */
    void render(){
        _color.setWhite();
        _g.clear(_color); // Clear the whole buffer

        // Ideally this will only do 1 iteration per loop
        do {
            do {
                _win.setGraphics();
                try { // Try to paint in the Graphics
                    _gs.render(_g);
                }
                finally { // If not, still dispose the Swing Graphics
                    _win.getJGraphics().dispose();
                }
            } while(_win.getBufferStrategy().contentsRestored());
            _win.getBufferStrategy().show(); // Show the buffer with the updated information (painted)
        } while(_win.getBufferStrategy().contentsLost());
    }

    /**
     * run function, called by the main thread for running the main loop of the game. Controls the
     * frame rate and calls for update and render functions, checks if new Input happened.
     */
    @Override
    public void run() {
        // FPS
        long startTime = System.nanoTime(); // Starting loop time
        long totalTime = 0; // Total time

        long targetTime = 1000000000L / FPS; // Time to run at FPS

        //Main Loop
        while(_running){
            if(_gs != null) {
                // Frame cap
                totalTime = System.nanoTime() - startTime;

                if (totalTime < targetTime) {
                    continue;
                } // if
                else {
                    // Calculate time passed between frames and convert it to seconds
                    _currentTime = System.nanoTime();
                    _nanoElapsedTime = _currentTime - _lastFrameTime;
                    _lastFrameTime = _currentTime;
                    _elapsedTime = (double) _nanoElapsedTime / 1.0E9;

                    update();

                    // Inform about the fps
                    if (_currentTime - _info > 1000000000L) {
                        long fps = _frames * 1000000000L / (_currentTime - _info);
                        //System.out.println("Info: " + fps + " fps");
                        _frames = 0;
                        _info = _currentTime;
                    } // if
                    ++_frames; // Update frames

                    // Clear and update graphics
                    render();

                    startTime = System.nanoTime();
                } // else
            } // if

            if (_tempGS != null) {
                setGameState();
            } // if
        } // while

        System.exit(1);
    } // run

    //------------------Implementation Methods---------------------

    /**
     * Requisite of Runnable class implementation
     *
     * @param e (Exception) Exception generated by the program.
     */
    @Override
    public void HandleException(Exception e) {
        System.out.println(e);
    } // HandleException

    @Override
    public void componentMoved(ComponentEvent componentEvent) {}

    @Override
    public void componentShown(ComponentEvent componentEvent) {}

    @Override
    public void componentHidden(ComponentEvent componentEvent) {}
}