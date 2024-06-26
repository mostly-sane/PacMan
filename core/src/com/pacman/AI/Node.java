package com.pacman.AI;
import com.pacman.Pair;
import java.util.ArrayList;

public class Node {
    public double pathCost;
    public Node parent;
    public Pair<Integer, Integer> location;
    public ArrayList<Node> adjacentNodes;
    public boolean isOpen;

    public double gCost;
    public double fCost;

    public Node(Pair<Integer, Integer> location) {
        this.location = location;
        this.adjacentNodes = new ArrayList<>();
    }

    @Override
    public String toString() {
        return location.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Node)) {
            return false;
        }
        Node node = (Node) obj;
        return location.equals(node.location);
    }
}
