package com.alfrescodev.client;

import com.alfrescodev.client.data.Hierarchy;
import com.alfrescodev.client.figure.HierarchyRepresentation;
import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;

/**
 *
 * Main entry point for this GWT application.
 * Entry point uses CanvasApp.html. It creates browser
 * canvas and adds it to div with id "canvas-container".
 * If client's browser does not supports the canvas feature
 * the error message will be displayed.
 *
 */
public class CanvasApp implements EntryPoint {

    public static final String CANVAS_HOLDER_ID = "canvas-wrapper";
    private static final String BUTTONS_HOLDER_ID = "control-buttons";
    private static final String BUTTON_STYLE_NAME = "action-button";
    private static final int BUTTON_WIDTH = 100;
    private static final int BUTTON_MARGIN_X = 20;
    private static final String CLEAR_CONFIRM_MESSAGE = "Are you sure to clear workspace?";

    private static final String upgradeMessage = "Your browser does not support the HTML5 Canvas. " +
            "Please upgrade your browser to view this page.";

    private static final int height = 400;
    private static final int width = 400;
    private static final int BUTTONS_PADDING_Y = 12;
    private static final int HIERARHY_DEFAULT_MAX_COUNT = 30;

    /**
     * The main hierarchy to paint.
     */
    private Hierarchy hierarchy;

    /**
     * The main module method.
     */
    public void onModuleLoad() {
        Canvas canvas = Canvas.createIfSupported();
        if (canvas == null) {
            RootPanel.get(CANVAS_HOLDER_ID).add(new Label(upgradeMessage));
            return;
        }
        canvas.setWidth(width + "px");
        canvas.setHeight(height + "px");
        canvas.setCoordinateSpaceWidth(width);
        canvas.setCoordinateSpaceHeight(height);
        RootPanel canvasHolder = RootPanel.get(CANVAS_HOLDER_ID);
        hierarchy = Hierarchy.generateRandomHierarchy(HIERARHY_DEFAULT_MAX_COUNT);
        new HierarchyRepresentation(hierarchy, canvas, canvasHolder).drawHierarchy();
        addButtons();
    }

    private Button createNewNodeButton() {
        Button button = new Button("New node");
        button.addStyleName(BUTTON_STYLE_NAME);
        button.setWidth(BUTTON_WIDTH + "px");
        button.getElement().getStyle().setMarginRight(BUTTON_MARGIN_X, Style.Unit.PX);
        button.addClickHandler(event -> addNewNode());
        return button;
    }

    private void addNewNode() {
        hierarchy.addNewNode();
        HierarchyRepresentation.redraw();
    }

    private Button createClearButton() {
        Button button = new Button("Clear");
        button.addStyleName(BUTTON_STYLE_NAME);
        button.setWidth(BUTTON_WIDTH + "px");
        button.getElement().getStyle().setMarginLeft(BUTTON_MARGIN_X, Style.Unit.PX);
        button.addClickHandler(event -> {
            if (Window.confirm(CLEAR_CONFIRM_MESSAGE)) {
                clearWorkspace();
            }
        });
        return button;
    }

    private void clearWorkspace() {
        hierarchy.getNodes().clear();
        HierarchyRepresentation.redraw();
    }

    /**
     * The method adds "New node" and "Clear" buttons to page.
     */
    private void addButtons() {
        FlowPanel buttonsPanel = new FlowPanel();
        buttonsPanel.getElement().getStyle().setDisplay(Style.Display.BLOCK);
        buttonsPanel.getElement().getStyle().setTextAlign(Style.TextAlign.CENTER);
        buttonsPanel.getElement().getStyle().setPaddingTop(BUTTONS_PADDING_Y, Style.Unit.PX);
        buttonsPanel.setWidth("100%");
        HorizontalPanel buttonsHorizontalPanel = new HorizontalPanel();
        buttonsHorizontalPanel.add(createNewNodeButton());
        buttonsHorizontalPanel.add(createClearButton());
        buttonsHorizontalPanel.getElement().getStyle().setDisplay(Style.Display.INLINE_BLOCK);
        buttonsPanel.add(buttonsHorizontalPanel);
        RootPanel.get(BUTTONS_HOLDER_ID).add(buttonsPanel);
    }

}
