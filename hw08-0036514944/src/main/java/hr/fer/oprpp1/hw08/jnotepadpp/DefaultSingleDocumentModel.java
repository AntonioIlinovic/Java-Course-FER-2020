package hr.fer.oprpp1.hw08.jnotepadpp;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Class DefaultSingleDocumentModel as implementation of SingleDocumentModel interface (add private helper methods and
 * properties and needed). It should define a constructor with two parameters: file path and text content; the
 * constructor should create an instance of JTextArea and set its text content; reference to this text area must be
 * returned from getTextComponent(). Further, the constructor should register a listener on JTextArea component’s
 * document model and use it to track its modified status flag (boolean property of SingleDocumentModel); each time this
 * flag is modified, all registered listeners should be notified.
 */
public class DefaultSingleDocumentModel implements SingleDocumentModel {

    private final List<SingleDocumentListener> listeners = new ArrayList<>();

    private final JTextArea textComponent;
    private Path filePath;
    private boolean modified;

    public DefaultSingleDocumentModel(Path filePath, String textContent) {
        this.filePath = filePath;
        this.textComponent = new JTextArea(textContent);
        this.textComponent.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                setModified(true);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                setModified(true);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                setModified(true);
            }
        });
    }

    @Override
    public JTextArea getTextComponent() {
        return textComponent;
    }

    @Override
    public Path getFilePath() {
        return filePath;
    }

    @Override
    public void setFilePath(Path filePath) {
        if (filePath == null)
            throw new RuntimeException("Path can't be null!");

        this.filePath = filePath;
        notifyPathChanged();
    }

    @Override
    public boolean isModified() {
        return modified;
    }

    @Override
    public void setModified(boolean modified) {
        this.modified = modified;
        notifyModified();
    }


    /**
     * Helper method that notifies all listeners that path has changed.
     */
    private void notifyPathChanged() {
        listeners.forEach(singleDocumentListener -> singleDocumentListener.documentFilePathUpdated(this));
    }

    /**
     * Helper method that notifies all listeners that document is modified.
     */
    private void notifyModified() {
        listeners.forEach(singleDocumentListener -> singleDocumentListener.documentModifyStatusUpdated(this));
    }

    @Override
    public void addSingleDocumentListener(SingleDocumentListener l) {
        listeners.add(l);
    }

    @Override
    public void removeSingleDocumentListener(SingleDocumentListener l) {
        listeners.remove(l);
    }
}
