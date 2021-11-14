package es.ucm.vm.engine;

import java.util.List;

/**
 * Interface for the Input methods of both platforms and the subclass for the Touch Events
 */
public interface Input {
    class TouchEvent{
        public enum TouchType{
            PRESSED,
            RELEASED,
            MOVED,
            CLICKED,
            KEY_PRESSED,
            KEY_RELEASED,
            KEY_TYPED,
            KEY_EXIT,
            KEY_RESTART
        }

        // Private Atributes --------------------
        // Position X and Y
        int _x, _y;
        /**
         * TouchType of the TouchEvent.
         */
        TouchType _t;

        /**
         * Constructor of a TouchEvent. Gives it a position in the screen.
         * @param x Position X (pixels)
         * @param y Position Y (pixels)
         * @param t TouchType of the event
         */
        public TouchEvent(int x, int y, TouchType t){
            _x = x;
            _y = y;
            _t = t;
        }

        /**
         * Return value of X position.
         * @return X position
         */
        public int getX(){
            return _x;
        }

        /**
         * Return value of Y position.
         * @return Y position
         */
        public int getY(){
            return _y;
        }

        /**
         * Return the type of the event, to be handled by the Logic
         * @return TouchType
         */
        public TouchType getType(){
            return _t;
        }
    }

    List<TouchEvent> getTouchEvents();
}
