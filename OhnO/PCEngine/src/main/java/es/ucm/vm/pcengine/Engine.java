package es.ucm.vm.pcengine;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;
import java.io.InputStream;

import es.ucm.vm.engine.AbstractEngine;

public class Engine extends AbstractEngine implements ComponentListener, WindowStateListener {
    @Override
    public void componentResized(ComponentEvent componentEvent) {

    }

    @Override
    public void componentMoved(ComponentEvent componentEvent) {

    }

    @Override
    public void componentShown(ComponentEvent componentEvent) {

    }

    @Override
    public void componentHidden(ComponentEvent componentEvent) {

    }

    @Override
    public void windowStateChanged(WindowEvent windowEvent) {

    }

    @Override
    public InputStream openInputStream(String filename) {
        return null;
    }

    @Override
    public void HandleException(Exception e) {

    }

    @Override
    public int getWinWidth() {
        return 0;
    }

    @Override
    public int getWinHeight() {
        return 0;
    }
}