package de.bsautermeister.snegg.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;

/**
 * @author goran on 25/07/2016.
 */
public class GdxUtils {

    /**
     * Clears screen using {@link Color#BLACK}.
     */
    public static void clearScreen() {
        clearScreen(Color.BLACK);
    }

    /**
     * Clears screen using specified {@link Color}.
     *
     * @param color The color for clearing the screen. If null black will be used.
     */
    public static void clearScreen(Color color) {
        if (color == null) {
            color = Color.BLACK;
        }

        Gdx.gl.glClearColor(color.r, color.g, color.b, color.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

}
