package com.pacman;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class CollisionChecker {
    public Rectangle pacManRect;
    Tile[][] grid;

    public CollisionChecker(Tile[][] grid) {
        this.grid = grid;
        this.pacManRect = new Rectangle(0, 0, 23, 23);
    }

    public boolean checkCollision(Vector2 newPosition) {
        pacManRect.setPosition(newPosition.x, newPosition.y);
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (!grid[i][j].open) {
                    if (pacManRect.overlaps(grid[i][j].rect)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}