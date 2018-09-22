package de.bsautermeister.snegg.input;

import com.badlogic.gdx.input.GestureDetector;

public class DirectionGestureDetector extends GestureDetector {
    public DirectionGestureDetector(DirectionGestureListener listener) {
        super(new DirectionGestureAdapter(listener));
    }

    private static class DirectionGestureAdapter extends GestureAdapter {
        DirectionGestureListener directionGestureListener;

        public DirectionGestureAdapter(DirectionGestureListener directionGestureListener) {
            this.directionGestureListener = directionGestureListener;
        }

        @Override
        public boolean fling(float velocityX, float velocityY, int button) {
            if (Math.abs(velocityX) > Math.abs(velocityY)) {
                if (velocityX > 0) {
                    directionGestureListener.onRight();
                } else {
                    directionGestureListener.onLeft();
                }
            } else {
                if (velocityY > 0) {
                    directionGestureListener.onDown();
                } else {
                    directionGestureListener.onUp();
                }
            }

            return super.fling(velocityX, velocityY, button);
        }
    }
}
