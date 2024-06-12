package com.pacman.Characters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.pacman.AI.Node;
import com.pacman.AI.PathDrawer;
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

    private Name name;
    private Float speed = 0.75f;
    private PathfindingComponent pathfindingComponent;
    public ArrayList<Node> path;
    private Tile targetTile;
    private Random random = new Random();
    public int eyeXOffset = 0;
    public boolean canMove = true;
    public Pair<Integer, Integer> startingLocation;
    public boolean isDying = false;

    public Ghost(int width, int height, Texture texture, PacMan game, Name name, Pair<Integer, Integer> startingLocation) {
        super(width, height, texture, game);
        this.name = name;
        this.startingLocation = startingLocation;
        pathfindingComponent = new PathfindingComponent(game, this);
        pathfindingComponent.convertToNodes(game.grid);
        movingFrames = new TextureRegion[2];
        setPosition(Utils.getPositionByIndex(startingLocation, game.tileWidth, game.tileHeight));
    }

    public void update(){
        super.update();
        if(targetTile == null || direction == null){
            return;
        }

        if(!canMove){
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

        if(collisionComponent.isGhostCollidingWithPlayer(game.player, this)){
            game.playerDeath();
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
                path = pathfindingComponent.getAvailableNodes(Utils.getCurrentTile(this, game.grid));
                if(!path.isEmpty()){
                    Node n = path.get(random.nextInt(path.size()));
                    Tile tile = Utils.getTileByIndex(n.location.getX(), n.location.getY(), game.grid);
                    path = pathfindingComponent.findPath(Utils.getCurrentTile(this, game.grid), tile);
                } else{
                   path = null;
                }
            break;
        }

        if(path == null){
            return;
        }

        if(path.size() > 1){
            targetTile = game.grid[path.get(1).location.getX()][path.get(1).location.getY()];
            direction = calculatedirection(targetTile);
        }
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

    private Tile getChaseTile(){
        Player player = game.player;
        Tile playerTile = Utils.getCurrentTile(player, game.grid);
        Tile result = null;
        int playerRow = game.player.getColumn();
        int playerColumn = game.player.getRow();
        switch(name){
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
        Tile playerFront = getAvailableTileWithOffset(playerRow, playerColumn, playerTile, playerTile, 4);
        if(playerFront == null){
            turnAround();
            recalculatePath();
        }
        return playerFront;
    }

    private Tile getAvailableTileWithOffset(int playerRow, int playerColumn, Tile result, Tile playerTile, int targetOffset) {
        switch (game.player.direction){
            case UP:
                for(int offset = targetOffset; offset >= 1; offset--) {
                    if(playerColumn - offset < 0){
                        continue;
                    }

                    if(pathfindingComponent.grid[playerRow][playerColumn - offset].isOpen) {
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

                    if(pathfindingComponent.grid[playerRow][playerColumn + offset].isOpen) {
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

                    if(pathfindingComponent.grid[playerRow - offset][playerColumn].isOpen) {
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

                    if (pathfindingComponent.grid[playerRow + offset][offset].isOpen) {
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

    public Texture getEyeTexture(){
        switch(direction){
            case UP:
                eyeXOffset = 0;
                return new Texture(Gdx.files.internal("sprites/eyes/d.png"));

            case DOWN:
                eyeXOffset = 0;
                return new Texture(Gdx.files.internal("sprites/eyes/u.png"));
            case LEFT:
                eyeXOffset = -1;
                return new Texture(Gdx.files.internal("sprites/eyes/l.png"));
            case RIGHT:
                eyeXOffset = 1;
                return new Texture(Gdx.files.internal("sprites/eyes/r.png"));
            default:
                return null;
        }
    }

    public void drawPath(ShapeRenderer shapeRenderer, PathDrawer pathDrawer) {
        pathDrawer.drawPath(shapeRenderer, path);
        pathDrawer.drawBlockedNodes(shapeRenderer, pathfindingComponent.grid);
    }

    public void turnAround(){
        switch(direction){
            case UP:
                direction = Direction.DOWN;
                break;
            case DOWN:
                direction = Direction.UP;
                break;
            case LEFT:
                direction = Direction.RIGHT;
                break;
            case RIGHT:
                direction = Direction.LEFT;
                break;
        }
    }
}