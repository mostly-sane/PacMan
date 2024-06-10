package com.pacman.Characters;

import com.badlogic.gdx.graphics.Texture;
import com.pacman.Components.CollisionComponent;
import com.pacman.Map.Tile;
import com.pacman.PacMan;
import com.pacman.Pair;

public class Character {
    public enum Direction {
        UP,
        DOWN,
        LEFT,
        RIGHT
    }
    public PacMan game;
    protected int width;
    protected int height;
    protected CollisionComponent collisionComponent;
    protected Pair<Float, Float> position = new Pair<>(0f, 0f);
    protected Texture texture;
    protected Direction direction = Direction.LEFT;

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

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void setCollisionComponent(CollisionComponent collisionComponent) {
        this.collisionComponent = collisionComponent;
    }

    public int getRow() {
        return (int) (position.getY() / game.tileHeight);
    }

    public int getColumn() {
        return (int) (position.getX() / game.tileWidth);
    }

    public Pair<Float, Float> getPositionByIndex(int i, int j, int w, int h) {
        return new Pair<>((float) i * w, (float) j * h);
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }
}