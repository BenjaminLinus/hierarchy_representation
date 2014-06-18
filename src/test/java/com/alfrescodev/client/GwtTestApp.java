package com.alfrescodev.client;

import com.alfrescodev.client.data.Hierarchy;
import com.alfrescodev.client.data.HierarchyPathFinder;
import com.alfrescodev.client.figure.HierarchyRepresentation;
import com.google.gwt.core.client.GWT;
import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * GWT JUnit <b>integration</b> tests must extend GWTTestCase.
 * Using <code>"GwtTest*"</code> naming pattern exclude them from running with
 * surefire during the test phase.
 * <p/>
 * If you run the tests using the Maven command line, you will have to
 * navigate with your browser to a specific url given by Maven.
 * See http://mojo.codehaus.org/gwt-maven-plugin/user-guide/testing.html
 * for details.
 */
public class GwtTestApp extends GWTTestCase {

    /**
     * Must refer to a valid module that sources this class.
     */
    public String getModuleName() {
        return "com.alfrescodev.AppJUnit";
    }

    public static Hierarchy newTestHierarchy1() {
        Hierarchy hierarchy = Hierarchy.clearHierarchy();

        Hierarchy.newNode(hierarchy, 0);
        Hierarchy.newNode(hierarchy, 1, 0);
        Hierarchy.newNode(hierarchy, 2, 0, 1);
        Hierarchy.newNode(hierarchy, 3);
        Hierarchy.newNode(hierarchy, 4, 0, 3);
        Hierarchy.newNode(hierarchy, 5, 0);
        Hierarchy.newNode(hierarchy, 6, 3);
        Hierarchy.newNode(hierarchy, 11, 6);
        Hierarchy.newNode(hierarchy, 12, 3, 0);
        Hierarchy.newNode(hierarchy, 13, 11, 6);
        Hierarchy.newNode(hierarchy, 17);
        Hierarchy.newNode(hierarchy, 13, 11, 6, 17);

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

    public static Hierarchy newTestHierarchy2() {
        Hierarchy hierarchy = Hierarchy.clearHierarchy();
        Hierarchy.newNode(hierarchy, 0);
        Hierarchy.newNode(hierarchy, 1);
        Hierarchy.newNode(hierarchy, 2, 0);
        Hierarchy.newNode(hierarchy, 3, 0, 1);
        Hierarchy.newNode(hierarchy, 4, 1);
        Hierarchy.newNode(hierarchy, 5, 1, 3);
        Hierarchy.newNode(hierarchy, 6, 5, 3);
        Hierarchy.newNode(hierarchy, 7, 2, 1);
        Hierarchy.newNode(hierarchy, 10, 2, 1);
        Hierarchy.newNode(hierarchy, 13);
        Hierarchy.newNode(hierarchy, 15, 13, 10);
        Hierarchy.newNode(hierarchy, 16);
        Hierarchy.newNode(hierarchy, 13, 16);

        Hierarchy.newNode(hierarchy, 9);
        return hierarchy;
    }

    /**
     * Test hierarchy 1.
     */
    public void testHierarchy1() {
        Hierarchy hierarchy = newTestHierarchy1();
        List<Integer> c = HierarchyPathFinder.createListFromHierarchy(hierarchy);
        List<Integer> list1 = c.subList(0, 4);
        List<Integer> list2 = new ArrayList<Integer>();
        list2.add(0);
        list2.add(17);
        list2.add(3);
        list2.add(7);
        assertEquals(true, list2.containsAll(list1) && list1.containsAll(list2));
        list1 = c.subList(4, 11);
        list2 = new ArrayList<Integer>();
        list2.add(1);
        list2.add(4);
        list2.add(5);
        list2.add(6);
        list2.add(12);
        list2.add(8);
        list2.add(14);
        assertEquals(true, list2.containsAll(list1) && list1.containsAll(list2));
        list1 = c.subList(11, 15);
        list2 = new ArrayList<Integer>();
        list2.add(2);
        list2.add(11);
        list2.add(16);
        list2.add(15);
        assertEquals(true, list2.containsAll(list1) && list1.containsAll(list2));
        list1 = c.subList(15, 17);
        list2 = new ArrayList<Integer>();
        list2.add(13);
        list2.add(22);
        assertEquals(true, list2.containsAll(list1) && list1.containsAll(list2));
        list1 = c.subList(17, 18);
        list2 = new ArrayList<Integer>();
        list2.add(23);
        assertEquals(true, list2.containsAll(list1) && list1.containsAll(list2));
        list1 = c.subList(18, 19);
        list2 = new ArrayList<Integer>();
        list2.add(24);
        assertEquals(true, list2.containsAll(list1) && list1.containsAll(list2));
        list1 = c.subList(19, 20);
        list2 = new ArrayList<Integer>();
        list2.add(25);
        assertEquals(true, list2.containsAll(list1) && list1.containsAll(list2));
    }

    /**
     * Test hierarchy 1.
     */
    public void testHierarchy2() {
        Hierarchy hierarchy = newTestHierarchy2();
        List<Integer> c = HierarchyPathFinder.createListFromHierarchy(hierarchy);
        List<Integer> list1 = c.subList(0, 4);
        List<Integer> list2 = new ArrayList<Integer>();
        list2.add(0);
        list2.add(16);
        list2.add(1);
        list2.add(9);
        assertEquals(true, list2.containsAll(list1) && list1.containsAll(list2));
        list1 = c.subList(4, 8);
        list2 = new ArrayList<Integer>();
        list2.add(2);
        list2.add(3);
        list2.add(4);
        list2.add(13);
        assertEquals(true, list2.containsAll(list1) && list1.containsAll(list2));
        list1 = c.subList(8, 11);
        list2 = new ArrayList<Integer>();
        list2.add(5);
        list2.add(7);
        list2.add(10);
        assertEquals(true, list2.containsAll(list1) && list1.containsAll(list2));
        list1 = c.subList(11, 13);
        list2 = new ArrayList<Integer>();
        list2.add(6);
        list2.add(15);
        assertEquals(true, list2.containsAll(list1) && list1.containsAll(list2));
    }

    /**
     * Testing for indexing nodes of hierarchy 3
     */
    public void testHierarchy3Indexing() {
        Hierarchy hierarchy = newTestHierarchy3();
        HierarchyRepresentation hierarchyRepresentation = HierarchyRepresentation.getInstance(hierarchy);
        hierarchyRepresentation.splitHierarchyByLevels(hierarchy.getNodes().keySet(), 0);
        hierarchyRepresentation.splitNodesByIndex();
        int indexOf0 = hierarchyRepresentation.getIndexOf(0);
        assertEquals(true, indexOf0 == hierarchyRepresentation.getIndexOf(3));
        assertEquals(true, indexOf0 == hierarchyRepresentation.getIndexOf(1));
        assertEquals(true, indexOf0 == hierarchyRepresentation.getIndexOf(4));
        assertEquals(true, indexOf0 == hierarchyRepresentation.getIndexOf(5));
        assertEquals(true, indexOf0 == hierarchyRepresentation.getIndexOf(6));
        assertEquals(true, indexOf0 == hierarchyRepresentation.getIndexOf(12));
        assertEquals(true, indexOf0 == hierarchyRepresentation.getIndexOf(2));
        int indexOf7 = hierarchyRepresentation.getIndexOf(7);
        assertEquals(true, indexOf7 == hierarchyRepresentation.getIndexOf(8));
        assertEquals(true, indexOf7 == hierarchyRepresentation.getIndexOf(14));
        assertEquals(true, indexOf7 == hierarchyRepresentation.getIndexOf(16));
        assertEquals(true, indexOf7 == hierarchyRepresentation.getIndexOf(15));
        assertEquals(true, indexOf7 == hierarchyRepresentation.getIndexOf(22));
        assertEquals(true, indexOf7 == hierarchyRepresentation.getIndexOf(23));
        assertEquals(true, indexOf7 == hierarchyRepresentation.getIndexOf(24));
        assertEquals(true, indexOf7 == hierarchyRepresentation.getIndexOf(25));
        int indexOf17 = hierarchyRepresentation.getIndexOf(17);
        assertEquals(true, indexOf17 == hierarchyRepresentation.getIndexOf(11));
        assertEquals(true, indexOf17 == hierarchyRepresentation.getIndexOf(13));
    }

}
