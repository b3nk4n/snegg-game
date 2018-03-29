package de.bsautermeister.snegg.model;

import com.badlogic.gdx.utils.Array;

import de.bsautermeister.snegg.config.GameConfig;

public class Snake {
    private Direction direction;

    private final SnakeHead head;
    private final Array<BodyPart> bodyParts;
    
    public Snake() {
        head = new SnakeHead();
        bodyParts = new Array<BodyPart>();
        reset();
    }

    public void reset() {
        bodyParts.clear();
        direction = Direction.RIGHT;
        head.setXY(0, 0);
    }

    public void update() {
        updateBodyParts();
        updateHead();
    }

    private void updateHead() {
        if (direction.isRight()) {
            head.moveX(GameConfig.SNAKE_SPEED);
        } else if (direction.isLeft()) {
            head.moveX(-GameConfig.SNAKE_SPEED);
        } else if (direction.isUp()) {
            head.moveY(GameConfig.SNAKE_SPEED);
        } else if (direction.isDown()) {
            head.moveY(-GameConfig.SNAKE_SPEED);
        }
    }

    private void updateBodyParts() {
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
        if (!this.direction.isOpposite(direction)) {
            this.direction = direction;
        }
    }
}
