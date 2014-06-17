package com.alfrescodev.client.data;

import java.util.*;

/**
 * The class represents hierarchy of dependencies;
 *
 * @author Alfrescodev.com
 *
 */
public class Hierarchy implements Cloneable {

    private Hierarchy() {
    }

    private Map<Integer, Node> nodes;

    private static List<Integer> newRandomParentsList(int id) {
        int parentsCount = (int) ( Math.random() * id );
        List<Integer> parentIds = new ArrayList<Integer>();
        for (int j=0; j < parentsCount; ++j) {
            int parentId = (int) ( Math.random() * id );
            parentIds.add(parentId);
        }
        return parentIds;
    }

    public static Hierarchy generateRandomHierarchy(int maxNodesCount) {
        Hierarchy hierarchy = new Hierarchy();
        hierarchy.nodes = new HashMap<Integer, Node>();
        int count = (int) (Math.random()*maxNodesCount) + 3;
        for (int id = 0; id < count; ++id) {
            List<Integer> parentIds = newRandomParentsList(id);
            //Window.alert("id = " + id);
            //Window.alert("parentIds = " + parentIds);
            Node node = new Node(id, parentIds);
            hierarchy.nodes.put(id, node);
        }
        return hierarchy;
    }

    private static void newNode(Hierarchy hierarchy, int id) {
        Node node = new Node(id, null);
        hierarchy.nodes.put(node.getId(), node);
    }

    private static void newNode(Hierarchy hierarchy, int id, Integer... parentIds) {
        Node node = new Node(id, Arrays.asList(parentIds));
        hierarchy.nodes.put(node.getId(), node);
    }

    public static Hierarchy newTestHierarchy2() {
        Hierarchy hierarchy = new Hierarchy();
        hierarchy.nodes = new HashMap<Integer, Node>();
        newNode(hierarchy, 0);
        newNode(hierarchy, 1);
        newNode(hierarchy, 2, 0);
        newNode(hierarchy, 3, 0, 1);
        newNode(hierarchy, 4, 1);
        newNode(hierarchy, 5, 1, 3);
        newNode(hierarchy, 6, 5, 3);
        newNode(hierarchy, 7, 2, 1);
        newNode(hierarchy, 10, 2, 1);
        newNode(hierarchy, 13);
        newNode(hierarchy, 15, 13, 10);
        newNode(hierarchy, 16);
        newNode(hierarchy, 13, 16);

        newNode(hierarchy, 9);
        return hierarchy;
    }

    public static Hierarchy newTestHierarchy() {
        Hierarchy hierarchy = new Hierarchy();
        hierarchy.nodes = new HashMap<Integer, Node>();

        newNode(hierarchy, 0);
        newNode(hierarchy, 1, 0);
        newNode(hierarchy, 2, 0, 1);
        newNode(hierarchy, 3);
        newNode(hierarchy, 4, 0 ,3);
        newNode(hierarchy, 5, 0);
        newNode(hierarchy, 6, 3);
        newNode(hierarchy, 11, 6);
        newNode(hierarchy, 12, 3, 0);
        newNode(hierarchy, 13, 11, 6);
        newNode(hierarchy, 17);
        newNode(hierarchy, 13, 11, 6, 17);

        newNode(hierarchy, 7);
        newNode(hierarchy, 8, 7);
        //newNode(hierarchy, 9, 7);
        //newNode(hierarchy, 10, 9);
        newNode(hierarchy, 14, 7);
        newNode(hierarchy, 15, 14, 7);
        newNode(hierarchy, 16, 14);
        newNode(hierarchy, 22, 15);
        newNode(hierarchy, 23, 22);
        newNode(hierarchy, 24, 23);
        newNode(hierarchy, 25, 24);

        /*
        newNode(hierarchy, 18);
        newNode(hierarchy, 19, 18);
        newNode(hierarchy, 21, 18);
        newNode(hierarchy, 20, 18, 19);
        */

        return hierarchy;
    }

    public Map<Integer, Node> getNodes() {
        return nodes;
    }

    public Hierarchy clone() {
        Hierarchy cloned = new Hierarchy();
        cloned.nodes = new HashMap<Integer, Node>();
        for (int id:this.nodes.keySet()) {
            Node node = new Node(id, this.nodes.get(id).getParentIds());
            cloned.nodes.put(id, node);
        }
        return cloned;
    }
}
