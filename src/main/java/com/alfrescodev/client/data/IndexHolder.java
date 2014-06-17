package com.alfrescodev.client.data;

import com.google.gwt.user.client.Window;

import java.util.ArrayList;
import java.util.List;

/**
 * Class for mutable indexing nodes.
 *
 * @author Alfrescodev.com
 *
 */
public class IndexHolder {

    private int value;
    private static List<IndexHolder> instances = new ArrayList<IndexHolder>();

    private IndexHolder(int initVal) {
        value = initVal;
    }

    private static IndexHolder findOrCreate(int initVal) {
        IndexHolder newIndexHolder = null;
        for (IndexHolder indexHolder:instances) {
            if (indexHolder.getValue() == initVal) {
                newIndexHolder = indexHolder;
            }
        }
        if (newIndexHolder == null) {
            newIndexHolder = new IndexHolder(initVal);
            instances.add(newIndexHolder);
        }
        return newIndexHolder;
    }

    public static IndexHolder getInstance(int initVal) {
        IndexHolder newInstance = findOrCreate(initVal);
        return newInstance;
    }

    public static List<IndexHolder> getInstances() {
        return instances;
    }

    public int getValue() {
        return value;
    }

    public void reSetValue(int val) {
        int oldVal = this.value;
        for (IndexHolder indexHolder:instances) {
            if (indexHolder.getValue() == oldVal) {
                indexHolder.value = val;
            }
        }
    }

    @Override
    public String toString() {
        return "IndexHolder{" +
                "value=" + value +
                '}';
    }
}
