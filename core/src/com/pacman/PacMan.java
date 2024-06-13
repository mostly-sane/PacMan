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
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.pacman.AI.PathDrawer;
import com.pacman.Characters.*;
import com.pacman.Characters.Character;
import com.pacman.Components.AnimationComponent;
import com.pacman.Components.CollisionComponent;
import com.pacman.Components.PlayerController;
import com.pacman.Map.LevelManager;
import com.pacman.Map.StageManager;
import com.pacman.Map.Tile;
import com.pacman.Map.Pill;
import com.badlogic.gdx.graphics.Color;


import java.util.*;

public class PacMan extends ApplicationAdapter {
	private FrameBuffer frameBuffer;
	private TextureRegion textureRegion;
	private Texture titleScreenTexture;
	private Animation<TextureRegion> pacmanAnimation;
	private Animation<TextureRegion> ghostAnimation;
	private float stateTime;
	public Sound wakaWakaSound;
	public Sound deathSound;
	public Sound ghostSound;
	public Sound scaredSound;
	public Sound startupSound;
	public ArrayList<Pair<Stage, Double>> stageTimes = new ArrayList<>();
	public int totalPills = 0;

	float elapsedTime = 0;

	private OrthographicCamera camera;
	private SpriteBatch batch;
	private PlayerController controller;
	public Player player;

	private int playerWidth;
	private int playerHeight;

	public Tile[][] grid;
	Pill[][] pillGrid;

	private Texture point200;

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

private Texture ghostEyeTexture;

	ShapeRenderer shapeRenderer;
	PathDrawer pathDrawer = new PathDrawer();

	StageManager stageManager;

	public void activatePowerMode() {
		stageManager.activatePowerMode(this);
	}

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

		ghostEyeTexture = new Texture(Gdx.files.internal("sprites/eyes/l.png"));
		titleScreenTexture = new Texture(Gdx.files.internal("sprites/ui/ready.png"));
		TextureRegion[] pacmanFrames = new TextureRegion[3];
		pacmanFrames[0] = new TextureRegion(new Texture(Gdx.files.internal("sprites/pacman/0.png")));
		pacmanFrames[1] = new TextureRegion(new Texture(Gdx.files.internal("sprites/pacman/1.png")));
		pacmanFrames[2] = new TextureRegion(new Texture(Gdx.files.internal("sprites/pacman/2.png")));
		pacmanAnimation = new Animation<>(0.3f, pacmanFrames);
		stateTime = 0f;

		TextureRegion[] ghostFrames = new TextureRegion[2];
		ghostFrames[0] = new TextureRegion(new Texture(Gdx.files.internal("sprites/ghosts/b-0.png")));
		ghostFrames[1] = new TextureRegion(new Texture(Gdx.files.internal("sprites/ghosts/b-1.png")));
		ghostAnimation = new Animation<>(0.5f, ghostFrames);

		stateTime = 0f;

		font = new BitmapFont();
		camera = new OrthographicCamera();
		camera.setToOrtho(false, appW, appH);
		batch = new SpriteBatch();

		point200 = new Texture(Gdx.files.internal("sprites/ui/200.png"));

		wakaWakaSound = Gdx.audio.newSound(Gdx.files.internal("sprites/Sounds/wakwaka.mp3"));
		deathSound = Gdx.audio.newSound(Gdx.files.internal("sprites/Sounds/death.mp3"));
		startupSound = Gdx.audio.newSound(Gdx.files.internal("sprites/Sounds/startup.mp3"));
	}

	private void initializeGame() {
		initializeLevel();
		initializePlayer();
		initializeGhosts();

		ghostSound = Gdx.audio.newSound(Gdx.files.internal("sprites/Sounds/ghost.mp3"));
		scaredSound = Gdx.audio.newSound(Gdx.files.internal("sprites/Sounds/scared.mp3"));
		ghostSound.loop();
		ghostSound.setVolume((long) 1.5, 1.5f);

		redrawGrid();

		startupSound.play();
		player.controller.canMove = false;
		for(Ghost ghost : ghosts){
			ghost.canMove = false;
		}

		Timer startupTimer = new Timer();
		startupTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				player.controller.canMove = true;
				for(Ghost ghost : ghosts){
					ghost.canMove = true;
				}
				elapsedTime = 0;
				initializeStages();
			}
		}, 4500);
	}


	private void initializeGame1() {
		initializeLevel1();
		initializePlayerLvl1();
		initializeGhosts();

		ghostSound = Gdx.audio.newSound(Gdx.files.internal("sprites/Sounds/ghost.mp3"));
		scaredSound = Gdx.audio.newSound(Gdx.files.internal("sprites/Sounds/scared.mp3"));
		ghostSound.loop();
		ghostSound.setVolume((long) 1.5, 1.5f);

		redrawGrid();

		startupSound.play();
		player.controller.canMove = false;
		for(Ghost ghost : ghosts){
			ghost.canMove = false;
		}

		Timer startupTimer = new Timer();
		startupTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				player.controller.canMove = true;
				for(Ghost ghost : ghosts){
					ghost.canMove = true;
				}
				elapsedTime = 0;
				initializeStages();
			}
		}, 4500);
	}

	private void initializeGhosts() {
		Blinky Blinky = new Blinky(20, 20, new Texture(Gdx.files.internal("sprites/ghosts/f-3.png")), this, new Pair<>(17, 1));
		Blinky.setDirection(Character.Direction.LEFT);
		ghosts[0] = Blinky;

		Blinky.recalculatePath();

		Pinky Pinky = new Pinky(20, 20, new Texture(Gdx.files.internal("sprites/ghosts/f-2.png")), this, new Pair<>(1, 1));
		ghosts[1] = Pinky;

		Pinky.recalculatePath();

		Inky Inky = new Inky(20, 20, new Texture(Gdx.files.internal("sprites/ghosts/f-1.png")), this, new Pair<>(17, 19));
		ghosts[2] = Inky;
		Inky.setDirection(Character.Direction.LEFT);

		Inky.recalculatePath();

		Clyde Clyde = new Clyde(20, 20, new Texture(Gdx.files.internal("sprites/ghosts/y-1.png")), this, new Pair<>(1, 19));
		ghosts[3] = Clyde;

		Clyde.recalculatePath();

		for(Ghost ghost : ghosts){
			ghost.setAnimationComponent(new AnimationComponent(ghost));
		}
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

		for(int i = 0; i < rows; i++){
			for(int j = 0; j < columns; j++){
				if(pillGrid[i][j].texture != null){
					totalPills++;
				}
			}
		}
	}

	private void initializeLevel1() {
		frameBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
		textureRegion = new TextureRegion(frameBuffer.getColorBufferTexture());
		textureRegion.flip(false, true);

		Gdx.graphics.setContinuousRendering(true);
		grid = LevelManager.loadLevel(Gdx.files.internal("levels/level1.txt").file());
		pillGrid = LevelManager.loadPills(Gdx.files.internal("levels/pillslvl1.txt").file());

		levelParams = LevelManager.getLevelParams(Gdx.files.internal("levels/level1.txt").file());
		String[] parts = levelParams.split(",");
		rows = Integer.parseInt(parts[0]);
		columns = Integer.parseInt(parts[1]);

		for(int i = 0; i < rows; i++){
			for(int j = 0; j < columns; j++){
				if(pillGrid[i][j].texture != null){
					totalPills++;
				}
			}
		}
	}

	private void initializePlayerLvl1() {
		Texture pacmanTexture = new Texture(Gdx.files.internal("sprites/pacman/0.png"));
		player = new Player(20, 20, pacmanTexture, this);
		float tileWidth = this.tileWidth;  // Ensure tileWidth matches the actual tile size
		float tileHeight = this.tileHeight; // Ensure tileHeight matches the actual tile size

		CollisionComponent collisionComponent = new CollisionComponent(this, player);

		player.setCollisionComponent(collisionComponent);
		controller = new PlayerController(player, pillGrid, wakaWakaSound); // Pass pillGrid and wakaWakaSound here
		player.setController(controller);

		player.setAnimationComponent(new AnimationComponent(player));

		playerWidth = player.getWidth();
		playerHeight = player.getHeight();

		player.setPosition(player.getPositionByIndex(9, 8, this.tileWidth, this.tileHeight));

		Gdx.input.setInputProcessor(controller);
	}

	private void initializePlayer() {
		Texture pacmanTexture = new Texture(Gdx.files.internal("sprites/pacman/0.png"));
		player = new Player(20, 20, pacmanTexture, this);
		float tileWidth = this.tileWidth;  // Ensure tileWidth matches the actual tile size
		float tileHeight = this.tileHeight; // Ensure tileHeight matches the actual tile size

		CollisionComponent collisionComponent = new CollisionComponent(this, player);

		player.setCollisionComponent(collisionComponent);
		controller = new PlayerController(player, pillGrid, wakaWakaSound); // Pass pillGrid and wakaWakaSound here
		player.setController(controller);

		player.setAnimationComponent(new AnimationComponent(player));

		playerWidth = player.getWidth();
		playerHeight = player.getHeight();

		player.setPosition(player.getPositionByIndex(9, 11, this.tileWidth, this.tileHeight));

		Gdx.input.setInputProcessor(controller);
	}

	private void initializeStages(){
		LevelManager.loadStages(Gdx.files.internal("levels/default.txt").file(), this);
		stageManager = new StageManager(stageTimes);
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
		Skin skin = new Skin(Gdx.files.internal("sprites/skin/uiskin.json"));

		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();

		stateTime += Gdx.graphics.getDeltaTime();
		TextureRegion currentPacmanFrame = pacmanAnimation.getKeyFrame(stateTime, true);
		batch.draw(currentPacmanFrame, appW / 2 - 100, appH / 2 + 100);


		TextureRegion currentGhostFrame = ghostAnimation.getKeyFrame(stateTime, true);
		batch.draw(currentGhostFrame, appW / 2 + 100, appH / 2 + 100);
		batch.draw(ghostEyeTexture, appW / 2 + 100, appH / 2 + 100);
batch.end();
		com.badlogic.gdx.scenes.scene2d.Stage stage = new com.badlogic.gdx.scenes.scene2d.Stage();
		Gdx.input.setInputProcessor(stage);

		BitmapFont font = new BitmapFont();
		TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
		textButtonStyle.font = font;
		textButtonStyle.fontColor = Color.WHITE;

		TextButton level1Button = new TextButton("Level 1", skin);
		if (Gdx.input.isTouched()) {
			int x = Gdx.input.getX();
			int y = Gdx.input.getY();
			y = appH - y;

			if (x >= appW / 2 - level1Button.getWidth() / 2 && x <= appW / 2 + level1Button.getWidth() / 2
					&& y >= appH / 2 + 50 && y <= appH / 2 + 50 + level1Button.getHeight()) {

				gameState = GameState.PLAYING;
				titleScreenTexture.dispose();
				initializeGame1();
			}
		}

		TextButton level2Button = new TextButton("Level 2", skin);
		level2Button.addListener(new ClickListener() {
			@Override
			public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
				// TODO: Implement level 2 logic
			}
		});

		TextButton level3Button = new TextButton("Level 3", skin);
		if (Gdx.input.isTouched()) {
			int x = Gdx.input.getX();
			int y = Gdx.input.getY();
			y = appH - y; // Adjust the y-coordinate to match the screen coordinates

			if (x >= appW / 2 - level3Button.getWidth() / 2 && x <= appW / 2 + level3Button.getWidth() / 2
					&& y >= appH / 2 - 50 && y <= appH / 2 - 50 + level3Button.getHeight()) {
				// If the touch is within the level3Button bounds
				gameState = GameState.PLAYING;
				titleScreenTexture.dispose(); // Dispose of the title screen texture
				initializeGame();
			}
		}

		stage.addActor(level1Button);
		stage.addActor(level2Button);
		stage.addActor(level3Button);

		// Position the buttons
		level1Button.setPosition(appW / 2 - level1Button.getWidth() / 2, appH / 2 + 50);
		level2Button.setPosition(appW / 2 - level2Button.getWidth() / 2, appH / 2);
		level3Button.setPosition(appW / 2 - level3Button.getWidth() / 2, appH / 2 - 50);

		stage.draw();

		if (Gdx.input.isTouched()) {
			int x = Gdx.input.getX();
			int y = Gdx.input.getY();
			y = appH - y;
		}
		titleScreenTexture.dispose();
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
				//ghost.drawPath(shapeRenderer, pathDrawer);
			}
		}
		player.update();
		batch.begin();
		TextureRegion currentFrame = player.animationToPlay().getKeyFrame(elapsedTime, true);
		batch.draw(currentFrame, player.getPosition().getX(), player.getPosition().getY(),
				playerWidth / 2, playerHeight / 2, playerWidth, playerHeight,
				1, 1, player.animationComponent.rotation);
		drawPills();
		batch.end();

		batch.begin();
		for (Ghost ghost : ghosts) {
			if (ghost != null) {
				if (ghost.showPoints) {
					batch.draw(point200, ghost.getPosition().getX(), ghost.getPosition().getY(), playerWidth, playerHeight);
				} else if (ghost.isVisible) {
					TextureRegion currentGhostFrame = (TextureRegion) ghost.getMovingAnimation().getKeyFrame(stateTime, true);
					batch.draw(currentGhostFrame, ghost.getPosition().getX(), ghost.getPosition().getY(),
							playerWidth / 2, playerHeight / 2, playerWidth, playerHeight, 1, 1, 0);
					if (stage != Stage.FRIGHTENED) {
						batch.draw(ghost.getEyeTexture(), ghost.getPosition().getX() + ghost.eyeXOffset, ghost.getPosition().getY() + 1,
								playerWidth, playerHeight);
					}
				}
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
		//titleScreenTexture.dispose();
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
		point200.dispose();
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
				float rotation = 180;

				Texture texture = grid[i][j].texture;
				TextureRegion textureRegion = new TextureRegion(texture);

				batch.begin();
				batch.draw(textureRegion, x, y, originX, originY, width, height, scaleX, scaleY, rotation);
				batch.end();
				frameBuffer.end();
			}
		}
	}

	public void playerDeath(){
		if (stage == Stage.CHASE) {
			stateTime = 0;
			player.isDying = true;
			player.controller.canMove = false;
			ghostSound.stop();
			for(Ghost ghost : ghosts){
				ghost.canMove = false;
			}
			deathSound.play();
			player.lives--;
			player.score = 0;
			Timer timer = new Timer();
			timer.schedule(new TimerTask() {
				@Override
				public void run() {
					restartLevel();
				}
			}, 2000);
		}
	}

	private void restartLevel() {
		Gdx.app.postRunnable(new Runnable() {
			@Override
			public void run() {
				initializeLevel();
				initializePlayer();
				initializeGhosts();
				initializeStages();
				redrawGrid();
			}
		});
	}

	public void win(){
		player.controller.canMove = false;
		Sound winSound = Gdx.audio.newSound(Gdx.files.internal("sprites/Sounds/win.mp3"));
		winSound.play();
	}
}
