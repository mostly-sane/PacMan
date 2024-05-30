package com.pacman;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;

import java.util.ArrayList;

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
	public CollisionChecker collisionChecker;
	boolean shouldDrawGrid = true;

	public MovingObject movingObject;
	private Texture pacmantexture;

	public void create(){
		super.create();
		frameBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
		textureRegion = new TextureRegion(frameBuffer.getColorBufferTexture());
		textureRegion.flip(false, true);

	movingObject = new MovingObject(0, 0, 50, 50, "badlogic.jpg", 2, 2);

		Gdx.graphics.setContinuousRendering(true);
		grid = LevelManager.loadLevel(Gdx.files.internal("levels/default.txt").file());

		levelParams = LevelManager.getLevelParams(Gdx.files.internal("levels/default.txt").file());
		String[] parts = levelParams.split(",");
		rows = Integer.parseInt(parts[0]);
		columns = Integer.parseInt(parts[1]);

		camera = new OrthographicCamera();
		camera.setToOrtho(true, appW, appH);
		batch = new SpriteBatch();

		pacmantexture = new Texture(Gdx.files.internal("sprites/pacman/0.png"));
		collisionChecker = new CollisionChecker(grid);
		controller = new PacManController(this, collisionChecker);
		int middleIndex = columns / 2;

		// Start from the bottom row (index 0), check if the tile at the middle index is open
		for (int i = middleIndex; i < rows || i >= 0; i += (i >= middleIndex ? 1 : -1)) {
			Tile tile = grid[5][i];
			if (tile.open) {
				// If it's open, set the Pacman's position to this tile's position
				movingObject.x = tile.x;
				movingObject.y = tile.y;
				break;
			}
		}

		controller = new PacManController(this, collisionChecker);
		Gdx.input.setInputProcessor(controller);
		redrawGrid();

		ArrayList<Tile> path = Utils.getShortestPath(grid, grid[1][1], grid[rows - 1][columns - 1]);
		System.out.println(path);
	}

	@Override
	public void render(){
		camera.update();
		batch.setProjectionMatrix(camera.combined);

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(Gdx.gl.GL_COLOR_BUFFER_BIT);
		batch.begin();
		batch.draw(textureRegion, 0, 0);
		batch.end();
		controller.update();
		//movingObject.update();
		batch.begin();
		batch.draw(pacmantexture, movingObject.x, movingObject.y, w, h); // Draw Pacman
		batch.end();

		//movingObject.update();

//		String fps = "FPS: " + Gdx.graphics.getFramesPerSecond();
//		System.out.println(fps);
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

	@Override
	public void pause() {
		System.out.println("Paused");
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
	}

	public void closeTile(int i, int j){
		grid[i][j].open = false;
		grid[i][j].setTexture(false);
		redrawGrid();
	}

	public void openTile(int i, int j){
		grid[i][j].open = true;
		grid[i][j].setTexture(true);
		redrawGrid();
	}
}
