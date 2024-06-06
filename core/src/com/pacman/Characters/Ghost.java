package com.pacman.Characters;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.pacman.AI.Node;
import com.pacman.AI.PathDrawer;
import com.pacman.Components.PathfindingComponent;
import com.pacman.Map.Tile;
import com.pacman.PacMan;
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
    Float speed = 0.75f;
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
        if(targetTile == null || direction == null){
            return;
        }

        switch(direction){
            case UP:
                position.setY(position.getY() - speed);
                if(position.getY() <= targetTile.y){
                    position.setY(targetTile.y);
                    recalculatePath();
                }
                break;
            case DOWN:
                position.setY(position.getY() + speed);
                if(position.getY() >= targetTile.y){
                    position.setY(targetTile.y);
                    recalculatePath();
                }
                break;
            case LEFT:
                position.setX(position.getX() - speed);
                if(position.getX() <= targetTile.x){
                    position.setX(targetTile.x);
                    recalculatePath();
                }
                break;
            case RIGHT:
                position.setX(position.getX() + speed);
                if(position.getX() >= targetTile.x){
                    position.setX(targetTile.x);
                    recalculatePath();
                }
                break;
        }
    }

    public void recalculatePath() {
        pathfindingComponent.reopenNodes();
        pathfindingComponent.blockNodeBehindGhost();

        switch(game.stage) {
            case CHASE:
                path = pathfindingComponent.findPath(Utils.getCurrentTile(this, game.grid), getChaseTile());
                break;
            case SCATTER:
                path = pathfindingComponent.findPath(Utils.getCurrentTile(this, game.grid), getScatterTile());
                break;
            case FRIGHTENED:
                path = pathfindingComponent.findPath(Utils.getCurrentTile(this, game.grid), getFrightenedTile());
                break;
        }

        if(path == null){
            return;
        }

        if(path.size() > 1){
            targetTile = game.grid[path.get(1).location.getX()][path.get(1).location.getY()];
            direction = calculatedirection(targetTile);
        }
        //pathfindingComponent.reopenNodes();
    }

    private Direction calculatedirection(Tile nextTile) {
        int row = getRow();
        int column = getColumn();
        if(nextTile.i == column && nextTile.j == row + 1){
            return Direction.DOWN;
        } else if(nextTile.i == column && nextTile.j == row - 1){
            return Direction.UP;
        } else if(nextTile.i == column + 1 && nextTile.j == row){
            return Direction.RIGHT;
        } else if(nextTile.i == column - 1 && nextTile.j == row){
            return Direction.LEFT;
        }
        return null;
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
                result = calculateTargetInky(playerTile);
                break;
            case CLYDE:
                if(Utils.getDistance(Utils.getCurrentTile(this, game.grid), playerTile) > 8){
                    result = playerTile;
                } else {
                    result = getScatterTile();
                }
                break;
        }
        return result;
    }

    private Tile calculateTargetPinky(int playerRow, int playerColumn, Tile result, Tile playerTile) {
        result = getAvailableTileWithOffset(playerRow, playerColumn, result, playerTile, 4);

        return result;
    }

    private Tile getAvailableTileWithOffset(int playerRow, int playerColumn, Tile result, Tile playerTile, int targetOffset) {
        switch (game.player.direction){
            case UP:
                for (int offset = targetOffset; offset >= 1; offset--) {
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
                for (int offset = targetOffset; offset >= 1; offset--) {
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
                for (int offset = targetOffset; offset >= 1; offset--) {
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
                for (int offset = targetOffset; offset >= 1; offset--) {
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

    private Tile calculateTargetInky(Tile playerTile) {
        Tile pacmanFront = getAvailableTileWithOffset(game.player.getRow(), game.player.getColumn(), playerTile, playerTile, 2);
        Tile blinkyPosition = Utils.getCurrentTile(game.ghosts[0], game.grid);
        int rowOffset = pacmanFront.i - blinkyPosition.i;
        int columnOffset = pacmanFront.j - blinkyPosition.j;

        if(blinkyPosition.i + rowOffset * 2 >= game.grid.length || blinkyPosition.j + columnOffset * 2 >= game.grid[0].length ||
                blinkyPosition.i + rowOffset * 2 < 0 || blinkyPosition.j + columnOffset * 2 < 0){
            return playerTile;
        }

        return Utils.getTileByIndex(blinkyPosition.i + rowOffset * 2, blinkyPosition.j + columnOffset * 2, game.grid);
    }

    private Tile getScatterTile(){
        switch(name){
            case BLINKY:
                return Utils.getTileByIndex(17, 1, game.grid);
            case PINKY:
                return Utils.getTileByIndex(1, 1, game.grid);
            case INKY:
                return Utils.getTileByIndex(17, 19, game.grid);
            case CLYDE:
                return Utils.getTileByIndex(1, 19, game.grid);
            default:
                return null;
        }
    }

    public void drawPath(ShapeRenderer shapeRenderer, PathDrawer pathDrawer) {
        pathDrawer.drawPath(shapeRenderer, path);
        pathDrawer.drawBlockedNodes(shapeRenderer, pathfindingComponent.nodes);
    }

    private Tile getFrightenedTile(){
        return null;
    }
}