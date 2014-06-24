package com.alfrescodev.client.widget;

import com.alfrescodev.client.data.Node;
import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;

/**
 * This widget represents a circle.
 *
 * @author Alfrescodev.com
 *
 */
public class CircleWidget extends Composite {

    private static final String STYLE_NAME = "circle-widget";
    public static final int Z_INDEX = 10;
    private double posX, posY;
    private double radius;
    private FlowPanel mainWidget;
    private static CircleWidget hightlightedInstance;
    private Canvas canvas;

    /**
     * Complicated node that the CircleWidget represents
     */
    private Node node;

    /**
     * Class constructor.
     *
     * @param node
     * @param x
     * @param y
     * @param radius
     * @param color
     * @param id
     * @param canvas
     */
    public CircleWidget(Node node, double x, double y, int radius,
                        String color, int id, Canvas canvas) {
        this.node = node;
        this.canvas = canvas;
        mainWidget = new FlowPanel();
        initWidget(mainWidget);
        setWidth(radius * 2 + "px");
        setHeight(radius * 2 + "px");
        setStyleName(STYLE_NAME);
        this.posX = x;
        this.posY = y;
        this.radius = radius;
        getElement().setInnerHTML(""+id);
        getElement().getStyle().setLineHeight(radius * 2, Style.Unit.PX);
        getElement().getStyle().setPosition(Style.Position.ABSOLUTE);
        getElement().getStyle().setTop(y-radius, Style.Unit.PX);
        getElement().getStyle().setLeft(x-radius, Style.Unit.PX);
        getElement().getStyle().setBackgroundColor(color);
        getElement().getStyle().setFontSize(14, Style.Unit.PX);
        getElement().getStyle().setFontWeight(Style.FontWeight.BOLD);
        getElement().getStyle().setZIndex(Z_INDEX);
    }

    /**
     * The method increment z-index of the mouse-overed widget.
     */
    public synchronized void incrementZindex() {
        if (hightlightedInstance == null) {
            hightlightedInstance = this;
        }
        hightlightedInstance.getElement().getStyle().setZIndex(Z_INDEX);
        getElement().getStyle().setZIndex(Z_INDEX+1);
        hightlightedInstance = this;
    }

    public HandlerRegistration addMouseOverHandler(MouseOverHandler handler) {
        return addDomHandler(handler, MouseOverEvent.getType());
    }

    public HandlerRegistration addMouseOutHandler(MouseOutHandler handler) {
        return addDomHandler(handler, MouseOutEvent.getType());
    }

    public double getPosY() {
        return posY;
    }

    public double getPosX() {
        return posX;
    }

    public Node getNode() {
        return node;
    }

    public double getRadius() {
        return radius;
    }

    public Canvas getCanvas() {
        return canvas;
    }

    public FlowPanel getMainWidget() {
        return mainWidget;
    }
}
