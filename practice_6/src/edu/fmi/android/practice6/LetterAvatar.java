package edu.fmi.android.practice6;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.ColorDrawable;

import java.util.Random;

public class LetterAvatar extends ColorDrawable {

    private final String character;
    private final Paint textPaint;
    private final Paint borderPaint;
    private static final int STROKE_WIDTH = 10;
    private static final float SHADE_FACTOR = 0.9f;

    private final Resources mResources;

    @SuppressLint("NewApi") public LetterAvatar(Context context, String character, int color) {
        super(color);

        this.character = character;
        this.textPaint = new Paint();
        this.borderPaint = new Paint();

        mResources = context.getResources();

        setColor(randomColor());

        // text paint settings
        textPaint.setColor(Color.WHITE);
        textPaint.setAntiAlias(true);
        textPaint.setFakeBoldText(true);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setTextAlign(Paint.Align.CENTER);

        // border paint settings
        borderPaint.setColor(getDarkerShade(getColor()));
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setStrokeWidth(STROKE_WIDTH);
    }

    private int getDarkerShade(int color) {
        return Color.rgb((int)(SHADE_FACTOR * Color.red(color)),
                (int)(SHADE_FACTOR * Color.green(color)),
                (int)(SHADE_FACTOR * Color.blue(color)));
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        // draw border
        canvas.drawRect(getBounds(), borderPaint);

        // draw text
        int width = canvas.getWidth();
        int height = canvas.getHeight();
        textPaint.setTextSize(height / 2);
        canvas.drawText(character, width/2, height/2 - ((textPaint.descent() + textPaint.ascent()) / 2) , textPaint);
    }

    private int randomColor() {
        Random random = new Random();
        String[] colorsArr = mResources.getStringArray(R.array.colors);
        return Color.parseColor(colorsArr[random.nextInt(colorsArr.length)]);
    }

    @Override
    public void setAlpha(int alpha) {
        textPaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        textPaint.setColorFilter(cf);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }
}