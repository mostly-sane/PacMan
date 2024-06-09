package com.pacman.Map;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import java.util.ArrayList;

public class Tile extends Rectangle {
    public int i = 0;
    public int j = 0;
    public boolean open;
    public Texture texture;

    public Tile(int i, int j, int w, int h, boolean open) {
        super(i * w, j * h, w, h);
        this.i = i;
        this.j = j;
        this.open = open;
        setTexture(open);
    }

    public void setTexture(boolean open) {
        if (open) {
            texture = new Texture("open.png");
        } else {
            texture = new Texture("blocked.png");
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
