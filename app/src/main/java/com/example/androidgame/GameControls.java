package com.example.androidgame;

import android.view.MotionEvent;

public class GameControls {

    MainActivity mainActivity;
    boolean cameraIsPanning;
    float touchX;
    float touchY;
    public GameControls(MainActivity activity){
        mainActivity = activity;
        cameraIsPanning = false;
         //touchX = 0;
       //  touchY = 0;
    }

    public void handleTouch(MotionEvent motionEvent) {
        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {


            // Player has touched finger to the screen
            case MotionEvent.ACTION_DOWN:
                touchX = motionEvent.getX();
                touchY = motionEvent.getY();
                mainActivity.gameView.gameGrid.checkTouch(touchX,touchY);
                break;

            case MotionEvent.ACTION_MOVE: {


                // Find the index of the active pointer and fetch its position
                cameraIsPanning = true;
                final float activeX = motionEvent.getX();
                final float activeY = motionEvent.getY();

                // Calculate the distance moved

                float dx = activeX - touchX;
                float dy = activeY - touchY;
                mainActivity.gameView.CAMERA_X += dx;
                mainActivity.gameView.CAMERA_Y += dy;

                // Remember this touch position for the next move event
                touchX = activeX;
                touchY = activeY;

                break;
            }
        }
    }
}
