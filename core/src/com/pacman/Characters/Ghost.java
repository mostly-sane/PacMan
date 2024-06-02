package com.pacman.Characters;

import com.badlogic.gdx.graphics.Texture;
import com.pacman.Map.Tile;
import com.pacman.PacMan;
import com.pacman.Pair;
import com.pacman.Utils;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class Ghost extends Character{
    Float speed = 0.5f;
    ArrayList<Tile> path;

    public Ghost(int width, int height, Texture texture, PacMan game) {
        super(width, height, texture, game);
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                recalculatePath();
            }
        }, 0, 300);
    }

    public void update(){
        if (path == null || path.isEmpty()) {
            return;
        }
        Tile targetTile = path.get(0);
        Pair<Float, Float> targetPosition = new Pair<>(targetTile.x, targetTile.y);

        if(targetPosition.getX() < 0 || targetPosition.getX() >= game.grid.length ||
                targetPosition.getY() < 0 || targetPosition.getY() >= game.grid[0].length) {
            // If the target position is out of grid bounds, do not move
            return;
        }

        if(position.getX() < targetPosition.getX()){
            position.setX(position.getX() + speed);
        } else if(position.getX() > targetPosition.getX()){
            position.setX(position.getX() - speed);
        } else if(position.getY() < targetPosition.getY()){
            position.setY(position.getY() + speed);
        } else if(position.getY() > targetPosition.getY()){
            position.setY(position.getY() - speed);
        }
    }

    public void setPath(ArrayList<Tile> path) {
        this.path = path;
    }

    public void recalculatePath() {
        path = Utils.getShortestPath(game.grid, getCurrentTile(), game.player.getCurrentTile());
    }
}