package com.pacman.Components;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;
import com.pacman.Characters.Character;
import com.pacman.Characters.Player;
import com.pacman.Map.Pill;

public class PlayerController extends InputAdapter {
    private Character.Direction desiredDirection = Character.Direction.LEFT;

    private Player player;
    private CollisionComponent collisionComponent;
    private final int MOVE_AMOUNT = 1;
    private Vector2 movementDirection = new Vector2();
    private Vector2 desiredMovementDirection = new Vector2();
    private Pill[][] pillGrid;
    public boolean isMoving = false;

    private Sound wakaWakaSound;

    public PlayerController(Player player, Pill[][] pillGrid, Sound wakaWakaSound) {
        this.player = player;
        this.collisionComponent = player.getCollisionComponent();
        this.pillGrid = pillGrid;
        this.wakaWakaSound = wakaWakaSound;
        movementDirection.set(-MOVE_AMOUNT, 0);
        desiredMovementDirection.set(-MOVE_AMOUNT, 0);
    }

    public void update() {
        isMoving = false;
        Vector2 oldPosition = new Vector2(player.getX(), player.getY());

        if (collisionComponent.canMove(new Vector2(player.getX(), player.getY()), desiredMovementDirection)) {
            movementDirection.set(desiredMovementDirection);
            player.setDirection(desiredDirection);
        }

        if (collisionComponent.canMove(oldPosition, movementDirection)) {
            oldPosition.add(movementDirection);
            isMoving = true;
        } else {
            alignWithGrid();
        }

        player.setX(oldPosition.x);
        player.setY(oldPosition.y);

        checkPillCollision();
    }

    private void alignWithGrid() {
        float x = player.getX();
        float y = player.getY();
        float w = player.getWidth();
        float h = player.getHeight();

        int gridX = Math.round(x / w) * (int) w;
        int gridY = Math.round(y / h) * (int) h;

        player.setX(gridX);
        player.setY(gridY);
    }

    public Vector2 getCollisionRectangleCenter() {
        float centerX = collisionComponent.pacManRect.x + collisionComponent.pacManRect.width / 2;
        float centerY = collisionComponent.pacManRect.y + collisionComponent.pacManRect.height / 2;
        return new Vector2(centerX, centerY);
    }

    private void checkPillCollision() {
        Vector2 playerCenter = getCollisionRectangleCenter();
        for (int i = 0; i < pillGrid.length; i++) {
            for (int j = 0; j < pillGrid[i].length; j++) {
                Pill pill = pillGrid[i][j];
                if (pill.texture != null) {
                    Vector2 pillCenter = new Vector2(pill.getX() + pill.getWidth() / 2, pill.getY() + pill.getHeight() / 2);
                    if (playerCenter.dst(pillCenter) <= (player.getWidth() / 4 + pill.getWidth() / 4)) {
                        switch (pill.type) {
                            case Pill.REGULAR_PILL:
                                player.increaseScore(10);
                            break;
                            case Pill.POWER_PILL:
                                player.game.activatePowerMode();
                            break;
                            case Pill.CHERRY:
                                player.increaseScore(200);
                            break;
                        }
                        pill.texture.dispose();
                        pill.texture = null;
                        wakaWakaSound.play();
                    }
                }
            }
        }
    }

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Input.Keys.S:
                desiredMovementDirection.set(0, -MOVE_AMOUNT);
                desiredDirection = Character.Direction.UP;
            break;
            case Input.Keys.A:
                desiredMovementDirection.set(-MOVE_AMOUNT, 0);
                desiredDirection = Character.Direction.LEFT;
            break;
            case Input.Keys.W:
                desiredMovementDirection.set(0, MOVE_AMOUNT);
                desiredDirection = Character.Direction.DOWN;
            break;
            case Input.Keys.D:
                desiredMovementDirection.set(MOVE_AMOUNT, 0);
                desiredDirection = Character.Direction.RIGHT;
            break;
        }
        return true;
    }
}
