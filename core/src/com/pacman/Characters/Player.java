package com.pacman.Characters;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.pacman.Components.PlayerController;
import com.pacman.PacMan;

public class Player extends Character {
    public int score = 0;
    PlayerController controller;
    public int lives = 3;

    public Player(int width, int height, Texture texture, PacMan game) {
        super(width, height, texture, game);
        movingFrames = new TextureRegion[3];
    }

    public void update(){
        super.update();
        System.out.println(score);
    }

    public void setController(PlayerController controller) {
        this.controller = controller;
    }

    public void increaseScore(int amount) {
        score += amount;
    }

    public int getScore() {
        return score;
    }

    @Override
    public Animation getMovingAnimation(){
        Texture texture0 = new Texture("sprites/pacman/0.png");
        Texture texture1 = new Texture("sprites/pacman/1.png");
        Texture texture2 = new Texture("sprites/pacman/2.png");

        movingFrames[0] = new TextureRegion(texture0);
        movingFrames[1] = new TextureRegion(texture1);
        movingFrames[2] = new TextureRegion(texture2);

        return new Animation(0.1f, movingFrames);
    }
}
