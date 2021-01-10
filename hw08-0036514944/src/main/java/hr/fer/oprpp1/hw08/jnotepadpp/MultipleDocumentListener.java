package hr.fer.oprpp1.hw08.jnotepadpp;

/**
 * Listener that calls methods when MultipleDocumentModel changes.
 */
public interface MultipleDocumentListener {

    /**
     * Notifies that current document of a model has changed. previousModel or currentModel can be null but not both.
     *
     * @param previousModel current document before
     * @param currentModel  current document now
     */
    void currentDocumentChanged(SingleDocumentModel previousModel, SingleDocumentModel currentModel);

    /**
     * Notifies that document is added to model.
     *
     * @param model that is added to model
     */
    void documentAdded(SingleDocumentModel model);

    /**
     * Notifies that document is removed from model.
     *
     * @param model that is removed from model
     */
    void documentRemoved(SingleDocumentModel model);

}
