package com.pacman.Characters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.pacman.Map.Tile;
import com.pacman.PacMan;
import com.pacman.Pair;
import com.pacman.Utils;

public class Inky extends Ghost{
    public Inky(int width, int height, Texture texture, PacMan game, Pair<Integer, Integer> startingLocation) {
        super(width, height, texture, game, startingLocation);
    }

    @Override
    public Animation getMovingAnimation(){
        if(game.stage == PacMan.Stage.FRIGHTENED){
            Texture texture0 = new Texture(Gdx.files.internal("sprites/ghosts/f-0.png"));
            Texture texture1 = new Texture(Gdx.files.internal("sprites/ghosts/f-1.png"));

            movingFrames[0] = new TextureRegion(texture0);
            movingFrames[1] = new TextureRegion(texture1);
        } else{
            Texture texture0 = new Texture(Gdx.files.internal("sprites/ghosts/b-0.png"));
            Texture texture1 = new Texture(Gdx.files.internal("sprites/ghosts/b-1.png"));

            movingFrames[0] = new TextureRegion(texture0);
            movingFrames[1] = new TextureRegion(texture1);
        }

        return new Animation(0.2f, movingFrames);
    }

    @Override
    public Tile getChaseTile(){
        Tile playerTile = Utils.getCurrentTile(game.player, game.grid);
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
}
