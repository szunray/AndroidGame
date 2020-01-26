package com.example.androidgame;

import android.graphics.Point;
import android.view.Display;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import static com.example.androidgame.GameGrid.TILE_HEIGHT;
import static com.example.androidgame.GameGrid.TILE_WIDTH;
import static com.example.androidgame.GameGrid.homeRoom;
import static java.lang.Math.abs;

public class Room {
    Tile[] grid;
    int centerX;
    int centerY;

    public void updateCenter(){
        int top;
        int bottom;
        int left;
        int right;

        left = grid[0].xpos;
        right = grid[0].xpos;
        top = grid[0].ypos;
        bottom = grid[0].ypos;

        for (Tile tile : grid){
            if (tile.xpos > right)
                right = tile.xpos;
            if(tile.xpos < left)
                left = tile.xpos;
            if (tile.ypos > bottom)
                bottom = tile.ypos;
            if (tile.ypos < top)
                top = tile.ypos;


        }
        centerX = (left + right)/2;
        centerY = (top + bottom)/2;
    }
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
        updateCenter();
        grid[5] .doorway = true;
        grid[24].doorway = true;
    }

//This will need changing to accomadate the different shapes and configurations
    // but its a start?
    public void explore(Room room, Tile offsetTile){
        GameGrid.Map.add(room);//Map.add(room)
        updateCenter();
        offsetTile.doorway = false;
        int offsetX = (centerX - offsetTile.xpos);
        int offsetY = (centerY - offsetTile.ypos);
        List<Tile> currentMap = new ArrayList<Tile>();
        int iterator = 0;
        for(Tile tile : grid){
            tile.index = iterator;
            //tile.doorway = false;
            currentMap.add(tile);
            iterator++;
        }

        for (Tile tile : room.grid){
            //tile.xpos += TILE_WIDTH;
            tile.ypos += (offsetTile.ypos+offsetY + TILE_HEIGHT);//offsetTile.ypos ;//+ TILE_HEIGHT;
            //tile.xpos -= offsetX*2;//+ offsetTile.xpos ;//+ TILE_WIDTH;
        tile.index = iterator;
        currentMap.add(tile);
        iterator++;
    }

        //grid = new Tile[currentMap.size()];
        //grid = currentMap.toArray(grid);
        //updateCenter();
       // grid = (Tile[])currentMap.toArray();

    }

}
