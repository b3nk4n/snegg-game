package de.bsautermeister.snegg.util;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Logger;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Utility methods for using with Viewports.
 *
 * @author goran on 20/08/2016.
 */
public class ViewportUtils {

    private static final Logger log = new Logger(ViewportUtils.class.getName(), Logger.DEBUG);

    private static final int DEFAULT_CELL_SIZE = 1;

    /**
     * Draws world grid for specified viewport using {@link ShapeRenderer}.
     *
     * @param viewport The viewport. Required.
     * @param renderer ShapeRenderer that will be used for drawing. Required.
     * @throws IllegalArgumentException if any param is null.
     */
    public static void drawGrid(Viewport viewport, ShapeRenderer renderer) {
        drawGrid(viewport, renderer, DEFAULT_CELL_SIZE);
    }

    /**
     * Draws world grid for specified viewport using {@link ShapeRenderer}.
     *
     * @param viewport The viewport. Required.
     * @param renderer ShapeRenderer that will be used for drawing. Required.
     * @param cellSize the size of cell in grid. If less than 1 by default 1 will be used.
     * @throws IllegalArgumentException if any param is null.
     */
    public static void drawGrid(Viewport viewport, ShapeRenderer renderer, int cellSize) {
        // validate parameters/arguments
        if (viewport == null) {
            throw new IllegalArgumentException("viewport param is required.");
        }

        if (renderer == null) {
            throw new IllegalArgumentException("renderer param is required.");
        }

        if (cellSize < DEFAULT_CELL_SIZE) {
            cellSize = DEFAULT_CELL_SIZE;
        }

        // copy old color from renderer
        Color oldColor = new Color(renderer.getColor());

        int worldWidth = (int) viewport.getWorldWidth();
        int worldHeight = (int) viewport.getWorldHeight();
        int doubleWorldWidth = worldWidth * 2;
        int doubleWorldHeight = worldHeight * 2;

        renderer.setProjectionMatrix(viewport.getCamera().combined);
        renderer.begin(ShapeRenderer.ShapeType.Line);

        renderer.setColor(Color.WHITE);

        // draw vertical lines
        for (int x = -doubleWorldWidth; x < doubleWorldWidth; x += cellSize) {
            renderer.line(x, -doubleWorldHeight, x, doubleWorldHeight);
        }

        // draw horizontal lines
        for (int y = -doubleWorldHeight; y < doubleWorldHeight; y += cellSize) {
            renderer.line(-doubleWorldWidth, y, doubleWorldWidth, y);
        }

        // draw 0/0 lines
        renderer.setColor(Color.RED);
        renderer.line(0, -doubleWorldHeight, 0, doubleWorldHeight);
        renderer.line(-doubleWorldWidth, 0, doubleWorldWidth, 0);

        // draw world bounds
        renderer.setColor(Color.GREEN);
        renderer.line(0, worldHeight, worldWidth, worldHeight);
        renderer.line(worldWidth, 0, worldWidth, worldHeight);

        renderer.end();
        renderer.setColor(oldColor);
    }

    /**
     * Prints pixels per unit for specified Viewport.
     *
     * @param viewport The viewport for which we want to print pixels per unit ratio. Required.
     * @throws IllegalArgumentException if viewport is null.
     */
    public static void debugPixelsPerUnit(Viewport viewport) {
        if (viewport == null) {
            throw new IllegalArgumentException("viewport param is required.");
        }

        float screenWidth = viewport.getScreenWidth();
        float screenHeight = viewport.getScreenHeight();

        float worldWidth = viewport.getWorldWidth();
        float worldHeight = viewport.getWorldHeight();

        float xPPU = screenWidth / worldWidth;
        float yPPU = screenHeight / worldHeight;

        log.debug("x PPU= " + xPPU + " yPPU= " + yPPU);
    }

    private ViewportUtils() {
    }
}
