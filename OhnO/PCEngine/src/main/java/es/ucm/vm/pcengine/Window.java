package es.ucm.vm.pcengine;

import java.awt.Graphics;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;

public class Window extends JFrame {
    //---------------------------------------------------------------
    //----------------------Private Atributes------------------------
    //---------------------------------------------------------------
    BufferStrategy _str;
    Graphics _gra;
    Engine _eng;

    /**
     * Window constructor. Sets the values for JFrame, saves Game instance in the atributes and
     * initialize in the init() function with the width and height values.
     *
     * @param width (int) Width of the window
     * @param height (int) Height of the window
     * @param title (String) Title that the window will have
     * @param en (Engine) An instance of Engine
     */
    public Window(int width, int height, String title, Engine en){
        super(title);
        _eng = en;

        init(width, height);
    } // Window

    /**
     * Init function for window. Sets window's size. States
     * EXIT_ON_CLOSE as default close operation.
     * It is configured to use active painting.
     * Then creates a BufferStrategy to be used while painting. Sets that BufferStrategy to
     * the Graphics.
     *
     * @param w (int) width of the window
     * @param h (int) height of the window
     */
    private void init(int w, int h) {
        setSize(w, h); // Size of window
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Close operation
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Fullscreen
        setUndecorated(true);
        setIgnoreRepaint(true); // Active painting

        setVisible(true); // Set Window visible

        // Create bufferStrategy
        int tries = 100; // Try to create bufferStrategy 100 times
        while(tries--> 0) {
            try{ // Ideally it will make this only once
                createBufferStrategy(2);
                break;
            } // try
            catch(Exception e){
                _eng.HandleException(e);
            } // catch
        } // while

        // Debugging
        if(tries == 0){
            // if bufferStrategy is not created...
            System.err.println("BufferStrategy not created");
            // End init (maybe return an error or something?)
            return;
        } // if

        // Save buffer strategy
        synchronized (this) {
            _str = getBufferStrategy();
            // Set Graphics with BuffStr
            setGraphics();
        } // sync
    } // init

    /**
     * Sets java graphics using BufferStrategy
     */
    public synchronized void setGraphics() {
        // Set graphics with previously initialized bufferStrategy
        synchronized (this){
            _gra = _str.getDrawGraphics();
        } // sync
    } // setGraphics

    /**
     * Get java graphics and return it. Used to draw images and etc.
     *
     * @return (java.awt.Graphics) Return Swing library Graphics.
     */
    public Graphics getJGraphics(){
        return _gra;
    } // getJGraphics
}
