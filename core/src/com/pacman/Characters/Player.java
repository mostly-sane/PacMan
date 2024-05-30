package com.pacman.Characters;
import com.badlogic.gdx.graphics.Texture;
import com.pacman.Components.PlayerController;

public class Player extends Character {
    PlayerController controller;

    public Player(int width, int height, Texture texture) {
        super(width, height, texture);
    }

    public PlayerController getController() {
        return controller;
    }

    public void setController(PlayerController controller) {
        this.controller = controller;
    }
}
