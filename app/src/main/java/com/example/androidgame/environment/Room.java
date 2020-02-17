package com.example.androidgame.environment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;

import com.example.androidgame.GameView;
import com.example.androidgame.common.AnimationUtils;
import com.example.androidgame.common.MyDrawable;
import com.example.androidgame.common.Element;
import com.example.androidgame.common.Touchable;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import android.content.res.Resources;

public class Room extends Element implements MyDrawable, Touchable {
    public Tile[] grid;
    public Point location;
    int centerX;
    int centerY;
    Tile lastTouchedTile;

    Bitmap spriteSheet;
    Rect[] sprites;

    public Room(GameView gameView) {
        // cut from previous project
        Context context = gameView.getContext();
        Resources resources = context.getResources();
        String nameOfImage = "basic_terrain";
        int resId = context.getResources().getIdentifier(nameOfImage, "drawable", context.getPackageName());
        spriteSheet = BitmapFactory.decodeResource(resources,resId);
        spriteSheet = Bitmap.createScaledBitmap(spriteSheet,904,1824,false);

        try {
            sprites = AnimationUtils.AnimationTools.readAnimXML(context,nameOfImage+"_data.xml");
        }catch(Exception r){
            Log.d("XMLREAD","File not found");
        }
        //end
        location = new Point(0, 0);
        int height = 5;
        int width = 5;
        Display display = GameView.gameDisplay;//gameView.getDisplay();
        Point size = new Point();
        display.getSize(size);
        int displayWidth = size.x;
        int displayHeight = size.y;

        grid = new Tile[25];
        int iterator = 0;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int cartX = location.x + x * Tile.TILE_WIDTH;
                int cartY = location.y + y * Tile.TILE_WIDTH;
                grid[iterator] = new Tile(cartX - cartY + (displayWidth / 2), (cartX + cartY) / 2+ (displayHeight / 4), this, iterator);
                iterator++;
            }
        }
        updateCenter();
        grid[5].doorway = true;
        grid[24].doorway = true;
    }

    public Tile getRandomTile(){
        // return random one that's not a door or currently occupied.
        return grid[0];
    }

    private void updateCenter() {
        int top;
        int bottom;
        int left;
        int right;

        left = grid[0].x;
        right = grid[0].x;
        top = grid[0].y;
        bottom = grid[0].y;

        for (Tile tile : grid) {
            if (tile.x > right)
                right = tile.x;
            if (tile.x < left)
                left = tile.x;
            if (tile.y > bottom)
                bottom = tile.y;
            if (tile.y < top)
                top = tile.y;
        }
        centerX = (left + right) / 2;
        centerY = (top + bottom) / 2;
    }

    //This will need changing to accommodate the different shapes and configurations
    // but its a start?
    public void explore(Room room, Tile offsetTile) {
        updateCenter();
        offsetTile.doorway = false;
        int offsetX = (room.centerX - offsetTile.x);
        int offsetY = (room.centerY - offsetTile.y);

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
            tile.x -= (offsetX);//offsetTile.ypos ;//+ TILE_HEIGHT;
            tile.y -= (offsetY);//offsetTile.ypos ;//+ TILE_HEIGHT;
            //tile.xpos -= offsetX*2;//+ offsetTile.xpos ;//+ TILE_WIDTH;
            tile.index = iterator;
            currentMap.add(tile);
            iterator++;
        }
        room.updateCenter();

        double xAdjust = Math.signum(room.centerX - centerX);
        double yAdjust = Math.signum(room.centerY - centerY);
        while (isOverlappingMap(room)) {
            adjust(room, xAdjust, yAdjust);
        }

        World.Map.add(room);//Map.add(room)

        //grid = new Tile[currentMap.size()];
        //grid = currentMap.toArray(grid);
        //updateCenter();
        // grid = (Tile[])currentMap.toArray();

    }

    private boolean isOverlapping(Room room) {
        for (Tile tile : grid) {
            for (Tile setTile : room.grid) {
                double distance = GameView.getDistance(tile.x, tile.y, setTile.x, setTile.y);
                if (distance < Tile.TILE_WIDTH_HALF / 2) {
                    setTile.highlighted = true;
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isOverlappingMap(Room room) {
        for (Room section : World.Map) {
            if (section.isOverlapping(room))
                return true;
        }
        return false;
    }

    private void adjust(Room room, double x, double y) {
        // int xOffset = offsetTile.xpos - room.centerX;
        //  int yOffset = offsetTile.ypos - room.centerY;

        for (Tile tile : room.grid) {
            tile.x += x * Tile.TILE_WIDTH_HALF;
            tile.y += y * Tile.TILE_WIDTH_HALF;
        }
        room.updateCenter();
        // room.centerY+=TILE_HEIGHT;
        //room.centerX+=TILE_HEIGHT;
    }

    @Override
    public void draw(Canvas c, Paint p) {
        for (Tile t : grid) {
            t.draw(c, p);
        }
    }

    @Override
    public void update(Point camera) {
    }

    @Override
    public boolean handleTouch(MotionEvent motionEvent, Point camera) {
        for(Tile tile : grid){
            if (tile.handleTouch(motionEvent, camera)){
                lastTouchedTile = tile;
                return true;
            }
        }
        return false;
    }
}
