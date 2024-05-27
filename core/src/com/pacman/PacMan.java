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
	private PacManController controller;

	public Tile currentTile;
	Tile[][] grid;

	String levelParams;
	int rows;
	int columns;

	int appW = 500;
	int appH = 560;
	int w = 500/25;
	int h = 560/28;

	Texture test;

	public void create(){
		//LevelManager.generateLevel("levels/default.txt");
		grid = LevelManager.loadLevel(Gdx.files.internal("levels/default.txt").file());

		levelParams = LevelManager.getLevelParams(Gdx.files.internal("levels/default.txt").file());
		String[] parts = levelParams.split(",");
		rows = Integer.parseInt(parts[0]);
		columns = Integer.parseInt(parts[1]);

		//w = appW / rows;
		//h = appH / columns;

		test = new Texture(Gdx.files.internal("test.png"));
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 500, 560);
		batch = new SpriteBatch();

		controller = new PacManController(this);
		Gdx.input.setInputProcessor(controller);
	}

	@Override
	public void render(){
		ScreenUtils.clear(0, 0, 0.2f, 1);
		camera.update();
		batch.setProjectionMatrix(camera.combined);

		redrawGrid();

	}

	public void dispose(){

	}

	public void redrawGrid(){
		batch.begin();
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				batch.draw(grid[i][j].texture, grid[i][j].x, grid[i][j].y, w, h);
			}
		}
		batch.end();
	}
}
