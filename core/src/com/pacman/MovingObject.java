package com.pacman;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

public class MovingObject extends Rectangle {
    Texture texture;
    float dx, dy;

    public MovingObject(float x, float y, float width, float height, String texturePath, float dx, float dy) {
        super(x, y, width, height);
        this.texture = new Texture(texturePath);
        this.dx = dx;
        this.dy = dy;
    }

    public void update() {
        x += dx;
        y += dy;
    }
}