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

    private static Set<Integer> newRandomParentsList(int id) {
        int parentsCount = (int) ( Math.random() * id );
        Set<Integer> parentIds = new HashSet<Integer>();
        for (int j=0; j < parentsCount; ++j) {
            int parentId = (int) ( Math.random() * id );
            parentIds.add(parentId);
        }
        return parentIds;
    }

    /**
     *
     * The method creates new randomly generated hierarchy object.
     *
     * @param maxNodesCount - max count of nodes in the random hiearachy.
     * @return randomly generated instance of Hierarchy
     */
    public static Hierarchy generateRandomHierarchy(int maxNodesCount) {
        Hierarchy hierarchy = new Hierarchy();
        hierarchy.nodes = new HashMap<Integer, Node>();
        int count = (int) (Math.random()*maxNodesCount) + 3;
        for (int id = 0; id < count; ++id) {
            Set<Integer> parentIds = newRandomParentsList(id);
            //Window.alert("id = " + id);
            //Window.alert("parentIds = " + parentIds);
            Node node = new Node(id, parentIds);
            hierarchy.nodes.put(id, node);
        }
        return hierarchy;
    }

    /**
     * The method creates new node id hierarchy.
     *
     * @param hierarchy
     * @param id - id of the new node.
     */
    public static void newNode(Hierarchy hierarchy, int id) {
        Node node = new Node(id, null);
        hierarchy.nodes.put(node.getId(), node);
    }

    /**
     * The method creates new node id hierarchy.
     *
     * @param hierarchy
     * @param id - id of the new node.
     * @param parentIds - parent ids of the new node.
     */
    public static void newNode(Hierarchy hierarchy, int id, Integer... parentIds) {
        Node node = new Node(id, new HashSet<Integer>(Arrays.asList(parentIds)));
        hierarchy.nodes.put(node.getId(), node);
    }

    /**
     *
     * The method creates new clear hierarchy.
     *
     * @return
     */
    public static Hierarchy clearHierarchy() {
        Hierarchy hierarchy = new Hierarchy();
        hierarchy.nodes = new HashMap<Integer, Node>();
        return hierarchy;
    }

    /**
     *
     * Test example of hierarchy.
     *
     * @return
     */
    public static Hierarchy newTestHierarchy3() {
        Hierarchy hierarchy = Hierarchy.clearHierarchy();

        Hierarchy.newNode(hierarchy, 0);
        Hierarchy.newNode(hierarchy, 1, 0);
        Hierarchy.newNode(hierarchy, 2, 0, 1);
        Hierarchy.newNode(hierarchy, 3);
        Hierarchy.newNode(hierarchy, 4, 0, 3);
        Hierarchy.newNode(hierarchy, 5, 0);
        Hierarchy.newNode(hierarchy, 6, 3);

        Hierarchy.newNode(hierarchy, 11);
        Hierarchy.newNode(hierarchy, 12, 3, 0);
        Hierarchy.newNode(hierarchy, 13, 11);
        Hierarchy.newNode(hierarchy, 17);
        Hierarchy.newNode(hierarchy, 13, 11, 17);

        Hierarchy.newNode(hierarchy, 7);
        Hierarchy.newNode(hierarchy, 8, 7);
        Hierarchy.newNode(hierarchy, 14, 7);
        Hierarchy.newNode(hierarchy, 15, 14, 7);
        Hierarchy.newNode(hierarchy, 16, 14);
        Hierarchy.newNode(hierarchy, 22, 15);
        Hierarchy.newNode(hierarchy, 23, 22);
        Hierarchy.newNode(hierarchy, 24, 23);
        Hierarchy.newNode(hierarchy, 25, 24);

        return hierarchy;
    }

    /**
     *
     * Test example of hierarchy.
     *
     * @return
     */
    public static Hierarchy newTestSmallHierarchy4() {
        Hierarchy hierarchy = Hierarchy.clearHierarchy();

        Hierarchy.newNode(hierarchy, 0);
        Hierarchy.newNode(hierarchy, 1, 0);
        Hierarchy.newNode(hierarchy, 2, 0, 1);
        Hierarchy.newNode(hierarchy, 3);
        Hierarchy.newNode(hierarchy, 4, 0, 3);

        return hierarchy;
    }

    /**
     *
     * Test example of hierarchy.
     *
     * @return
     */
    public static Hierarchy newTestHierarchy2() {
        Hierarchy hierarchy = clearHierarchy();
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

    /**
     *
     * Test example of hierarchy.
     *
     * @return
     */
    public static Hierarchy newTestHierarchy() {
        Hierarchy hierarchy = clearHierarchy();

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

    /**
     * The method returns hierarchy nodes Map.
     *
     * @return
     */
    public Map<Integer, Node> getNodes() {
        return nodes;
    }

    /**
     * The method returns a new copy of the hierarchy.
     *
     * @return an instance of the hierarchy.
     */
    public Hierarchy clone() {
        Hierarchy cloned = new Hierarchy();
        cloned.nodes = new HashMap<Integer, Node>();
        for (int id:this.nodes.keySet()) {
            Node node = new Node(id, this.nodes.get(id).getParentIds());
            cloned.nodes.put(id, node);
        }
        return cloned;
    }

    /**
     * The method adds to the hierarchy a new node with next free id.
     */
    public void addNewNode() {
        int newId = maxNodesId() + 1;
        nodes.put(newId, new Node(newId));
    }

    /**
     *
     * The method finds and return max node id in hierarchy.
     *
     * @return
     */
    private int maxNodesId() {
        int maxId = 0;
        if (nodes!=null && !nodes.isEmpty()) {
            for (int nodeId : nodes.keySet()) {
                if (nodeId > maxId)
                    maxId = nodeId;
            }
        }
        return maxId;
    }
}
