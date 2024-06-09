package com.pacman;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.pacman.AI.PathDrawer;
import com.pacman.Characters.Ghost;
import com.pacman.Characters.Player;
import com.pacman.Components.AnimationComponent;
import com.pacman.Components.CollisionComponent;
import com.pacman.Components.PlayerController;
import com.pacman.Map.LevelManager;
import com.pacman.Map.StageManager;
import com.pacman.Map.Tile;
import com.pacman.Map.Pill;

import java.awt.*;
import java.util.*;

public class PacMan extends ApplicationAdapter {
	private FrameBuffer frameBuffer;
	private TextureRegion textureRegion;
	private Texture titleScreenTexture; // Title screen texture
	private Animation<TextureRegion> pacmanAnimation;
	private float stateTime;
public Sound wakaWakaSound;
	public ArrayList<Pair<Stage, Double>> stageTimes = new ArrayList<>();

	float elapsedTime = 0;

	private OrthographicCamera camera;
	private SpriteBatch batch;
	private PlayerController controller;
	private AnimationComponent animationComponent;
	public Player player;

	private int playerWidth;
	private int playerHeight;

	public Tile[][] grid;
	Pill[][] pillGrid;

	public Ghost[] ghosts = new Ghost[4];

	String levelParams;
	int rows;
	int columns;

	int appW = 475;
	int appH = 525;
	public int tileWidth = appW/19;
	public int tileHeight = appH/21;

	boolean shouldDrawGrid = true;

	private BitmapFont font;

	ShapeRenderer shapeRenderer;
	PathDrawer pathDrawer = new PathDrawer();

	public enum Stage {
		CHASE,
		SCATTER,
		FRIGHTENED
	}

	public Stage stage = Stage.CHASE;

	public enum GameState {
		TITLE_SCREEN,
		PLAYING
	}

	public GameState gameState = GameState.TITLE_SCREEN;

	public void create() {
		super.create();

		shapeRenderer = new ShapeRenderer();

		titleScreenTexture = new Texture(Gdx.files.internal("sprites/ui/ready.png")); // Initialize title screen texture

		TextureRegion[] pacmanFrames = new TextureRegion[3];
		pacmanFrames[0] = new TextureRegion(new Texture(Gdx.files.internal("sprites/pacman/0.png")));
		pacmanFrames[1] = new TextureRegion(new Texture(Gdx.files.internal("sprites/pacman/1.png")));
		pacmanFrames[2] = new TextureRegion(new Texture(Gdx.files.internal("sprites/pacman/2.png")));
		pacmanAnimation = new Animation<TextureRegion>(0.1f, pacmanFrames);
		stateTime = 0f;

		font = new BitmapFont();
		camera = new OrthographicCamera();
		camera.setToOrtho(false, appW, appH);
		batch = new SpriteBatch();

		wakaWakaSound = Gdx.audio.newSound(Gdx.files.internal("sprites/Sounds/wakwaka.mp3"));
	}

	private void initializeGame() {
		initializeLevel();
		initializePlayer();
		initializeStages();
		initializeGhosts();

		redrawGrid();
	}

	private void initializeGhosts() {
		Ghost Blinky = new Ghost(20, 20, new Texture(Gdx.files.internal("sprites/ghosts/f-3.png")), this, Ghost.Name.BLINKY);
		Blinky.setPosition(Utils.getPositionByIndex(17, 1, tileWidth, tileHeight));
		ghosts[0] = Blinky;

		Blinky.recalculatePath();

		Ghost Pinky = new Ghost(20, 20, new Texture(Gdx.files.internal("sprites/ghosts/f-2.png")), this, Ghost.Name.PINKY);
		Pinky.setPosition(Utils.getPositionByIndex(1, 1, tileWidth, tileHeight));
		ghosts[1] = Pinky;

		Pinky.recalculatePath();

		Ghost Inky = new Ghost(20, 20, new Texture(Gdx.files.internal("sprites/ghosts/f-1.png")), this, Ghost.Name.INKY);
		Inky.setPosition(Utils.getPositionByIndex(17, 19, tileWidth, tileHeight));
		ghosts[2] = Inky;

		Inky.recalculatePath();

		Ghost Clyde = new Ghost(20, 20, new Texture(Gdx.files.internal("sprites/ghosts/y-1.png")), this, Ghost.Name.CLYDE);
		Clyde.setPosition(Utils.getPositionByIndex(1, 19, tileWidth, tileHeight));
		ghosts[3] = Clyde;

		Clyde.recalculatePath();

//		Timer timer = new Timer();
//		timer.scheduleAtFixedRate(new TimerTask() {
//			@Override
//			public void run() {
//				try {
//					for(Ghost ghost : ghosts){
//						if(ghost != null){
//							ghost.recalculatePath();
//						}
//					}
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		}, 5, 500);
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
	}

	private void initializePlayer() {
		Texture pacmanTexture = new Texture(Gdx.files.internal("sprites/pacman/0.png"));
		player = new Player(20, 20, pacmanTexture, this);
		float tileWidth = this.tileWidth;  // Ensure tileWidth matches the actual tile size
		float tileHeight = this.tileHeight; // Ensure tileHeight matches the actual tile size

		CollisionComponent collisionComponent = new CollisionComponent(grid, tileWidth, tileHeight);

		player.setCollisionComponent(collisionComponent);
		controller = new PlayerController(player, pillGrid, wakaWakaSound); // Pass pillGrid and wakaWakaSound here
		player.setController(controller);

		animationComponent = new AnimationComponent(player);
		player.setAnimationComponent(animationComponent);

		playerWidth = player.getWidth();
		playerHeight = player.getHeight();

		player.setPosition(player.getPositionByIndex(9, 11, this.tileWidth, this.tileHeight));

		Gdx.input.setInputProcessor(controller);
	}

	private void initializeStages(){
		LevelManager.loadStages(Gdx.files.internal("levels/default.txt").file(), this);
		StageManager stageManager = new StageManager(stageTimes);
		stageManager.start(this);
	}

	@Override
	public void render() {
		elapsedTime += Gdx.graphics.getDeltaTime();
		stateTime += Gdx.graphics.getDeltaTime();
		camera.update();
		batch.setProjectionMatrix(camera.combined);

		if (gameState == GameState.TITLE_SCREEN) {
			renderTitleScreen();
		} else if (gameState == GameState.PLAYING) {
			renderGame();
		}
	}

	private void renderTitleScreen() {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		batch.draw(titleScreenTexture, (appW - titleScreenTexture.getWidth()) / 2, (appH - titleScreenTexture.getHeight()) / 2);
		TextureRegion currentFrame = pacmanAnimation.getKeyFrame(stateTime, true);
		batch.draw(currentFrame, (appW - currentFrame.getRegionWidth()) / 2, (appH - titleScreenTexture.getHeight()) / 2 - currentFrame.getRegionHeight() - 10);
		batch.end();

		if (Gdx.input.isTouched()) {
			int x = Gdx.input.getX();
			int y = Gdx.input.getY();
			y = appH - y;

			if (x >= (appW - titleScreenTexture.getWidth()) / 2 && x <= (appW + titleScreenTexture.getWidth()) / 2
					&& y >= (appH - titleScreenTexture.getHeight()) / 2 && y <= (appH + titleScreenTexture.getHeight()) / 2) {
				gameState = GameState.PLAYING;
				initializeGame();
			}
		}
	}

	private void renderGame() {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		batch.draw(textureRegion, 0, 0);
		batch.end();
		controller.update();
		for(Ghost ghost : ghosts){
			if(ghost != null){
				ghost.update();
				ghost.drawPath(shapeRenderer, pathDrawer);
			}
		}

		batch.begin();
		TextureRegion currentFrame = (TextureRegion) animationComponent.getCurrentAnimation()
				.getKeyFrame(elapsedTime, true);
		batch.draw(currentFrame, player.getPosition().getX(), player.getPosition().getY(),
				playerWidth / 2, playerHeight / 2, playerWidth, playerHeight,
				1, 1, animationComponent.getRotation());
		drawPills();
		batch.end();

		batch.begin();
		for(int i = 0; i < ghosts.length; i++){
			if(ghosts[i] != null){
				batch.draw(ghosts[i].getTexture(), ghosts[i].getPosition().getX(), ghosts[i].getPosition().getY(),
						playerWidth / 2, playerHeight / 2, playerWidth, playerHeight);
			}
			//TODO ці перевірки це костиль. прибрати
		}
		batch.end();
	}

	private void drawPills() {
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				Pill pill = pillGrid[i][j];
				if (pill.texture != null) {
					batch.draw(pill.texture, pill.x, pill.y, pill.width, pill.height);
				}
			}
		}
	}

	public void dispose() {
		batch.dispose();
		frameBuffer.dispose();
		titleScreenTexture.dispose();
		for (Tile[] row : grid) {
			for (Tile tile : row) {
				if (tile.texture != null) {
					tile.texture.dispose();
				}
			}
		}
		for (Pill[] row : pillGrid) {
			for (Pill pill : row) {
				if (pill.texture != null) {
					pill.texture.dispose();
				}
			}
		}
		player.getTexture().dispose();
		font.dispose();
		wakaWakaSound.dispose();
	}

	public void resize(int width, int height) {
		appW = width;
		appH = height;
		tileWidth = appW / 19;
		tileHeight = appH / 21;
		shouldDrawGrid = true;
	}

	@Override
	public void pause() {
		System.out.println("Paused");
	}

	public void redrawGrid() {
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				frameBuffer.begin();
				// Save the current sprite's position and dimensions
				float x = grid[i][j].x;
				float y = grid[i][j].y;
				float originX = tileWidth / 2;
				float originY = tileHeight / 2;
				float width = tileWidth;
				float height = tileHeight;
				float scaleX = 1;
				float scaleY = 1;
				float rotation = 180; // Rotate 180 degrees

				Texture texture = grid[i][j].texture;
				TextureRegion textureRegion = new TextureRegion(texture);

				batch.begin();
				batch.draw(textureRegion, x, y, originX, originY, width, height, scaleX, scaleY, rotation);
				batch.end();
				frameBuffer.end();
			}
		}
	}

	public void closeTile(int i, int j) {
		grid[i][j].open = false;
		grid[i][j].setTexture(false);
		redrawGrid();
	}

	public void openTile(int i, int j) {
		grid[i][j].open = true;
		grid[i][j].setTexture(true);
		redrawGrid();
	}

	public void collectPill(int i, int j) {
		pillGrid[i][j].type = 0;
		pillGrid[i][j].setTexture();
	}
}
