package com.pacman.Characters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.pacman.Map.Tile;
import com.pacman.PacMan;
import com.pacman.Pair;
import com.pacman.Utils;

public class Blinky extends Ghost{
    public Blinky(int width, int height, Texture texture, PacMan game, Pair<Integer, Integer> startingLocation) {
        super(width, height, texture, game, startingLocation);
        moving0 = new Texture(Gdx.files.internal("sprites/ghosts/r-0.png"));
        moving1 = new Texture(Gdx.files.internal("sprites/ghosts/r-1.png"));
    }

    @Override
    public Tile getChaseTile(){
        return Utils.getCurrentTile(game.player, game.grid);
    }
}
