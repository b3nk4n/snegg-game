package de.bsautermeister.snegg.model;

import com.badlogic.gdx.utils.Array;

import de.bsautermeister.snegg.common.Resettable;
import de.bsautermeister.snegg.common.Updateable;
import de.bsautermeister.snegg.config.GameConfig;

public class Snake implements Resettable, Updateable {
    private Direction lastDirection;
    private Direction direction;

    private float snakeMoveTimer;

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
        snakeMoveTimer = 0f;
        head.reset();
    }

    @Override
    public void update(float delta) {
        snakeMoveTimer += delta;
        if (snakeMoveTimer >= GameConfig.MOVE_TIME) {
            snakeMoveTimer -= GameConfig.MOVE_TIME;
            bodyPartsMovementStep();
            headMovementStep();
        }

        head.update(delta);
        for (BodyPart body : bodyParts) {
            body.update(delta);
        }
    }

    private void headMovementStep() {
        if (direction.isRight()) {
            head.smoothMoveX(GameConfig.SNAKE_SPEED);
        } else if (direction.isLeft()) {
            head.smoothMoveX(-GameConfig.SNAKE_SPEED);
        } else if (direction.isUp()) {
            head.smoothMoveY(GameConfig.SNAKE_SPEED);
        } else if (direction.isDown()) {
            head.smoothMoveY(-GameConfig.SNAKE_SPEED);
        }
        lastDirection = direction;
    }

    private void bodyPartsMovementStep() {
        if (bodyParts.size > 0) {
            BodyPart tail = bodyParts.removeIndex(0);
            tail.setXY(head.getX(), head.getY());
            bodyParts.add(tail);
        }
    }

    public SnakeHead getHead() {
        return head;
    }

    public void insertBodyPart() {
        BodyPart bodyPart = new BodyPart();
        bodyPart.setXY(head.getX(), head.getY());
        bodyParts.insert(0, bodyPart);
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
