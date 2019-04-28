package com.alfrescodev.client.figure;

import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.CssColor;

/**
 *
 * This figure represents circle on canvas.
 *
 */
public class Circle {

    private static final CssColor BLACK = CssColor.make("rgba(0,0,0,1)");
    private static final int TEXT_Y_PADDING = 4;

    private CssColor color;
    private double posX, posY;
    private double radius;
    private int id;
    private int level;

    public Circle(double x, double y, double radius, CssColor color) {
        this.color = color;
        this.posX = x;
        this.posY = y;
        this.radius = radius;
    }

    public Circle(double x, double y, double radius, CssColor color, int id) {
        this(x, y, radius, color);
        this.id = id;
    }

    public void draw(Context2d context) {
        context.setFillStyle(color);
        context.beginPath();
        context.arc(posX, posY, radius, 0, Math.PI * 2.0, true);
        context.setTextAlign(Context2d.TextAlign.CENTER);
        context.closePath();
        context.fill();

        context.setFillStyle(BLACK);
        context.beginPath();
        context.strokeText("" + id, posX, posY + TEXT_Y_PADDING);
        context.closePath();
        context.fill();
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Circle circle = (Circle) o;

        if (id != circle.id) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id;
    }

    public double getPosX() {
        return posX;
    }

    public double getPosY() {
        return posY;
    }
}
