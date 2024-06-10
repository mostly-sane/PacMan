package com.pacman.Characters;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.pacman.Components.AnimationComponent;
import com.pacman.Components.PlayerController;
import com.pacman.PacMan;

public class Player extends Character {
    private int score = 0;
    PlayerController controller;
    AnimationComponent animationComponent;

    public Player(int width, int height, Texture texture, PacMan game) {
        super(width, height, texture, game);
    }

    public void setController(PlayerController controller) {
        this.controller = controller;
    }

    public void setAnimationComponent(AnimationComponent animationComponent) {
        this.animationComponent = animationComponent;
    }

    public void increaseScore(int amount) {
        score += amount;
    }

    public int getScore() {
        return score;
    }
}
