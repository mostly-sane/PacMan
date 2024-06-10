package com.pacman.Components;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.pacman.Characters.Character;


public class AnimationComponent {
    private Character character;
    private TextureRegion[] idleFrames = new TextureRegion[3];
    public float rotation = 0;

    public AnimationComponent(Character character){
        this.character = character;
    }

    public void updateRotation(){
        if (character == null) {
            throw new IllegalStateException("Player is not initialized");
        }

        switch (character.getDirection()){
            case UP:
                rotation = 90;
                break;
            case DOWN:
                rotation = 270;
                break;
            case LEFT:
                rotation = 0;
                break;
            case RIGHT:
                rotation = 180;
                break;
        }
    }
}