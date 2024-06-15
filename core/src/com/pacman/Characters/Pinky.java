package com.pacman.Characters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.pacman.Map.Tile;
import com.pacman.PacMan;
import com.pacman.Pair;
import com.pacman.Utils;

public class Pinky extends Ghost{
    public Pinky(int width, int height, Texture texture, PacMan game, Pair<Integer, Integer> startingLocation) {
        super(width, height, texture, game, startingLocation);

        moving0 = new Texture("sprites/ghosts/p-0.png");
        moving1 = new Texture("sprites/ghosts/p-1.png");
    }

    @Override
    public Tile getChaseTile(){
        Player player = game.player;
        Tile playerTile = Utils.getCurrentTile(player, game.grid);
        Tile result;
        int playerRow = game.player.getColumn();
        int playerColumn = game.player.getRow();

        if(player.controller.isMoving){
            Tile playerFront = getAvailableTileWithOffset(playerRow, playerColumn, playerTile, playerTile, 4);
            if(playerFront == null){
                turnAround();
                recalculatePath();
            }
            result = playerFront;
        } else {
            result = playerTile;
        }
        return result;
    }
}
