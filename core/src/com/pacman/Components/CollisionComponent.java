package com.pacman.Components;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.pacman.Characters.Character;
import com.pacman.Characters.Ghost;
import com.pacman.Characters.Player;
import com.pacman.Map.Tile;
import com.pacman.PacMan;

import java.util.ArrayList;

public class CollisionComponent {
    public Rectangle pacManRect;
    public Rectangle tileRect;
    private Tile[][] grid;
    private Character parent;

    public CollisionComponent(PacMan game, Character parent) {
        this.grid = game.grid;
        this.parent = parent;
        this.pacManRect = new Rectangle(0, 0, game.tileWidth, game.tileHeight);
        this.tileRect = new Rectangle(0, 0, game.tileWidth, game.tileHeight);
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

    public boolean isGhostCollidingWithPlayer(Player player, Ghost ghost) {
        Rectangle ghostRect = new Rectangle(ghost.getX(), ghost.getY(), ghost.getWidth(), ghost.getHeight());
        Rectangle playerRect = new Rectangle(player.getX(), player.getY(), player.getWidth(), player.getHeight());
        return ghostRect.overlaps(playerRect);
    }
}
