package com.pacman;
import com.badlogic.gdx.math.Rectangle;
import java.util.ArrayList;

public class Tile extends Rectangle {
    int i = 0;
    int j = 0;
    boolean open;
    double f = 0;
    double g = 0;
    double h = 0;

    Tile(int i, int j, int w, int h, boolean open){
        super(i * w, j * h, w, h);
        this.i = i;
        this.j = j;
        this.open = open;
    }

    public ArrayList<Tile> getNeighbors(Tile[][] grid){
        ArrayList<Tile> result = new ArrayList<>();
        int i = this.j;
        int j = this.j;
        if(i > 0){
            result.add(grid[i - 1][j]);
        }
        if(i < grid.length - 1){
            result.add(grid[i + 1][j]);
        }
        if(j > 0){
            result.add(grid[i][j - 1]);
        }
        if(j < grid[0].length - 1){
            result.add(grid[i][j + 1]);
        }
        return result;
    }
}
