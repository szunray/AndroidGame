package com.example.androidgame.common;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;

public abstract class Element {
    public int x, y, width, height;
    Rect boundary;

    public Element(){
        this(0,0,0,0);
    }

    public Element(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        boundary = new Rect(x, y - height, x + width, y);
    }

    public abstract void update(Point camera);
}
