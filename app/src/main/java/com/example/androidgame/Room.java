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

    public void updateCenter() {
        int top;
        int bottom;
        int left;
        int right;

        left = grid[0].xpos;
        right = grid[0].xpos;
        top = grid[0].ypos;
        bottom = grid[0].ypos;

        for (Tile tile : grid) {
            if (tile.xpos > right)
                right = tile.xpos;
            if (tile.xpos < left)
                left = tile.xpos;
            if (tile.ypos > bottom)
                bottom = tile.ypos;
            if (tile.ypos < top)
                top = tile.ypos;


        }
        centerX = (left + right) / 2;
        centerY = (top + bottom) / 2;
    }

    public Room(Display display) {

        int height = 5;
        int width = 5;

        Point size = new Point();
        display.getSize(size);
        int displayWidth = size.x;
        int displayHeight = size.y;

        grid = new Tile[25];
        int iterator = 0;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                grid[iterator] = new Tile((x * TILE_WIDTH), (y * TILE_HEIGHT), iterator);
                GameGrid.convertToIso(grid[iterator], display);
                iterator++;
            }
        }
        updateCenter();
        grid[5].doorway = true;
        grid[24].doorway = true;
    }

    //This will need changing to accomadate the different shapes and configurations
    // but its a start?
    public void explore(Room room, Tile offsetTile) {
        updateCenter();
        offsetTile.doorway = false;
        int offsetX = (room.centerX - centerX);
        int offsetY = (room.centerY - centerY);

        List<Tile> currentMap = new ArrayList<Tile>();
        int iterator = 0;
        for (Tile tile : grid) {
            tile.index = iterator;
            //tile.doorway = false;
            currentMap.add(tile);
            iterator++;
        }

        for (Tile tile : room.grid) {
            //tile.xpos += TILE_WIDTH;
            tile.xpos += (offsetX);//offsetTile.ypos ;//+ TILE_HEIGHT;
            tile.ypos -= (offsetY);//offsetTile.ypos ;//+ TILE_HEIGHT;
            //tile.xpos -= offsetX*2;//+ offsetTile.xpos ;//+ TILE_WIDTH;
            tile.index = iterator;
            currentMap.add(tile);
            iterator++;
        }
        room.updateCenter();

        double xAdjust = Math.signum(offsetTile.xpos - room.centerX);
        double yAdjust = Math.signum(offsetTile.ypos - room.centerY);
        while (isOverlappingMap(room)) {
            adjust(room, xAdjust, yAdjust);
        }

        GameGrid.Map.add(room);//Map.add(room)

        //grid = new Tile[currentMap.size()];
        //grid = currentMap.toArray(grid);
        //updateCenter();
        // grid = (Tile[])currentMap.toArray();

    }

    public boolean isOverlapping(Room room) {
        for (Tile tile : grid) {
            for (Tile setTile : room.grid) {
                double distance = GameView.getDistance(tile.xpos, tile.ypos, setTile.xpos, setTile.ypos);
                if (distance < TILE_HEIGHT) {
                    setTile.highlighted = true;
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isOverlappingMap(Room room) {
        for (Room section : GameGrid.Map) {
            if (section.isOverlapping(room))
                return true;
        }
        return false;
    }

    public void adjust(Room room, double x, double y) {
        // int xOffset = offsetTile.xpos - room.centerX;
        //  int yOffset = offsetTile.ypos - room.centerY;

        for (Tile tile : room.grid) {
            tile.xpos += x * TILE_HEIGHT / 2;
            tile.ypos += y * TILE_HEIGHT / 2;
        }
        room.updateCenter();
        // room.centerY+=TILE_HEIGHT;
        //room.centerX+=TILE_HEIGHT;
    }

}
