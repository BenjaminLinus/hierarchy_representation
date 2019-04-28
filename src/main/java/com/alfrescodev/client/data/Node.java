package com.alfrescodev.client.data;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * The class represents node in hierarchy;
 *
 */
public class Node implements Comparable<Node> {

    /**
     * Id of the node
     */
    private int id;
    /**
     * Parents ids of the node.
     */
    private Set<Integer> parentIds;
    /**
     * Node's level in hierarchy
     */
    private Integer level;
    /**
     * Nodes have indexes to split different independent hierarchies.
     */
    private IndexHolder indexHolder;

    /**
     *
     * Constructor creates new node.
     *
     * @param nodeId - id for the new node.
     * @param parentIds - ids of parent nodes of the current node.
     */
    public Node(int nodeId, Set<Integer> parentIds) {
        this(nodeId);
        this.parentIds = parentIds;
    }

    /**
     *
     * Constructor creates new node.
     *
     * @param nodeId - id for the new node.
     */
    public Node(int nodeId) {
        this.id = nodeId;
    }

    public int getId() {
        return id;
    }

    public Set<Integer> getParentIds() {
        return parentIds;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Node node = (Node) o;

        if (id != node.id) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public IndexHolder getIndexHolder() {
        return indexHolder;
    }

    public void setIndexHolder(IndexHolder indexHolder) {
        this.indexHolder = indexHolder;
    }

    @Override
    public String toString() {
        return "Node{" +
                "id=" + id +
                ", level=" + level +
                ", indexHolder=" + indexHolder +
                ", parents=" + parentIds +
                '}';
    }

    public void addParentId(Integer id) {
        if (parentIds == null)
            parentIds = new HashSet<Integer>();
        parentIds.add(id);
    }

    /**
     *
     * The method converts a collection of nodes to collection of integers.
     * Integers represents node's ids.
     *
     * @param nodesList
     * @return
     */
    public static Collection<Integer> getIdsList(Collection<Node> nodesList) {
        Set<Integer> ids = new HashSet<Integer>();
        for (Node node : nodesList) {
            ids.add(node.getId());
        }
        return ids;
    }

    /**
     *
     * The method overwrites node's parents to new values.
     *
     * @param newIds - new parent ids.
     */
    public void clearAndAddParentsIds(Collection<Integer> newIds) {
        if (this.parentIds == null)
            this.parentIds = new HashSet<Integer>();
        parentIds.clear();
        parentIds.addAll(newIds);
    }

    /**
     *
     * Compare nodes.
     *
     * @param o
     * @return
     */
    public int compareTo(Node o) {
        return new Integer(id).compareTo(o.id);

    }
}
