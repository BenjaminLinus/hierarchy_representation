package com.alfrescodev.client.data;

import java.util.Collection;
import java.util.List;

/**
 * The class represents node in hierarchy;
 *
 * @author Alfrescodev.com
 *
 */
public class Node {

    private int id;
    private Collection<Integer> parentIds;
    private Integer level;
    /**
     * Nodes have indexes to split different independent hierarchies.
     */
    private IndexHolder indexHolder;

    public Node(int nodeId, Collection<Integer> parentIds) {
        this.id = nodeId;
        this.parentIds = parentIds;
    }

    public int getId() {
        return id;
    }

    public Collection<Integer> getParentIds() {
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
                '}';
    }
}
