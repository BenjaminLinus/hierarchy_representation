package com.alfrescodev.client.widget;

import com.alfrescodev.client.action.OkCancelAction;
import com.alfrescodev.client.data.Hierarchy;
import com.alfrescodev.client.data.Node;
import com.alfrescodev.client.figure.HierarchyRepresentation;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;

import java.util.*;

/**
 *
 * Widget for context menu, opens when a widget highlights.
 * The class is a singletone.
 *
 */
public class NodeHighlightContextMenu extends PopupPanel {

    private static final int CONEXT_MENU_TOP_MARGIN = 35;
    private static final int CONEXT_MENU_LEFT_MARGIN = 4;
    private static final double MIN_ADD_LINKS_MENU_DIFF = 200;
    private static final int CONEXT_MENU_LEFT_MARGIN_SUB = 10;
    private static final String DELETE_CONFIRM_MESSAGE = "Are you sure to delete node?";
    private static final String STYLE_NAME = "hightlight-context-menu";
    private static final String LABEL_STYLE_NAME = "menu-item";
    private static final byte V_PADDING = 10;
    private static final byte H_PADDING = 5;
    private static final int MENU_ITEM_FONT_SIZE = 12;
    private static final String CLEAR_LABEL_CONTENT = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";

    private static volatile NodeHighlightContextMenu singleInstance;

    private FlowPanel offsetPanel;
    private HorizontalPanel mainWidget;
    private VerticalPanel verticalPanel = new VerticalPanel();
    private int verticalPanelElementsCount;
    private Set<Widget> verticalPanelElements = new HashSet<Widget>();
    private FlowPanel deleteNodeLabel, addLinkLabel, deleteLinksLabel, clearLabel;
    private volatile AddLinksMenu addLinksMenuInstance;
    private volatile DeleteLinksMenu deleteLinksMenuInstance;
    private Hierarchy hierarchy;

    /**
     * Complicated CircleWidget, that represents the node
     */
    private CircleWidget circleWidget;

    private NodeHighlightContextMenu(Hierarchy hierarchy) {
        super(true);
        this.hierarchy = hierarchy;
        getElement().getStyle().setPosition(Style.Position.ABSOLUTE);
        createHorizontalPanel();
        setWidget(mainWidget);
        setStyleName(STYLE_NAME);
    }

    private void createHorizontalPanel() {
        mainWidget = new HorizontalPanel();
        offsetPanel = new FlowPanel();
        //offsetPanel.getElement().setInnerHTML("aaaa");
        mainWidget.add(offsetPanel);
        createVerticalPanel();
        mainWidget.add(verticalPanel);
    }

    private static void setHeightForMenuItems(int lineHeight) {
        for (Widget widget : singleInstance.verticalPanelElements) {
            widget.getElement().getStyle().setLineHeight(lineHeight, Style.Unit.PX);
            widget.setHeight(lineHeight+"px");
        }
    }

    /**
     * The method return single instance of the class.
     *
     * @param circleWidget
     * @param radius
     * @param offsetLeft
     * @param offsetTop
     * @param hierarchy
     * @return
     */
    public static NodeHighlightContextMenu getInstance(CircleWidget circleWidget,
                                                       Integer radius, Double offsetLeft,
                                                       Double offsetTop, Hierarchy hierarchy) {
        if (singleInstance == null) {
            synchronized (NodeHighlightContextMenu.class) {
                if (singleInstance == null) {
                    singleInstance = new NodeHighlightContextMenu(hierarchy);
                }
            }
        }
        synchronized (NodeHighlightContextMenu.class) {
            if (circleWidget != singleInstance.circleWidget) {
                singleInstance.circleWidget = circleWidget;
                singleInstance.hideLinksMenu(singleInstance.addLinksMenuInstance);
                singleInstance.hideLinksMenu(singleInstance.deleteLinksMenuInstance);
                singleInstance.showVerticalPanel();
            }
            if (offsetTop != null)
                singleInstance.getElement().getStyle().setTop(offsetTop, Style.Unit.PX);
            if (offsetLeft != null) {
                singleInstance.offsetPanel.getElement().getStyle().setWidth(offsetLeft, Style.Unit.PX);
                singleInstance.formVerticalPanel();
            }
            if (radius != null) {
                singleInstance.getElement().getStyle().setLeft(radius, Style.Unit.PX);
                int lineHeight = ( radius * 2 - 2 * V_PADDING ) / singleInstance.verticalPanelElementsCount;
                setHeightForMenuItems(lineHeight);
            }
        }
        return singleInstance;
    }

    private FlowPanel createMenuItem(String caption, ClickHandler clickHandler) {
        FlowPanel labelWrapper = new FlowPanel();
        labelWrapper.getElement().getStyle().setPosition(Style.Position.RELATIVE);
        Label label = new Label(caption);
        label.getElement().getStyle().setDisplay(Style.Display.INLINE_BLOCK);
        label.getElement().getStyle().setFontSize(MENU_ITEM_FONT_SIZE, Style.Unit.PX);
        label.getElement().getStyle().setLineHeight(MENU_ITEM_FONT_SIZE, Style.Unit.PX);
        label.setStyleName(LABEL_STYLE_NAME);
        label.addClickHandler(clickHandler);
        label.getElement().getStyle().setWhiteSpace(Style.WhiteSpace.NOWRAP);
        labelWrapper.add(label);
        return labelWrapper;
    }

    /**
     * The method creates the panel with control buttons:
     * delete node, delete links, add links.
     */
    private void formVerticalPanel() {
        verticalPanelElementsCount = 0;
        verticalPanel.clear();
        verticalPanelElements.clear();

        verticalPanel.add(deleteNodeLabel);
        ++verticalPanelElementsCount;
        verticalPanelElements.add(deleteNodeLabel);

        if (hasLinks()) {
            verticalPanel.add(deleteLinksLabel);
            ++verticalPanelElementsCount;
            verticalPanelElements.add(deleteLinksLabel);
        }

        verticalPanel.add(addLinkLabel);
        ++verticalPanelElementsCount;
        verticalPanelElements.add(addLinkLabel);

        verticalPanel.add(clearLabel);
        verticalPanelElements.add(clearLabel);
    }

    private void createVerticalPanel() {
        verticalPanel.getElement().getStyle().setPaddingTop(V_PADDING, Style.Unit.PX);
        verticalPanel.getElement().getStyle().setPaddingBottom(V_PADDING, Style.Unit.PX);
        verticalPanel.getElement().getStyle().setPaddingRight(H_PADDING, Style.Unit.PX);
        deleteNodeLabel = createMenuItem("Delete", new ClickHandler() {
            public void onClick(ClickEvent event) {
                deleteNodeClick();
            }
        });
        deleteLinksLabel = createMenuItem("Delete links", new ClickHandler() {
            public void onClick(ClickEvent event) {
                deleteLinksClick();
            }
        });
        addLinkLabel = createMenuItem("Add link", new ClickHandler() {
            public void onClick(ClickEvent event) {
                addLinkClick();
            }
        });
        clearLabel = new FlowPanel();
        clearLabel.getElement().setInnerHTML(CLEAR_LABEL_CONTENT);
        clearLabel.getElement().getStyle().setDisplay(Style.Display.NONE);
    }

    private boolean hasLinks() {
        Collection<Integer> parentIds = circleWidget.getNode().getParentIds();
        return ((parentIds != null) && (parentIds.size() > 0));
    }

    private void hideVerticalPanel() {
        deleteNodeLabel.getElement().getStyle().setDisplay(Style.Display.NONE);
        deleteLinksLabel.getElement().getStyle().setDisplay(Style.Display.NONE);
        addLinkLabel.getElement().getStyle().setDisplay(Style.Display.NONE);
        if (circleWidget != null)
            clearLabel.setHeight( (((int)circleWidget.getRadius() - V_PADDING)*2)+"px");
        clearLabel.getElement().getStyle().setDisplay(Style.Display.BLOCK);
    }

    private void showVerticalPanel() {
        deleteNodeLabel.getElement().getStyle().setDisplay(Style.Display.BLOCK);
        deleteLinksLabel.getElement().getStyle().setDisplay(Style.Display.BLOCK);
        addLinkLabel.getElement().getStyle().setDisplay(Style.Display.BLOCK);
        clearLabel.getElement().getStyle().setDisplay(Style.Display.NONE);
    }

    /**
     * The method shows menu for delete or add links.
     *
     * @param linksManageMenu
     */
    private void showLinksMenu(LinksManageMenu linksManageMenu) {
        deleteLinksMenuInstance().getElement().getStyle().setDisplay(Style.Display.NONE);
        addLinksMenuInstance().getElement().getStyle().setDisplay(Style.Display.NONE);
        linksManageMenu.getElement().getStyle().setDisplay(Style.Display.INLINE_BLOCK);
        hideVerticalPanel();
        double diff = (circleWidget.getCanvas().getCoordinateSpaceWidth() - circleWidget.getPosX());
        int sub = 0;
        if (diff < MIN_ADD_LINKS_MENU_DIFF) {
            sub = (int) (MIN_ADD_LINKS_MENU_DIFF - diff) + CONEXT_MENU_LEFT_MARGIN_SUB;
        }
        linksManageMenu.getElement().getStyle().setLeft(circleWidget.getRadius()
                + CONEXT_MENU_LEFT_MARGIN - sub, Style.Unit.PX);
    }

    /**
     * The method hides menu for delete or add links.
     *
     * @param linksManageMenu
     */
    private void hideLinksMenu(LinksManageMenu linksManageMenu) {
        if (linksManageMenu != null)
            linksManageMenu.getElement().getStyle().setDisplay(Style.Display.NONE);
        showVerticalPanel();
    }

    private void addLinkClick() {
        circleWidget.incrementZindex();
        showLinksMenu(addLinksMenuInstance());
    }

    private void deleteNodeClick() {
        if (Window.confirm(DELETE_CONFIRM_MESSAGE)) {
            deleteNode(circleWidget.getNode());
            HierarchyRepresentation.redraw();
        }
    }

    private void deleteNode(Node node) {
        Map<Integer, Node> nodes = hierarchy.getNodes();
        nodes.remove(node.getId());
        for (Node tNode : nodes.values()) {
            Set<Integer> p = tNode.getParentIds();
            if (p != null)
                p.remove(node.getId());
        }
    }

    private void deleteLinksClick() {
        circleWidget.incrementZindex();
        showLinksMenu(deleteLinksMenuInstance());
    }

    private void addManageLinksMenu(LinksManageMenu linksManageMenu) {
        mainWidget.add(linksManageMenu);
        linksManageMenu.setHeight(2 * circleWidget.getRadius() + CONEXT_MENU_TOP_MARGIN * 2 + "px");
        //int top = - (int) (circleWidget.getItemHeight() - 2*circleWidget.getRadius()) /2;
        int top = -CONEXT_MENU_TOP_MARGIN;
        linksManageMenu.getElement().getStyle().setTop(top, Style.Unit.PX);
    }

    private OkCancelAction newLinksOkCancelAction() {
        OkCancelAction okCancelAction = new OkCancelAction() {

            public void onOk() {
                hideLinksMenu(addLinksMenuInstance);
                hideLinksMenu(deleteLinksMenuInstance);
            }

            public void onCancel() {
                hideLinksMenu(addLinksMenuInstance);
                hideLinksMenu(deleteLinksMenuInstance);
            }
        };
        return okCancelAction;
    }

    /**
     *
     * Returns the single instance of "add links" menu.
     *
     * @return
     */
    private LinksManageMenu addLinksMenuInstance() {
        if (addLinksMenuInstance == null) {
            synchronized (NodeHighlightContextMenu.class) {
                if (addLinksMenuInstance == null) {
                    OkCancelAction okCancelAction = newLinksOkCancelAction();
                    addLinksMenuInstance = AddLinksMenu.getInstance(okCancelAction);
                    addManageLinksMenu(addLinksMenuInstance);
                }
            }
        }
        synchronized (NodeHighlightContextMenu.class) {
            addLinksMenuInstance.setupLinksTable(formAvailableLinks(), circleWidget.getNode());
        }
        return addLinksMenuInstance;
    }

    /**
     * Returns the single instance of "delete links" menu.
     *
     * @return
     */
    private LinksManageMenu deleteLinksMenuInstance() {
        if (deleteLinksMenuInstance == null) {
            synchronized (NodeHighlightContextMenu.class) {
                if (deleteLinksMenuInstance == null) {
                    OkCancelAction okCancelAction = newLinksOkCancelAction();
                    deleteLinksMenuInstance = DeleteLinksMenu.getInstance(okCancelAction);
                    addManageLinksMenu(deleteLinksMenuInstance);
                }
            }
        }
        synchronized (NodeHighlightContextMenu.class) {
            deleteLinksMenuInstance.setupLinksTable(
                    parentNodes(circleWidget.getNode()), circleWidget.getNode());
        }
        return deleteLinksMenuInstance;
    }

    /**
     *
     * The method returns a set of parents nodes for the specified node.
     *
     * @param node
     * @return
     */
    private Set<Node> parentNodes(Node node) {
        Set<Node> set = new TreeSet<Node>();
        Set<Integer> parentIds = node.getParentIds();
        if (parentIds != null) {
            for (int id:parentIds) {
                set.add(hierarchy.getNodes().get(id));
            }
        }
        return set;
    }

    /**
     *
     * The method creates a set of links available to add to
     * current node.
     *
     * @return
     */
    private Set<Node> formAvailableLinks() {
        Set<Node> availableLinks = new TreeSet<Node>();
        Node currentNode = circleWidget.getNode();
        for (Node node : hierarchy.getNodes().values()) {
            if (!hasInParents(node, currentNode) && !(node.equals(currentNode))
                    && (currentNode.getParentIds() == null ||
                        !currentNode.getParentIds().contains(node.getId()))
                    ) {
                availableLinks.add(node);
            }
        }
        return availableLinks;
    }

    /**
     *
     * Does the @param node have the @param toFind in parents ?
     *
     * @param node
     * @param toFind
     * @return
     */
    private boolean hasInParents(Node node, Node toFind) {
        boolean hasInParents = false;
        Collection<Integer> parents = node.getParentIds();
        if (parents != null)
            for (Integer parentId : parents) {
                if (parentId == toFind.getId() ||
                      hasInParents(hierarchy.getNodes().get(parentId), toFind)) {
                    hasInParents = true;
                }
            }
        return hasInParents;
    }

}
