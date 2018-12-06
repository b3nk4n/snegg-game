package de.bsautermeister.snegg.util.debug;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Logger;

/**
 * Controller for debugging {@link OrthographicCamera}.
 *
 * @author goran on 22/08/2016.
 */
public class DebugCameraController {

    // == constants ==
    private static final Logger log = new Logger(DebugCameraController.class.getName(), Logger.DEBUG);

    // == attributes ==
    private Vector2 position = new Vector2();
    private Vector2 startPosition = new Vector2();
    private float zoom = 1.0f;
    private DebugCameraInfo info;

    // == constructors ==
    public DebugCameraController() {
        init();
    }

    // == init ==
    private void init() {
        info = new DebugCameraInfo();

        log.info("cameraInfo= " + info);
    }

    // == public methods ==

    /**
     * Sets start position of camera.
     *
     * @param x the x position.
     * @param y the y position.
     */
    public void setStartPosition(float x, float y) {
        startPosition.set(x, y);
        setPosition(x, y);
    }

    /**
     * Applies internal position and zoom to specified camera.
     * Call this method after handling debug input.
     *
     * @param camera The camera instance.
     * @throws IllegalArgumentException if camera param is null.
     * @see #handleDebugInput(float)
     */
    public void applyTo(OrthographicCamera camera) {
        if (camera == null) {
            throw new IllegalArgumentException("camera cannot be null.");
        }

        camera.position.set(position, 0);
        camera.zoom = zoom;
        camera.update();
    }

    /**
     * Handles debug input. Call this in you setScore cycle.
     *
     * @param delta the delta time.
     */
    public void handleDebugInput(float delta) {
        if (Gdx.app.getType() != Application.ApplicationType.Desktop) {
            return;
        }

        float moveSpeed = info.getMoveSpeed() * delta;
        float zoomSpeed = info.getZoomSpeed() * delta;

        // setScore control
        if (info.isLeftPressed()) {
            moveLeft(moveSpeed);
        }

        if (info.isRightPressed()) {
            moveRight(moveSpeed);
        }

        if (info.isUpPressed()) {
            moveUp(moveSpeed);
        }

        if (info.isDownPressed()) {
            moveDown(moveSpeed);
        }

        // zoom control
        if (info.isZoomInPressed()) {
            zoomIn(zoomSpeed);
        }
        if (info.isZoomOutPressed()) {
            zoomOut(zoomSpeed);
        }

        // reset control
        if (info.isResetPressed()) {
            reset();
        }

        // log control
        if (info.isLogPressed()) {
            logDebug();
        }

    }

    // == private methods ==
    private void setPosition(float x, float y) {
        position.set(x, y);
    }

    private void setZoom(float value) {
        zoom = MathUtils.clamp(value, info.getMaxZoomIn(), info.getMaxZoomOut());
    }

    private void moveCamera(float xSpeed, float ySpeed) {
        setPosition(position.x + xSpeed, position.y + ySpeed);
    }

    private void moveLeft(float speed) {
        moveCamera(-speed, 0);
    }

    private void moveRight(float speed) {
        moveCamera(speed, 0);
    }

    private void moveUp(float speed) {
        moveCamera(0, speed);
    }

    private void moveDown(float speed) {
        moveCamera(0, -speed);
    }

    private void zoomIn(float zoomSpeed) {
        setZoom(zoom + zoomSpeed);
    }

    private void zoomOut(float zoomSpeed) {
        setZoom(zoom - zoomSpeed);
    }

    private void reset() {
        position.set(startPosition);
        setZoom(1.0f);
    }

    private void logDebug() {
        log.debug("position= " + position + " zoom= " + zoom);
    }
}
