package com.alfrescodev.client;

import com.alfrescodev.client.data.Hierarchy;
import com.alfrescodev.client.data.HierarchyPathFinder;
import com.alfrescodev.client.figure.Circle;
import com.alfrescodev.client.figure.HierarchyRepresentation;
import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.CssColor;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;

import java.util.Collection;
import java.util.HashMap;

/**
 *
 * Main entry point for this GWT application.
 * Entry point uses CanvasApp.html. It creates browser
 * canvas and adds it to div with id "canvas-container".
 * If client's browser does not supports the canvas feature
 * the error message will be displayed.
 *
 * @author alfrescodev.com
 */
public class CanvasApp implements EntryPoint {

    static final String holderId = "canvas-container";

    static final String upgradeMessage = "Your browser does not support the HTML5 Canvas. Please upgrade your browser to view this page.";

    static final int height = 400;
    static final int width = 400;

    Canvas canvas;

    final CssColor redrawColor = CssColor.make("rgba(255,0,0,1)");
    Context2d context;

    public void onModuleLoad() {
        canvas = Canvas.createIfSupported();
        if (canvas == null) {
            RootPanel.get(holderId).add(new Label(upgradeMessage));
            return;
        }
        canvas.setWidth(width + "px");
        canvas.setHeight(height + "px");
        canvas.setCoordinateSpaceWidth(width);
        canvas.setCoordinateSpaceHeight(height);
        RootPanel.get(holderId).add(canvas);
        context = canvas.getContext2d();
        Hierarchy hierarchy = Hierarchy.newTestHierarchy3();
        //Hierarchy hierarchy = Hierarchy.generateRandomHierarchy(30);
        Hierarchy originHierarchy = hierarchy.clone();
        HierarchyRepresentation.drawHierarchy(canvas, hierarchy);
        Collection<Integer> c = HierarchyPathFinder.createListFromHierarchy(originHierarchy);
        printList(c);
    }

    private void printList(Collection<Integer> list) {
        StringBuilder listText = new StringBuilder("");
        int i = 0;
        for (int id:list) {
            listText.append(id);
            if (i < (list.size() - 1)) {
                listText.append(", ");
            }
            else {
                listText.append(";");
            }
            ++i;
        }
        RootPanel.get("nodes-list").add(new InlineLabel(listText.toString()));
    }
}
