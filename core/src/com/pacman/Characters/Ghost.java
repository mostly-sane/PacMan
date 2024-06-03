package com.pacman.Characters;

import com.badlogic.gdx.graphics.Texture;
import com.pacman.AI.Node;
import com.pacman.Components.PathfindingComponent;
import com.pacman.Map.Tile;
import com.pacman.PacMan;
import com.pacman.Pair;
import com.pacman.Utils;

import java.util.*;

public class Ghost extends Character{
    public enum Name {
        BLINKY,
        PINKY,
        INKY,
        CLYDE
    }

    Name name;
    Float speed = 0.5f;
    public ArrayList<Node> path = new ArrayList<>();
    PathfindingComponent pathfindingComponent;
    Tile targetTile;

    public Ghost(int width, int height, Texture texture, PacMan game, Name name) {
        super(width, height, texture, game);
        this.name = name;
        pathfindingComponent = new PathfindingComponent(game, this);
        pathfindingComponent.convertToNodes(game.grid);
    }

    public void update(){
        Pair<Float, Float> targetPosition = new Pair<>(targetTile.x, targetTile.y);

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
        pathfindingComponent.findPath(Utils.getCurrentTile(this, game.grid),
                Utils.getCurrentTile(game.player, game.grid));
        targetTile = game.grid[path.get(1).location.getX()][path.get(1).location.getY()];
        System.out.println(targetTile);
    }

    private void printPath(){
        if(path == null){
            System.out.println("No path found");
            return;
        }
        for(Node node : path){
            System.out.println(node.location);
        }
    }



    private void getChaseTile(){

    }

    private void getScatterTile(){

    }

    private void getFrightenedTile(){

    }
}