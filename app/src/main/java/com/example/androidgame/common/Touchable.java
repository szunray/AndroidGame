package com.example.androidgame.common;

import android.graphics.Point;
import android.view.MotionEvent;

public interface Touchable {
    boolean handleTouch(MotionEvent motionEvent, Point camera);
}
