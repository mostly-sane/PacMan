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

    private Player player;
    private CollisionComponent CollisionComponent;
    private final int MOVE_AMOUNT = 3;
    private Vector2 movementDirection = new Vector2(); // Vector to store movement direction

    public PlayerController(Player player) {
        this.player = player;
        this.CollisionComponent = player.getCollisionComponent();
    }

    public void update() {
        // Update Pacman's position based on the movement direction
        player.setX((movementDirection.x * MOVE_AMOUNT) + player.getX());
        player.setY((movementDirection.y * MOVE_AMOUNT) + player.getY());

        // Check collision
        if (CollisionComponent.collides(new Vector2(player.getX(), player.getY()))) {
            // If collision occurs, revert Pacman's position
            //player.movingObject.x -= movementDirection.x * MOVE_AMOUNT;
            //player.movingObject.y -= movementDirection.y * MOVE_AMOUNT;
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
                movementDirection.set(0, -1); // Move up
                currentDirection = movementDirectionEnum.UP;
                break;
            case Input.Keys.A:
                movementDirection.set(-1, 0); // Move left
                currentDirection = movementDirectionEnum.LEFT;
                break;
            case Input.Keys.S:
                movementDirection.set(0, 1); // Move down
                currentDirection = movementDirectionEnum.DOWN;
                break;
            case Input.Keys.D:
                movementDirection.set(1, 0); // Move right
                currentDirection = movementDirectionEnum.RIGHT;
                break;
        }
        return true;
    }
}