package com.pacman;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.pacman.Characters.Ghost;
import com.pacman.Characters.Player;
import com.pacman.Components.AnimationComponent;
import com.pacman.Components.CollisionComponent;
import com.pacman.Components.PlayerController;
import com.pacman.Map.LevelManager;
import com.pacman.Map.Tile;
import com.pacman.Map.Pill;

import java.util.Timer;
import java.util.TimerTask;

public class PacMan extends ApplicationAdapter {
	private FrameBuffer frameBuffer;
	private TextureRegion textureRegion;

	float elapsedTime = 0;
	TextureRegion[] frames = new TextureRegion[3];
	Animation testAnimation;

	private OrthographicCamera camera;
	private SpriteBatch batch;
	private PlayerController controller;
	private AnimationComponent animationComponent;
	public Player player;

	public Ghost testGhost;

	private int playerWidth;
	private int playerHeight;

	public Tile[][] grid;
	Pill[][] pillGrid;

	String levelParams;
	int rows;
	int columns;

	int appW = 475;
	int appH = 525;
	public int w = appW/19;
	public int h = appH/21;

	boolean shouldDrawGrid = true;

	public void create(){
		super.create();

		initializeLevel();

		initializePlayer();

		testGhost = new Ghost(20, 20, new Texture(Gdx.files.internal("sprites/ghosts/f-3.png")), this);
		testGhost.setPosition(Player.getPositionByIndex(14, 15, w, h));

//		Timer timer = new Timer();
//		TimerTask task = new TimerTask() {
//			@Override
//			public void run() {
//				System.out.println("Recalculating path");
//				testGhost.recalculatePath();
//			}
//		};

		// Schedule the task to run every 1 second
		//timer.scheduleAtFixedRate(task, 0, 1000);

		redrawGrid();
	}

	private void initializeLevel() {
		frameBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
		textureRegion = new TextureRegion(frameBuffer.getColorBufferTexture());
		textureRegion.flip(false, true);

		Gdx.graphics.setContinuousRendering(true);
		grid = LevelManager.loadLevel(Gdx.files.internal("levels/default.txt").file());
		pillGrid = LevelManager.loadPills(Gdx.files.internal("levels/pills.txt").file());

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
		player = new Player(20, 20, pacmantexture, this);
		CollisionComponent collisionComponent = new CollisionComponent(grid);

		player.setCollisionComponent(collisionComponent);
		controller = new PlayerController(player);
		player.setController(controller);

		animationComponent = new AnimationComponent(player);
		player.setAnimationComponent(animationComponent);

		playerWidth = player.getWidth();
		playerHeight = player.getHeight();

		player.setPosition(Player.getPositionByIndex(1, 1, w, h));

		Gdx.input.setInputProcessor(controller);
	}

	@Override
	public void render(){
		elapsedTime += Gdx.graphics.getDeltaTime();
		camera.update();
		batch.setProjectionMatrix(camera.combined);

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(Gdx.gl.GL_COLOR_BUFFER_BIT);
		batch.begin();
		batch.draw(textureRegion, 0, 0);
		batch.end();
		controller.update();
		//testGhost.update();

		batch.begin();
		TextureRegion currentFrame = (TextureRegion) animationComponent.getCurrentAnimation()
				.getKeyFrame(elapsedTime, true);
		batch.draw(currentFrame, player.getPosition().getX(), player.getPosition().getY(),
				playerWidth / 2, playerHeight / 2, playerWidth, playerHeight,
				1, 1, animationComponent.getRotation());
		drawPills();
		batch.end();

		batch.begin();
		batch.draw(testGhost.getTexture(), testGhost.getPosition().getX(), testGhost.getPosition().getY(),
				playerWidth / 2, playerHeight / 2, playerWidth, playerHeight);
		batch.end();


		//movingObject.update();
	}

	private void drawPills() {
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				if (pillGrid[i][j].active) {
					batch.draw(pillGrid[i][j].texture, pillGrid[i][j].x, pillGrid[i][j].y,
							pillGrid[i][j].width, pillGrid[i][j].height);
				}
			}
		}
	}

	public void dispose(){
		batch.dispose();
		frameBuffer.dispose();
		for (Tile[] row : grid) {
			for (Tile tile : row) {
				tile.texture.dispose();
			}
		}
		for (Pill[] row : pillGrid) {
			for (Pill pill : row) {
				pill.texture.dispose();
			}
		}
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

	public void collectPill(int i, int j){
		pillGrid[i][j].active = false;
		pillGrid[i][j].setTexture(false);
	}
}
