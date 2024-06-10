package com.pacman.Map;

import com.pacman.PacMan;
import com.pacman.Pair;

import java.io.*;

import static java.lang.Double.valueOf;

public class LevelManager {
    public static Tile[][] loadLevel(File level) {
        Tile[][] grid;
        String line;
        int x;
        int y;
        int w;
        int h;

        try (BufferedReader reader = new BufferedReader(new FileReader(level))) {
            line = reader.readLine();
            String[] parts = line.split(",");
            x = Integer.parseInt(parts[0]);
            y = Integer.parseInt(parts[1]);
            w = Integer.parseInt(parts[2]);
            h = Integer.parseInt(parts[3]);
            grid = new Tile[x][y];

            int i = 0;
            while ((line = reader.readLine()) != null) {
                if(line.equals("")){
                    break;
                }
                parts = line.split(",");
                for (int j = 0; j < parts.length; j++) {
                    boolean open = Integer.parseInt(parts[j]) == 1;
                    grid[j][i] = new Tile(j, i, w, h, open);
                }
                i++;
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        return grid;
    }

    public static Pill[][] loadPills(File level) {
        Pill[][] grid;
        String line;
        int x;
        int y;
        int w;
        int h;

        try (BufferedReader reader = new BufferedReader(new FileReader(level))) {
            line = reader.readLine();
            String[] parts = line.split(",");
            x = Integer.parseInt(parts[0]);
            y = Integer.parseInt(parts[1]);
            w = Integer.parseInt(parts[2]);
            h = Integer.parseInt(parts[3]);
            grid = new Pill[x][y];

            int i = 0;
            while ((line = reader.readLine()) != null) {
                parts = line.split(",");
                for (int j = 0; j < parts.length; j++) {
                    int type = Integer.parseInt(parts[j]);
                    grid[j][i] = new Pill(j * w, i * h, w, h, type);
                }
                i++;
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        return grid;
    }

    public static String getLevelParams(File level) {
        String line;
        try (BufferedReader reader = new BufferedReader(new FileReader(level))) {
            line = reader.readLine();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        return line;
    }

    public static void generateLevel(String level) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(level))) {
            for (int i = 0; i < 21; i++) {
                for (int j = 0; j < 19; j++) {
                    writer.write("1"); // 1 represents an open tile
                    if (j < 18) {
                        writer.write(",");
                    }
                }
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadStages(File level, PacMan game) {
        try (BufferedReader reader = new BufferedReader(new FileReader(level))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if(line.equals("Stages")){
                    break;
                }
            }
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                PacMan.Stage stage = PacMan.Stage.valueOf(parts[0]);
                Double duration = valueOf(parts[1]);
                game.stageTimes.add(new Pair<>(stage, duration));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
