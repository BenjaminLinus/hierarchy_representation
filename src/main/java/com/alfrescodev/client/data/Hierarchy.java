package com.alfrescodev.client.data;

import java.util.*;

/**
 * The class represents hierarchy of dependencies;
 *
 */
public class Hierarchy implements Cloneable {

    private Hierarchy() {
    }

    private Map<Integer, Node> nodes;

    private static Set<Integer> newRandomParentsList(int id) {
        int parentsCount = (int) ( Math.random() * id );
        Set<Integer> parentIds = new HashSet<>();
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
        hierarchy.nodes = new HashMap<>();
        int count = (int) (Math.random()*maxNodesCount) + 3;
        for (int id = 0; id < count; ++id) {
            Set<Integer> parentIds = newRandomParentsList(id);
            Node node = new Node(id, parentIds);
            hierarchy.nodes.put(id, node);
        }
        return hierarchy;
    }

    /**
     * The method creates new node id hierarchy.
     *
     * @param id - id of the new node.
     */
    public void newNode(int id) {
        Node node = new Node(id, null);
        this.nodes.put(node.getId(), node);
    }

    /**
     * The method creates new node id hierarchy.
     *
     * @param id - id of the new node.
     * @param parentIds - parent ids of the new node.
     */
    public void newNode(int id, Integer... parentIds) {
        Node node = new Node(id, new HashSet<>(Arrays.asList(parentIds)));
        this.nodes.put(node.getId(), node);
    }

    /**
     *
     * The method creates new clear hierarchy.
     *
     * @return
     */
    public static Hierarchy clearHierarchy() {
        Hierarchy hierarchy = new Hierarchy();
        hierarchy.nodes = new HashMap<>();
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
        hierarchy.newNode(0);
        hierarchy.newNode(1, 0);
        hierarchy.newNode(2, 0, 1);
        hierarchy.newNode(3);
        hierarchy.newNode(4, 0, 3);
        hierarchy.newNode(5, 0);
        hierarchy.newNode(6, 3);
        hierarchy.newNode(11);
        hierarchy.newNode(12, 3, 0);
        hierarchy.newNode(13, 11);
        hierarchy.newNode(17);
        hierarchy.newNode(13, 11, 17);
        hierarchy.newNode(7);
        hierarchy.newNode(8, 7);
        hierarchy.newNode(14, 7);
        hierarchy.newNode(15, 14, 7);
        hierarchy.newNode(16, 14);
        hierarchy.newNode(22, 15);
        hierarchy.newNode(23, 22);
        hierarchy.newNode(24, 23);
        hierarchy.newNode( 25, 24);
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
        hierarchy.newNode(0);
        hierarchy.newNode(1, 0);
        hierarchy.newNode(2, 0, 1);
        hierarchy.newNode(3);
        hierarchy.newNode(4, 0, 3);
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
        hierarchy.newNode(0);
        hierarchy.newNode(1);
        hierarchy.newNode(2, 0);
        hierarchy.newNode(3, 0, 1);
        hierarchy.newNode(4, 1);
        hierarchy.newNode(5, 1, 3);
        hierarchy.newNode(6, 5, 3);
        hierarchy.newNode(7, 2, 1);
        hierarchy.newNode(10, 2, 1);
        hierarchy.newNode(13);
        hierarchy.newNode(15, 13, 10);
        hierarchy.newNode(16);
        hierarchy.newNode(13, 16);
        hierarchy.newNode(9);
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
        hierarchy.newNode(0);
        hierarchy.newNode(1, 0);
        hierarchy.newNode(2, 0, 1);
        hierarchy.newNode(3);
        hierarchy.newNode(4, 0 ,3);
        hierarchy.newNode(5, 0);
        hierarchy.newNode(6, 3);
        hierarchy.newNode(11, 6);
        hierarchy.newNode( 12, 3, 0);
        hierarchy.newNode(13, 11, 6);
        hierarchy.newNode(17);
        hierarchy.newNode(13, 11, 6, 17);
        hierarchy.newNode(7);
        hierarchy.newNode(8, 7);
        hierarchy.newNode(14, 7);
        hierarchy.newNode(15, 14, 7);
        hierarchy.newNode(16, 14);
        hierarchy.newNode(22, 15);
        hierarchy.newNode(23, 22);
        hierarchy.newNode(24, 23);
        hierarchy.newNode(25, 24);
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
        cloned.nodes = new HashMap<>();
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
