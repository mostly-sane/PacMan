package com.pacman.Characters;

import com.badlogic.gdx.graphics.Texture;
import com.pacman.AI.Node;
import com.pacman.Map.Tile;
import com.pacman.PacMan;
import com.pacman.Pair;
import com.pacman.Utils;

import java.util.*;

public class Ghost extends Character{
    Float speed = 0.5f;
    Node[][] nodes;
    ArrayList<Node> path = new ArrayList<>();
    Tile targetTile;

    public Ghost(int width, int height, Texture texture, PacMan game) {
        super(width, height, texture, game);
        convertToNodes(game.grid);
    }

    public void convertToNodes(Tile[][] grid){
        nodes = new Node[grid.length][grid[0].length];
        for(int i = 0; i < grid.length; i++){
            for(int j = 0; j < grid[0].length; j++){
                nodes[i][j] = new Node(new Pair<>(i, j));
                nodes[i][j].isOpen = grid[i][j].open;
            }
        }
        for(int i = 0; i < grid.length; i++){
            for(int j = 0; j < grid[0].length; j++){
                if(i > 0 && nodes[i-1][j].isOpen){
                    nodes[i][j].adjacentNodes.add(nodes[i-1][j]);
                }
                if(i < grid.length - 1 && nodes[i+1][j].isOpen){
                    nodes[i][j].adjacentNodes.add(nodes[i+1][j]);
                }
                if(j > 0 && nodes[i][j-1].isOpen){
                    nodes[i][j].adjacentNodes.add(nodes[i][j-1]);
                }
                if(j < grid[0].length - 1 && nodes[i][j+1].isOpen){
                    nodes[i][j].adjacentNodes.add(nodes[i][j+1]);
                }
            }
        }
    }

    public void update(){
        Pair<Float, Float> targetPosition = new Pair<>(targetTile.x, targetTile.y);

        if(position.getX() < targetPosition.getX()){
            position.setX(position.getX() + speed);
        } else if(position.getX() > targetPosition.getX()){
            position.setX(position.getX() - speed);
        } else if(position.getY() < targetPosition.getY()){
            position.setY(position.getY() + speed);
        } else if(position.getY() > targetPosition.getY()){
            position.setY(position.getY() - speed);
        }
    }

    public void recalculatePath() {
        findPath(Utils.getCurrentTile(this, game.grid), Utils.getCurrentTile(game.player, game.grid));
        targetTile = game.grid[path.get(1).location.getX()][path.get(1).location.getY()];
        System.out.println(targetTile);
    }

    private double heuristic(Node x, Node y) {
        return Math.abs(x.location.getX()-y.location.getX()) + Math.abs(x.location.getY()-y.location.getY());
    }

    public void findPath(Tile start, Tile end){
        Node startNode = nodes[start.i][start.j];
        Node endNode = nodes[end.i][end.j];

        Set<Node> exploredNodes = new HashSet<>();
        //override compare method
        PriorityQueue<Node> unexploredNodes = new PriorityQueue<>(25, Comparator.comparingDouble(i -> i.pathCost));
        
        startNode.gCost = 0;
        unexploredNodes.add(startNode);
        boolean isFound = false;
        
        while(!unexploredNodes.isEmpty() && !isFound){
            Node currentNode = unexploredNodes.poll();
            exploredNodes.add(currentNode);

            if(startNode.location.getX().equals(endNode.location.getX()) && startNode.location.getY().equals(endNode.location.getY())){
                isFound = true;
            }

            for(Node childNode : currentNode.adjacentNodes){
                double cost = childNode.pathCost;
                double tempGCost = currentNode.fCost + cost;
                double tempFCost = tempGCost + heuristic(childNode, endNode);
                if(exploredNodes.contains(childNode) && (tempFCost >= childNode.fCost)){
                    continue;
                }
                else if(!unexploredNodes.contains(childNode) || tempFCost < childNode.fCost){
                    childNode.parent = currentNode;
                    childNode.gCost = tempGCost;
                    childNode.fCost = tempFCost;
                    if(unexploredNodes.contains(childNode)){
                        unexploredNodes.remove(childNode);
                    }
                    unexploredNodes.add(childNode);
                }
            }
        }
        backtrack(endNode);
    }

    private void backtrack(Node endNode) {
        path = new ArrayList<>();
        Node currentNode = endNode;
        path.add(currentNode);
        while(currentNode.parent != null){
            currentNode = currentNode.parent;
            path.add(currentNode);
        }
        Collections.reverse(path);
        resetNodes();
    }

    private void printPath(){
        if(path == null){
            System.out.println("No path found");
            return;
        }
        for(Node node : path){
            System.out.println(node.location);
        }
    }

    private void resetNodes(){
        for(Node[] row : nodes){
            for(Node node : row){
                node.parent = null;
                node.fCost = 0;
                node.gCost = 0;
            }
        }
    }
}