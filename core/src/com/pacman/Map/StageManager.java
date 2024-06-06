package com.pacman.Map;

import com.pacman.PacMan;
import com.pacman.Pair;

import java.util.*;

public class StageManager {
    private ArrayList<Pair<PacMan.Stage, Double>> stages;
    private Timer timer;
    private int currentStageIndex;

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

        double delay = currentStagePair.getY() * 1000; // Convert to milliseconds

        // Only schedule the next stage change if the duration is not negative
        if (delay >= 0) {
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    currentStageIndex = (currentStageIndex + 1) % stages.size(); // Loop back to the start if we've reached the end
                    changeStage(game);
                }
            }, (long) delay);
        }
    }
}