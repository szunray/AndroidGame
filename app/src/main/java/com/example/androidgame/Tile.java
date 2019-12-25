package com.example.androidgame;

public class Tile {

    int xpos;
    int ypos;
    int xActual;
    int yActual;
    boolean highlighted;
    boolean occupied;
    Pawn occupier;

    public Tile(){
    }

    public Tile (int x, int y){
        xpos = x;
        ypos = y;
        highlighted = false;
        occupied = false;
    }

    public void Occupy(Pawn pawn){
        occupier = pawn;
        occupied = true;
    }

    public void Empty(){
        occupier = null;
        occupied = false;
    }


}
