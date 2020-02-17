package com.example.androidgame.environment;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;
import android.view.MotionEvent;

import com.example.androidgame.characters.Pawn;
import com.example.androidgame.common.Element;
import com.example.androidgame.common.Touchable;
import com.example.androidgame.common.MyDrawable;

import static android.webkit.ConsoleMessage.MessageLevel.LOG;

public class Tile extends Element implements MyDrawable, Touchable {

    public static int TILE_WIDTH = 150;
    public static int TILE_WIDTH_HALF = TILE_WIDTH / 2;

    Rect touchBox;
    Rect touchBox2x;

    int index;
    boolean highlighted;
    boolean path;
    boolean occupied;
    public boolean doorway;
    Pawn occupier;
    Room room;

    public Tile(int x, int y, Room room, int gridIndex) {
        super(x, y, TILE_WIDTH, TILE_WIDTH);
        this.room = room;
        index = gridIndex;
        highlighted = false;
        occupied = false;
        doorway = false;

        // This assumes each tile is a square.
        // In the future I believe they will be diamonds? So this will need to change.
        touchBox = new Rect(x - TILE_WIDTH_HALF,
                y - TILE_WIDTH_HALF,
                x + TILE_WIDTH_HALF,
                y + TILE_WIDTH_HALF);

        // An easy way to determine if a tile is adjacent to this tile
        // is to see if the boxes intersect when you increase the size of one of the boxes.
        touchBox2x = new Rect(x - TILE_WIDTH,
                y - TILE_WIDTH,
                x + TILE_WIDTH,
                y + TILE_WIDTH);
    }

    public void Occupy(Pawn pawn) {
        occupier = pawn;
        occupied = true;
        pawn.currentTile = this;
    }

    public void Empty() {
        if (occupier != null && occupier.currentTile == this){
            occupier.currentTile = null;
        }
        occupier = null;
        occupied = false;

    }

    // See if the passed-in tile overlaps with this tile.
    public boolean isAdjacent(Tile tile){
        return touchBox2x.intersect(tile.touchBox);
    }

    public Room getRoom(){
        return room;
    }

    @Override
    public void draw(Canvas c, Paint p) {
        p.setColor(Color.argb(255, 249, 129, 0));

        if (highlighted) {
            p.setColor(Color.argb(255, 250, 250, 0));
        }
        if (occupied) {
            p.setColor(Color.argb(255, 200, 50, 0));
        }
        if (doorway) {
            p.setColor(Color.argb(255, 255, 255, 255));
        }
        //
        RectF location = new RectF(x,y, x+(TILE_WIDTH),y+(TILE_WIDTH));
        c.drawBitmap(room.spriteSheet,room.sprites[1],location,p);
        //
        //c.drawCircle((float) x, (float) y, TILE_WIDTH_HALF, p);
    }

    @Override
    public void update(Point camera) {

    }

    @Override
    public boolean handleTouch(MotionEvent motionEvent, Point camera) {
        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
            // Player has touched finger to the screen
            case MotionEvent.ACTION_DOWN:
                float touchX = motionEvent.getX();
                float touchY = motionEvent.getY();

                if (touchBox.contains((int) touchX - camera.x, (int) touchY - camera.y)) {
                    highlighted = true;
                    return true;
                }
                if (!path) {
                    highlighted = false;
                }
        }
        return false;
    }
}
