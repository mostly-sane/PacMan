package com.pacman;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;

public class PacManController extends InputAdapter {
    private PacMan pacMan;

    public PacManController(PacMan pacMan) {
        this.pacMan = pacMan;
    }

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
        return true;
    }
}