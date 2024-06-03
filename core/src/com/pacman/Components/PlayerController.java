package com.pacman.Components;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector2;
import com.pacman.Characters.Player;
import com.pacman.Map.Pill;

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
    private Pill[][] pillGrid;  // Add this line

    public PlayerController(Player player, Pill[][] pillGrid) {
        this.player = player;
        this.collisionComponent = player.getCollisionComponent();
        this.pillGrid = pillGrid;  // Add this line
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

        // Check for pill collision
        checkPillCollision();
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


    public Vector2 getCollisionRectangleCenter() {
        float centerX = collisionComponent.pacManRect.x + collisionComponent.pacManRect.width / 2;
        float centerY = collisionComponent.pacManRect.y + collisionComponent.pacManRect.height / 2;
        return new Vector2(centerX, centerY);
    }

    private void checkPillCollision() {
        Vector2 playerCenter = getCollisionRectangleCenter();
        for (int i = 0; i < pillGrid.length; i++) {
            for (int j = 0; j < pillGrid[i].length; j++) {
                Pill pill = pillGrid[i][j];
                if (pill.active) {
                    Vector2 pillCenter = new Vector2(pill.getX() + pill.getWidth() / 2, pill.getY() + pill.getHeight() / 2);
                    // Adjust the distance threshold here
                    if (playerCenter.dst(pillCenter) <= (player.getWidth() / 4 + pill.getWidth() / 4)) {
                        player.increaseScore(10);
                        pill.active = false;
                        pill.setTexture(false);
                    }
                }
            }
        }
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
