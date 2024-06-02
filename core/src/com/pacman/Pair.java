package com.pacman;

public class Pair<T, U> {
    private T first;
    private U second;

    public Pair(T first, U second) {
        this.first = first;
        this.second = second;
    }

    public T getX() {
        return first;
    }

    public U getY() {
        return second;
    }

    public void setX(T x) {
        this.first = x;
    }

    public void setY(U y) {
        this.second = y;
    }

    @Override
    public String toString() {
        return "(" + first + ", " + second + ")";
    }
}