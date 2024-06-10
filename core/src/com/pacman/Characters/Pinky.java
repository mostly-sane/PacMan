package com.pacman.Characters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.pacman.PacMan;

public class Pinky extends Ghost{

    public Pinky(int width, int height, Texture texture, PacMan game, Name name) {
        super(width, height, texture, game, name);
    }

    @Override
    public Animation getMovingAnimation(){
        if(game.stage == PacMan.Stage.FRIGHTENED){
            Texture texture0 = new Texture(Gdx.files.internal("sprites/ghosts/f-0.png"));
            Texture texture1 = new Texture(Gdx.files.internal("sprites/ghosts/f-1.png"));

            movingFrames[0] = new TextureRegion(texture0);
            movingFrames[1] = new TextureRegion(texture1);
        } else{
            Texture texture0 = new Texture(Gdx.files.internal("sprites/ghosts/p-0.png"));
            Texture texture1 = new Texture(Gdx.files.internal("sprites/ghosts/p-1.png"));

            movingFrames[0] = new TextureRegion(texture0);
            movingFrames[1] = new TextureRegion(texture1);
        }
        return new Animation(0.2f, movingFrames);
    }
}
