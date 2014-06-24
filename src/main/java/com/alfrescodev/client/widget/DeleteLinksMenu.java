package com.alfrescodev.client.widget;

import com.alfrescodev.client.action.OkCancelAction;
import com.alfrescodev.client.data.Node;
import com.alfrescodev.client.figure.HierarchyRepresentation;
import com.google.gwt.cell.client.ActionCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.ClickableTextCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.EventTarget;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.Style;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Window;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import static com.google.gwt.dom.client.BrowserEvents.CLICK;

/**
 *
 * Widget for context menu, opens when "delete links" clicked.
 * The class is a singletone.
 *
 * @author Alfrescodev.com
 *
 */
public class DeleteLinksMenu extends LinksManageMenu {

    private static final String HEADER_CAPTION = "Node parents";
    private static final byte TABLE_PADDING_Y = 5;
    private static final String DROP_IMAGE_STYLE_NAME = "drop-image";
    private static final String IMAGE_PATH = "images/drop.png";
    private static final String DROP_IMAGE_TITLE = "Remove link";

    private static DeleteLinksMenu singleInstance;

    private class RemoveLinkCell extends ClickableTextCell {

        public void render(Context context,
                           SafeHtml value,
                           SafeHtmlBuilder sb) {
            sb.appendHtmlConstant("<img title=\"" + DROP_IMAGE_TITLE + "\" class=\"" + DROP_IMAGE_STYLE_NAME +
                    "\" width=\"16\" height=\"16\" src=\"" + IMAGE_PATH
                    + "\">");
        }

        @Override
        public void onBrowserEvent(Context context, Element parent, String value,
                                   NativeEvent event, ValueUpdater<String> valueUpdater) {
            super.onBrowserEvent(context, parent, value, event, valueUpdater);
            if (CLICK.equals(event.getType())) {
                EventTarget eventTarget = event.getEventTarget();
                if (!Element.is(eventTarget)) {
                    return;
                }
                if (parent.getFirstChildElement().isOrHasChild(Element.as(eventTarget))) {
                    // Ignore clicks that occur outside of the main element.
                    removeParentId(value);
                    onEnterKeyDown(context, parent, value, event, valueUpdater);
                }
            }
        }

    }

    private void removeParentId(String id) {
        getDataProviderList().remove(new Node(new Integer(id).intValue()));
        getTable().redraw();
    }

    @Override
    protected void okClick() {
        boolean r = false;
        Collection<Integer> newParentsIds = Node.getIdsList(getDataProviderList());
        Collection<Integer> oldParentsIds = getCurrentNode().getParentIds();
        if (!newParentsIds.equals(oldParentsIds)) {
            r=true;
            getCurrentNode().clearAndAddParentsIds(newParentsIds);
        }
        super.okClick();
        if (r) {
            HierarchyRepresentation.redrawPlease();
        }
    }

    @Override
    protected String getHeaderCaption() {
        return HEADER_CAPTION;
    }

    @Override
    protected Column<Node, ?> secondColumn() {

        ClickableTextCell clickableTextCell = new RemoveLinkCell();

        Column<Node, String> imageColumn = new Column<Node, String>(clickableTextCell)
            {
                @Override
                public String getValue(Node node) {
                    return ""+node.getId();
                }
            };
        return imageColumn;
    }

    @Override
    public void setupLinksTable(Set<Node> availableLinks, Node currentNode) {
        getDataProviderList().clear();
        getDataProviderList().addAll(availableLinks);
        getTable().redraw();
        setPrev(availableLinks);
        super.setupLinksTable(availableLinks, currentNode);
    }

    private DeleteLinksMenu(OkCancelAction okCancelAction) {
        super(okCancelAction);
        getTable().getElement().getStyle().setPaddingTop(TABLE_PADDING_Y, Style.Unit.PX);
    }

    public static DeleteLinksMenu getInstance(OkCancelAction okCancelAction) {
        if (singleInstance == null) {
            synchronized (NodeHighlightContextMenu.class) {
                if (singleInstance == null) {
                    singleInstance = new DeleteLinksMenu(okCancelAction);
                }
            }
        }
        return singleInstance;
    }

}
