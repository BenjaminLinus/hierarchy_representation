package com.alfrescodev.client.widget;

import com.alfrescodev.client.action.OkCancelAction;
import com.alfrescodev.client.data.Node;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.view.client.ListDataProvider;

import java.util.List;
import java.util.Set;

/**
 *
 * The base class for links manage menu (add links menu and delete links menu).
 *
 * @author Alfrescodev.com
 */
public abstract class LinksManageMenu extends FlowPanel implements HasMouseWheelHandlers {

    private static final String BACKGROUND = "#F5F5F5";
    private static final String STYLE_NAME = "links-manage-menu";
    private static final String BORDER_COLOR = "#777777";
    private static final int HEADER_H_PADDING = 15;
    private static final int HEADER_FONT = 14;
    private static final int HEADER_LINE_HEIGHT = 18;
    private static final int HEADER_HEIGHT = 20;
    private static final int Z_INDEX = 20;
    private static final int BUTTONS_HEIGHT = 25;
    private static final int BUTTON_MARGIN_X = 5;
    private static final int BUTTON_MARGIN_Y = 4;
    private static final int BUTTONS_PADDING_X = 7;
    private static final int BUTTON_WIDTH = 70;
    protected static final byte LINE_HEIGHT = 12;
    protected static final byte MIDDLE_PANEL_HEIGHT = 90;
    protected static final byte TABLE_PERCENT_PADDING_X = 16;
    private static final String BUTTON_STYLE_NAME = "action-button";
    protected static final String TABLE_STYLE_NAME = "nodes-table";
    private static final int SCROLL_K = 5;
    private static final String NO_ITEMS_MESSAGE = "No items";

    private VerticalPanel mainPanel;
    private FlowPanel noItemsPanel;

    private OkCancelButton okButton;
    private OkCancelButton cancelButton;
    private OkCancelAction okCancelAction;
    private ScrollPanel scrollPanel;
    private CellTable<Node> table;
    private List<Node> dataProviderList;
    private Set<Node> prev;
    private Node currentNode;

    /**
     *
     * The caption for panel header.
     *
     * @return
     */
    protected abstract String getHeaderCaption();

    /**
     *
     * The method creates the second column in table.
     *
     * @return
     */
    protected abstract Column<Node, ?> secondColumn();

    /**
     * The class represents ok or cancel button.
     * The class uses css styles to disable or enable button.
     */
    private class OkCancelButton extends Button {

        public OkCancelButton(String caption) {
            super(caption);
        }

        private static final String DISABLED_STYLE_NAME = "disabled";

        @Override
        public void setEnabled(boolean enabled) {
            super.setEnabled(true);
            if (enabled) {
                removeStyleName(DISABLED_STYLE_NAME);
            }
            else
                addStyleName(DISABLED_STYLE_NAME);
        }

        public boolean isNotDisabled() {
            return !this.getStyleName().contains(DISABLED_STYLE_NAME);
        }

    }

    /**
     *
     * Class constructor
     *
     * @param okCancelAction - handler for ok or cancel click.
     */
    protected LinksManageMenu(OkCancelAction okCancelAction) {
        this.okCancelAction = okCancelAction;
        setStyleName(STYLE_NAME);
        getElement().getStyle().setPosition(Style.Position.ABSOLUTE);
        getElement().getStyle().setBackgroundColor(BACKGROUND);
        getElement().getStyle().setBorderStyle(Style.BorderStyle.SOLID);
        getElement().getStyle().setBorderWidth(1, Style.Unit.PX);
        getElement().getStyle().setBorderColor(BORDER_COLOR);
        getElement().getStyle().setZIndex(Z_INDEX);
        createMainPanel();
        add(mainPanel);
        addMouseWheelHandler(new MouseWheelHandler() {
            public void onMouseWheel(MouseWheelEvent event) {
                event.preventDefault();
                int dy = event.getDeltaY() / Math.abs(event.getDeltaY()) * SCROLL_K; 
                contentPanel().setVerticalScrollPosition(
                        contentPanel().getVerticalScrollPosition() + dy);
            }
        });
    }

    private CellTable<Node> createLinksTable() {
        table = new CellTable<Node>();
        TextColumn<Node> nameColumn = new TextColumn<Node>() {
            @Override
            public String getValue(Node node) {
                if (node == null)
                    return "null";
                else
                    return new Integer(node.getId()).toString();
            }
        };
        table.addColumn(nameColumn);
        table.addColumn(secondColumn());
        table.setWidth(100+"%");
        table.addStyleName(TABLE_STYLE_NAME);
        table.getElement().getStyle().setLineHeight(LINE_HEIGHT, Style.Unit.PX);
        table.getElement().getStyle().setFontWeight(Style.FontWeight.NORMAL);
        table.getElement().getStyle().setPaddingLeft(TABLE_PERCENT_PADDING_X, Style.Unit.PCT);
        table.getElement().getStyle().setPaddingRight(TABLE_PERCENT_PADDING_X, Style.Unit.PCT);
        //table.getElement().getStyle().setTextAlign(Style.TextAlign.CENTER);
        ListDataProvider<Node> dataProvider = new ListDataProvider<Node>();
        dataProviderList = dataProvider.getList();
        dataProvider.addDataDisplay(table);
        return table;
    }

    /**
     *
     * Middle panel for containing main content
     * in vertical panel.
     *
     * @return
     */
    protected ScrollPanel contentPanel() {
        if (scrollPanel == null) {
            scrollPanel = createContentPanel();
        }
        return scrollPanel;
    }

    private ScrollPanel createContentPanel() {
        ScrollPanel panel = new ScrollPanel();
        panel.getElement().getStyle().setTextAlign(Style.TextAlign.CENTER);
        panel.add(createLinksTable());
        panel.getElement().getStyle().setOverflowY(Style.Overflow.AUTO);
        panel.setHeight(MIDDLE_PANEL_HEIGHT+"px");
        return panel;
    }

    private Label createCaptionLabel() {
        Label caption = new Label(getHeaderCaption());
        caption.getElement().getStyle().setWhiteSpace(Style.WhiteSpace.NOWRAP);
        caption.getElement().getStyle().setPaddingLeft(HEADER_H_PADDING, Style.Unit.PX);
        caption.getElement().getStyle().setPaddingRight(HEADER_H_PADDING, Style.Unit.PX);
        caption.getElement().getStyle().setFontSize(HEADER_FONT, Style.Unit.PX);
        caption.getElement().getStyle().setFontWeight(Style.FontWeight.BOLD);
        caption.getElement().getStyle().setLineHeight(HEADER_LINE_HEIGHT, Style.Unit.PX);
        caption.getElement().getStyle().setTextAlign(Style.TextAlign.CENTER);
        caption.setHeight(HEADER_HEIGHT+"px");
        return caption;
    }

    /**
     * on ok click...
     */
    protected void okClick() {
        if (okButton.isNotDisabled())
            okCancelAction.onOk();
    }

    /**
     * on calcel click...
     */
    protected void cancelClick() {
        if (cancelButton.isNotDisabled())
            okCancelAction.onCancel();
    }

    private void createOkButton() {
        okButton = new OkCancelButton("Ok");
        okButton.addStyleName(BUTTON_STYLE_NAME);
        //okButton.setEnabled(false);
        okButton.setWidth(BUTTON_WIDTH + "px");
        okButton.getElement().getStyle().setMarginRight(BUTTON_MARGIN_X, Style.Unit.PX);
        okButton.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                okClick();
            }
        });
    }

    private void createCancelButton() {
        cancelButton = new OkCancelButton("Cancel");
        cancelButton.addStyleName(BUTTON_STYLE_NAME);
        cancelButton.setWidth(BUTTON_WIDTH + "px");
        cancelButton.getElement().getStyle().setMarginLeft(BUTTON_MARGIN_X, Style.Unit.PX);
        cancelButton.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
            cancelClick();
            }
        });
    }

    private Panel createControlButtons() {
        FlowPanel panel = new FlowPanel();
        //panel.getElement().getStyle().setVerticalAlign(Style.VerticalAlign.BOTTOM);
        //panel.getElement().getStyle().setPosition(Style.Position.ABSOLUTE);
        //panel.getElement().getStyle().setBottom(BUTTON_MARGIN_Y, Style.Unit.PX);
        panel.setHeight(BUTTONS_HEIGHT + 1 + "px");
        //panel.getElement().getStyle().setLeft(0, Style.Unit.PX);
        //panel.getElement().getStyle().setRight(0, Style.Unit.PX);
        panel.getElement().getStyle().setLineHeight(BUTTONS_HEIGHT, Style.Unit.PX);
        panel.getElement().getStyle().setTextAlign(Style.TextAlign.CENTER);
        panel.getElement().getStyle().setPaddingLeft(BUTTONS_PADDING_X, Style.Unit.PX);
        panel.getElement().getStyle().setPaddingRight(BUTTONS_PADDING_X, Style.Unit.PX);
        panel.getElement().getStyle().setPaddingBottom(BUTTON_MARGIN_Y, Style.Unit.PX);

        HorizontalPanel buttons = new HorizontalPanel();
        //buttons.getElement().getStyle().setVerticalAlign(Style.VerticalAlign.BOTTOM);
        buttons.setHeight(BUTTONS_HEIGHT + "px");
        buttons.getElement().getStyle().setLineHeight(BUTTONS_HEIGHT, Style.Unit.PX);
        createOkButton();
        buttons.add(okButton);
        createCancelButton();
        buttons.add(cancelButton);
        buttons.getElement().getStyle().setTextAlign(Style.TextAlign.CENTER);
        buttons.getElement().getStyle().setDisplay(Style.Display.INLINE_BLOCK);

        panel.add(buttons);
        return panel;
    }

    /**
     * the method hides content panel
     */
    protected void hideContentPanel() {
        contentPanel().getElement().getStyle().setDisplay(Style.Display.NONE);
    }

    /**
     * the method shows content panel
     */
    protected void showContentPanel() {
        contentPanel().getElement().getStyle().setDisplay(Style.Display.BLOCK);
    }

    /**
     * the method hides "no items" label
     */
    protected void hideNoItemsPanel() {
        noItemsPanel().getElement().getStyle().setDisplay(Style.Display.NONE);
    }

    /**
     * the method shows "no items" label
     */
    protected void showNoItemsPanel() {
        noItemsPanel().getElement().getStyle().setDisplay(Style.Display.BLOCK);
    }

    private void createMainPanel() {
        mainPanel = new VerticalPanel();
        mainPanel.setWidth("100%");
        mainPanel.setHeight("100%");
        mainPanel.add(createCaptionLabel());
        mainPanel.add(contentPanel());
        hideContentPanel();
        mainPanel.add(noItemsPanel());
        mainPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_BOTTOM);
        mainPanel.add(createControlButtons());
    }

    private Panel noItemsPanel() {
        if (noItemsPanel == null) {
            noItemsPanel = createItemsPanel();
            noItemsPanel.getElement().getStyle().setTextAlign(Style.TextAlign.CENTER);
        }
        return noItemsPanel;
    }

    /**
     *
     * The method fills the table of nodes.
     *
     * @param availableLinks
     * @param currentNode
     */
    public void setupLinksTable(Set<Node> availableLinks, Node currentNode) {
        this.currentNode = currentNode;
        if (getDataProviderList() != null && getDataProviderList().size() > 0) {
            showContentPanel();
            hideNoItemsPanel();
        }
        else {
            hideContentPanel();
            showNoItemsPanel();
        }
    }

    private FlowPanel createItemsPanel() {
        FlowPanel panel = new FlowPanel();
        panel.add(new Label(NO_ITEMS_MESSAGE));
        return panel;
    }

    protected Button getOkButton() {
        return okButton;
    }

    public HandlerRegistration addMouseWheelHandler(MouseWheelHandler handler) {
        return addDomHandler(handler, MouseWheelEvent.getType());
    }

    protected List<Node> getDataProviderList() {
        return dataProviderList;
    }

    protected CellTable<Node> getTable() {
        return table;
    }

    protected Set<Node> getPrev() {
        return prev;
    }

    protected void setPrev(Set<Node> prev) {
        this.prev = prev;
    }

    protected Node getCurrentNode() {
        return currentNode;
    }
}
