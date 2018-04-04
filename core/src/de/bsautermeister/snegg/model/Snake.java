package de.bsautermeister.snegg.model;

import com.badlogic.gdx.utils.Array;

import de.bsautermeister.snegg.common.Resettable;
import de.bsautermeister.snegg.common.Updateable;
import de.bsautermeister.snegg.config.GameConfig;

public class Snake implements Resettable, Updateable {
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
        bodyPartsMovementStep();
        headMovementStep();
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
            GameObject prev = getHead();
            for (BodyPart body : bodyParts) {
                body.setXY(prev.getX(), prev.getY());
                prev = body;
            }
        }
    }

    public SnakeHead getHead() {
        return head;
    }

    public void insertBodyPart() {
        BodyPart bodyPart = new BodyPart();

        if (bodyParts.size == 0) {
            bodyPart.gotoXY(head.getX(), head.getY());
        } else {
            BodyPart lastBodyPart = bodyParts.get(bodyParts.size - 1);
            bodyPart.gotoXY(lastBodyPart.getX(), lastBodyPart.getY());
        }

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
