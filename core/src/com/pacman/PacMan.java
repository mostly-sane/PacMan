package com.pacman;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.ScreenUtils;

public class PacMan extends ApplicationAdapter {
	private OrthographicCamera camera;
	private SpriteBatch batch;

	int rows = 25;
	int columns = 28;
	int appW = 500;
	int appH = 560;
	int w = appW / rows;
	int h = appH / columns;

	Texture test;

	Tile[][] grid = new Tile[rows][columns];

	public void create(){
		test = new Texture(Gdx.files.internal("test.png"));
		camera = new OrthographicCamera();
		camera.setToOrtho(true, 500, 560);
		batch = new SpriteBatch();

		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				grid[i][j] = new Tile(i, j, w, h, true);
			}
		}
	}

	@Override
	public void render(){
		ScreenUtils.clear(0, 0, 0.2f, 1);
		camera.update();

		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				batch.draw(test, grid[i][j].x, grid[i][j].y, w, h);
			}
		}
		batch.end();
	}

	public void dispose(){

	}
}
