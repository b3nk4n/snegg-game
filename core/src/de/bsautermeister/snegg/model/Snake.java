package de.bsautermeister.snegg.model;

import com.badlogic.gdx.utils.Array;

import de.bsautermeister.snegg.common.Resettable;
import de.bsautermeister.snegg.common.Updateable;
import de.bsautermeister.snegg.config.GameConfig;

public class Snake implements Resettable, Updateable {
    // why the fuck to we still need this epsilon? why is there a glitch for world-wrap bottom/right without this?
    private static final float EPS = 0.0025f;

    private Direction lastDirection;
    private Direction direction;

    private final SnakeHead head;
    private final Array<BodyPart> bodyParts;
    
    public Snake() {
        head = new SnakeHead();
        bodyParts = new Array<BodyPart>();
        reset();
    }

    @Override
    public void reset() {
        bodyParts.clear();
        direction = Direction.RIGHT;
        lastDirection = Direction.RIGHT;
        head.reset();
    }

    @Override
    public void update(float delta) {
        head.update(delta);
        for (BodyPart body : bodyParts) {
            body.update(delta);
        }
    }

    public void movementStep() {
        headMovementStep();
        bodyPartsMovementStep();
    }

    private void headMovementStep() {
        if (direction.isRight()) {
            head.moveX(GameConfig.SNAKE_SPEED);
        } else if (direction.isLeft()) {
            head.moveX(-GameConfig.SNAKE_SPEED);
        } else if (direction.isUp()) {
            head.moveY(GameConfig.SNAKE_SPEED);
        } else if (direction.isDown()) {
            head.moveY(-GameConfig.SNAKE_SPEED);
        }
        lastDirection = direction;
    }

    private void bodyPartsMovementStep() {
        if (bodyParts.size > 0) {
            SmoothGameObject prev;
            for (int i = bodyParts.size - 1; i >= 0; --i) {
                SmoothGameObject bodyPart = bodyParts.get(i);
                if (i > 0) {
                    prev = bodyParts.get(i - 1);
                } else {
                    prev = head;
                }
                checkGameObjectOutOfBounds(bodyPart);
                bodyPart.setXY(prev.getX(), prev.getY());
            }
        }
    }

    public void checkSnakeHeadOutOfBounds() {
        SnakeHead snakeHead = getHead();
        checkGameObjectOutOfBounds(snakeHead);
    }

    public void checkSnakeBodyPartsOutOfBounds() {
        for (BodyPart bodyPart : getBodyParts()) {
            checkGameObjectOutOfBounds(bodyPart);
        }
    }

    private void checkGameObjectOutOfBounds(SmoothGameObject gameObject) {
        if (gameObject.getX() >= GameConfig.WORLD_WIDTH - EPS) {
            gameObject.gotoX(0);
        } else if (gameObject.getX() < -GameConfig.SNAKE_SIZE + EPS) {
            gameObject.gotoX(GameConfig.WORLD_WIDTH - GameConfig.SNAKE_SIZE);
        }

        if (gameObject.getY() >= GameConfig.MAX_Y - EPS) {
            gameObject.gotoY(0);
        } else if (gameObject.getY() < -GameConfig.SNAKE_SIZE + EPS) {
            gameObject.gotoY(GameConfig.MAX_Y - GameConfig.SNAKE_SIZE);
        }
    }

    public SnakeHead getHead() {
        return head;
    }

    public void insertBodyPart() {
        BodyPart bodyPart = new BodyPart();

        float x, y;
        if (bodyParts.size == 0) {
            x = head.getX();
            y = head.getY();
        } else {
            BodyPart lastBodyPart = bodyParts.get(bodyParts.size - 1);
            x = lastBodyPart.getX();
            y = lastBodyPart.getY();
        }

        bodyPart.gotoXY(x, y);
        bodyParts.add(bodyPart);
    }

    public Array<BodyPart> getBodyParts() {
        return bodyParts;
    }

    public void setDirection(Direction direction) {
        if (!this.lastDirection.isOpposite(direction)) {
            this.direction = direction;
        }
    }
}
