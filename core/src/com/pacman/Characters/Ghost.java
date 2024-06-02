package com.pacman.Characters;

import com.badlogic.gdx.graphics.Texture;
import com.pacman.Map.Tile;
import com.pacman.PacMan;
import com.pacman.Pair;

import java.util.*;

public class Ghost extends Character{
    Float speed = 0.5f;
    ArrayList<Tile> path;
    ArrayList<Tile> reversedPath = new ArrayList<>();
    ArrayList<Tile> result = new ArrayList<>();

    public Ghost(int width, int height, Texture texture, PacMan game) {
        super(width, height, texture, game);
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                recalculatePath();
            }
        }, 0, 100);
    }

    public void update(){
        if (path == null || path.isEmpty()) {
            return;
        }
        Tile targetTile = path.get(0);
        Pair<Float, Float> targetPosition = new Pair<>(targetTile.x, targetTile.y);

        if(Math.abs(position.getX() - targetPosition.getX()) < speed){
            if(Math.abs(position.getY() - targetPosition.getY()) < speed){
                path.remove(0); // remove the reached tile from the path
                if(path.isEmpty()){
                    return; // if there are no more tiles in the path, exit the method
                }
                targetTile = path.get(0); // get the next tile in the path
                targetPosition = new Pair<>(targetTile.x, targetTile.y);
            }
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

    public void recalculatePath() {
        path = getShortestPath(game.grid, getCurrentTile(), game.player.getCurrentTile());
        System.out.println(game.player.getCurrentTile());
    }

    public ArrayList<Tile> getShortestPath(Tile[][] grid, Tile start, Tile end){
        result.clear();

        PriorityQueue<Tile> openSet = new PriorityQueue<>(Comparator.comparingDouble(t -> t.f));
        Set<Tile> closedSet = new HashSet<>();

        if(grid == null || start == null || end == null){
            return result;
        }

        if(start.equals(end)){
            result.add(start);
            return result;
        }

        openSet.add(start);

        while(!openSet.isEmpty()){
            Tile current = openSet.poll();

            if(current.equals(end)){
                return retracePath(current);
            }

            openSet.remove(current);
            closedSet.add(current);

            for(Tile neighbor : current.getNeighbors(grid)){
                if(!neighbor.open || closedSet.contains(neighbor)){
                    continue;
                }

                double newG = current.g + 1;
                if(!openSet.contains(neighbor)){
                    openSet.add(neighbor);
                } else if(newG >= neighbor.g){
                    continue;
                }

                neighbor.cameFrom = current;
                neighbor.g = newG;
                neighbor.h = heuristic(neighbor, end);
                neighbor.f = neighbor.g + neighbor.h;

            }
        }
        return result;
    }

    private ArrayList<Tile> retracePath(Tile current) {
        reversedPath.clear();
        while(current != null){
            reversedPath.add(0, current);
            current = current.cameFrom;
        }
        return reversedPath;
    }

    private static double heuristic(Tile neighbor, Tile end) {
        return Math.abs(neighbor.i - end.i) + Math.abs(neighbor.j - end.j);
    }
}