package com.example.androidgame.characters;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.MotionEvent;

import com.example.androidgame.GameView;
import com.example.androidgame.common.MyDrawable;
import com.example.androidgame.common.Element;
import com.example.androidgame.common.Touchable;
import com.example.androidgame.environment.World;
import com.example.androidgame.environment.Room;
import com.example.androidgame.environment.Tile;

public class Pawn extends Element implements MyDrawable, Touchable {

    public static int PAWN_RADIUS = Tile.TILE_WIDTH_HALF / 2;

    int speed;
    int[] moveOrders;
    Tile[] moveOrders_Tiles = new Tile[0];
    int moveOrderIndex;
    boolean isMoving;

    public boolean isHighlighted;

    public Tile currentTile;

    World map;

    public Pawn(Tile currentTile) {
        super(World.homeRoom.grid[0].x, World.homeRoom.grid[0].y, 150, 150);

        speed = 10;
        isMoving = true;
        //map = gameGrid;
        moveOrderIndex = 0;
        moveOrders = new int[]{1, 7, 8, 2, 3};

        this.currentTile = currentTile;
        currentTile.Occupy(this);
    }

    public void newOrders(int[] newOrders) {

        moveOrderIndex = 0;
        moveOrders = newOrders;
        isMoving = true;
    }

    public void readOrders(Tile[] newOrders) {
        moveOrderIndex = 0;
        moveOrders_Tiles = newOrders;
        isMoving = true;
    }

    public void move() {

        if (moveOrderIndex == moveOrders_Tiles.length) {
            isMoving = false;
        }
        if (!isMoving) {
            if (currentTile.doorway) {
                // using a blank room temporarily
                Room blankRoom = new Room(World.gameView);
                //
                currentTile.getRoom().explore(blankRoom, currentTile);
                //World.homeRoom.updateCenter();
            }
            return;
        }
        Tile target;
        target = moveOrders_Tiles[moveOrderIndex];


        if (x == (target.x)) {

        } else if ((x + speed) < target.x) {
            x = x + speed;
        } else if (x < target.x) {
            x = target.x;
        } else if (x - speed > target.x) {
            x = x - speed;
        } else if (x > target.x) {
            x = target.x;
        }

        if (y == target.y) {

        } else if (y + speed < target.y) {
            y = y + speed;
        } else if (y < target.y) {
            y = target.y;
        } else if (y - speed > target.y) {
            y = y - speed;
        } else if (y > target.y) {
            y = target.y;
        }

        if (x == target.x && y == target.y) {
            currentTile.Empty();
            target.Occupy(this);
            moveOrderIndex++;
        }
    }

    @Override
    public void draw(Canvas c, Paint p) {
        p.setColor(Color.argb(255, 0, 129, 100));
        c.drawCircle((float) x, (float) y, PAWN_RADIUS, p);
    }

    @Override
    public void update(Point camera) {
        move();
    }

    @Override
    public boolean handleTouch(MotionEvent motionEvent, Point camera) {
        return false;
    }
}
