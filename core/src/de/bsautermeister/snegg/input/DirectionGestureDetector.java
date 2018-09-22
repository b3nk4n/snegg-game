package de.bsautermeister.snegg.input;

import com.badlogic.gdx.input.GestureDetector;

public class DirectionGestureDetector extends GestureDetector {
    public DirectionGestureDetector(DirectionGestureListener listener) {
        super(new DirectionGestureAdapter(0f, listener));
    }

    public DirectionGestureDetector(float velocityThreshold, DirectionGestureListener listener) {
        super(new DirectionGestureAdapter(velocityThreshold, listener));
    }

    private static class DirectionGestureAdapter extends GestureAdapter {
        private final DirectionGestureListener directionGestureListener;
        private final float velocityThreshold;

        public DirectionGestureAdapter(float velocityThreshold, DirectionGestureListener directionGestureListener) {
            this.velocityThreshold = velocityThreshold;
            this.directionGestureListener = directionGestureListener;
        }

        @Override
        public boolean fling(float velocityX, float velocityY, int button) {
            float absVelocityX = Math.abs(velocityX);
            float absVelocityY = Math.abs(velocityY);

            if (absVelocityX > absVelocityY && absVelocityX >= velocityThreshold) {
                if (velocityX > 0) {
                    directionGestureListener.onRight();
                } else {
                    directionGestureListener.onLeft();
                }
            } else if (absVelocityY > absVelocityX && absVelocityY >= velocityThreshold) {
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
