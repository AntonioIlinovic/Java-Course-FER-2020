package hr.fer.oprpp1.hw08.jnotepadpp;

import javax.swing.*;
import java.nio.file.Path;

/**
 * SingleDocumentModel represents a model of single document, having information about file path from which document was
 * loaded (can be null for new document), document modification status and reference to Swing component which is used
 * for editing (each document has its own editor component).
 * <p>
 * Subject for its state information and define appropriate registration/deregistration methods for its listeners
 * (Observer design pattern).
 */
public interface SingleDocumentModel {

    /**
     * Returns textComponent of a document.
     *
     * @return textComponent of a document
     */
    JTextArea getTextComponent();

    /**
     * Returns Path of a document.
     * @return Path of a document
     */
    Path getFilePath();

    /**
     * Sets Path of a document.
     * @param path to set for document, can't be null
     */
    void setFilePath(Path path);

    /**
     * Returns if document is modified.
     * @return true if document is modified, false otherwise
     */
    boolean isModified();

    /**
     * Sets modified flag of document.
     * @param modified to set modified flag of document
     */
    void setModified(boolean modified);

    /**
     * Registers listener.
     *
     * @param l listener to register
     */
    void addSingleDocumentListener(SingleDocumentListener l);

    /**
     * Unregisters listener.
     *
     * @param l listener to unregister
     */
    void removeSingleDocumentListener(SingleDocumentListener l);

}
