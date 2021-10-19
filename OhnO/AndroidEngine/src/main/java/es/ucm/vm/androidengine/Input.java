package es.ucm.vm.androidengine;

import android.view.MotionEvent;
import android.view.View;

import es.ucm.vm.engine.AbstractInput;

public class Input extends AbstractInput implements es.ucm.vm.engine.Input, View.OnTouchListener {
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return false;
    }
}
