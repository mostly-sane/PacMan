package com.pacman.Components;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;
import com.pacman.Characters.Character;
import com.pacman.Characters.Player;
import com.pacman.Map.Pill;

public class PlayerController extends InputAdapter {
    private Character.Direction desiredDirection = Character.Direction.LEFT;

    private Player player;
    private CollisionComponent collisionComponent;
    private final int MOVE_AMOUNT = 1;
    private Vector2 movementDirection = new Vector2();
    private Vector2 desiredMovementDirection = new Vector2();
    private Pill[][] pillGrid;  // Add this line
    public boolean isMoving = false;

    private Sound wakaWakaSound;

    public PlayerController(Player player, Pill[][] pillGrid, Sound wakaWakaSound) {
        this.player = player;
        this.collisionComponent = player.getCollisionComponent();
        this.pillGrid = pillGrid;  // Add this line
        this.wakaWakaSound = wakaWakaSound; // Initialize waka waka sound
        movementDirection.set(-MOVE_AMOUNT, 0);
        desiredMovementDirection.set(-MOVE_AMOUNT, 0);
    }

    public void update() {
        isMoving = false;
        Vector2 oldPosition = new Vector2(player.getX(), player.getY());

        // Check if the desired direction is free and change direction
        if (collisionComponent.canMove(new Vector2(player.getX(), player.getY()), desiredMovementDirection)) {
            movementDirection.set(desiredMovementDirection);
            player.direction = desiredDirection;
        }

        // Try to move in the current direction
        if (collisionComponent.canMove(oldPosition, movementDirection)) {
            oldPosition.add(movementDirection);
            isMoving = true;
        } else {
            // Align Pac-Man with the grid
            alignWithGrid();
        }

        // Move Pac-Man
        player.setX(oldPosition.x);
        player.setY(oldPosition.y);

        // Check for pill collision
        checkPillCollision();

        // Update isMoving
        //isMoving = !new Vector2(player.getX(), player.getY()).equals(oldPosition);
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
                if (pill.texture != null) { // Check if there's a pill texture
                    Vector2 pillCenter = new Vector2(pill.getX() + pill.getWidth() / 2, pill.getY() + pill.getHeight() / 2);
                    // Adjust the distance threshold here
                    if (playerCenter.dst(pillCenter) <= (player.getWidth() / 4 + pill.getWidth() / 4)) {
                        switch (pill.type) {
                            case Pill.REGULAR_PILL:
                                player.increaseScore(10);
                                break;
                            case Pill.POWER_PILL:
//TODO power pill effect
                                break;
                            case Pill.CHERRY:
                                player.increaseScore(200);
                                break;
                        }
                        // Deactivate the pill
                        pill.texture.dispose(); // Dispose the texture
                        pill.texture = null; // Set texture to null to indicate pill is collected
                        wakaWakaSound.play();
                    }
                }
            }
        }
    }

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Input.Keys.S:
                desiredMovementDirection.set(0, -MOVE_AMOUNT);
                desiredDirection = Character.Direction.UP;
                break;
            case Input.Keys.A:
                desiredMovementDirection.set(-MOVE_AMOUNT, 0);
                desiredDirection = Character.Direction.LEFT;
                break;
            case Input.Keys.W:
                desiredMovementDirection.set(0, MOVE_AMOUNT);
                desiredDirection = Character.Direction.DOWN;
                break;
            case Input.Keys.D:
                desiredMovementDirection.set(MOVE_AMOUNT, 0);
                desiredDirection = Character.Direction.RIGHT;
                break;
        }
        return true;
    }
}
