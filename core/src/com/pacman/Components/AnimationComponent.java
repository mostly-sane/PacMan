package com.pacman.Components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.pacman.Characters.Player;

public class AnimationComponent {
    private Player player;
    private TextureRegion[] idleFrames = new TextureRegion[3];
    private float rotation = 0;

    public AnimationComponent(Player player){
        this.player = player;
    }

    public Animation getCurrentAnimation(){
        if (player == null) {
            throw new IllegalStateException("Player is not initialized");
        }

        Texture texture0 = new Texture(Gdx.files.internal("sprites/pacman/0.png"));
        Texture texture1 = new Texture(Gdx.files.internal("sprites/pacman/1.png"));
        Texture texture2 = new Texture(Gdx.files.internal("sprites/pacman/2.png"));

        idleFrames[0] = new TextureRegion(texture0);
        idleFrames[1] = new TextureRegion(texture1);
        idleFrames[2] = new TextureRegion(texture2);

        switch (player.getDirection()){
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

        return new Animation(0.05f, idleFrames);
    }

    public float getRotation(){
        return rotation;
    }
}