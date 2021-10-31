package es.ucm.vm.engine;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractInput implements Input {
    //---------------------------------------------------------------
    //--------------------Protected Atributes------------------------
    //---------------------------------------------------------------
    // Event list (Or even Queue)
    protected List<TouchEvent> _touchEvn;

    /**
     * Returns all the events stored in the event list, then cleans the internal list for future
     * use.
     *
     * @return (List<TouchEvent>) List of the events.
     */
    @Override
    public List<TouchEvent> getTouchEvents() {
        // Create a temporal list to return events
        List<TouchEvent> tmp;

        synchronized (this){
            // Copy internal list
            tmp = new ArrayList<TouchEvent>(_touchEvn);
            // Clear internal list
            _touchEvn.clear();
        } // synchronized

        // Return event list
        return tmp;
    } // getTouchEvent

    /**
     * Adds a new event to the event list.
     *
     * @param event (TouchEvent) New event.
     */
    protected synchronized void addEvent(TouchEvent event) {
        synchronized (this){
            _touchEvn.add(event);
        } // synchronized
    } // addEvent
}
