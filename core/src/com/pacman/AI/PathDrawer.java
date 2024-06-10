package com.pacman.AI;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.Color;

import java.util.List;

public class PathDrawer {
    private static final int TILE_SIZE = 25;

    public void drawPath(ShapeRenderer shapeRenderer, List<Node> path) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.RED); // Set the color of the path
        if(path == null) {
            shapeRenderer.end();
            return;
        }

        for (int i = 0; i < path.size() - 1; i++) {
            Node node1 = path.get(i);
            Node node2 = path.get(i + 1);

            int x1 = (node1.location.getX() * TILE_SIZE) + TILE_SIZE / 2;
            int y1 = node1.location.getY() * TILE_SIZE + TILE_SIZE / 2;
            int x2 = node2.location.getX() * TILE_SIZE + TILE_SIZE / 2;
            int y2 = node2.location.getY() * TILE_SIZE + TILE_SIZE / 2;

            shapeRenderer.line(x1, y1, x2, y2); // Draw a line between the two nodes
        }
        shapeRenderer.end();
    }

    public void drawBlockedNodes(ShapeRenderer shapeRenderer, Node[][] nodes) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.RED);

        for (int i = 0; i < nodes.length; i++) {
            for (int j = 0; j < nodes[i].length; j++) {
                Node node = nodes[i][j];
                if (!node.isOpen) {
                    int x = node.location.getX() * TILE_SIZE;
                    int y = node.location.getY() * TILE_SIZE;
                    shapeRenderer.rect(x, y, TILE_SIZE, TILE_SIZE);
                }
            }
        }

        shapeRenderer.end();
    }
}