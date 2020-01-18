package com.example.androidgame;

import android.graphics.Point;
import android.view.Display;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.abs;

public class GameGrid {
    public static int TILE_WIDTH = 150;
    public static int TILE_HEIGHT = 150; //This will probably go elsewhere later.

    int height;
    int width;

    public boolean pawnHighlighted = false;

    ArrayList<Tile> highlightedTiles = new ArrayList<Tile>();


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

    public void checkTouch(float touchX, float touchY){

        if (touchedGameGrid(touchX, touchY)){
            return;
        }

    }

    public boolean touchedGameGrid(float touchX, float touchY){

        boolean gridTouched = false;
        pawnHighlighted = false;
        double closestDistance = TILE_WIDTH + 1;
        Tile touchedTile = new Tile();
        for (Tile tile : grid){
            tile.highlighted = false;

            // yActual is very likely off by about TileWidth/2.
            //I doubt that Android draws circles from the center, outward.
            double tileToTouchDistance = GameView.getDistance((int)touchX,(int)(touchY-TILE_HEIGHT),tile.xActual,tile.yActual);

            if(tileToTouchDistance<closestDistance){
                    closestDistance = tileToTouchDistance;
                    touchedTile = tile;
                    gridTouched = true;
                }

        }
        if (touchedTile.occupied){
            pawnHighlighted = true;
            List<Tile> adjacentTiles = findAdjacent(touchedTile);
            for (Tile tile : adjacentTiles){
                tile.highlighted = true;
            }

        }
        touchedTile.highlighted = true;
        return gridTouched;
    }

    public ArrayList<Tile> findAdjacent(Tile tile){
        ArrayList<Tile> tiles = new ArrayList<Tile>();

        for (Tile gridTile : grid){
            //The 1.5 appended at the end of this equation is serviceable,
            //but it could be better
            if (GameView.getDistance(tile.xActual,tile.yActual,gridTile.xActual,gridTile.yActual) < TILE_HEIGHT*1.5)
            {
                tiles.add(gridTile);
            }
        }

        return tiles;
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
