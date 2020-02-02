package com.example.androidgame;

public class Tile {

    int index;
    int xpos;
    int ypos;
    int xActual;
    int yActual;
    boolean highlighted;
    boolean occupied;
    boolean doorway;
    Pawn occupier;

    public Tile() {
    }

    public Tile(int x, int y, int gridIndex) {
        index = gridIndex;
        xpos = x;
        ypos = y;
        highlighted = false;
        occupied = false;
        doorway = false;
    }

    public void Occupy(Pawn pawn) {
        occupier = pawn;
        occupied = true;
    }

    public void Empty() {
        occupier = null;
        occupied = false;
    }


}
