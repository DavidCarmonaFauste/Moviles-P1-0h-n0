package es.ucm.vm.androidengine;

import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

import es.ucm.vm.engine.AbstractInput;

/**
 * Android implementation of the Input class. Manages screen touch events and stores them as multiplatform
 * events
 */
public class Input extends AbstractInput implements es.ucm.vm.engine.Input, View.OnTouchListener {
    //---------------------------------------------------------------
    //---------------------Private Attributes------------------------
    //---------------------------------------------------------------
    Graphics _g;

    /**
     * Constructor. Saves an instance of Graphics to reposition the Event coordinates to the logic
     * canvas so that Logic can process them correctly. Initializes the event list, in which all the
     * events will be stored and returned when asked.
     *
     * @param g Graphics instance.
     */
    Input(Graphics g){
        // Init list
        _touchEvn = new ArrayList<TouchEvent>();
        // Save Graphics
        _g = g;
    } // Input

    /**
     * Called when the screen is touched. Gets the android event and processes it to create our own
     * TouchEvent and add it to the TouchEvent list. Repositions coordinates of the event and gives
     * it a TouchType value.
     *
     * @param view (View) View instance
     * @param motionEvent (MotionEvent) ME received from the view
     * @return (boolean) True if touched, false if not (Android internal management)
     */
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        // TouchEvent to be created from the MotionEvent
        TouchEvent _te;

        // Check if screen has been touched
        boolean touched = false;

        // Positions for the TouchEvent
        int x, y;

        switch(motionEvent.getAction()){ // Process MotionEvent
            case MotionEvent.ACTION_DOWN:
                // Screen touched
                // Check if it is in canvas
                if(_g.isInCanvas((int)motionEvent.getX(), (int)motionEvent.getY())){
                    // If it is in canvas, reposition coordinates to place them in canvas.
                    x = _g.reverseRepositionX((int)motionEvent.getX() - _g.getCanvas().getX());
                    y = _g.reverseRepositionY((int)motionEvent.getY() - _g.getCanvas().getY());
                }
                else{
                    // If not, don't reposition, is not very important because it won't be processed
                    x = (int)motionEvent.getX();
                    y = (int)motionEvent.getY();
                }
                // Create a new TouchEvent with all the parameters needed
                _te = new TouchEvent(x, y, TouchEvent.TouchType.CLICKED);

                // Add event to the list
                addEvent(_te);

                touched = true;
                break;

            case MotionEvent.ACTION_UP:
                // Finger stopped touching the screen
                // Check if it is in canvas
                if(_g.isInCanvas((int)motionEvent.getX(), (int)motionEvent.getY())){
                    // If it is in canvas, reposition coordinates to place them in canvas.
                    x = _g.reverseRepositionX((int)motionEvent.getX() - _g.getCanvas().getX());
                    y = _g.reverseRepositionY((int)motionEvent.getY() - _g.getCanvas().getY());
                }
                else{
                    // If not, don't reposition, is not very important because it won't be processed
                    x = (int)motionEvent.getX();
                    y = (int)motionEvent.getY();
                }
                // Create a new TouchEvent with all the parameters needed
                _te = new TouchEvent(x, y, TouchEvent.TouchType.RELEASED);

                // Add that event to the list
                addEvent(_te);

                touched = true;
                break;
        } // switch

        return touched;
    } // onTouch
}
