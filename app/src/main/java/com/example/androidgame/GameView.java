package com.example.androidgame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.androidgame.characters.Pawn;
import com.example.androidgame.environment.Background;
import com.example.androidgame.environment.World;

public class GameView extends SurfaceView implements Runnable {

    // helps center the gamegrid and will likely assist with the camera.
    public static Display gameDisplay;

    // Our Thread
    Thread gameThread = null;

    // Will be used with Paint and Canvas
    SurfaceHolder ourHolder;

    // Is the game playing?
    volatile boolean playing;

    // Canvas and Paint objects
    Canvas canvas;
    Paint paint;
    World world;
    Pawn[] pawns;
    UserInterface ui;
    Background bg;

    boolean cameraIsPanning;
    boolean givingCommands;
    float touchX;
    float touchY;

    // Camera location
    Point camera = new Point(0, 0);

    public GameView(Context context, Display display) {
        super(context);

        //Initialize Holder and Paint objects
        ourHolder = getHolder();
        paint = new Paint();
        gameDisplay = display;
        world = new World(display);
        pawns = new Pawn[1];
        pawns[0] = new Pawn(world.getStartingTile());
        ui = new UserInterface();
        bg = new Background(context);
    }

    // Calculate animations & movement outside of the draw cycle,
    // so everything is given the same amount of time to animate.
    private void update() {
        bg.update(camera);
        world.update(camera);
        for (Pawn pawn : pawns) {
            pawn.update(camera);
        }
        ui.update(camera);
    }

    // Draw the scene
    public void drawScene() {
        if (ourHolder.getSurface().isValid()) {
            // Lock the canvas ready to draw
            // Make the drawing surface our canvas object
            canvas = ourHolder.lockCanvas();

            canvas.save();
            canvas.translate(camera.x, camera.y);

            bg.draw(canvas, paint);
            world.draw(canvas, paint);
            for (Pawn pawn : pawns) {
                pawn.draw(canvas, paint);
            }

            canvas.restore();

            ui.draw(canvas, paint);

            ourHolder.unlockCanvasAndPost(canvas);
        }
    }

    public void handleTouch(MotionEvent motionEvent) {
        if (ui.handleTouch(motionEvent, camera)){
            cameraIsPanning = false;
            return;
        }
        for (Pawn pawn : pawns) {
            if (pawn.handleTouch(motionEvent, camera)){
                cameraIsPanning = false;
                return;
            }
        }
        if (world.handleTouch(motionEvent, camera)){
            cameraIsPanning = false;
            return;
        }

        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
            // Player has touched finger to the screen
            case MotionEvent.ACTION_DOWN:
                cameraIsPanning = false;
                touchX = motionEvent.getX();
                touchY = motionEvent.getY();
               // mainActivity.gameView.gameGrid.checkTouch(touchX, touchY);
                break;

            case MotionEvent.ACTION_MOVE: {

                // Find the index of the active pointer and fetch its position
                final float activeX = motionEvent.getX();
                final float activeY = motionEvent.getY();

                // If we didn't get an "ACTION_DOWN" event before panning, set touchX and touchY here
                // so that the scene doesn't dump across the display.
                if (!cameraIsPanning){
                    touchX = activeX;
                    touchY = activeY;
                    cameraIsPanning = true;
                    return;
                }

                cameraIsPanning = true;

                // Calculate the distance moved
                float dx = activeX - touchX;
                float dy = activeY - touchY;
                camera.x += dx;
                camera.y += dy;

                // Remember this touch position for the next move event
                touchX = activeX;
                touchY = activeY;

                break;
            }
        }
    }

    public static double getDistance(int x1, int y1, int x2, int y2) {
        double distance = Math.sqrt(((x2 - x1) * (x2 - x1)) + ((y2 - y1) * (y2 - y1)));
        return distance;
    }

    @Override
    public void run() {
        while (playing) {
            update();
            drawScene();
        }
    }

    // shutdown our thread.
    public void pause() {
        playing = false;

        try {
            gameThread.join();
        } catch (InterruptedException e) {
            Log.e("Error:", "joining thread");
        }
    }

    // start our thread.
    public void resume() {
        playing = true;
        gameThread = new Thread(this);
        gameThread.start();
    }
}
