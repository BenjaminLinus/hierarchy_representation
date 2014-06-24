package com.alfrescodev.client.figure;

import com.alfrescodev.client.action.RedrawHandler;
import com.alfrescodev.client.data.Hierarchy;
import com.alfrescodev.client.data.HierarchyPathFinder;
import com.alfrescodev.client.data.IndexHolder;
import com.alfrescodev.client.data.Node;
import com.alfrescodev.client.widget.CircleWidget;
import com.alfrescodev.client.widget.NodeHighlightContextMenu;
import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.CssColor;
import com.google.gwt.dom.client.Touch;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

import java.util.*;

/**
 *
 * The class draws the hierarchy object.
 *
 * @author Alfrescodev.com
 *
 */
public class HierarchyRepresentation {

    private static final CssColor REDRAW_COLOR = CssColor.make("rgba(255,0,0,1)");
    private static final CssColor LIGHTBLUE_COLOR = CssColor.make("rgba(222,243,255,1)");
    private static final CssColor BLUE_COLOR = CssColor.make("rgba(146,208,243,1)");
    private static final CssColor GRAYBLUE_COLOR = CssColor.make("rgba(158,198,222,1)");
    private static final CssColor GRAYBLUE_COLOR2 = CssColor.make("rgba(159,194,215,1)");
    private static final CssColor WHITE_COLOR = CssColor.make("rgba(255,255,255,1)");
    private static final int ITEM_WIDTH = 160;
    private static final int ITEM_HEIGHT = 160;
    private static final int ITEM_RADIUS = (ITEM_HEIGHT - 6) / 4;
    private static final int PADDING_LEFT = 5;
    private static final int PADDING_TOP = 5;
    private static final CssColor ITEM_COLOR = GRAYBLUE_COLOR2;
    private static final String NODE_NAME_CANVAS = "canvas";
    private static final String NODES_LIST_ID = "nodes-list";
    private static final String NODES_LIST_TITLE_ID = "nodes-list-title";
    private static final String NODES_LIST_TITLE_DEFAULT = "The nodes list:";
    private static final String NODES_LIST_TITLE_EMPTY = "The nodes list is empty.";

    private Map<Integer, Collection<Node>> nodesMap = new HashMap<Integer, Collection<Node>>();
    private Map<Integer, Map<Integer, Collection<Node>>> nodesByIndexMap =
            new HashMap<Integer, Map<Integer, Collection<Node>>>();

    private LevelHolder levelHolder = new LevelHolder();

    /**
     * Variable for indexing hierarchies.
     */
    private IndexHolder indexHolder = IndexHolder.getInstance(0);
    private int indexCounter = 0;

    /**
     * Variable for indexing hierarchies.
     */
    private Integer currentIndex;

    /**
     * Representing hierarchy.
     */
    private Hierarchy hierarchy;

    /**
     * Html canvas for painting.
     */
    private Canvas canvas;

    private RootPanel canvasHolder;

    private static RedrawHandler redrawHandler;

    /**
     *
     * The handler for a CircleWidget's mouse over event.
     *
     * @author Alfrescodev.com
     *
     */
    private class CircleWidgetMouseHandler implements MouseOverHandler, MouseOutHandler {

        private static final int MENU_MARGIN_RIGHT = 5;
        private static final String MOUSE_OVERED_STYLE = "circle-mouse-overed";

        /**
         * Complicated CircleWidget
         */
        private CircleWidget circleWidget;

        public CircleWidgetMouseHandler(CircleWidget circleWidget) {
            this.circleWidget = circleWidget;
        }

        public void onMouseOver(final MouseOverEvent me) {
            Widget widget = (Widget) me.getSource();
            widget.addStyleName(MOUSE_OVERED_STYLE);
            clearMainWidget();
            circleWidget.getMainWidget().add(NodeHighlightContextMenu.
                    getInstance(circleWidget, (int) circleWidget.getRadius(),
                            circleWidget.getRadius() - MENU_MARGIN_RIGHT, 0.0, hierarchy));
        }

        public void onMouseOut(final MouseOutEvent me) {
            Widget widget = (Widget) me.getSource();
            widget.removeStyleName(MOUSE_OVERED_STYLE);
            clearMainWidget();
        }

        private void clearMainWidget() {
            Iterator<Widget> iterator = circleWidget.getMainWidget().iterator();
            while (iterator.hasNext()) {
                iterator.next().removeFromParent();
            }
        }

        public void attach() {
            circleWidget.addMouseOutHandler(this);
            circleWidget.addMouseOverHandler(this);
        }
    }

    /**
     * The class holds a level value in hierarchy.
     */
    private static class LevelHolder {

        private int level;

        public int getLevel() {
            return level;
        }

        public synchronized void setLevel(int level) {
            this.level = level;
        }

        public synchronized void increment() {
            ++level;
        }

    }

    /**
     *
     * The method returns an index value of the node with the specified id.
     *
     * @param id
     * @return
     */
    public Integer getIndexOf(int id) {
        Integer index = null;
        for (Map<Integer, Collection<Node>> levelMap : nodesByIndexMap.values()) {
            for (Collection<Node> nodes : levelMap.values()) {
                for (Node node:nodes) {
                    if (node.getId() == id)
                        index = node.getIndexHolder().getValue();
                }
            }
        }
        return index;
    }

    /**
     *
     * HierarchyRepresentation constructor.
     *
     * @param hierarchy
     * @param canvas
     * @param canvasHolder
     */
    private HierarchyRepresentation(Hierarchy hierarchy, Canvas canvas, RootPanel canvasHolder) {
        this(hierarchy);
        this.canvas = canvas;
        this.canvasHolder = canvasHolder;
    }

    /**
     * Creates the handler for redraw request outside of this instance.
     */
    private void createRedrawHandler() {
        redrawHandler = new RedrawHandler() {
            public void redrawPlease() {
                drawHierarchy();
            }
        };
    }

    /**
     * the method executes redraw request.
     */
    public static void redrawPlease() {
        if (redrawHandler != null) {
            redrawHandler.redrawPlease();
        }
    }

    /**
     * The class constructor.
     *
     * @param hierarchy
     */
    private HierarchyRepresentation(Hierarchy hierarchy) {
        this.hierarchy = hierarchy;
        createRedrawHandler();
    }

    /**
     *
     * The method draws the hierarchy in the specified canvas
     * and div, wrapped the canvas.
     *
     * @param canvasHolder - div, wrapped the canvas.
     * @param canvas
     * @param hierarchy
     */
    public static void drawHierarchy(RootPanel canvasHolder, Canvas canvas, Hierarchy hierarchy) {
        HierarchyRepresentation hierarchyRepresentation =
                new HierarchyRepresentation(hierarchy, canvas, canvasHolder);
        hierarchyRepresentation.drawHierarchy();
    }

    /**
     * The method sets instance variables to clear values.
     */
    private void initVariables() {
        nodesByIndexMap.clear();
        levelHolder = new LevelHolder();
        indexHolder = IndexHolder.getInstance(0);
        indexCounter = 0;
        nodesMap.clear();
        for (Node node:hierarchy.getNodes().values()) {
            node.setIndexHolder(null);
            node.setLevel(null);
        }
    }

    private void drawHierarchy() {
        initVariables();
        Hierarchy originHierarchy = hierarchy.clone();
        splitHierarchyByLevels(hierarchy.getNodes().keySet(), 0);
        splitNodesByIndex();
        drawNodesMap();
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
        RootPanel.get(NODES_LIST_ID).clear();
        if (list != null && !list.isEmpty()) {
            RootPanel.get(NODES_LIST_ID).add(new InlineLabel(listText.toString()));
            RootPanel.get(NODES_LIST_TITLE_ID).getElement().setInnerHTML(NODES_LIST_TITLE_DEFAULT);
        }
        else {
            RootPanel.get(NODES_LIST_TITLE_ID).getElement().setInnerHTML(NODES_LIST_TITLE_EMPTY);
        }
    }

    /**
     * The method creates a new instance of hierarchy.
     *
     * @param hierarchy
     * @return
     */
    public static HierarchyRepresentation getInstance(Hierarchy hierarchy) {
        return new HierarchyRepresentation(hierarchy);
    }

    /**
     * The method create nodesByIndexMap which maps
     * indexes and independent sub-hierarchies of the root
     * hierarchy.
     */
    public void splitNodesByIndex() {
        for (int level:nodesMap.keySet()) {
            Collection<Node> nodes = nodesMap.get(level);
            for (Node node:nodes) {
                int index = node.getIndexHolder().getValue();
                Map<Integer, Collection<Node>> indexMap = nodesByIndexMap.get(index);
                if (indexMap == null) {
                    indexMap = new HashMap<Integer, Collection<Node>>();
                    nodesByIndexMap.put(index, indexMap);
                }
                Collection<Node> levelNodes = indexMap.get(level);
                if (levelNodes == null) {
                    levelNodes = new HashSet<Node>();
                    indexMap.put(level, levelNodes);
                }
                levelNodes.add(node);
            }
        }
        nodesMap.clear();
    }

    /**
     * The method creates new widget which represents the hierarchy's node.
     *
     * @param node - representing node.
     * @param x - x-coord on canvas
     * @param y - y-coord on canvas.
     * @return new widget
     */
    private CircleWidget newCircleWidget(Node node, double x, double y) {
        CircleWidget circleWidget = new CircleWidget(node, x, y, ITEM_RADIUS,
                ITEM_COLOR.value(), node.getId(), canvas);
        CircleWidgetMouseHandler mouseHandler = new CircleWidgetMouseHandler(circleWidget);
        mouseHandler.attach();
        return circleWidget;
    }

    private void createCircles(Collection<Node> list, int maxLeveSize, int level,
                                      Map<Integer, CircleWidget> circlesMap, int offsetSize) {
        double k = (double) maxLeveSize / (double) list.size();
        int i = 0;
        for (Node node:list) {
            double x = PADDING_LEFT + offsetSize * ITEM_WIDTH + (i + 1) * ITEM_WIDTH * k - ITEM_WIDTH * k / 2;
            double y = PADDING_TOP + (level + 1) * ITEM_HEIGHT - ITEM_HEIGHT / 2;
            CircleWidget c = newCircleWidget(node, x, y);
            circlesMap.put(node.getId(), c);
            ++i;
        }
    }

    /**
     * The method draws links between nodes on canvas.
     *
     * @param circlesMap
     * @param context2d
     */
    private void drawLinks(Map<Integer, CircleWidget> circlesMap,
                                  Context2d context2d) {

        for (int index:nodesByIndexMap.keySet()) {
            for (int level:nodesByIndexMap.get(index).keySet()) {
                Collection<Node> list = nodesByIndexMap.get(index).get(level);
                for (Node node : list) {
                    drawLinks(circlesMap, node, context2d, hierarchy);
                }
            }
        }
    }

    /**
     * The method draws links between nodes on canvas.
     *
     * @param circlesMap
     * @param node
     * @param context2d
     * @param hierarchy
     */
    private static void drawLinks(Map<Integer, CircleWidget> circlesMap, Node node,
                                  Context2d context2d, Hierarchy hierarchy) {
        if (node.getParentIds() != null && node.getParentIds().size() > 0) {
            for (Integer pid:node.getParentIds()) {
                Node p = hierarchy.getNodes().get(pid);
                context2d.setFillStyle(ITEM_COLOR);
                context2d.beginPath();
                double x = circlesMap.get(node.getId()).getPosX();
                double y = circlesMap.get(node.getId()).getPosY();
                context2d.moveTo(x, y);
                double newX = circlesMap.get(p.getId()).getPosX();
                double newY = circlesMap.get(p.getId()).getPosY();
                context2d.lineTo(newX, newY);
                context2d.closePath();
                context2d.stroke();
            }
        }
    }

    /**
     * The method draws hierarchy nodes on canvas.
     */
    private void drawNodesMap() {
        int maxLeveSize = 0;
        int offsetSize = 0;
        int height = 0;
        canvasHolder.clear();
        canvasHolder.add(canvas);
        canvas.setCoordinateSpaceHeight(0);
        canvas.setHeight(0 + "px");
        canvas.setCoordinateSpaceWidth(0);
        canvas.setWidth(0 + "px");
        canvasHolder.setHeight(0 + "px");
        canvasHolder.setWidth(0 + "px");
        Map<Integer, CircleWidget> circlesMap = new HashMap<Integer, CircleWidget>();
        for (int index:nodesByIndexMap.keySet()) {
            offsetSize += maxLeveSize;
            maxLeveSize =  findMaxLevelSize(index);
            int width = ( maxLeveSize + offsetSize ) * ITEM_WIDTH + PADDING_LEFT * 2;
            int newHeight = PADDING_TOP * 2 + ITEM_HEIGHT * nodesByIndexMap.get(index).keySet().size();
            if (newHeight > height) {
                height = newHeight;
                canvasHolder.setHeight(height + "px");
                canvas.setHeight(height + "px");
                canvas.setCoordinateSpaceHeight(height);
            }
            canvasHolder.setWidth(width + "px");
            canvas.setWidth(width + "px");
            canvas.setCoordinateSpaceWidth(width);
            for (int level:nodesByIndexMap.get(index).keySet()) {
                Collection<Node> list = nodesByIndexMap.get(index).get(level);
                createCircles(list, maxLeveSize, level, circlesMap, offsetSize);
            }
        }
        canvas.getContext2d().clearRect(0, 0,
                canvas.getCoordinateSpaceWidth(), canvas.getCoordinateSpaceHeight());
        drawLinks(circlesMap, canvas.getContext2d());
        drawCircles(circlesMap, canvasHolder);
    }

    /**
     * The method clears canvas first,
     * than adds widgets to diw which wraps the canvas.
     * Widget represents hierarchy nodes.
     *
     * @param circlesMap
     * @param canvasHolder
     */
    private static void drawCircles(Map<Integer, CircleWidget> circlesMap, RootPanel canvasHolder) {
        Iterator<Widget> iterator = canvasHolder.iterator();
        while (iterator.hasNext()) {
            Widget widget = iterator.next();
            if (!widget.getElement().getNodeName().toLowerCase().equals(NODE_NAME_CANVAS)) {
                canvasHolder.remove(widget);
            }
        }
        for (CircleWidget circle:circlesMap.values()) {
            canvasHolder.add(circle);
        }
    }

    private int findMaxLevelSize(int index) {
        int maxLevelSize = 0;
        for (Collection<Node> levelNodes:nodesByIndexMap.get(index).values()) {
            if (levelNodes.size() > maxLevelSize) {
                maxLevelSize = levelNodes.size();
            }
        }
        return maxLevelSize;
    }

    /**
     * The method recursively marks and sets levels to nodes.
     *
     * @param node
     * @param deep
     * @param level
     * @return
     */
    private int findNewLevel(Node node, int deep, int level) {
        splitHierarchyByLevels(node.getParentIds(), deep+1);
        if (levelHolder.getLevel() > level) {
            level = levelHolder.getLevel();
        }
        /**
         * Indexing elements to split different independent hierarchies.
         */
        if (currentIndex == null) {
            currentIndex = indexHolder.getValue();
        }
        else if (currentIndex != indexHolder.getValue() && deep != 0) {
            indexHolder.reSetValue(currentIndex);
        }
        Collection<Node> nodesSet = nodesMap.get(levelHolder.getLevel());
        if (nodesSet == null) nodesSet = new HashSet<Node>();
        nodesSet.add(node);
        node.setLevel(levelHolder.getLevel());
        nodesMap.put(levelHolder.getLevel(), nodesSet);
        node.setIndexHolder(indexHolder);
        return level;
    }

    /**
     * The method recursively marks and sets levels to nodes.
     *
     * @param node
     * @param deep
     * @param level
     * @return
     */
    private int findInheritedLevel(Node node, int deep, int level) {
        if (node.getLevel()!=null && node.getLevel() > level)
            level = node.getLevel();
        /**
         * Indexing elements to split different independent hierarchies.
         */
        if (currentIndex == null) {
            //currentIndex = node.getIndexHolder().getValue();
            indexHolder = node.getIndexHolder();
            currentIndex = indexHolder.getValue();
        }
        else if (currentIndex != node.getIndexHolder().getValue() && deep != 0) {
            node.getIndexHolder().reSetValue(currentIndex);
            indexHolder = node.getIndexHolder();
        }
        return level;
    }

    /**
     *
     * The method recursively marks and sets levels to nodes.
     *
     * @param nodeIds
     * @param deep
     * @return
     */
    private int findLevel(Collection<Integer> nodeIds, int deep) {
        int level = 0;
        currentIndex = null;
        for (Integer nodeId:nodeIds) {
            Node node = hierarchy.getNodes().get(nodeId);
            if (deep == 0) {
                currentIndex = null;
            }
            if (node.getLevel() == null) {
                level = findNewLevel(node, deep, level);
            }
            else {
                level = findInheritedLevel(node, deep, level);
            }
        }
        return level;
    }

    /**
     * The method recursively marks and sets levels to nodes.
     *
     * @param nodes - nodes ids collection.
     * @param deep - deep of recursion.
     */
    public void splitHierarchyByLevels(Collection<Integer> nodes, int deep) {
        if (nodes!=null && nodes.size() > 0) {
            int level = findLevel(nodes, deep);
            levelHolder.setLevel(level + 1);
        }
        else {
            levelHolder.setLevel(0);
            int newVal = ++indexCounter;
            //Window.alert("new indexHolder " + newVal);
            indexHolder = IndexHolder.getInstance(newVal);
        }
    }

}
