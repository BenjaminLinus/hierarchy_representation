package com.alfrescodev.client;

import com.alfrescodev.client.data.Hierarchy;
import com.alfrescodev.client.data.HierarchyPathFinder;
import com.alfrescodev.client.figure.HierarchyRepresentation;
import com.google.gwt.junit.client.GWTTestCase;

import java.util.ArrayList;
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

    private static Hierarchy newTestHierarchy1() {
        Hierarchy hierarchy = Hierarchy.clearHierarchy();

        hierarchy.newNode(0);
        hierarchy.newNode(1, 0);
        hierarchy.newNode(2, 0, 1);
        hierarchy.newNode(3);
        hierarchy.newNode(4, 0, 3);
        hierarchy.newNode(5, 0);
        hierarchy.newNode(6, 3);
        hierarchy.newNode(11, 6);
        hierarchy.newNode(12, 3, 0);
        hierarchy.newNode(13, 11, 6);
        hierarchy.newNode(17);
        hierarchy.newNode(13, 11, 6, 17);

        hierarchy.newNode(7);
        hierarchy.newNode(8, 7);
        hierarchy.newNode(14, 7);
        hierarchy.newNode(15, 14, 7);
        hierarchy.newNode(16, 14);
        hierarchy.newNode( 22, 15);
        hierarchy.newNode(23, 22);
        hierarchy.newNode( 24, 23);
        hierarchy.newNode( 25, 24);

        return hierarchy;
    }

    private static Hierarchy newTestHierarchy3() {
        Hierarchy hierarchy = Hierarchy.clearHierarchy();

        hierarchy.newNode(0);
        hierarchy.newNode( 1, 0);
        hierarchy.newNode( 2, 0, 1);
        hierarchy.newNode(3);
        hierarchy.newNode(4, 0, 3);
        hierarchy.newNode( 5, 0);
        hierarchy.newNode( 6, 3);

        hierarchy.newNode( 11);
        hierarchy.newNode(12, 3, 0);
        hierarchy.newNode(13, 11);
        hierarchy.newNode(17);
        hierarchy.newNode(13, 11, 17);

        hierarchy.newNode(7);
        hierarchy.newNode( 8, 7);
        hierarchy.newNode( 14, 7);
        hierarchy.newNode( 15, 14, 7);
        hierarchy.newNode(16, 14);
        hierarchy.newNode(22, 15);
        hierarchy.newNode(23, 22);
        hierarchy.newNode(24, 23);
        hierarchy.newNode(25, 24);

        return hierarchy;
    }

    private static Hierarchy newTestHierarchy2() {
        Hierarchy hierarchy = Hierarchy.clearHierarchy();
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
        assertTrue(list2.containsAll(list1) && list1.containsAll(list2));
        list1 = c.subList(4, 11);
        list2 = new ArrayList<Integer>();
        list2.add(1);
        list2.add(4);
        list2.add(5);
        list2.add(6);
        list2.add(12);
        list2.add(8);
        list2.add(14);
        assertTrue(list2.containsAll(list1) && list1.containsAll(list2));
        list1 = c.subList(11, 15);
        list2 = new ArrayList<Integer>();
        list2.add(2);
        list2.add(11);
        list2.add(16);
        list2.add(15);
        assertTrue(list2.containsAll(list1) && list1.containsAll(list2));
        list1 = c.subList(15, 17);
        list2 = new ArrayList<Integer>();
        list2.add(13);
        list2.add(22);
        assertTrue(list2.containsAll(list1) && list1.containsAll(list2));
        list1 = c.subList(17, 18);
        list2 = new ArrayList<Integer>();
        list2.add(23);
        assertTrue(list2.containsAll(list1) && list1.containsAll(list2));
        list1 = c.subList(18, 19);
        list2 = new ArrayList<Integer>();
        list2.add(24);
        assertTrue(list2.containsAll(list1) && list1.containsAll(list2));
        list1 = c.subList(19, 20);
        list2 = new ArrayList<Integer>();
        list2.add(25);
        assertTrue(list2.containsAll(list1) && list1.containsAll(list2));
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
        assertTrue(list2.containsAll(list1) && list1.containsAll(list2));
        list1 = c.subList(4, 8);
        list2 = new ArrayList<Integer>();
        list2.add(2);
        list2.add(3);
        list2.add(4);
        list2.add(13);
        assertTrue(list2.containsAll(list1) && list1.containsAll(list2));
        list1 = c.subList(8, 11);
        list2 = new ArrayList<Integer>();
        list2.add(5);
        list2.add(7);
        list2.add(10);
        assertTrue(list2.containsAll(list1) && list1.containsAll(list2));
        list1 = c.subList(11, 13);
        list2 = new ArrayList<Integer>();
        list2.add(6);
        list2.add(15);
        assertTrue(list2.containsAll(list1) && list1.containsAll(list2));
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
        assertEquals(indexOf0, (int) hierarchyRepresentation.getIndexOf(3));
        assertEquals(indexOf0, (int) hierarchyRepresentation.getIndexOf(1));
        assertEquals(indexOf0, (int) hierarchyRepresentation.getIndexOf(4));
        assertEquals(indexOf0, (int) hierarchyRepresentation.getIndexOf(5));
        assertEquals(indexOf0, (int) hierarchyRepresentation.getIndexOf(6));
        assertEquals(indexOf0, (int) hierarchyRepresentation.getIndexOf(12));
        assertEquals(indexOf0, (int) hierarchyRepresentation.getIndexOf(2));
        int indexOf7 = hierarchyRepresentation.getIndexOf(7);
        assertEquals(indexOf7, (int) hierarchyRepresentation.getIndexOf(8));
        assertEquals(indexOf7, (int) hierarchyRepresentation.getIndexOf(14));
        assertEquals(indexOf7, (int) hierarchyRepresentation.getIndexOf(16));
        assertEquals(indexOf7, (int) hierarchyRepresentation.getIndexOf(15));
        assertEquals(indexOf7, (int) hierarchyRepresentation.getIndexOf(22));
        assertEquals(indexOf7, (int) hierarchyRepresentation.getIndexOf(23));
        assertEquals(indexOf7, (int) hierarchyRepresentation.getIndexOf(24));
        assertEquals(indexOf7, (int) hierarchyRepresentation.getIndexOf(25));
        int indexOf17 = hierarchyRepresentation.getIndexOf(17);
        assertEquals(indexOf17, (int) hierarchyRepresentation.getIndexOf(11));
        assertEquals(indexOf17, (int) hierarchyRepresentation.getIndexOf(13));
    }

}
