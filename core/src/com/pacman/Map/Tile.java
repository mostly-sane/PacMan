package com.pacman.Map;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.pacman.PacMan;

import java.util.ArrayList;

public class Tile extends Rectangle {
    public int i;
    public int j;
    public boolean open;
    public Texture texture;
    private PacMan game;

    public Tile(int i, int j, int w, int h, boolean open, PacMan game) {
        super(i * w, j * h, w, h);
        this.i = i;
        this.j = j;
        this.open = open;
        this.game = game;
        setTexture(open);
    }

    public void setTexture(boolean open) {
        if (open) {
            texture = game.openTexture;
        } else {
            texture = game.closedTexture;
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
