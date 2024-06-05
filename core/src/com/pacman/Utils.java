package com.pacman;

import com.pacman.Map.Tile;
import com.pacman.Characters.Character;

public class Utils {
    public static Pair<Float, Float> getPositionByIndex(int i, int j, int w, int h) {
        return new Pair<>((float) i * w, (float) j * h);
    }

    public static Tile getCurrentTile(Character character, Tile[][] grid) {
        int i = (int) (character.getX() / grid[0][0].width);
        int j = (int) (character.getY() / grid[0][0].height);
        return grid[i][j];
    }

    public static Tile getTileByIndex(int i, int j, Tile[][] grid) {
        return grid[i][j];
    }

    public static int getDistance(Tile start, Tile end) {
        return Math.abs(start.i - end.i) + Math.abs(start.j - end.j);
    }
}
