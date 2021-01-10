package hr.fer.oprpp1.hw08.jnotepadpp;

import java.nio.file.Path;

/**
 * MultipleDocumentModel represents a model capable of holding zero, one or more documents, where each document and
 * having a concept of current document – the one which is shown to the user and on which user works.
 * <p>
 * Subject for its state information and define appropriate registration/deregistration methods for its listeners
 * (Observer design pattern).
 */
public interface MultipleDocumentModel extends Iterable<SingleDocumentModel> {

    /**
     * Creates new document in a model.
     *
     * @return newly created document
     */
    SingleDocumentModel createNewDocument();

    /**
     * Returns current document of a model.
     *
     * @return current document of a model
     */
    SingleDocumentModel getCurrentDocument();

    /**
     * loadDocument loads specified file from disk, creates SingleDocumentModel for it, add tab and switch to it. If
     * there already is SingleDocumentModel for specified document, does not create new one, but switches view to this
     * document.
     *
     * @param path from which to load a document, must not be null
     * @return SingleDocumentModel created from loaded document
     */
    SingleDocumentModel loadDocument(Path path);

    /**
     * Saves given document to given path in UTF-8 encoding. If saveDocument is called with newPath of some existing
     * SingleDocumentModel, method will fail and tell the user that the specified file is already opened.
     *
     * @param model   document to save
     * @param newPath , can be null; if null, document should be saved using path associated from document, otherwise,
     *                new path should be used and after saving is completed, document's path is updated to newPath
     */
    void saveDocument(SingleDocumentModel model, Path newPath);

    /**
     * Removes specified document (does not check modification status or ask any questions).
     *
     * @param model document to remove
     */
    void closeDocument(SingleDocumentModel model);

    /**
     * Registers listener.
     *
     * @param l listener to register
     */
    void addMultipleDocumentListener(MultipleDocumentListener l);

    /**
     * Unregisters listener.
     *
     * @param l listener to unregister
     */
    void removeMultipleDocumentListener(MultipleDocumentListener l);

    /**
     * Returns number of opened documents in model.
     *
     * @return number of opened documents in model
     */
    int getNumberOfDocuments();

    /**
     * Returns document at specified index.
     *
     * @param index of document
     * @return document at specified index
     */
    SingleDocumentModel getDocument(int index);

}
