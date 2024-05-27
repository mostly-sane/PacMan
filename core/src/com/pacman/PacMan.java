package com.pacman;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;


public class PacMan extends ApplicationAdapter {

	private FrameBuffer frameBuffer;
	private TextureRegion textureRegion;

	private OrthographicCamera camera;
	private SpriteBatch batch;
	private PacManController controller;

	public Tile currentTile;
	Tile[][] grid;

	String levelParams;
	int rows;
	int columns;

	int appW = 475;
	int appH = 525;
	int w = appW/19;
	int h = appH/21;

	Texture test;

	boolean shouldDrawGrid = true;

	public void create(){
		super.create();
		frameBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
		textureRegion = new TextureRegion(frameBuffer.getColorBufferTexture());
		textureRegion.flip(false, true);

		Gdx.graphics.setContinuousRendering(false);
		//Gdx.graphics.requestRendering();
		//LevelManager.generateLevel("levels/default.txt");
		grid = LevelManager.loadLevel(Gdx.files.internal("levels/default.txt").file());

		levelParams = LevelManager.getLevelParams(Gdx.files.internal("levels/default.txt").file());
		String[] parts = levelParams.split(",");
		rows = Integer.parseInt(parts[0]);
		columns = Integer.parseInt(parts[1]);

		camera = new OrthographicCamera();
		camera.setToOrtho(true, appW, appH);
		batch = new SpriteBatch();

		controller = new PacManController(this);
		Gdx.input.setInputProcessor(controller);

		redrawGrid();
		Gdx.graphics.requestRendering();
	}

	@Override
	public void render(){
		camera.update();
		batch.setProjectionMatrix(camera.combined);

		Gdx.gl.glClear(Gdx.gl.GL_COLOR_BUFFER_BIT);
		batch.begin();
		batch.draw(textureRegion, 0, 0);
		batch.end();

		String fps = "FPS: " + Gdx.graphics.getFramesPerSecond();
		System.out.println(fps);

//		if(shouldDrawGrid){
//			//ScreenUtils.clear(0, 0, 0.2f, 1);
//			redrawGrid();
//		}
	}

	public void dispose(){

	}

	public void resize(int width, int height) {
		appW = width;
		appH = height;
		w = appW/19;
		h = appH/21;
		shouldDrawGrid = true;
	}

	public void redrawGrid(){
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				frameBuffer.begin();
				// Save the current sprite's position and dimensions
				float x = grid[i][j].x;
				float y = grid[i][j].y;
				float originX = w/2;
				float originY = h/2;
				float width = w;
				float height = h;
				float scaleX = 1;
				float scaleY = 1;
				float rotation = 180; // Rotate 180 degrees

				Texture texture = grid[i][j].texture;
				TextureRegion textureRegion = new TextureRegion(texture);

				batch.begin();
				// Draw the sprite with rotation
				batch.draw(textureRegion, x, y, originX, originY, width, height, scaleX, scaleY, rotation);
				batch.end();
			}
		}
		frameBuffer.end();
		//shouldDrawGrid = false;
	}
}
