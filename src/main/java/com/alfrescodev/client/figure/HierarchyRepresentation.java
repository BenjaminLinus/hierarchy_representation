package com.alfrescodev.client.figure;

import com.alfrescodev.client.data.Hierarchy;
import com.alfrescodev.client.data.IndexHolder;
import com.alfrescodev.client.data.Node;
import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.CssColor;
import com.google.gwt.dom.client.Touch;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.user.client.Window;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

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

    private Map<Integer, Collection<Node>> nodesMap = new HashMap<Integer, Collection<Node>>();
    private Map<Integer, Map<Integer, Collection<Node>>> nodesByIndexMap =
            new HashMap<Integer, Map<Integer, Collection<Node>>>();

    private LevelHolder levelHolder = new LevelHolder();

    private IndexHolder indexHolder = IndexHolder.getInstance(0);
    private int indexCounter = 0;

    // mouse positions relative to canvas
    private int mouseX, mouseY;

    private Integer currentIndex;

    private Hierarchy hierarchy;

    private Canvas canvas;

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

    private HierarchyRepresentation(Hierarchy hierarchy, Canvas canvas) {
        this.hierarchy = hierarchy;
        this.canvas = canvas;
    }

    public static void drawHierarchy(Canvas canvas, Hierarchy hierarchy) {
        HierarchyRepresentation hierarchyRepresentation = new HierarchyRepresentation(hierarchy, canvas);
        //Window.alert("hierarchy.getNodes().values() = "+hierarchy.getNodes().values());
        hierarchyRepresentation.splitHierarchyByLevels(hierarchy.getNodes().keySet(), 0);
        hierarchyRepresentation.splitNodesByIndex();
        hierarchyRepresentation.drawNodesMap();
    }

    private void splitNodesByIndex() {
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
        nodesMap = null;
    }

    private static void createCircles(Collection<Node> list, int maxLeveSize, int level,
                                      Map<Integer, Circle> circlesMap, int offsetSize) {
        double k = (double) maxLeveSize / (double) list.size();
        int i = 0;
        for (Node node:list) {
            double x = PADDING_LEFT + offsetSize * ITEM_WIDTH + (i + 1) * ITEM_WIDTH * k - ITEM_WIDTH * k / 2;
            double y = PADDING_TOP + (level + 1) * ITEM_HEIGHT - ITEM_HEIGHT / 2;
            Circle c = new Circle(x, y, ITEM_RADIUS, ITEM_COLOR, node.getId());
            circlesMap.put(node.getId(), c);
            c.setLevel(level);
            //c.setLevel(node.getLevel());
            //c.setLevel(node.getIndexHolder().getValue());
            ++i;
        }
    }

    private void drawLinks(Map<Integer, Circle> circlesMap,
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

    private static void drawLinks(Map<Integer, Circle> circlesMap, Node node,
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

    private void drawNodesMap() {
        int maxLeveSize = 0;
        int offsetSize = 0;
        int height = 0;
        Map<Integer, Circle> circlesMap = new HashMap<Integer, Circle>();
        for (int index:nodesByIndexMap.keySet()) {
            offsetSize += maxLeveSize;
            maxLeveSize =  findMaxLevelSize(index);
            int width = ( maxLeveSize + offsetSize ) * ITEM_WIDTH + PADDING_LEFT * 2;
            int newHeight = PADDING_TOP * 2 + ITEM_HEIGHT * nodesByIndexMap.get(index).keySet().size();
            if (newHeight > height) {
                height = newHeight;
                canvas.setHeight(height + "px");
                canvas.setCoordinateSpaceHeight(height);
            }
            canvas.setWidth(width + "px");
            canvas.setCoordinateSpaceWidth(width);
            for (int level:nodesByIndexMap.get(index).keySet()) {
                Collection<Node> list = nodesByIndexMap.get(index).get(level);
                createCircles(list, maxLeveSize, level, circlesMap, offsetSize);
            }
        }
        drawLinks(circlesMap, canvas.getContext2d());
        drawCircles(circlesMap, canvas.getContext2d());
    }

    private static void drawCircles(Map<Integer, Circle> circlesMap, Context2d context2d) {
        for (Circle circle:circlesMap.values()) {
            circle.draw(context2d);
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
        //Window.alert("level setted. node = "+node+" nodes = "+nodesMap);
        nodesMap.put(levelHolder.getLevel(), nodesSet);
        node.setIndexHolder(indexHolder);
        //Window.alert("setIndexHolder to "+node.getId()+"! indexHolder = " + indexHolder);
        return level;
    }

    private int findInheritedLevel(Node node, int deep, int level) {
        if (node.getLevel()!=null && node.getLevel() > level)
            level = node.getLevel();
        //Window.alert("findInheritedLevel! currentIndex = "+currentIndex);
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

    private void splitHierarchyByLevels(Collection<Integer> nodes, int deep) {
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

    void initHandlers() {
        canvas.addMouseMoveHandler(new MouseMoveHandler() {
            public void onMouseMove(MouseMoveEvent event) {
                mouseX = event.getRelativeX(canvas.getElement());
                mouseY = event.getRelativeY(canvas.getElement());
            }
        });

        canvas.addMouseOutHandler(new MouseOutHandler() {
            public void onMouseOut(MouseOutEvent event) {
                mouseX = -200;
                mouseY = -200;
            }
        });

        canvas.addTouchMoveHandler(new TouchMoveHandler() {
            public void onTouchMove(TouchMoveEvent event) {
                event.preventDefault();
                if (event.getTouches().length() > 0) {
                    Touch touch = event.getTouches().get(0);
                    mouseX = touch.getRelativeX(canvas.getElement());
                    mouseY = touch.getRelativeY(canvas.getElement());
                }
                event.preventDefault();
            }
        });

        canvas.addTouchEndHandler(new TouchEndHandler() {
            public void onTouchEnd(TouchEndEvent event) {
                event.preventDefault();
                mouseX = -200;
                mouseY = -200;
            }
        });

        canvas.addGestureStartHandler(new GestureStartHandler() {
            public void onGestureStart(GestureStartEvent event) {
                event.preventDefault();
            }
        });
    }

}
