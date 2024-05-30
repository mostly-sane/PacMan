package com.pacman.Map;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import java.util.ArrayList;

public class Tile extends Rectangle {
    public int i = 0;
    public int j = 0;
    public boolean open;
    public double f = 0;
    public double g = 0;
    public double h = 0;
    public Texture texture;
    public Tile cameFrom = null;
    public Rectangle rect;

    Tile(int i, int j, int w, int h, boolean open){
        super(i * w, j * h, w, h);
        this.i = i;
        this.j = j;
        this.open = open;
        setTexture(open);
    }

    public ArrayList<Tile> getNeighbors(Tile[][] grid){
        ArrayList<Tile> result = new ArrayList<>();
        int i = this.i;
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

    public void setTexture(boolean open){
       if(open){
           texture = new Texture("open.png");
       } else {
           texture = new Texture("blocked.png");
           this.rect = new Rectangle(x, y, 20, 20);
       }
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Tile && ((Tile) obj).i == i && ((Tile) obj).j == j;
    }

    @Override
    public String toString() {
        return i + "," + j;
    }
}