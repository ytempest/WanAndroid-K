package com.ytempest.wanandroid.widget.jumping;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class ShapeView extends View {

    public static final int SHAPE_CIRCLE = 1;
    public static final int SHAPE_SQUARE = 2;
    public static final int SHAPE_TRIANGLE = 3;

    private Shape mShape = Shape.SQUARE;
    private Paint mShapePaint;
    private Path mTrianglePath;

    public ShapeView(Context context) {
        this(context, null);
    }

    public ShapeView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShapeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mShapePaint = new Paint();
        mShapePaint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        final int width = getMeasuredWidth();
        final int height = getMeasuredHeight();
        mShapePaint.setColor(mShape.color);
        switch (mShape.id) {
            case SHAPE_CIRCLE:
                canvas.drawCircle(width / 2F, height / 2F, width / 2F, mShapePaint);
                break;

            case SHAPE_SQUARE:
                canvas.drawRect(0, 0, width, height, mShapePaint);
                break;

            case SHAPE_TRIANGLE:
                canvas.drawPath(getTrianglePath(), mShapePaint);
                break;

            default:
                break;
        }
    }

    public int getShapeId() {
        return mShape.id;
    }

    public void switchNextShape() {
        Shape[] shapes = Shape.values();
        int next = (mShape.ordinal() + 1) % shapes.length;
        mShape = shapes[next];
        invalidate();
    }

    public Path getTrianglePath() {
        if (mTrianglePath == null) {
            mTrianglePath = new Path();
        }
        mTrianglePath.reset();
        final int width = getMeasuredWidth();
        final int height = getMeasuredHeight();

        final float triangleH = (float) (Math.sqrt(3) / 2 * width);
        mTrianglePath.moveTo(width / 2F, 0);
        mTrianglePath.lineTo(0, triangleH);
        mTrianglePath.lineTo(width, triangleH);

        final float offset = (height - triangleH) / 2F;
        mTrianglePath.offset(0, offset);

        mTrianglePath.close();
        return mTrianglePath;
    }

    private enum Shape {
        SQUARE(SHAPE_SQUARE, 0xAAE84E40),
        CIRCLE(SHAPE_CIRCLE, 0xAA738FFE),
        TRIANGLE(SHAPE_TRIANGLE, 0xAA72D572),
        ;
        public final int id;
        public final int color;

        Shape(int id, int color) {
            this.id = id;
            this.color = color;
        }
    }
}
