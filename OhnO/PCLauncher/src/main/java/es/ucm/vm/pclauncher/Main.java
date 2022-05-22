package es.ucm.vm.pclauncher;

import es.ucm.vm.logic.Logic;
import es.ucm.vm.pcengine.Engine;

public class Main {
    static final int FPS = 60; // FPS to control frame rate.

    /**
     * Main method called when application starts. Creates a new Engine and Logic and puts them
     * together to play the game.
     *
     * @param args (String[]) Arguments given to app
     */
    public static void main(String[] args){
        // Create Engine and Logic
        Engine _eng = new Engine();

        // Unify Engine and Logic
        Logic _log = new Logic(_eng);
        _log.initLogic();

        _eng.saveGameState(_log.getGameState());

        _eng.setFPS(FPS);

        // Run game
        _eng.run();
    } // main
}