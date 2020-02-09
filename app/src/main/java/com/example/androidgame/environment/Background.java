package com.example.androidgame.environment;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.support.v4.content.res.ResourcesCompat;
import android.util.DisplayMetrics;

import com.example.androidgame.R;
import com.example.androidgame.common.MyDrawable;
import com.example.androidgame.common.Element;

public class Background extends Element implements MyDrawable {

    Drawable bgImage;

    double imageScale = 2.0;
    int imageWidth = (int) (1920 * imageScale);
    int imageHeight = (int) (1080 * imageScale);

    int displayWidth, displayHeight;
    int left, top, right, bottom;

    public Background(Context context) {
        // Initialize the background
        bgImage = ResourcesCompat.getDrawable(context.getResources(), R.drawable.background, null);

        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        displayWidth = dm.widthPixels;
        displayHeight = dm.heightPixels;

        left = -imageWidth / 2 + displayWidth / 2;
        top = -imageHeight / 2 + displayHeight / 2;
        right = -left;
        bottom = -top;

        bgImage.setBounds(left, top, right, bottom);
    }

    // Do any fancy animations to the background here
    public void update(Point camera) {
        left = -imageWidth / 2 + displayWidth / 2 - camera.x / 2;
        top = -imageHeight / 2 + displayHeight / 2 - camera.y / 2;
        right = left + imageWidth;
        bottom = top + imageHeight;

        bgImage.setBounds(left, top, right, bottom);
    }

    @Override
    public void draw(Canvas c, Paint p) {
        bgImage.draw(c);
    }
}
