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
    PathfindingComponent pathfindingComponent;
    public ArrayList<Node> path;
    Tile targetTile;

    public Ghost(int width, int height, Texture texture, PacMan game, Name name) {
        super(width, height, texture, game);
        this.name = name;
        pathfindingComponent = new PathfindingComponent(game, this);
        pathfindingComponent.convertToNodes(game.grid);
    }

    public void update(){
        Pair<Float, Float> targetPosition = new Pair<>(targetTile.x, targetTile.y);
        if(position.equals(targetPosition)){
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

    public void recalculatePath() {
        switch(game.state) {
            case CHASE:
                pathfindingComponent.findPath(Utils.getCurrentTile(this, game.grid), getChaseTile());
                break;
            case SCATTER:
                pathfindingComponent.findPath(Utils.getCurrentTile(this, game.grid), getScatterTile());
                break;
            case FRIGHTENED:
                pathfindingComponent.findPath(Utils.getCurrentTile(this, game.grid), getFrightenedTile());
                break;
        }

        targetTile = game.grid[path.get(1).location.getX()][path.get(1).location.getY()];
        //System.out.println(targetTile);
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

    private Tile getChaseTile(){
        Player player = game.player;
        Tile playerTile = Utils.getCurrentTile(player, game.grid);
        Tile result = null;
        int playerRow = game.player.getColumn();
        int playerColumn = game.player.getRow();
        switch (name){
            case BLINKY:
                result = playerTile;
                break;
            case PINKY:
                if(player.controller.isMoving){
                    result = calculateTargetPinky(playerRow, playerColumn, result, playerTile);
                } else {
                    result = playerTile;
                }
                break;
            case INKY:
                result = playerTile;
                break;
            case CLYDE:
                result = playerTile;
                break;
        }
        return result;
    }

    private Tile calculateTargetPinky(int playerRow, int playerColumn, Tile result, Tile playerTile) {
        switch (game.player.direction){
            case UP:
                for (int offset = 4; offset >= 1; offset--) {
                    if(playerColumn - offset < 0){
                        continue;
                    }

                    if(pathfindingComponent.nodes[playerRow][playerColumn - offset].isOpen) {
                        result = Utils.getTileByIndex(playerRow, playerColumn - offset, game.grid);
                        break;
                    }

                    result = playerTile;
                }
                break;
            case DOWN:
                for (int offset = 4; offset >= 1; offset--) {
                    if(playerColumn + offset >= game.grid[0].length){
                        continue;
                    }

                    if(pathfindingComponent.nodes[playerRow][playerColumn + offset].isOpen) {
                        result = Utils.getTileByIndex(playerRow, playerColumn + offset, game.grid);
                        break;
                    }

                    result = playerTile;
                }
            break;
            case LEFT:
                for (int offset = 4; offset >= 1; offset--) {
                    if(playerRow - offset < 0){
                        continue;
                    }

                    if(pathfindingComponent.nodes[playerRow - offset][playerColumn].isOpen) {
                        result = Utils.getTileByIndex(playerRow - offset, playerColumn, game.grid);
                        break;
                    }

                    result = playerTile;
                }
            break;
            case RIGHT:
                for (int offset = 4; offset >= 1; offset--) {
                    if(playerRow + offset >= game.grid.length){
                        continue;
                    }

                    if (pathfindingComponent.nodes[playerRow + offset][offset].isOpen) {
                        result = Utils.getTileByIndex(playerRow + offset, playerColumn, game.grid);
                        break;
                    }

                    result = playerTile;
                }
            break;
        }

        return result;
    }

    private Tile getScatterTile(){
        return null;
    }

    private Tile getFrightenedTile(){
        return null;
    }
}