package com.example.androidgame;

public class Tile {

    int xpos;
    int ypos;
    int xActual;
    int yActual;
    boolean highlighted;

    public Tile(){
    }

    public Tile (int x, int y){
        xpos = x;
        ypos = y;
        highlighted = false;
    }
}
