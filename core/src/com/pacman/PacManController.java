package com.pacman;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
public class PacManController extends InputAdapter {
    private PacMan pacMan;
    private CollisionChecker collisionChecker;
    private final int MOVE_AMOUNT = 3;
    private Vector2 movementDirection = new Vector2(); // Vector to store movement direction

    public PacManController(PacMan pacMan, CollisionChecker collisionChecker) {
        this.pacMan = pacMan;
        this.collisionChecker = pacMan.collisionChecker;
    }

    public void update() {
        // Update Pacman's position based on the movement direction
        pacMan.movingObject.x += movementDirection.x * MOVE_AMOUNT;
        pacMan.movingObject.y += movementDirection.y * MOVE_AMOUNT;

        // Check collision
        if (collisionChecker.checkCollision(new Vector2(pacMan.movingObject.x, pacMan.movingObject.y))) {
            // If collision occurs, revert Pacman's position
            pacMan.movingObject.x -= movementDirection.x * MOVE_AMOUNT;
            pacMan.movingObject.y -= movementDirection.y * MOVE_AMOUNT;
            // Stop movement
            movementDirection.set(0, 0);
        }
    }

    // Key press events
    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Keys.W:
                movementDirection.set(0, -1); // Move up
                break;
            case Keys.A:
                movementDirection.set(-1, 0); // Move left
                break;
            case Keys.S:
                movementDirection.set(0, 1); // Move down
                break;
            case Keys.D:
                movementDirection.set(1, 0); // Move right
                break;
        }
        return true;
    }
}
