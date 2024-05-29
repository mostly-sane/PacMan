package com.pacman;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class CollisionChecker {

Tile[][] grid;

public CollisionChecker(Tile[][] grid, int blockedTileWidth, int blockedTileHeight) {
    this.grid = grid;
    this.blockedTileWidth = blockedTileWidth;
    this.blockedTileHeight = blockedTileHeight;
}

int blockedTileWidth = 20;
int blockedTileHeight = 20;


int pacManWidth = 20;
int pacManHeight = 20;

    public boolean checkCollision(Vector2 position) {
        int x = (int) position.x;
        int y = (int) position.y;

        int i = x / blockedTileWidth;
        int j = y / blockedTileHeight;

        if (i < 0 || i >= grid.length || j < 0 || j >= grid[0].length) {
            return true;
        }

        Tile tile = grid[i][j];
        if (!tile.open) {
            return true;
        }

        return false;
    }


}
