package com.pacman.Characters;

import com.badlogic.gdx.graphics.Texture;
import com.pacman.Components.CollisionComponent;
import com.pacman.Map.Tile;
import com.pacman.PacMan;
import com.pacman.Pair;

public class Character {
    protected PacMan game;
    protected int width;
    protected int height;
    protected CollisionComponent collisionComponent;
    protected Pair<Float, Float> position = new Pair<>(0f, 0f);
    protected Texture texture;

    public Character(int width, int height, Texture texture, PacMan game) {
        this.width = width;
        this.height = height;
        this.texture = texture;
        this.game = game;
    }

    public CollisionComponent getCollisionComponent() {
        return collisionComponent;
    }

    public Pair<Float, Float> getPosition() {
        return position;
    }

    public void setPosition(Pair<Float, Float> position) {
        this.position = position;
    }

    public Float getX() {
        return position.getX();
    }

    public Float getY() {
        return position.getY();
    }

    public void setX(float x) {
        this.position.setX(x);
    }

    public void setY(float y) {
        this.position.setY(y);
    }

    public Texture getTexture() {
        return texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setCollisionComponent(CollisionComponent collisionComponent) {
        this.collisionComponent = collisionComponent;
    }

    public static Pair<Float, Float> getPositionByIndex(int i, int j, int w, int h) {
        return new Pair<>((float) i * w, (float) j * h);
    }

    protected Tile getCurrentTile() {
        int i = ((int) (position.getX() / game.w));
        int j = (int) (position.getY() / game.h);

        Tile tile = game.grid[i][j];
        return tile;
    }

    protected Pair<Integer, Integer> getTileByIndex(int x, int y) {
        return new Pair<>(x * width, y * height);
    }
}
