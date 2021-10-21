package es.ucm.vm.androidengine;

import java.io.InputStream;

import es.ucm.vm.engine.AbstractEngine;

public class Engine extends AbstractEngine implements Runnable {

    @Override
    public void run() {

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