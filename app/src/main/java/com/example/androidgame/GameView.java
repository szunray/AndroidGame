package com.example.androidgame;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameView extends SurfaceView implements Runnable {

    // Our Thread
    Thread gameThread = null;

    //Will be used with Paint and Canvas
    SurfaceHolder ourHolder;

    //Is the game playing?
    volatile boolean playing;

    //Canvas and Paint objects
    Canvas canvas;
    Paint paint;
    GameGrid gameGrid;
    Pawn[] pawns;

    int CAMERA_X = 0;
    int CAMERA_Y = 0 ;
    //Tracks the game's Framerate
    long fps;

    //Used to help calculate FPS
    long thisTimeFrame;

    // helps center the gamegrid and will likely assist with the camera.
    Display gameDisplay;

    public GameView(Context context, Display display) {
        super(context);

        //Initialize Holder and Paint objects
        ourHolder = getHolder();
        paint = new Paint();
        gameDisplay = display;
        gameGrid = new GameGrid(display);
        pawns = new Pawn[1];
        pawns[0] = new Pawn(gameGrid);

    }
    public void drawScene(){
        if (ourHolder.getSurface().isValid()) {

            // Lock the canvas ready to draw
            // Make the drawing surface our canvas object

            canvas = ourHolder.lockCanvas();


            // Draw the background color
            canvas.drawColor(Color.argb(255, 26, 128, 182));
            drawGrid();
            drawPawns();

            ourHolder.unlockCanvasAndPost(canvas);

        }
        }
    public void drawGrid(){

            // Choose the brush color for drawing
            paint.setColor(Color.argb(255, 249, 129, 0));
            for (int x = 0; x <gameGrid.grid.length; x++){
                canvas.drawCircle((float)gameGrid.grid[x].xpos + CAMERA_X, (float)gameGrid.grid[x].ypos + CAMERA_Y,gameGrid.TILE_WIDTH/2,paint);
            }
            //canvas.drawCircle(x, y, radius, paint);

    }

    public void drawPawns(){
        paint.setColor(Color.argb(255, 0, 129, 100));

        for (Pawn pawn : pawns){
            pawn.move();
            canvas.drawCircle((float)pawn.xPosition + CAMERA_X, (float)pawn.yPosition + CAMERA_Y,gameGrid.TILE_WIDTH/3,paint);
        }
    }

    @Override
    public void run() {
        while (playing) {

            //capture the current time in milliseconds
            long startFrameTime = System.currentTimeMillis();
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
