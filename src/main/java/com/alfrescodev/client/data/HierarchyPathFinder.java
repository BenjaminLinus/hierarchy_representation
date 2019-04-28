package com.alfrescodev.client.data;

import java.util.*;

/**
 * The class for searching path in hierarchy.
 *
 */
public class HierarchyPathFinder {

    private Hierarchy hierarchy;
    private LevelHolder levelHolder = new LevelHolder();
    private Map<Integer, Collection<Integer>> nodesMap = new HashMap<Integer, Collection<Integer>>();

    /**
     * The class holds a level value in hierarchy.
     */
    private class LevelHolder {

        private int level;

        public int getLevel() {
            return level;
        }

        public synchronized void setLevel(int level) {
            this.level = level;
        }

    }

    private HierarchyPathFinder(Hierarchy hierarchy) {
        this.hierarchy = hierarchy;
    }

    /**
     * The main method to find a path.
     *
     * @return
     */
    private List<Integer> createPathList() {
        List<Integer> path = new ArrayList<Integer>();
        createLevelsMap(hierarchy.getNodes().keySet());
        for (int level:nodesMap.keySet()) {
            for (int nodeId:nodesMap.get(level)) {
                path.add(nodeId);
            }
        }
        return path;
    }

    /**
     * The method returns the path in hierarchy.
     *
     * @param hierarchy
     * @return
     */
    public static List<Integer> createListFromHierarchy(Hierarchy hierarchy) {
        return new HierarchyPathFinder(hierarchy).createPathList();
    }

    /**
     *
     * The method splits the hierarchy by levels.
     *
     * @param nodes
     */
    private void createLevelsMap(Collection<Integer> nodes) {
        if (nodes!=null && nodes.size() > 0) {
            int level = markLevel(nodes);
            levelHolder.setLevel(level + 1);
        }
        else {
            levelHolder.setLevel(0);
        }
    }

    private int markLevel(Collection<Integer> nodeIds) {
        int level = 0;
        for (Integer nodeId:nodeIds) {
            Node node = hierarchy.getNodes().get(nodeId);
            if (node.getLevel() == null) {
                level = markLevelRecursively(node, level);
            }
            else if (node.getLevel()!=null && node.getLevel() > level)
                level = node.getLevel();
        }
        return level;
    }

    /**
     *
     * The method sets the level to node with clear level.
     *
     * @param node
     * @param level
     * @return
     */
    private int markLevelRecursively(Node node, int level) {
        createLevelsMap(node.getParentIds());
        if (levelHolder.getLevel() > level) {
            level = levelHolder.getLevel();
        }
        Collection<Integer> nodesSet = nodesMap.get(levelHolder.getLevel());
        if (nodesSet == null) nodesSet = new HashSet<Integer>();
        nodesSet.add(node.getId());
        node.setLevel(levelHolder.getLevel());
        nodesMap.put(levelHolder.getLevel(), nodesSet);
        return level;
    }

}
