package com.pacman.Components;

import com.pacman.AI.Node;
import com.pacman.Characters.Ghost;
import com.pacman.Map.Tile;
import com.pacman.PacMan;
import com.pacman.Pair;
import java.util.*;

public class PathfindingComponent {
    private PacMan game;
    private Ghost parent;
    public Node[][] grid;
    ArrayList<Node> path = new ArrayList<>();
    ArrayList<Node> availableNodes = new ArrayList<>();

    public PathfindingComponent(PacMan game, Ghost ghost) {
        this.parent = ghost;
        this.game = game;
    }

    public void convertToNodes(Tile[][] grid){
        this.grid = new Node[grid.length][grid[0].length];
        for(int i = 0; i < grid.length; i++){
            for(int j = 0; j < grid[0].length; j++){
                this.grid[i][j] = new Node(new Pair<>(i, j));
                this.grid[i][j].isOpen = grid[i][j].open;
            }
        }
        for(int i = 0; i < grid.length; i++){
            for(int j = 0; j < grid[0].length; j++){
                if(i > 0 && this.grid[i-1][j].isOpen){
                    this.grid[i][j].adjacentNodes.add(this.grid[i-1][j]);
                }
                if(i < grid.length - 1 && this.grid[i+1][j].isOpen){
                    this.grid[i][j].adjacentNodes.add(this.grid[i+1][j]);
                }
                if(j > 0 && this.grid[i][j-1].isOpen){
                    this.grid[i][j].adjacentNodes.add(this.grid[i][j-1]);
                }
                if(j < grid[0].length - 1 && this.grid[i][j+1].isOpen){
                    this.grid[i][j].adjacentNodes.add(this.grid[i][j+1]);
                }
            }
        }
    }

    private double heuristic(Node x, Node y) {
        return Math.abs(x.location.getX()-y.location.getX()) + Math.abs(x.location.getY()-y.location.getY());
    }

    public ArrayList<Node> findPath(Tile start, Tile end){
        resetNodes();
        if(start == null || end == null){
            return null;
        }

        Node startNode = grid[start.i][start.j];
        Node endNode = grid[end.i][end.j];

        Set<Node> exploredNodes = new HashSet<>();
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
                if(!childNode.isOpen){
                    unexploredNodes.remove(childNode);
                    continue;
                }

                double cost = childNode.pathCost;
                double tempGCost = currentNode.fCost + cost;
                double tempFCost = tempGCost + heuristic(childNode, endNode);

                if(exploredNodes.contains(childNode) && (tempFCost >= childNode.fCost)){
                    continue;
                } else if(!unexploredNodes.contains(childNode) || tempFCost < childNode.fCost){
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
        return backtrack(endNode);
    }

    private ArrayList<Node> backtrack(Node endNode) {
        path.clear();
        Node currentNode = endNode;
        path.add(currentNode);
        while(currentNode.parent != null){
            currentNode = currentNode.parent;
            path.add(currentNode);
        }
        Collections.reverse(path);
        resetNodes();
        return path;
    }

    public Node blockNodeBehindGhost() {
        Node nodeBehindGhost;
        int row = parent.getRow();
        int column = parent.getColumn();
        switch(parent.getDirection()){
            case UP:
                row++;
                break;
            case DOWN:
                row--;
                break;
            case LEFT:
                column++;
                break;
            case RIGHT:
                column--;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + parent.getDirection());
        }

        if(row < 0 || row >= grid.length || column < 0 || column >= grid[0].length){
            return null;
        }

        nodeBehindGhost = grid[column][row];
        return nodeBehindGhost;
    }

    public ArrayList<Node> getAvailableNodes(Tile tile){
        availableNodes.clear();
        Node node = grid[tile.i][tile.j];
        for(Node adjacentNode : node.adjacentNodes){
            if(adjacentNode.isOpen){
                availableNodes.add(adjacentNode);
            }
        }
        return availableNodes;
    }

    public void reopenNodes(){
        for(int i = 0; i < game.grid.length; i++){
            for(int j = 0; j < game.grid[0].length; j++){
                grid[i][j].isOpen = game.grid[i][j].open;
            }
        }
    }

    private void resetNodes(){
        for(Node[] row : grid){
            for(Node node : row){
                node.parent = null;
                node.fCost = 0;
                node.gCost = 0;
            }
        }
    }
}
