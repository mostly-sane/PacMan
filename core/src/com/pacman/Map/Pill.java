package com.pacman.Map;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

public class Pill extends Rectangle {
    public boolean active;
    public Texture texture;

    public Pill(int x, int y, int width, int height, boolean active) {
        super(x, y, width, height);
        this.active = active;
        setTexture(active);
    }

    public void setTexture(boolean active) {
        if (active) {
            texture = new Texture("sprites/map/pill.png");
        } else {

        }
    }
}
