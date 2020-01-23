package com.example.androidgame;

import android.graphics.Point;
import android.view.Display;

import static com.example.androidgame.GameGrid.TILE_HEIGHT;
import static com.example.androidgame.GameGrid.TILE_WIDTH;

public class Room {
    Tile[] grid;

    public Room(Display display){

        int height = 5;
        int width = 5;

        Point size = new Point();
        display.getSize(size);
        int displayWidth = size.x;
        int displayHeight = size.y;

        grid = new Tile[25];
        int iterator = 0;
        for(int y = 0; y < height; y++){
            for(int x = 0; x < width; x++){
                grid[iterator]  = new Tile((x*TILE_WIDTH) ,(y*TILE_HEIGHT),iterator );
                GameGrid.convertToIso(grid[iterator],display);
                iterator ++;
            }
        }
        grid[24].doorway = true;
    }

}
