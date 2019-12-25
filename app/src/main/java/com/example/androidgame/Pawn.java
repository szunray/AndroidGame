package com.example.androidgame;

public class Pawn {
    int xPosition;
    int yPosition;
    int speed;
    int[] moveOrders;
    int moveOrderIndex;
    boolean isMoving;
    GameGrid map;

    public Pawn(GameGrid gameGrid){
        xPosition = gameGrid.grid[0].xpos;
        yPosition = gameGrid.grid[0].ypos;
        speed = 10;
        isMoving = true;
        map = gameGrid;
        moveOrderIndex = 0;
        moveOrders = new int[]{1,7,8,2,3};
    }
    public void move(){
        if (moveOrderIndex == moveOrders.length){
            isMoving = false;
            map.grid[moveOrders[moveOrderIndex-1]].Occupy(this);
        }
        if(!isMoving){
            return;
        }
        Tile target = map.grid[moveOrders[moveOrderIndex]];

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
