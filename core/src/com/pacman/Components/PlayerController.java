package com.pacman.Components;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector2;
import com.pacman.Characters.Player;

public class PlayerController extends InputAdapter {



    public enum MovementDirectionEnum {
        UP,
        DOWN,
        LEFT,
        RIGHT
    }

    public MovementDirectionEnum currentDirection = MovementDirectionEnum.LEFT;
    private MovementDirectionEnum desiredDirection = MovementDirectionEnum.LEFT;

    private Player player;
    private CollisionComponent collisionComponent;
    private final int MOVE_AMOUNT = 1;
    private Vector2 movementDirection = new Vector2();
    private Vector2 desiredMovementDirection = new Vector2();

    public PlayerController(Player player) {
        this.player = player;
        this.collisionComponent = player.getCollisionComponent();
        movementDirection.set(-MOVE_AMOUNT, 0);
        desiredMovementDirection.set(-MOVE_AMOUNT, 0);
    }

    public void update() {
        Vector2 newPosition = new Vector2(player.getX(), player.getY());

        // Check if the desired direction is free and change direction
        if (collisionComponent.canMove(new Vector2(player.getX(), player.getY()), desiredMovementDirection)) {
            movementDirection.set(desiredMovementDirection);
            currentDirection = desiredDirection;
        }

        // Try to move in the current direction
        if (collisionComponent.canMove(newPosition, movementDirection)) {
            newPosition.add(movementDirection);
        } else {
            // Align Pac-Man with the grid
            alignWithGrid();
        }

        // Move Pac-Man
        player.setX(newPosition.x);
        player.setY(newPosition.y);
    }

    private void alignWithGrid() {
        float x = player.getX();
        float y = player.getY();
        float w = player.getWidth();
        float h = player.getHeight();

        // Align to the nearest grid position
        int gridX = Math.round(x / w) * (int) w;
        int gridY = Math.round(y / h) * (int) h;

        player.setX(gridX);
        player.setY(gridY);
    }

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Input.Keys.W:
                desiredMovementDirection.set(0, -MOVE_AMOUNT);
                desiredDirection = MovementDirectionEnum.UP;
                break;
            case Input.Keys.A:
                desiredMovementDirection.set(-MOVE_AMOUNT, 0);
                desiredDirection = MovementDirectionEnum.LEFT;
                break;
            case Input.Keys.S:
                desiredMovementDirection.set(0, MOVE_AMOUNT);
                desiredDirection = MovementDirectionEnum.DOWN;
                break;
            case Input.Keys.D:
                desiredMovementDirection.set(MOVE_AMOUNT, 0);
                desiredDirection = MovementDirectionEnum.RIGHT;
                break;
        }
        return true;
    }
}
