package com.pacman.Components;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.pacman.Map.Tile;
import java.util.ArrayList;

public class CollisionComponent {
    public Rectangle pacManRect;
    public Rectangle tileRect;
    private Tile[][] grid;

    public CollisionComponent(Tile[][] grid, float tileWidth, float tileHeight) {
        this.grid = grid;
        this.pacManRect = new Rectangle(0, 0, tileWidth, tileHeight);
        this.tileRect = new Rectangle(0, 0, tileWidth, tileHeight);
    }

    public boolean canMove(Vector2 currentPosition, Vector2 moveVector) {
        pacManRect.setPosition(currentPosition.x + moveVector.x, currentPosition.y + moveVector.y);
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (!grid[i][j].open) {
                    tileRect.set(grid[i][j].x, grid[i][j].y, grid[i][j].width, grid[i][j].height);
                    if (pacManRect.overlaps(tileRect)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
}
