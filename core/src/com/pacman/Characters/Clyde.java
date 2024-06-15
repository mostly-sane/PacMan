package com.pacman.Characters;

import com.badlogic.gdx.graphics.Texture;
import com.pacman.Map.Tile;
import com.pacman.PacMan;
import com.pacman.Pair;
import com.pacman.Utils;

public class Clyde extends Ghost{
    public Clyde(int width, int height, Texture texture, PacMan game, Pair<Integer, Integer> startingLocation) {
        super(width, height, texture, game, startingLocation);
        moving0 = new Texture("sprites/ghosts/y-0.png");
        moving1 = new Texture("sprites/ghosts/y-1.png");
    }

    @Override
    public Tile getChaseTile(){
        Tile result;
        Tile playerTile = Utils.getCurrentTile(game.player, game.grid);
        if(Utils.getDistance(Utils.getCurrentTile(this, game.grid), playerTile) > 8){
            result = playerTile;
        } else {
            result = getScatterTile();
        }
        return result;
    }
}
