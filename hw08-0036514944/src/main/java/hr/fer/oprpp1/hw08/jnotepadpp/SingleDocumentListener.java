package hr.fer.oprpp1.hw08.jnotepadpp;

/**
 * Listener that calls methods when SingleDocumentModel changes.
 */
public interface SingleDocumentListener {

    /**
     * Notifies that specified document is modified.
     *
     * @param model that is modified
     */
    void documentModifyStatusUpdated(SingleDocumentModel model);

    /**
     * Notifies that specified document path has changed.
     *
     * @param model
     */
    void documentFilePathUpdated(SingleDocumentModel model);

}
