package com.pacman.Components;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.pacman.Map.Tile;

public class CollisionComponent {
    public Rectangle pacManRect;
    Tile[][] grid;

    public CollisionComponent(Tile[][] grid) {
        this.grid = grid;
        this.pacManRect = new Rectangle(0, 0, 23, 23);
    }

    public boolean collides(Vector2 newPosition) {
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