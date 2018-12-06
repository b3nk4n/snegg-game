package de.bsautermeister.snegg.model;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Logger;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import de.bsautermeister.snegg.common.Resettable;
import de.bsautermeister.snegg.common.Updateable;
import de.bsautermeister.snegg.config.GameConfig;
import de.bsautermeister.snegg.serializer.BinarySerializable;

public class Snake implements Resettable, Updateable, BinarySerializable {
    private static final Logger LOG = new Logger(Snake.class.getName(), GameConfig.LOG_LEVEL);

    // why the fuck to we still need this epsilon? why is there a glitch for world-wrap bottom/right without this?
    private static final float EPS = 0.0025f;

    private Direction lastDirection;
    private Direction direction;

    /**
     * Defines the maximum time the whole snake should require to move. This is due to the game is
     * slowly increasing the speed. Conequently, the whole snake movement has to get faster, too.
     */
    private float maxTotalMoveTime;

    private final SnakeHead head;
    private final Array<BodyPart> bodyParts;

    private float happyTimer = 0f;
    
    public Snake() {
        head = new SnakeHead();
        bodyParts = new Array<BodyPart>();
        reset();
    }

    @Override
    public void reset() {
        bodyParts.clear();

        direction = Direction.UP;
        lastDirection = Direction.UP;
        head.reset();
        insertBodyPart();
        maxTotalMoveTime = GameConfig.MOVE_TIME;
    }

    @Override
    public void update(float delta) {
        head.update(delta);
        for (BodyPart body : bodyParts) {
            body.update(delta);
        }

        if (happyTimer >= 0) {
            happyTimer -= delta;
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
        if (gameObject.getX() >= GameConfig.MAX_X - EPS) {
            gameObject.gotoWorldWrapX(GameConfig.MIN_X);
        } else if (gameObject.getX() < GameConfig.MIN_X - GameConfig.SNAKE_SIZE + EPS) {
            gameObject.gotoWorldWrapX(GameConfig.MAX_X - GameConfig.SNAKE_SIZE);
        }

        if (gameObject.getY() >= GameConfig.MAX_Y - EPS) {
            gameObject.gotoWorldWrapY(GameConfig.MIN_Y);
        } else if (gameObject.getY() < GameConfig.MIN_Y - GameConfig.SNAKE_SIZE + EPS) {
            gameObject.gotoWorldWrapY(GameConfig.MAX_Y - GameConfig.SNAKE_SIZE);
        }
    }

    public SnakeHead getHead() {
        return head;
    }

    public void insertBodyPart() {
        if (length() >= GameConfig.MAX_SNAKE_LENGTH) {
            // ensure the snake is never getting to large for the whole game field
            return;
        }

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

        bodyPart.gotoWorldWrapXY(x, y);
        bodyParts.add(bodyPart);

        final float STATIC_STAY_TIME = 0.05f;

        float duration = GameConfig.INITIAL_TRANS_DURATION - ((float)Math.sqrt(bodyParts.size) * 0.01f);
        float delay = Math.min(GameConfig.INITIAL_TRANS_DELAY, (maxTotalMoveTime - duration - STATIC_STAY_TIME) / bodyParts.size);

        head.setTransitionDuration(duration);

        int i = 1;
        for (BodyPart body : bodyParts) {
            body.setTransitionDuration(duration);
            body.setTransitionDelay(i++ * delay);
        }

    }

    public Array<BodyPart> getBodyParts() {
        return bodyParts;
    }

    public void setDirection(Direction direction) {
        if (!this.lastDirection.isOpposite(direction)) {
            this.direction = direction;
        }
    }

    public Direction getDirection() {
        return direction;
    }

    public void makeHappy() {
        happyTimer = GameConfig.SNAKE_HAPPY_TIME;
    }

    public boolean isHappy() {
        return happyTimer > 0f;
    }

    public void setMaxTotalMoveTime(float maxTotalMoveTime) {
        this.maxTotalMoveTime = maxTotalMoveTime;
    }

    public int length() {
        return 1 + bodyParts.size;
    }

    @Override
    public void write(DataOutputStream out) throws IOException {
        out.writeUTF(lastDirection.name());
        out.writeUTF(direction.name());
        out.writeFloat(happyTimer);
        out.writeFloat(maxTotalMoveTime);
        head.write(out);

        out.writeInt(bodyParts.size);
        for (BodyPart bodyPart : bodyParts) {
            bodyPart.write(out);
        }
    }

    @Override
    public void read(DataInputStream in) throws IOException {
        lastDirection = Enum.valueOf(Direction.class, in.readUTF());
        direction = Enum.valueOf(Direction.class, in.readUTF());
        happyTimer = in.readFloat();
        maxTotalMoveTime = in.readFloat();
        head.read(in);

        int bodyPartSize = in.readInt();
        for (int i = 0; i < bodyPartSize; i++) {
            if (i < bodyParts.size) {
                bodyParts.get(i).read(in);
            } else {
                BodyPart bodyPart = new BodyPart();
                bodyPart.read(in);
                bodyParts.add(bodyPart);
            }
        }
    }
}
