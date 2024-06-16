package com.pacman.Characters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.pacman.AI.Node;
import com.pacman.Components.PathfindingComponent;
import com.pacman.Map.Tile;
import com.pacman.PacMan;
import com.pacman.Pair;
import com.pacman.Utils;

import java.util.*;

public class Ghost extends Character{
    private Float speed = 0.75f;
    private PathfindingComponent pathfindingComponent;
    public ArrayList<Node> path;
    private Tile targetTile;
    private Random random = new Random();
    public int eyeXOffset = 0;
    public boolean canMove = true;
    public Pair<Integer, Integer> startingLocation;
    public boolean isDying = false;
    Sound deathSound = Gdx.audio.newSound(Gdx.files.internal("sprites/Sounds/ghostdeath.mp3"));
    public boolean fearEnding = false;

    public boolean isVisible = true;
    public boolean showPoints = false;
    public float pointsDisplayTime = 5.0f;
    private float pointsElapsed = 0f;

    protected Texture frightened0 = new Texture(Gdx.files.internal("sprites/ghosts/f-0.png"));
    protected Texture frightened1 = new Texture(Gdx.files.internal("sprites/ghosts/f-1.png"));

    private Texture eyesDown = new Texture(Gdx.files.internal("sprites/eyes/d.png"));
    private Texture eyesUp = new Texture(Gdx.files.internal("sprites/eyes/u.png"));
    private Texture eyesLeft = new Texture(Gdx.files.internal("sprites/eyes/l.png"));
    private Texture eyesRight = new Texture(Gdx.files.internal("sprites/eyes/r.png"));

    protected Texture moving0;
    protected Texture moving1;

    public Pair<Float, Float> deathLocation;


    public Ghost(int width, int height, Texture texture, PacMan game, Pair<Integer, Integer> startingLocation) {
        super(width, height, texture, game);
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

        if (showPoints) {
            pointsElapsed += Gdx.graphics.getDeltaTime();
            if (pointsElapsed >= pointsDisplayTime) {
                showPoints = false;
                pointsElapsed = 0f;
                isVisible = true;
                canMove = true;
            }
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
            if(game.stage == PacMan.Stage.FRIGHTENED && !isDying){
                die();
            } else{
                game.playerDeath();
            }
        }
    }

    public void recalculatePath() {
        pathfindingComponent.reopenNodes();
        pathfindingComponent.blockNodeBehindGhost();

        if(isDying){
            path = pathfindingComponent.findPath(Utils.getCurrentTile(this, game.grid),
                    Utils.getTileByIndex(startingLocation.getX(), startingLocation.getY(), game.grid));

        } else{
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

    protected Tile getChaseTile(){
        return null;
    }

    protected Tile getAvailableTileWithOffset(int playerRow, int playerColumn, Tile result, Tile playerTile, int targetOffset) {
        playerRow--;
        playerColumn--;
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

    protected Tile getScatterTile(){
        return Utils.getTileByIndex(startingLocation.getX(), startingLocation.getY(), game.grid);
    }

    public Texture getEyeTexture(){
        if(direction == null){
            return eyesUp;
        }
        switch(direction){
            case UP:
                eyeXOffset = 0;
                return eyesDown ;
            case DOWN:
                eyeXOffset = 0;
                return eyesUp;
            case LEFT:
                eyeXOffset = -1;
                return eyesLeft;
            case RIGHT:
                eyeXOffset = 1;
                return eyesRight;
            default:
                return eyesUp;
        }
    }

    @Override
    public Animation getMovingAnimation(){
        if(game.stage == PacMan.Stage.FRIGHTENED){
            movingFrames[0] = new TextureRegion(frightened0);
            movingFrames[1] = new TextureRegion(frightened1);
        } else{
            movingFrames[0] = new TextureRegion(moving0);
            movingFrames[1] = new TextureRegion(moving1);
        }
        return new Animation(0.2f, movingFrames);
    }

    public Animation getCurrentAnimation(){
        if(fearEnding){
            return getFrightenedExitAnimation();
        } else{
            return getMovingAnimation();
        }
    }

    public Animation getFrightenedExitAnimation() {
        Texture texture0 = new Texture(Gdx.files.internal("sprites/ghosts/f-1.png"));
        Texture texture1 = new Texture(Gdx.files.internal("sprites/ghosts/f-2.png"));

        movingFrames[0] = new TextureRegion(texture0);
        movingFrames[1] = new TextureRegion(texture1);

        return new Animation(0.2f, movingFrames);
    }


    public void turnAround(){
        if(direction == null){
            return;
        }
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

    public void die(){
        deathLocation = Utils.getPositionByIndex(getColumn(), getRow(), game.tileWidth, game.tileHeight);
        isDying = true;
        isVisible = false;
        showPoints = true;
        deathSound.play();
        game.player.increaseScore(200);
        speed = 2f;
        recalculatePath();

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Gdx.app.postRunnable(() -> {
                    setPosition(getPositionByIndex(startingLocation.getX(), startingLocation.getY(), game.tileWidth, game.tileHeight));
                    isDying = false;
                    isVisible = true;
                    showPoints = false;
                    speed = 0.75f;
                });
            }
        }, (long) (pointsDisplayTime * 1000));
    }
}