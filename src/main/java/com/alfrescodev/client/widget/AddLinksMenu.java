package com.alfrescodev.client.widget;

import com.alfrescodev.client.action.OkCancelAction;
import com.alfrescodev.client.data.Node;
import com.alfrescodev.client.figure.HierarchyRepresentation;
import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.user.cellview.client.Column;

import java.util.HashSet;
import java.util.Set;

/**
 *
 * Widget for context menu, opens when "add links" clicked.
 * The class is a singletone.
 *
 */
public class AddLinksMenu extends LinksManageMenu {

    private static final String HEADER_CAPTION = "Add parents";
    private static AddLinksMenu singleInstance;
    /**
     * Collection of checkboxed nodes.
     */
    private Set<Node> selected = new HashSet<Node>();
    /**
     * Checkboxes table cell.
     */
    private CheckboxCell checkboxCell;

    /**
     *
     * The caption for panel header.
     *
     * @return
     */
    @Override
    protected String getHeaderCaption() {
        return HEADER_CAPTION;
    }

    private void updateOkButton() {
        if (selected.size() > 0) {
            getOkButton().setEnabled(true);
        }
        else
            getOkButton().setEnabled(false);
    }

    /**
     * On click ok button.
     */
    @Override
    protected void okClick() {
        boolean r = false;
        if (selected != null && selected.size() > 0) {
            r=true;
            for (Node node : selected) {
                getCurrentNode().addParentId(node.getId());
            }
        }
        super.okClick();
        if (r) {
            HierarchyRepresentation.redraw();
        }
    }

    /**
     *
     * The method creates checkboxes column to select nodes.
     *
     * @return
     */
    @Override
    protected Column<Node, Boolean> secondColumn() {
        checkboxCell = new CheckboxCell();
        Column<Node, Boolean> secondColumn = new Column<Node, Boolean>(checkboxCell) {

            @Override
            public Boolean getValue(Node object) {
                return false;
            }

        };
        secondColumn.setFieldUpdater(new FieldUpdater<Node, Boolean>() {
            public void update(int index, Node node, Boolean value) {
                if (value != null && value.booleanValue()) {
                    selected.add(node);
                }
                else
                    selected.remove(node);
                updateOkButton();
            }
        });
        return secondColumn;
    }

    /**
     *
     * The method fills the table of nodes.
     *
     * @param availableLinks
     * @param currentNode
     */
    public void setupLinksTable(Set<Node> availableLinks, Node currentNode) {
        if (!availableLinks.equals(getPrev())) {
            getDataProviderList().clear();
            selected.clear();
            getDataProviderList().addAll(availableLinks);
            for (Node node:availableLinks) {
                checkboxCell.setViewData(node, false);
            }
            updateOkButton();
            getTable().redraw();
            int size = availableLinks.size();
            if (getTable().getPageSize() < size)
                getTable().setPageSize(size + 1);
            setPrev(availableLinks);
        }
        super.setupLinksTable(availableLinks, currentNode);
    }

    private AddLinksMenu(OkCancelAction okCancelAction) {
        super(okCancelAction);
    }

    /**
     *
     * Returns single instance of the class.
     *
     * @param okCancelAction
     * @return
     */
    public static AddLinksMenu getInstance(OkCancelAction okCancelAction) {
        if (singleInstance == null) {
            synchronized (NodeHighlightContextMenu.class) {
                if (singleInstance == null) {
                    singleInstance = new AddLinksMenu(okCancelAction);
                }
            }
        }
        return singleInstance;
    }
}
