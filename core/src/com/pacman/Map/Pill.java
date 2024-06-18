package com.pacman.Map;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

public class Pill extends Rectangle {
    public static final int REGULAR_PILL = 1;
    public static final int POWER_PILL = 2;
    public static final int CHERRY = 3;
    public static final int HEAL = 4;

    public int type;
    public Texture texture;

    public Pill(int x, int y, int width, int height, int type) {
        super(x, y, width, height);
        this.type = type;
        setTexture();
    }

    public void setTexture() {
        String texturePath;
        switch (type) {
            case REGULAR_PILL:
                texturePath = "sprites/map/pill.png";
                break;
            case POWER_PILL:
                texturePath = "sprites/map/big-1.png";
                break;
            case CHERRY:
                texturePath = "sprites/fruits/cherry.png";
                break;
            case HEAL:
                texturePath = "sprites/ui/life.png";
                break;
            default:
                texturePath = null;
                break;
        }
        if (texturePath != null) {
            texture = new Texture(texturePath);
        } else {
            if (texture != null) {
                texture.dispose();
                texture = null;
            }
        }
    }
}

