package com.pacman.Characters;

import com.badlogic.gdx.graphics.Texture;
import com.pacman.Map.Tile;
import com.pacman.PacMan;
import com.pacman.Pair;
import com.pacman.Utils;

public class Inky extends Ghost{
    public Inky(int width, int height, Texture texture, PacMan game, Pair<Integer, Integer> startingLocation) {
        super(width, height, texture, game, startingLocation);
        moving0 = new Texture("sprites/ghosts/b-0.png");
        moving1 = new Texture("sprites/ghosts/b-1.png");
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
