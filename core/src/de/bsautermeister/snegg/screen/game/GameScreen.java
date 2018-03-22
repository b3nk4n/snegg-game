package de.bsautermeister.snegg.screen.game;

import com.badlogic.gdx.ScreenAdapter;

import de.bsautermeister.snegg.SneggGame;

public class GameScreen extends ScreenAdapter {
    private final SneggGame game;

    public GameScreen(SneggGame game) {
        this.game = game;
    }
}
