/*
 * Copyright (c) 2018.
 *
 * Anthony Ngure
 *
 * Email : anthonyngure25@gmail.com
 */

package ke.co.toshngure.showcase.shape;

import android.graphics.Canvas;
import android.graphics.Paint;

import ke.co.toshngure.showcase.target.Target;


/**
 * Specifies a shape of the target (e.g circle, rectangle).
 * Implementations of this interface will be responsible to draw the shape
 * at specified center point (x, y).
 */
public interface Shape {

    /**
     * Draw shape on the canvas init the center at (x, y) using Paint object provided.
     */
    void draw(Canvas canvas, Paint paint, int x, int y, int padding);

    /**
     * Get width of the shape.
     */
    int getWidth();

    /**
     * Get height of the shape.
     */
    int getHeight();

    /**
     * Update shape bounds if necessary
     */
    void updateTarget(Target target);

}
