package com.example.androidgame;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.MotionEvent;

import com.example.androidgame.characters.Pawn;
import com.example.androidgame.common.MyDrawable;
import com.example.androidgame.common.Touchable;

import static com.example.androidgame.characters.Pawn.PAWN_RADIUS;

public class UserInterface implements MyDrawable, Touchable {

    int x = 100, y = 100;
    float touchX, touchY;
    boolean touched;

    Rect healthBarRect = new Rect(100, 100, 600, 250);


    public UserInterface() {
    }

    @Override
    public void draw(Canvas c, Paint p) {
        drawHealthBar(c, p);
    }

    private void drawHealthBar(Canvas c, Paint p) {
        // Background transparent square to hold all the health bar info
        Paint transparent = new Paint();
        transparent.setARGB(128, 255, 255, 255);
        c.drawRect(healthBarRect.left,
                healthBarRect.top,
                healthBarRect.right,
                healthBarRect.bottom, transparent);

        // Draw the pawn that this health bar applies to.
        // Should just call pawn.draw here in the future
        p.setColor(Color.argb(255, 0, 129, 100));
        c.drawCircle((float) 175, (float) 175, PAWN_RADIUS, p);

        // Red background of the health bar
        p.setColor(Color.RED);
        c.drawRect(250, 125, 550, 225, p);

        // Green foreground of the health bar
        p.setColor(Color.GREEN);
        c.drawRect(255, 130, 545, 220, p);
    }

    @Override
    public boolean handleTouch(MotionEvent motionEvent, Point camera) {

        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
            // Player has touched finger to the screen
            case MotionEvent.ACTION_DOWN:
                touchX = motionEvent.getX();
                touchY = motionEvent.getY();
                touched = healthBarRect.contains((int) touchX, (int) touchY);
                return touched;

            case MotionEvent.ACTION_UP:
                // We shouldn't actually do anything until the user releases their finger,
                // which happens here
                touchX = motionEvent.getX();
                touchY = motionEvent.getY();
                if (touched && healthBarRect.contains((int) touchX, (int) touchY)) {
                    touched = false;
                    return true;
                }
                return false;

            case MotionEvent.ACTION_MOVE: {
                if (!touched)
                    return false;

                final float activeX = motionEvent.getX();
                final float activeY = motionEvent.getY();

                if (healthBarRect.contains((int) activeX, (int) activeY)) {
                    // Some UIs may have draggable elements.
                    // Just do nothing for now.
                    return true;
                }
            }
        }

        return false;
    }

    public void update(Point camera) {
    }
}
