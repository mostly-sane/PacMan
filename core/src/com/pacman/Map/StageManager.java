package com.pacman.Map;

import com.pacman.Characters.Ghost;
import com.pacman.PacMan;
import com.pacman.Pair;

import java.util.*;

public class StageManager {
    private final ArrayList<Pair<PacMan.Stage, Double>> stages;
    private Timer timer;
    private TimerTask currentTask;
    private int currentStageIndex;
    private static final int POWER_PILL_DURATION = 5000;

    public StageManager(ArrayList<Pair<PacMan.Stage, Double>> stageTimes) {
        this.stages = stageTimes;
        this.timer = new Timer();
        this.currentStageIndex = 0;
    }

    public void start(PacMan game) {
        changeStage(game);
    }

    private void changeStage(PacMan game) {
        Pair<PacMan.Stage, Double> currentStagePair = stages.get(currentStageIndex);
        PacMan.Stage currentStage = currentStagePair.getX();
        game.stage = currentStage;
        System.out.println("Current stage: " + currentStage);
        for (Ghost ghost : game.ghosts) {
            if (ghost != null) {
                ghost.turnAround();
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

        currentTask = new TimerTask() {
            @Override
            public void run() {
                changeStage(game);
                game.scaredSound.stop();
                game.ghostSound.loop();
                for(Ghost ghost : game.ghosts){
                    ghost.fearEnding = false;
                }
            }
        };
        timer.schedule(currentTask, POWER_PILL_DURATION);
    }
}
