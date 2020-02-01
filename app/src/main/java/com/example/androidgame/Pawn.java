package com.example.androidgame;

public class Pawn {
    int xPosition;
    int yPosition;
    int speed;
    int[] moveOrders;
    Tile[]moveOrders_Tiles = new Tile[0];
    int moveOrderIndex;
    boolean isMoving;

    Tile tile;

    GameGrid map;

    public Pawn(GameGrid gameGrid){
        xPosition = gameGrid.homeRoom.grid[0].xpos;
        yPosition = gameGrid.homeRoom.grid[0].ypos;
        speed = 10;
        isMoving = true;
        map = gameGrid;
        moveOrderIndex = 0;
        moveOrders = new int[]{1,7,8,2,3};

    }

    public Tile getTile(){
        return map.getTile(this);
    }

    public Room getRoom(){
        return map.getRoom(this);
    }
    public void newOrders(int[] newOrders){

        moveOrderIndex = 0;
        moveOrders = newOrders;
        isMoving = true;
    }

    public void readOrders(Tile[] newOrders){
        moveOrderIndex = 0;
        moveOrders_Tiles = newOrders;
        isMoving = true;
    }
    public void move(){


        if (moveOrderIndex == moveOrders_Tiles.length){
            isMoving = false;
            tile = getTile();
            tile.Occupy(this);
        }
        if(!isMoving){
            tile = getTile();
            if(getTile().doorway){
                // using a blank room temporarily
                Tile door = getTile();
                Room blankRoom = new Room(GameView.gameDisplay);
                //
                getRoom().explore(blankRoom, door);
                //GameGrid.homeRoom.updateCenter();
            }
            return;
        }
        Tile target;
            target = moveOrders_Tiles[moveOrderIndex];


        if (xPosition == (target.xpos)) {

        }
        else if ((xPosition + speed) < target.xpos ){
            xPosition = xPosition +speed;
        }
        else if (xPosition < target.xpos){
            xPosition = target.xpos;
        }
        else if (xPosition - speed > target.xpos){
            xPosition = xPosition - speed;
        }
        else if (xPosition > target.xpos){
            xPosition = target.xpos;
        }

        if (yPosition == target.ypos){

        }
        else if (yPosition + speed <target.ypos) {
            yPosition = yPosition + speed;
        }
        else if (yPosition < target.ypos){
            yPosition = target.ypos;
        }
        else if (yPosition - speed  > target.ypos){
            yPosition = yPosition - speed;
        }
        else if (yPosition > target.ypos){
            yPosition = target.ypos;
        }

        if(xPosition == target.xpos && yPosition == target.ypos ){
            moveOrderIndex ++;
        }
    }

}
