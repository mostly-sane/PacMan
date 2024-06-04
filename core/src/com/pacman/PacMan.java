package com.pacman;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
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
import com.pacman.Map.StageManager;
import com.pacman.Map.Tile;
import com.pacman.Map.Pill;

import java.util.*;

public class PacMan extends ApplicationAdapter {
	private FrameBuffer frameBuffer;
	private TextureRegion textureRegion;

	//public HashMap<Stage, Double> stageTimes = new HashMap<>();
	public ArrayList<Pair<Stage, Double>> stageTimes = new ArrayList<>();

	float elapsedTime = 0;

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
	public int tileWidth = appW/19;
	public int tileHeight = appH/21;

	boolean shouldDrawGrid = true;

	private BitmapFont font;

	public enum Stage {
		CHASE,
		SCATTER,
		FRIGHTENED
	}

	public Stage stage = Stage.CHASE;

	public void create() {
		super.create();

		initializeLevel();
		initializePlayer();
		initializeStages();

		testGhost = new Ghost(20, 20, new Texture(Gdx.files.internal("sprites/ghosts/f-3.png")), this, Ghost.Name.PINKY);
		testGhost.setPosition(Utils.getPositionByIndex(14, 15, tileWidth, tileHeight));

		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				try {
					testGhost.recalculatePath();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}, 5, 300);


		redrawGrid();

		font = new BitmapFont();
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
		Texture pacmanTexture = new Texture(Gdx.files.internal("sprites/pacman/0.png"));
		player = new Player(20, 20, pacmanTexture, this);
		float tileWidth = this.tileWidth;  // Ensure tileWidth matches the actual tile size
		float tileHeight = this.tileHeight; // Ensure tileHeight matches the actual tile size

		CollisionComponent collisionComponent = new CollisionComponent(grid, tileWidth, tileHeight);

		player.setCollisionComponent(collisionComponent);
		controller = new PlayerController(player, pillGrid); // Pass pillGrid here
		player.setController(controller);

		animationComponent = new AnimationComponent(player);
		player.setAnimationComponent(animationComponent);

		playerWidth = player.getWidth();
		playerHeight = player.getHeight();

		player.setPosition(player.getPositionByIndex(1, 1, this.tileWidth, this.tileHeight));

		Gdx.input.setInputProcessor(controller);
	}

	private void initializeStages(){
		LevelManager.loadStages(Gdx.files.internal("levels/default.txt").file(), this);
		StageManager stageManager = new StageManager(stageTimes);
		stageManager.start();
	}

	@Override
	public void render() {
		elapsedTime += Gdx.graphics.getDeltaTime();
		camera.update();
		batch.setProjectionMatrix(camera.combined);

		batch.begin();
		batch.draw(textureRegion, 0, 0);
		batch.end();
		controller.update();
		testGhost.update();

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

	public void dispose() {
		batch.dispose();
		frameBuffer.dispose();
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
		pillGrid[i][j].active = false;
		pillGrid[i][j].setTexture(false);
	}
}
