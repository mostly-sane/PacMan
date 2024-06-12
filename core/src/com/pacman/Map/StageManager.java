package com.pacman.Map;

import com.pacman.Characters.Ghost;
import com.pacman.PacMan;
import com.pacman.Pair;
import com.pacman.Components.CollisionComponent;

import java.util.*;

public class StageManager {
    private ArrayList<Pair<PacMan.Stage, Double>> stages;
    private PacMan.Stage currentStage;
    private Pair<PacMan.Stage, Double> currentStagePair;
    private Timer timer;
    private TimerTask currentTask;
    private int currentStageIndex;
    private Timer powerPillTimer;
    private static final int POWER_PILL_DURATION = 5000; // Duration of the power pill effect in milliseconds
    private CollisionComponent collisionComponent;

    public StageManager(ArrayList<Pair<PacMan.Stage, Double>> stageTimes, CollisionComponent collisionComponent) {
        this.stages = stageTimes;
        this.collisionComponent = collisionComponent;
        this.timer = new Timer();
        this.currentStageIndex = 0;
    }

    public void start(PacMan game) {
        changeStage(game);
    }

    private void changeStage(PacMan game) {
        currentStagePair = stages.get(currentStageIndex);
        currentStage = currentStagePair.getX();
        game.stage = currentStage;
        //System.out.println("Current stage: " + currentStage);
        for (Ghost ghost : game.ghosts) {
            if (ghost != null) {
                ghost.recalculatePath();
            }
        }

        double delay = currentStagePair.getY() * 1000;

        if (delay >= 0) {
            currentTask = new TimerTask() {
                @Override
                public void run() {
                    currentStageIndex = (currentStageIndex + 1) % stages.size();
                    changeStage(game);
                }
            };
            timer.schedule(currentTask, (long) delay);
        }
    }

    public void activatePowerMode(PacMan game) {
        if (currentTask != null) {
            currentTask.cancel();
            timer.cancel();
            timer = new Timer();
        }

        game.stage = PacMan.Stage.FRIGHTENED;
        game.ghostSound.stop();
        game.scaredSound.loop();
        //System.out.println("Current stage: " + game.stage);

        currentTask = new TimerTask() {
            @Override
            public void run() {
                changeStage(game);
                game.scaredSound.stop();
                game.ghostSound.loop();
            }
        };
        timer.schedule(currentTask, POWER_PILL_DURATION);

        startGhostDeathCheck(game);
    }

    private void startGhostDeathCheck(PacMan game) {
        powerPillTimer = new Timer();

        powerPillTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {

                for (Ghost ghost : game.ghosts) {
                    if (ghost.isDying) {
                        continue;
                    }

                    if (collisionComponent.isGhostCollidingWithPlayer(game.player, ghost)) {
                        game.ghostDeath(ghost);
                    }
                }
            }
        }, 0, 100);
    }
}
