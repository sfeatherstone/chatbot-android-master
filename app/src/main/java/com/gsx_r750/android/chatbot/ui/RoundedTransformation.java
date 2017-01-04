package com.gsx_r750.android.chatbot.ui;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Shader;

public class RoundedTransformation implements com.squareup.picasso.Transformation {
    private final int margin;  // dp

    // margin is the board in dp
    public RoundedTransformation(final int margin) {
        this.margin = margin;
    }

    @Override
    public Bitmap transform(final Bitmap source) {

        int radius = Math.min((source.getWidth() - margin) / 2, (source.getHeight() - margin) / 2);

        final Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setShader(new BitmapShader(source, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));

        Bitmap output = Bitmap.createBitmap(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        canvas.drawCircle((source.getWidth() - margin) / 2, (source.getHeight() - margin) / 2, radius - 2, paint);

        if (source != output) {
            source.recycle();
        }

        Paint paint1 = new Paint();
        paint1.setColor(Color.WHITE);
        paint1.setStyle(Paint.Style.STROKE);
        paint1.setAntiAlias(true);
        paint1.setStrokeWidth(2);
        canvas.drawCircle((source.getWidth() - margin) / 2, (source.getHeight() - margin) / 2, radius - 2, paint1);


        return output;
    }

    @Override
    public String key() {
        return "rounded";
    }
}