package com.cloudfoyo.magazine.extras;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by nilesh on 7/12/15.
 */
public class HorizontalLine extends View {

    Paint p = new Paint();
    public HorizontalLine(Context context) {
        super(context);

    }

    public HorizontalLine(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HorizontalLine(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int offset = canvas.getHeight() / 2;

        p.setAntiAlias(true);

        p.setStrokeWidth(0);
        p.setStyle(Paint.Style.STROKE);
        p.setColor(Color.LTGRAY);

        canvas.drawLine(0, offset, canvas.getWidth(), offset, p);
    }
}
