package de.bsautermeister.snegg.common;

import de.bsautermeister.snegg.services.PlayGameServices;

public class GameServiceApp extends GameApp {

    private final PlayGameServices gameServices;

    public GameServiceApp(PlayGameServices gameServices) {
        this.gameServices = gameServices;
    }

    public PlayGameServices getGameServices() {
        return gameServices;
    }
}
