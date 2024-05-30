package com.pacman.Characters;
import com.badlogic.gdx.graphics.Texture;
import com.pacman.Components.AnimationComponent;
import com.pacman.Components.PlayerController;
import com.pacman.PacMan;

public class Player extends Character {
    PlayerController controller;
    AnimationComponent animationComponent;

    public Player(int width, int height, Texture texture, PacMan game) {
        super(width, height, texture, game);
    }

    public PlayerController getController() {
        return controller;
    }

    public void setController(PlayerController controller) {
        this.controller = controller;
    }

    public AnimationComponent getAnimationComponent() {
        return animationComponent;
    }

    public void setAnimationComponent(AnimationComponent animationComponent) {
        this.animationComponent = animationComponent;
    }
}
