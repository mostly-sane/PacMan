package com.pacman.Components;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector2;
import com.pacman.Characters.Player;

public class PlayerController extends InputAdapter {

    public enum movementDirectionEnum{
        UP,
        DOWN,
        LEFT,
        RIGHT
    }

    movementDirectionEnum currentDirection = movementDirectionEnum.LEFT;
    movementDirectionEnum desiredDirection = movementDirectionEnum.LEFT;

    private Player player;
    private CollisionComponent CollisionComponent;
    private final int MOVE_AMOUNT = 1;
    private Vector2 movementDirection = new Vector2(); // Vector to store movement direction
    private Vector2 desiredMovementDirection = new Vector2(); // Vector to store desired movement direction

    public PlayerController(Player player) {
        this.player = player;
        this.CollisionComponent = player.getCollisionComponent();
    }

    public void update() {
        // Check if desired direction is open
        if (!CollisionComponent.collides(new Vector2(player.getX() + desiredMovementDirection.x * MOVE_AMOUNT, player.getY() + desiredMovementDirection.y * MOVE_AMOUNT))) {
            movementDirection.set(desiredMovementDirection);
            currentDirection = desiredDirection;
        }

        // Update Pacman's position based on the movement direction
        player.setX((movementDirection.x * MOVE_AMOUNT) + player.getX());
        player.setY((movementDirection.y * MOVE_AMOUNT) + player.getY());

        // Check collision
        if (CollisionComponent.collides(new Vector2(player.getX(), player.getY()))) {
            player.setX(player.getX() - movementDirection.x * MOVE_AMOUNT);
            player.setY(player.getY() - movementDirection.y * MOVE_AMOUNT);
            // Stop movement
            movementDirection.set(0, 0);
        }


    }

    // Key press events
    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Input.Keys.W:
                desiredMovementDirection.set(0, -1); // Move up
                desiredDirection = movementDirectionEnum.UP;
                break;
            case Input.Keys.A:
                desiredMovementDirection.set(-1, 0); // Move left
                desiredDirection = movementDirectionEnum.LEFT;
                break;
            case Input.Keys.S:
                desiredMovementDirection.set(0, 1); // Move down
                desiredDirection = movementDirectionEnum.DOWN;
                break;
            case Input.Keys.D:
                desiredMovementDirection.set(1, 0); // Move right
                desiredDirection = movementDirectionEnum.RIGHT;
                break;
        }
        return true;
    }
}