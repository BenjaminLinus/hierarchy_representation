package com.alfrescodev.client.action;

/**
 *
 * Interface for handle OK and Cancel action.
 *
 */
public interface OkCancelAction {

    /**
     * Handle Ok button click.
     */
    void onOk();

    /**
     * Handle cancel button click.
     */
    void onCancel();

}
