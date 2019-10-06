package com.example.androidgame;

import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.MotionEvent;

public class MainActivity extends AppCompatActivity {

    GameView gameView;
    GameControls gameControls;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Display display = getWindowManager().getDefaultDisplay();

        // Initialize gameView and set it as the view
        gameView = new GameView(this, display);

        setContentView(gameView);
        gameControls = new GameControls(this);
    }

    // This method executes when the player starts the game
    @Override
    protected void onResume() {
        super.onResume();

        // Tell the gameView resume method to execute
        gameView.resume();
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        gameControls.handleTouch(motionEvent);
        return true;
    }
    // This method executes when the player quits the game
    @Override
    protected void onPause() {
        super.onPause();

        // Tell the gameView pause method to execute
        gameView.pause();
    }

}
