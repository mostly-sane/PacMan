package com.pacman;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.pacman.Characters.Player;
import com.pacman.Components.CollisionComponent;
import com.pacman.Components.PlayerController;
import com.pacman.Map.LevelManager;
import com.pacman.Map.Tile;

public class PacMan extends ApplicationAdapter {
	private FrameBuffer frameBuffer;
	private TextureRegion textureRegion;

	private OrthographicCamera camera;
	private SpriteBatch batch;
	private PlayerController controller;
	private Player player;
	private int playerWidth;
	private int playerHeight;

	Tile[][] grid;

	String levelParams;
	int rows;
	int columns;

	int appW = 475;
	int appH = 525;
	int w = appW/19;
	int h = appH/21;

	boolean shouldDrawGrid = true;

	public void create(){
		super.create();

		initializeLevel();

		initializePlayer();

		redrawGrid();
	}

	private void initializeLevel() {
		frameBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
		textureRegion = new TextureRegion(frameBuffer.getColorBufferTexture());
		textureRegion.flip(false, true);

		Gdx.graphics.setContinuousRendering(true);
		grid = LevelManager.loadLevel(Gdx.files.internal("levels/default.txt").file());

		levelParams = LevelManager.getLevelParams(Gdx.files.internal("levels/default.txt").file());
		String[] parts = levelParams.split(",");
		rows = Integer.parseInt(parts[0]);
		columns = Integer.parseInt(parts[1]);

		camera = new OrthographicCamera();
		camera.setToOrtho(true, appW, appH);
		batch = new SpriteBatch();
	}

	private void initializePlayer() {
		Texture pacmantexture = new Texture(Gdx.files.internal("sprites/pacman/0.png"));
		player = new Player(20, 20, pacmantexture);
		CollisionComponent collisionComponent = new CollisionComponent(grid);

		player.setCollisionComponent(collisionComponent);
		controller = new PlayerController(player);
		player.setController(controller);

		playerWidth = player.getWidth();
		playerHeight = player.getHeight();

		player.setPosition(Player.getPositionByIndex(1, 1, w, h));

//		int middleIndex = columns / 2; ???

		Gdx.input.setInputProcessor(controller);
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
		batch.draw(player.getTexture(), player.getPosition().getX(), player.getPosition().getY(),
				playerWidth, playerHeight); // Draw Pacman
		batch.end();

		//movingObject.update();
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
