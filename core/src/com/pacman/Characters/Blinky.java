package com.pacman.Characters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.pacman.PacMan;

public class Blinky extends Ghost{
    public Blinky(int width, int height, Texture texture, PacMan game, Name name) {
        super(width, height, texture, game, name);
    }

    @Override
    public Animation getMovingAnimation(){
        Texture texture0 = new Texture(Gdx.files.internal("sprites/ghosts/r-0.png"));
        Texture texture1 = new Texture(Gdx.files.internal("sprites/ghosts/r-1.png"));

        movingFrames[0] = new TextureRegion(texture0);
        movingFrames[1] = new TextureRegion(texture1);

        return new Animation(0.2f, movingFrames);
    }
}
