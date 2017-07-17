package com.xiaopo.flying.raindrop;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import java.util.Random;

/**
 * @author wupanjie
 */

public class RainDrop {
    private static final String TAG = "RainDrop";
    private static final PorterDuffXfermode SRC_OVER_MODE = new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER);
    private static final PorterDuffXfermode SRC_IN_MODE = new PorterDuffXfermode(PorterDuff.Mode.SRC_IN);
    private static final PorterDuffXfermode SCREEN_MODE = new PorterDuffXfermode(PorterDuff.Mode.SCREEN);

    private Bitmap rainDropBitmap;
    private Bitmap bufferBitmap;

    private Canvas canvas;
    private Canvas bufferCanvas;

    private final int width;
    private final int height;
    private float scale;
    private DropOptions options;

    private int dropletsPixelDensity = 1;

    private final Bitmap dropColor;
    private final Bitmap dropAlpha;

    private Paint bitmapPaint;
    private Paint clearPaint;

    private Random random = new Random();

    public RainDrop(int width, int height, float scale, Bitmap dropColor, Bitmap dropAlpha, DropOptions options) {
        this.width = width;
        this.height = height;
        this.scale = scale;
        this.dropColor = dropColor;
        this.dropAlpha = dropAlpha;
        this.options = options == null ? new DropOptions() : options;

        init();
    }

    private void init() {
        this.bitmapPaint = new Paint();
        bitmapPaint.setFilterBitmap(true);

        this.clearPaint = new Paint();
        clearPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));

        rainDropBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        bufferBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(rainDropBitmap);
        bufferCanvas = new Canvas(bufferBitmap);

        draw();
    }

    private void draw() {
        canvas.save();
        canvas.translate(600, 800);

        bufferCanvas.drawPaint(clearPaint);

        bitmapPaint.setXfermode(SRC_OVER_MODE);
        bufferCanvas.drawBitmap(dropColor, new Matrix(), bitmapPaint);

        bitmapPaint.setXfermode(SCREEN_MODE);
        bufferCanvas.drawColor(Color.argb(0, 0, 0, 128));

        bitmapPaint.setXfermode(SRC_OVER_MODE);
        canvas.drawBitmap(dropAlpha, new Matrix(), bitmapPaint);

        bitmapPaint.setXfermode(SRC_IN_MODE);
        canvas.drawBitmap(bufferBitmap, new Matrix(), bitmapPaint);

        canvas.restore();
    }

    public Bitmap getRainDropBitmap() {
        return rainDropBitmap;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
