package com.pacman;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

public class Utils {
    public static ArrayList<Tile> getShortestPath(Tile[][] grid, Tile start, Tile end){
        ArrayList<Tile> result = new ArrayList<>();
        ArrayList<Tile> openSet = new ArrayList<>();
        ArrayList<Tile> closedSet = new ArrayList<>();

        if(grid == null || start == null || end == null){
            return result;
        }

        if(start.equals(end)){
            result.add(start);
            return result;
        }

        openSet.add(start);

        while(!openSet.isEmpty()){
            Tile current = getLowestF(openSet);

            if(current.equals(end)){
                return retracePath(current);
            }

            openSet.remove(current);
            closedSet.add(current);

            for(Tile neighbor : current.getNeighbors(grid)){
                if(!neighbor.open || closedSet.contains(neighbor)){
                    continue;
                }

                double newG = current.g + 1;
                if(!openSet.contains(neighbor)){
                    openSet.add(neighbor);
                } else if(newG >= neighbor.g){
                    continue;
                }

                neighbor.cameFrom = current;
                neighbor.g = newG;
                neighbor.h = heuristic(neighbor, end);
                neighbor.f = neighbor.g + neighbor.h;

            }
        }
        return new ArrayList<>();
    }

    private static ArrayList<Tile> retracePath(Tile current) {
        ArrayList<Tile> result = new ArrayList<>();
        result.add(current);
        while(current.cameFrom != null){
            current = current.cameFrom;
            result.add(current);
        }
        Collections.reverse(result);
        return result;
    }

    private static double heuristic(Tile neighbor, Tile end) {
        return Math.abs(neighbor.i - end.i) + Math.abs(neighbor.j - end.j);
    }

    private static Tile getLowestF(ArrayList<Tile> openSet){
        Tile result = openSet.get(0);
        for(Tile t : openSet){
            if(t.f < result.f){
                result = t;
            }
        }
        return result;
    }
}
