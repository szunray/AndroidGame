package com.example.androidgame;

import android.graphics.Point;
import android.view.Display;

public class GameGrid {
    public static int TILE_WIDTH = 150;
    public static int TILE_HEIGHT = 150; //This will probably go elsewhere later.

    int height;
    int width;
    //Display display;
    //Tile[][] grid;

    Tile[] grid;
    public GameGrid(){
        height = 5;
        width = 5;
        grid = new Tile[25];
        int iterator = 0;
        for(int y = 0; y < height; y++){
            for(int x = 0; x < width; x++){
                grid[iterator]  = new Tile(x*TILE_WIDTH,y*TILE_HEIGHT);
                iterator ++;
            }
        }


    }

    public GameGrid(Display display){
        height = 5;
        width = 5;
        Point size = new Point();
        display.getSize(size);
        int displayWidth = size.x;
        int displayHeight = size.y;

        grid = new Tile[25];
        int iterator = 0;
        for(int y = 0; y < height; y++){
            for(int x = 0; x < width; x++){
                grid[iterator]  = new Tile((x*TILE_WIDTH) ,(y*TILE_HEIGHT) );
                convertToIso(grid[iterator],display);
                iterator ++;
            }
        }


    }

    public void convertToIso(Tile tile,Display display){
        Point size = new Point();
        display.getSize(size);
        int displayWidth = size.x;
        int displayHeight = size.y;
        int[] coordinates = carToIso(tile.xpos,tile.ypos);
        tile.xpos = coordinates[0]+(displayWidth/2);
        tile.ypos = coordinates[1]+ (displayHeight/4);
    }
    //Cartesian to isometric:
    public int[] carToIso(int cartX, int cartY) {
        int isoX = cartX - cartY;
        int isoY = (cartX + cartY) / 2;
        int[] ans = {isoX, isoY};
        return ans;
    }

}
