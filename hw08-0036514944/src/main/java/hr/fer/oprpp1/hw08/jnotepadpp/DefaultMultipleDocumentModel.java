package hr.fer.oprpp1.hw08.jnotepadpp;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Class DefaultMultipleDocumentModel by inheriting from JTabbedPane and implementing MultipleDocumentModel. This class
 * should have a collection of SingleDocumentModel objects, a reference to current SingleDocumentModel and a support for
 * listeners management. Add private helper methods and properties and needed. Ensure that in this class you track which
 * tab is currently shown to user and update current SingleDocumentModel accordingly. Implement loadDocument to load
 * specified file from disk, create SingleDocumentModel for it, add tab and switch to it. If there already is
 * SingleDocumentModel for specified document, do not create new one, but switch view to this document. When loading new
 * document or creating empty documents, add appropriate code/listeners which will ensure that tab icon tracks
 * modification status of document (this is internal detail of DefaultMultipleDocumentModel class; outside code in not
 * aware of this).
 */
public class DefaultMultipleDocumentModel extends JTabbedPane implements MultipleDocumentModel {

    private final List<MultipleDocumentListener> listeners;

    private final List<SingleDocumentModel> documents;
    private SingleDocumentModel currentDocument;


    /**
     * Constructor for DefaultMultipleDocumentModel
     */
    public DefaultMultipleDocumentModel() {
        listeners = new ArrayList<>();
        documents = new ArrayList<>();

        /* Listener when tab is switched */
        addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                JTabbedPane pane = (JTabbedPane) e.getSource();

                int selectedIndex = pane.getSelectedIndex();
                DefaultMultipleDocumentModel.this.setCurrentDocument(
                        selectedIndex == -1 ?
                                null :
                                getDocument(selectedIndex));

                System.out.println("state changed");
//                DefaultMultipleDocumentModel.this.notifyCurrentDocumentChanged(currentDocument, currentDocument);
            }
        });
    }


    private void setCurrentDocument(SingleDocumentModel document) {
        if (document == getCurrentDocument())
            return;

        SingleDocumentModel previousDocument = getCurrentDocument();
        this.currentDocument = document;
        setSelectedIndex(documents.indexOf(document));
        notifyCurrentDocumentChanged(previousDocument, getCurrentDocument());
    }

    @Override
    public SingleDocumentModel getCurrentDocument() {
        return currentDocument;
    }


    /**
     * Implementation of SingleDocumentListener that is used in this MultipleDocumentModel.
     */
    class DefaultSingleDocumentListener implements SingleDocumentListener {

        @Override
        public void documentModifyStatusUpdated(SingleDocumentModel model) {
            setIconAt(documents.indexOf(model), model.isModified() ? Icons.MODIFIED : Icons.UNMODIFIED);
            DefaultMultipleDocumentModel.this.notifyCurrentDocumentChanged(currentDocument, currentDocument);
        }

        @Override
        public void documentFilePathUpdated(SingleDocumentModel model) {
            setTitleAt(documents.indexOf(model), model.getFilePath().getFileName().toString());
        }
    }

    @Override
    public SingleDocumentModel createNewDocument() {
        SingleDocumentModel newDocument = new DefaultSingleDocumentModel(null, "");
        newDocument.addSingleDocumentListener(new DefaultSingleDocumentListener());

        documents.add(newDocument);
        addTab("(unnamed)", Icons.UNMODIFIED, new JScrollPane(newDocument.getTextComponent()));
        setCurrentDocument(newDocument);
        notifyDocumentAdded(newDocument);
        return newDocument;
    }

    @Override
    public SingleDocumentModel loadDocument(Path path) {
        if (path == null)
            throw new RuntimeException("Path of loaded document can't be null!");

        /* Find if there is already document with given path */
        for (SingleDocumentModel document : documents) {
            if (document.getFilePath() != null && document.getFilePath().equals(path)) {
                document.addSingleDocumentListener(new DefaultSingleDocumentListener());
                setCurrentDocument(document);
                return document;
            }
        }

        byte[] octets;
        try {
            octets = Files.readAllBytes(path);
        } catch (IOException ioException) {
            throw new RuntimeException("Error while reading file!");
        }
        String fileText = new String(octets, StandardCharsets.UTF_8);
        SingleDocumentModel loadedDocument = new DefaultSingleDocumentModel(path, fileText);

        documents.add(loadedDocument);
        loadedDocument.addSingleDocumentListener(new DefaultSingleDocumentListener());
        addTab(loadedDocument.getFilePath().getFileName().toString(),
                loadedDocument.isModified() ? Icons.MODIFIED : Icons.UNMODIFIED,
                new JScrollPane(loadedDocument.getTextComponent()));
        setCurrentDocument(loadedDocument);
        notifyDocumentAdded(loadedDocument);

        return loadedDocument;
    }

    @Override
    public void saveDocument(SingleDocumentModel model, Path newPath) {
        /*
        saveDocument: newPath can be null; if null, document should be saved using path associated from
        document, otherwise, new path should be used and after saving is completed, document’s path should be
        updated to newPath
         */
        if (newPath == null)
            newPath = model.getFilePath();

        try {
            Files.writeString(newPath, model.getTextComponent().getText());
        } catch (IOException e) {
            throw new RuntimeException("Error while saving file!");
        }
        model.setFilePath(newPath);
        model.setModified(false);
        notifyCurrentDocumentChanged(model, model);
    }

    @Override
    public void closeDocument(SingleDocumentModel model) {
        int newSelectedIndex = Math.max(0, documents.indexOf(model) - 1);

        removeTabAt(documents.indexOf(model));
        documents.remove(model);
        notifyDocumentRemoved(model);

        if (getNumberOfDocuments() != 0)
            setCurrentDocument(documents.get(newSelectedIndex));
    }


    @Override
    public void addMultipleDocumentListener(MultipleDocumentListener l) {
        listeners.add(l);
    }

    @Override
    public void removeMultipleDocumentListener(MultipleDocumentListener l) {
        listeners.remove(l);
    }

    /**
     * Notifies all listeners that current document has changed.
     *
     * @param previousModel
     * @param currentModel
     */
    private void notifyCurrentDocumentChanged(SingleDocumentModel previousModel, SingleDocumentModel currentModel) {
        listeners.forEach(multipleDocumentListener -> multipleDocumentListener.currentDocumentChanged(previousModel, currentModel));
    }

    /**
     * Notifies all listeners that document was added.
     *
     * @param model
     */
    private void notifyDocumentAdded(SingleDocumentModel model) {
        listeners.forEach(multipleDocumentListener -> multipleDocumentListener.documentAdded(model));
    }

    /**
     * Notifies all listeners that document was removed.
     * @param model
     */
    private void notifyDocumentRemoved(SingleDocumentModel model) {
        listeners.forEach(multipleDocumentListener -> multipleDocumentListener.documentRemoved(model));
    }

    @Override
    public int getNumberOfDocuments() {
        return documents.size();
    }

    @Override
    public SingleDocumentModel getDocument(int index) {
        return documents.get(index);
    }

    @Override
    public Iterator<SingleDocumentModel> iterator() {
        return documents.iterator();
    }

}
