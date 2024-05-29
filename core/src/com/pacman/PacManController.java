package com.pacman;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

public class PacManController extends InputAdapter {
    private PacMan pacMan;
    private CollisionChecker collisionChecker;
    private final int MOVE_AMOUNT = 5;

    public PacManController(PacMan pacMan, CollisionChecker collisionChecker) {
        this.pacMan = pacMan;
this.collisionChecker = pacMan.collisionChecker;
    }



    public void update() {
        Vector2 newPosition = new Vector2(pacMan.movingObject.x, pacMan.movingObject.y);
        if (Gdx.input.isKeyPressed(Keys.W)) {
            newPosition.y -= MOVE_AMOUNT;
        } else if (Gdx.input.isKeyPressed(Keys.A)) {
            newPosition.x -= MOVE_AMOUNT;
        } else if (Gdx.input.isKeyPressed(Keys.S)) {
            newPosition.y += MOVE_AMOUNT;
        } else if (Gdx.input.isKeyPressed(Keys.D)) {
            newPosition.x += MOVE_AMOUNT;
        }

        if (!collisionChecker.checkCollision(newPosition)) {
            pacMan.movingObject.x = newPosition.x;
            pacMan.movingObject.y = newPosition.y;
            System.out.println("Collision: " + collisionChecker.checkCollision(newPosition));
        }
    }





/*
    @Override
    public boolean keyDown(int keycode) {
        int currentI = pacMan.grid[0].length;
        int currentJ = pacMan.grid.length;

        for (int i = 0; i < pacMan.grid.length; i++) {
            for (int j = 0; j < pacMan.grid[0].length; j++) {
                if (pacMan.grid[i][j] == pacMan.currentTile) {
                    currentI = i;
                    currentJ = j;
                    break;
                }
            }
        }

        switch (keycode) {
            case Keys.W:
                // Move PacMan up
                if (currentI > 0 && pacMan.grid[currentI - 1][currentJ].open) {
                    pacMan.currentTile = pacMan.grid[currentI - 1][currentJ];
                }
                break;
            case Keys.A:
                // Move PacMan left
                if (currentJ > 0 && pacMan.grid[currentI][currentJ - 1].open) {
                    pacMan.currentTile = pacMan.grid[currentI][currentJ - 1];
                }
                break;
            case Keys.S:
                // Move PacMan down
                if (currentI < pacMan.grid.length - 1 && pacMan.grid[currentI + 1][currentJ].open) {
                    pacMan.currentTile = pacMan.grid[currentI + 1][currentJ];
                }
                break;
            case Keys.D:
                // Move PacMan right
                if (currentJ < pacMan.grid[0].length - 1 && pacMan.grid[currentI][currentJ + 1].open) {
                    pacMan.currentTile = pacMan.grid[currentI][currentJ + 1];
                }
                break;
            default:
                return false;
        }
        pacMan.movingObject.x = pacMan.currentTile.x;
        pacMan.movingObject.y = pacMan.currentTile.y;
        return true;
    }
*/
}