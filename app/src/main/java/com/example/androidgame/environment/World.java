package com.example.androidgame.environment;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.Display;
import android.view.MotionEvent;

import com.example.androidgame.GameView;
import com.example.androidgame.characters.Pawn;
import com.example.androidgame.common.MyDrawable;
import com.example.androidgame.common.Element;
import com.example.androidgame.common.Touchable;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.abs;

public class World extends Element implements MyDrawable, Touchable {

    public static ArrayList<Room> Map = new ArrayList<Room>();
    public static Room homeRoom;

    float touchX, touchY;

    public boolean pawnHighlighted = false;
    public Pawn pathBuildingPawn;
    List<Tile> pathTiles = new ArrayList<Tile>();

    public World(Display display) {
        homeRoom = new Room(display);
        Map.add(homeRoom);
    }

    public Tile getStartingTile(){
        return homeRoom.getRandomTile();
    }

    public Room getRoom(Pawn pawn) {
        double closestDistance = Tile.TILE_WIDTH_HALF * 2 + 1;

        //determine the touched tile
        Tile pawnTile = null;
        Room pawnRoom = new Room(GameView.gameDisplay);
        for (Room room : Map) {
            for (Tile tile : room.grid) {

                // yActual is very likely off by about TileWidth/2.
                //I doubt that Android draws circles from the center, outward.
                double tileToPawnDistance = GameView.getDistance((int) pawn.x, (int) (pawn.y), tile.x, tile.y);

                if (tileToPawnDistance < closestDistance) {
                    closestDistance = tileToPawnDistance;
                    pawnTile = tile;
                    pawnRoom = room;
                }
            }
        }

        return pawnRoom;
    }

    public boolean buildPath(Tile touchedTile) {
        for (Room room : Map) {
            // add touched tile to building path if possible
            if (touchedTile != null && touchedTile.highlighted) {
                pathTiles.add(touchedTile);
                clearHighlights();

                List<Tile> adjacentTiles = findAdjacent(touchedTile);
                for (Tile tile : adjacentTiles) {
                    tile.highlighted = true;
                }
            }
        }
        if (pathTiles.size() >= 4) {
            for (Room room : Map) {
                for (Tile tile : room.grid) {
                    if (tile.occupier == pathBuildingPawn) {
                        tile.Empty();
                    }
                }
            }

            sendMoveOrders();
        }

        return true;
    }

    public boolean sendMoveOrders() {

        List<Integer> Orders = new ArrayList<Integer>();
        List<Tile> orders = new ArrayList<Tile>();
        for (Tile tile : pathTiles) {

            //Orders.add(tile.index);
            orders.add(tile);
        }
        pathBuildingPawn.currentTile.Empty();
        //int[] moveOrders = toIntArray(Orders);
        Tile[] moveOrders_Tile = orders.toArray(new Tile[orders.size()]);
        pathBuildingPawn.readOrders(moveOrders_Tile);
        pawnHighlighted = false;
        clearHighlights();
        pathTiles.clear();

        return true;
    }

    int[] toIntArray(List<Integer> list) {
        int[] ret = new int[list.size()];
        for (int i = 0; i < ret.length; i++)
            ret[i] = list.get(i);
        return ret;
    }

    public boolean clearHighlights() {
        for (Room room : Map) {
            for (Tile tile : homeRoom.grid) {
                tile.highlighted = false;
            }
        }
        return true;
    }

    public boolean touchedGameGrid(Tile touchedTile) {

        if (pawnHighlighted) {
            return false;
        }
        pawnHighlighted = false;

        if (touchedTile.occupied) {
            pawnHighlighted = true;
            pathBuildingPawn = touchedTile.occupier;

            List<Tile> adjacentTiles = findAdjacent(touchedTile);
            for (Tile tile : adjacentTiles) {
                tile.highlighted = true;
            }
        }
        touchedTile.highlighted = true;
        return true;
    }

    public ArrayList<Tile> findAdjacent(Tile tile) {
        ArrayList<Tile> tiles = new ArrayList<Tile>();

        for (Room room : Map) {
            for (Tile gridTile : room.grid) {
                //The 1.5 appended at the end of this equation is serviceable,
                //but it could be better
                if (GameView.getDistance(tile.x, tile.y, gridTile.x, gridTile.y) < Tile.TILE_WIDTH_HALF / 2 * 1.5) {
                    tiles.add(gridTile);
                }
            }
        }

        return tiles;
    }

    public static void convertToIso(Tile tile, Display display) {
        Point size = new Point();
        display.getSize(size);
        int displayWidth = size.x;
        int displayHeight = size.y;
        int[] coordinates = carToIso(tile.x, tile.y);
        tile.x = coordinates[0] + (displayWidth / 2);
        tile.y = coordinates[1] + (displayHeight / 4);
    }

    //Cartesian to isometric:
    public static int[] carToIso(int cartX, int cartY) {
        int isoX = cartX - cartY;
        int isoY = (cartX + cartY) / 2;
        int[] ans = {isoX, isoY};
        return ans;
    }

    @Override
    public void draw(Canvas c, Paint p) {
        for (Room room : Map) {
            room.draw(c, p);
        }

        // If the current pawn is moving, highlight the path tiles in a different color.
        for (Tile tile : pathTiles) {
            p.setColor(Color.argb(255, 250, 0, 0));
            c.drawCircle((float) tile.x, (float) tile.y, Tile.TILE_WIDTH_HALF, p);
        }
    }

    public boolean checkTouch(Tile tile) {
        if (pawnHighlighted) {
            buildPath(tile);
        }
        if (touchedGameGrid(tile)) {
            return true;
        }
        return false;
    }

    @Override
    public boolean handleTouch(MotionEvent motionEvent, Point camera) {

        Tile lastTouchedTile = null;
        for (Room room : Map) {
            if (room.handleTouch(motionEvent, camera)) {
                lastTouchedTile = room.lastTouchedTile;
                break;
            }
        }

        if (lastTouchedTile != null) {
            checkTouch(lastTouchedTile);
            return true;
        }
        return false;
    }

    @Override
    public void update(Point camera) {

    }
}
