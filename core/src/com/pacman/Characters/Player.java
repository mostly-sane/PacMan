package com.pacman.Characters;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.pacman.Components.PlayerController;
import com.pacman.PacMan;

public class Player extends Character {
    public int score = 0;
    public PlayerController controller;
    public Animation<TextureRegion> dyingAnimation;
    public Animation<TextureRegion> movingAnimation;
    public boolean isDying = false;

    public Player(int width, int height, Texture texture, PacMan game) {
        super(width, height, texture, game);
        movingFrames = new TextureRegion[3];
        createDyingAnimation();
        createMovingAnimation();
    }

    public void update(){
        super.update();
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

    public Animation<TextureRegion> animationToPlay(){
        if(isDying){
            return dyingAnimation;
        } else {
            return movingAnimation;
        }
    }

    private void createDyingAnimation(){
        Texture texture0 = new Texture("sprites/pacman/d-0.png");
        Texture texture1 = new Texture("sprites/pacman/d-1.png");
        Texture texture2 = new Texture("sprites/pacman/d-2.png");
        Texture texture3 = new Texture("sprites/pacman/d-3.png");
        Texture texture4 = new Texture("sprites/pacman/d-4.png");
        Texture texture5 = new Texture("sprites/pacman/d-5.png");
        Texture texture6 = new Texture("sprites/pacman/d-6.png");
        Texture texture7 = new Texture("sprites/pacman/d-7.png");
        Texture texture8 = new Texture("sprites/pacman/d-8.png");
        Texture texture9 = new Texture("sprites/pacman/d-9.png");
        Texture texture10 = new Texture("sprites/pacman/d-10.png");

        TextureRegion[] dyingFrames = new TextureRegion[11];

        dyingFrames[0] = new TextureRegion(texture0);
        dyingFrames[1] = new TextureRegion(texture1);
        dyingFrames[2] = new TextureRegion(texture2);
        dyingFrames[3] = new TextureRegion(texture3);
        dyingFrames[4] = new TextureRegion(texture4);
        dyingFrames[5] = new TextureRegion(texture5);
        dyingFrames[6] = new TextureRegion(texture6);
        dyingFrames[7] = new TextureRegion(texture7);
        dyingFrames[8] = new TextureRegion(texture8);
        dyingFrames[9] = new TextureRegion(texture9);
        dyingFrames[10] = new TextureRegion(texture10);

        dyingAnimation = new Animation(0.1f, dyingFrames);
        dyingAnimation.setPlayMode(Animation.PlayMode.NORMAL);
    }

    public void createMovingAnimation(){
        Texture texture0 = new Texture("sprites/pacman/0.png");
        Texture texture1 = new Texture("sprites/pacman/1.png");
        Texture texture2 = new Texture("sprites/pacman/2.png");

        movingFrames[0] = new TextureRegion(texture0);
        movingFrames[1] = new TextureRegion(texture1);
        movingFrames[2] = new TextureRegion(texture2);

        movingAnimation = new Animation(0.1f, movingFrames);
    }

    public void increaseLives() {
        if(game.playerLives < 3){
            game.playerLives++;
        }
    }
}
