package hr.fer.oprpp1.hw08.jnotepadpp;

import hr.fer.oprpp1.hw08.jnotepadpp.localization.FormLocalizationProvider;
import hr.fer.oprpp1.hw08.jnotepadpp.localization.LJMenu;
import hr.fer.oprpp1.hw08.jnotepadpp.localization.LocalizableAction;
import hr.fer.oprpp1.hw08.jnotepadpp.localization.LocalizationProvider;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.Collator;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.lang.Character.toUpperCase;

/**
 * JNotepadPP as JFrame with menus and toolbars and an instance of DefaultMultipleDocumentModel; in initGUI, create this
 * instance and add it to appropriate container. After that, for all purposes, look at that object only through
 * MultipleDocumentModel interface; implement all communication from your program (from menu actions etc.) with this
 * object only through the methods declared in this interface.
 * <p>
 * In JNotepadPP, you will conceptually have menubar, toolbar, a single instance of DefaultMultipleDocumentModel and a
 * single instance of statusbar. Let us repeat this: you are not allowed to create multiple statusbars (for example, for
 * each document separate statusbar). For gui elements which must track and dynamically show information on current
 * document, implement them to observe changes of current document, and on each change unregister appropriate listeners
 * from “old” current document, register them on “new” current document and update presented information.
 */
public class JNotepadPP extends JFrame {

    private final FormLocalizationProvider flp = new FormLocalizationProvider(LocalizationProvider.getInstance(), this);
    private final DefaultMultipleDocumentModel documentsTabs;

    public JNotepadPP() {
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                exitAction.actionPerformed(null);
            }
        });
        // TODO Change this line
        setLocation(-900, 100);
        setSize(800, 600);

        documentsTabs = new DefaultMultipleDocumentModel();
        lengthLabel = new JLabel();
        lnColSelLabel = new JLabel("", SwingConstants.LEFT);
        timeLabel = new JLabel("", SwingConstants.RIGHT);

        initGUI();
    }

    private JPanel statusBar;

    private void initGUI() {

        getContentPane().setLayout(new BorderLayout());
        JPanel documentsTabsAndStatusbar = new JPanel(new BorderLayout());
        documentsTabsAndStatusbar.add(documentsTabs, BorderLayout.CENTER);
        /* Create and place statusBar */
        documentsTabsAndStatusbar.add(createStatusBar(), BorderLayout.PAGE_END);
        getContentPane().add(documentsTabsAndStatusbar, BorderLayout.CENTER);

        createActions();
        createMenus();
        createToolbars();

        documentsTabs.addMultipleDocumentListener(new MultipleDocumentListener() {
            @Override
            public void currentDocumentChanged(SingleDocumentModel previousModel, SingleDocumentModel currentModel) {

                if (previousModel == null && currentModel == null)
                    System.out.println("Previous and current models are null");

                /* Register statusBar on caret */
                if (currentModel != null) {
                    JTextComponent textComponent = currentModel.getTextComponent();
                    textComponent.addCaretListener(new CaretListener() {
                        @Override
                        public void caretUpdate(CaretEvent e) {
                            statusBarChanged(textComponent, e);
                        }
                    });
                }

                String pathName = "(unnamed)";
                if (currentModel != null && currentModel.getFilePath() != null)
                    pathName = currentModel.getFilePath().toString();
                setTitle(pathName + " - JNotepad++");

                enableDisableActions();
            }

            @Override
            public void documentAdded(SingleDocumentModel model) {

            }

            @Override
            public void documentRemoved(SingleDocumentModel model) {
                if (documentsTabs.getNumberOfDocuments() == 0)
                    setTitle("JNotepad++");
            }
        });
        documentsTabs.createNewDocument();
    }


    /* StatusBar variables, statusBar labels must be final, or else there will be NullPointerException */
    private final JLabel lengthLabel;
    private final JLabel lnColSelLabel;
    private final JLabel timeLabel;
    private final DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private int length = 0;
    private int ln = 1;
    private int col = 1;
    private int sel = 0;

    /**
     * Helper method that creates and returns statusBar.
     *
     * @return created statusBar
     */
    private JPanel createStatusBar() {
        JPanel statusBar = new JPanel(new GridLayout(0, 3));
        statusBar.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));

        lengthLabel.setText(flp.getString("length") + ": " + length);
        lnColSelLabel.setText("Ln:" + ln + "  Col:" + col + "  Sel:" + sel);
//        lnColSelLabel.setHorizontalAlignment(SwingConstants.LEADING);
        timeLabel.setText(LocalDateTime.now().format(dateTimeFormat));

        statusBar.add(lengthLabel);
        statusBar.add(lnColSelLabel);
        statusBar.add(timeLabel);
        secondTimer.start();

        return statusBar;
    }

    private void statusBarChanged(JTextComponent textComponent, CaretEvent e) {

        int caretPosition = textComponent.getCaretPosition();
        Caret caret_obj = textComponent.getCaret();
        Document doc = textComponent.getDocument();
        Element root = doc.getDefaultRootElement();

        int document_length = doc.getLength();
        int row_index = root.getElementIndex(caretPosition);
        int column_index = caretPosition - root.getElement(row_index).getStartOffset();
        int selected_no = Math.abs(caret_obj.getDot() - caret_obj.getMark());

        length = document_length;
        ln = row_index + 1;
        col = column_index + 1;
        sel = selected_no;

        lengthLabel.setText(flp.getString("length") + ": " + length);
        lnColSelLabel.setText(String.format("%s:%d %s:%d %s:%d",
                "ln", ln,
                "col", col,
                "sel", sel));

        enableDisableActions();
    }

    private final Timer secondTimer = new Timer(1000, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            timeLabel.setText(LocalDateTime.now().format(dateTimeFormat));
        }
    });


    /* From here onwards we define Actions for buttons in menus and toolbar. */
    private final Action newAction = new LocalizableAction("new", flp) {
        @Override
        public void actionPerformed(ActionEvent e) {
            documentsTabs.createNewDocument();
            enableDisableActions();
        }
    };
    private final Action openAction = new LocalizableAction("open", flp) {
        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser fc = new JFileChooser();
            fc.setDialogTitle(flp.getString("open_file"));
            if (fc.showDialog(JNotepadPP.this, flp.getString("open")) != JFileChooser.APPROVE_OPTION)
                return;

            File fileName = fc.getSelectedFile();
            Path filePath = fileName.toPath();
            if (!Files.isReadable(filePath)) {
                JOptionPane.showMessageDialog(
                        JNotepadPP.this,
                        flp.getString("file") + " " + fileName.getAbsolutePath() + " " + flp.getString("does_not_exist") + "!",
                        flp.getString("error"),
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            documentsTabs.loadDocument(filePath);
            enableDisableActions();
        }
    };
    private final Action saveAction = new LocalizableAction("save", flp) {
        @Override
        public void actionPerformed(ActionEvent e) {
            SingleDocumentModel currentDocument = documentsTabs.getCurrentDocument();
            if (currentDocument.getFilePath() == null) {
                saveAsAction.actionPerformed(null);
                return;
            }

            documentsTabs.saveDocument(currentDocument, currentDocument.getFilePath());
            enableDisableActions();
        }
    };
    private final Action saveAsAction = new LocalizableAction("save_as", flp) {
        @Override
        public void actionPerformed(ActionEvent e) {
            SingleDocumentModel currentDocument = documentsTabs.getCurrentDocument();

            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle(flp.getString("save"));
            if (fileChooser.showSaveDialog(JNotepadPP.this) != JFileChooser.APPROVE_OPTION)
                return;
            Path savePath = fileChooser.getSelectedFile().toPath();

            if (Files.exists(savePath)) {
                String[] options = new String[]{
                        flp.getString("yes"),
                        flp.getString("no"),
                        flp.getString("cancel")
                };
                int userChoice = JOptionPane.showOptionDialog(
                        JNotepadPP.this,
                        flp.getString("do_you_want_to_overwrite_file") + " " + savePath,
                        flp.getString("overwrite_file"),
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.ERROR_MESSAGE,
                        null,
                        options,
                        null);
                if (userChoice == JOptionPane.NO_OPTION)
                    return;
            }

            documentsTabs.saveDocument(currentDocument, savePath);
            enableDisableActions();
        }
    };
    private final Action closeAction = new LocalizableAction("close", flp) {
        @Override
        public void actionPerformed(ActionEvent e) {
            SingleDocumentModel currentDocument = documentsTabs.getCurrentDocument();
            closeDocument(currentDocument);
            enableDisableActions();
        }
    };
    private final Action exitAction = new LocalizableAction("exit", flp) {
        @Override
        public void actionPerformed(ActionEvent e) {
            int numOfDocuments = documentsTabs.getNumberOfDocuments();
            for (int i = 0; i < numOfDocuments; i++) {
                /* We will try to close each document. Each time we try to close unsaved document, user will choose what
                to do with that document. He can choose to save those changes, discard them, or stop closing documents
                completely (by clicking CANCEL_OPTION).
                 */
                SingleDocumentModel documentToRemove = documentsTabs.getCurrentDocument();
                boolean shouldContinueClosingDocuments = closeDocument(documentToRemove);
                if (!shouldContinueClosingDocuments) {
                    enableDisableActions();
                    return;
                }
            }

            dispose();
        }
    };

    /**
     * Method that closes given document. If document is modified it asks user to save unsaved changes. If user chooses
     * YES_OPTION it will save changes and close it. If user chooses NO_OPTION it will just close document. If either
     * YES_OPTION or NO_OPTION is chosen the method will return true, because closing was successful. Otherwise it will
     * return false, because closing was unsuccessful.
     *
     * @param document to be closed
     * @return true if closing was successful, false if closing was unsuccessful
     */
    private boolean closeDocument(SingleDocumentModel document) {
        if (!document.isModified()) {
            documentsTabs.closeDocument(document);
            return true;
        }

        String[] options = new String[]{
                flp.getString("yes"),
                flp.getString("no"),
                flp.getString("cancel")
        };
        int userChoice = JOptionPane.showOptionDialog(
                JNotepadPP.this,
                flp.getString("file_is_unsaved_do_you_want_to_save_before_closing") + " " + (document.getFilePath() == null ? "(unnamed)" : document.getFilePath()),
                flp.getString("save_before_closing"),
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.ERROR_MESSAGE,
                null,
                options,
                null);
        boolean closingSuccessful = false;
        if (userChoice == JOptionPane.YES_OPTION)
            saveAction.actionPerformed(null);
        if (userChoice == JOptionPane.YES_OPTION || userChoice == JOptionPane.NO_OPTION) {
            documentsTabs.closeDocument(document);
            closingSuccessful = true;
        }

        enableDisableActions();
        return closingSuccessful;
    }

    private final Action cutAction = new LocalizableAction("cut", flp) {
        @Override
        public void actionPerformed(ActionEvent e) {
            JTextComponent textComponent = documentsTabs.getCurrentDocument().getTextComponent();
            textComponent.cut();
        }
    };
    private final Action copyAction = new LocalizableAction("copy", flp) {
        @Override
        public void actionPerformed(ActionEvent e) {
            JTextComponent textComponent = documentsTabs.getCurrentDocument().getTextComponent();
            textComponent.copy();
        }
    };
    private final Action pasteAction = new LocalizableAction("paste", flp) {
        @Override
        public void actionPerformed(ActionEvent e) {
            JTextComponent textComponent = documentsTabs.getCurrentDocument().getTextComponent();
            textComponent.paste();
        }
    };
    private final Action summaryAction = new LocalizableAction("summary", flp) {
        @Override
        public void actionPerformed(ActionEvent e) {
            JTextComponent textComponent = documentsTabs.getCurrentDocument().getTextComponent();
            Document doc = textComponent.getDocument();
            String text = "";
            try {
                text = doc.getText(0, doc.getLength());
            } catch (BadLocationException exception) {
                exception.printStackTrace();
            }

            int numOfChars = doc.getLength();
            int numOfNonBlankChars = text.replaceAll("\\s+", "").length();
            int numOfLines = (int) text.lines().count();

            JOptionPane.showMessageDialog(
                    JNotepadPP.this,
                    flp.getString("num_of_characters") + ": " + numOfChars + "\n" +
                            flp.getString("num_of_non_blank_characters") + ": " + numOfNonBlankChars + "\n" +
                            flp.getString("num_of_lines") + ": " + numOfLines + "\n",
                    flp.getString("document_summary"),
                    JOptionPane.INFORMATION_MESSAGE);
        }
    };
    private final Action uppercaseAction = new LocalizableAction("uppercase", flp) {
        @Override
        public void actionPerformed(ActionEvent e) {
            characterFunction.runMethod();
        }

        private final CaseTemplateMethod characterFunction = new CaseTemplateMethod() {

            @Override
            char templateMethod(char c) {
                return Character.toUpperCase(c);
            }
        };
    };
    private final Action lowercaseAction = new LocalizableAction("lowercase", flp) {
        @Override
        public void actionPerformed(ActionEvent e) {
            characterFunction.runMethod();
        }

        private final CaseTemplateMethod characterFunction = new CaseTemplateMethod() {

            @Override
            char templateMethod(char c) {
                return Character.toLowerCase(c);
            }
        };
    };
    private final Action invertcaseAction = new LocalizableAction("invertcase", flp) {
        @Override
        public void actionPerformed(ActionEvent e) {
            characterFunction.runMethod();
        }

        private final CaseTemplateMethod characterFunction = new CaseTemplateMethod() {

            @Override
            char templateMethod(char c) {
                if (Character.isLowerCase(c)) {
                    return Character.toUpperCase(c);
                } else if (Character.isUpperCase(c)) {
                    return Character.toLowerCase(c);
                }
                return c;
            }
        };
    };

    /**
     * Class with template method. It operates on selected text. It will operate on text for example: turn all selected
     * text to UPPERCASE, lowercase, iNVERTcASE etc...
     */
    private abstract class CaseTemplateMethod {
        public void runMethod() {
            JTextArea textComponent = documentsTabs.getCurrentDocument().getTextComponent();
            Document doc = textComponent.getDocument();
            int docLen = Math.abs(textComponent.getCaret().getDot() - textComponent.getCaret().getMark());
            int offset = 0;
            if (docLen != 0) {
                offset = Math.min(textComponent.getCaret().getDot(), textComponent.getCaret().getMark());
            } else {
                docLen = doc.getLength();
            }
            try {
                String text = doc.getText(offset, docLen);
                text = operateOnTextMethod(text);
                doc.remove(offset, docLen);
                doc.insertString(offset, text, null);
            } catch (BadLocationException ex) {
                ex.printStackTrace();
            }
        }

        private String operateOnTextMethod(String text) {
            char[] chars = text.toCharArray();
            for (int i = 0; i < chars.length; i++) {
                char c = chars[i];
                chars[i] = templateMethod(c);
            }
            return new String(chars);
        }

        abstract char templateMethod(char c);
    }

    private final Action ascendingAction = new LocalizableAction("ascending", flp) {
        @Override
        public void actionPerformed(ActionEvent e) {
            lineMethod.runMethod();
        }

        private final SortUniqueTemplateMethod lineMethod = new SortUniqueTemplateMethod() {
            @Override
            java.util.List<String> runTemplateMethod(java.util.List<String> linesToProcess) {
                Locale currentLocale = new Locale(LocalizationProvider.getInstance().getCurrentLanguage());
                Collator currentCollator = Collator.getInstance(currentLocale);
                linesToProcess.sort(currentCollator);
                return linesToProcess;
            }
        };

    };
    private final Action descendingAction = new LocalizableAction("descending", flp) {
        @Override
        public void actionPerformed(ActionEvent e) {
            lineMethod.runMethod();
        }

        private final SortUniqueTemplateMethod lineMethod = new SortUniqueTemplateMethod() {
            @Override
            java.util.List<String> runTemplateMethod(java.util.List<String> linesToProcess) {
                Locale currentLocale = new Locale(LocalizationProvider.getInstance().getCurrentLanguage());
                Collator currentCollator = Collator.getInstance(currentLocale);
                linesToProcess.sort(currentCollator.reversed());
                return linesToProcess;
            }
        };

    };
    private final Action uniqueAction = new LocalizableAction("unique", flp) {
        @Override
        public void actionPerformed(ActionEvent e) {
            lineMethod.runMethod();
        }

        private final SortUniqueTemplateMethod lineMethod = new SortUniqueTemplateMethod() {
            @Override
            java.util.List<String> runTemplateMethod(java.util.List<String> linesToProcess) {
                return linesToProcess.stream().distinct().collect(Collectors.toList());
            }
        };

    };

    private abstract class SortUniqueTemplateMethod {
        public void runMethod() {
            JTextComponent textComponent = documentsTabs.getCurrentDocument().getTextComponent();
            Document doc = textComponent.getDocument();
            Element root = doc.getDefaultRootElement();
            Caret caret = textComponent.getCaret();

            int dotLine = root.getElementIndex(caret.getDot());
            int markLine = root.getElementIndex(caret.getMark());
            int startLine = Math.min(dotLine, markLine);
            int endLine = Math.max(dotLine, markLine);

            java.util.List<String> selectedLines = getLinesFromText(textComponent, startLine, endLine);
            selectedLines = runTemplateMethod(selectedLines);
            changeLinesWithGiven(textComponent, selectedLines, startLine, endLine);
        }

        abstract java.util.List<String> runTemplateMethod(java.util.List<String> linesToProcess);

        /**
         * Helper method that replaces lines from startLine to endLine with given selectedLines in textComponent.
         *
         * @param textComponent in which lines are changed
         * @param selectedLines lines to change with
         * @param startLine     from which to start changing
         * @param endLine       to which to end changing
         */
        private void changeLinesWithGiven(JTextComponent textComponent, java.util.List<String> selectedLines, int startLine, int endLine) {
             /*
                System.out.println("Number of rows: " +root.getElementCount());
                for (int line_no = 0; line_no < root.getElementCount(); line_no++) {
                    Element line_i = root.getElement(line_no);
                    int line_i_start_offset = line_i.getStartOffset();
                    int line_i_end_offset = line_i.getEndOffset();
                    int line_i_length = line_i_end_offset - line_i_start_offset;
                    try {
                        System.out.println("line " + line_no + " : " + doc.getText(line_i_start_offset, line_i_length));
                    } catch (BadLocationException exception) {
                        exception.printStackTrace();
                    }
                }
            */

            Document doc = textComponent.getDocument();
            Element root = doc.getDefaultRootElement();

            Element start_line = root.getElement(startLine);
            Element end_line = root.getElement(endLine);
            int start_replace_offset = start_line.getStartOffset();
            /* Math.min(...) is used because .getEndOffset() can return value greater than document length */
            int end_replace_offset = Math.min(end_line.getEndOffset(), doc.getLength());
            int remove_length = end_replace_offset - start_replace_offset;

            Optional<String> replaceWith = selectedLines.stream().reduce(String::concat);
            try {
                doc.remove(start_replace_offset, remove_length);
                doc.insertString(start_replace_offset, replaceWith.get(), null);
            } catch (BadLocationException exception) {
                exception.printStackTrace();
            }
        }

        /**
         * Helper method returns lines from textComponent from startLine including, to endLine including.
         *
         * @param textComponent from which to return lines
         * @param startLine     including
         * @param endLine       including
         * @return list of strings as lines
         */
        private java.util.List<String> getLinesFromText(JTextComponent textComponent, int startLine, int endLine) {
            java.util.List<String> selectedLines = new ArrayList<>();
            for (int line_index = startLine; line_index <= endLine; line_index++) {

                Document doc = textComponent.getDocument();
                Element root = doc.getDefaultRootElement();

                Element line_i = root.getElement(line_index);
                int line_i_start_offset = line_i.getStartOffset();
                /* Math.min(...) is used because .getEndOffset() can return value greater than document length */
                int line_i_end_offset = Math.min(line_i.getEndOffset(), doc.getLength());
                int line_i_length = line_i_end_offset - line_i_start_offset;
                try {
                    String line_i_text = doc.getText(line_i_start_offset, line_i_length);
                    selectedLines.add(line_i_text);
                } catch (BadLocationException exception) {
                    exception.printStackTrace();
                }
            }

            return selectedLines;
        }
    }


    /**
     * This method will enable/disable actions depending on current state of application.
     */
    public void enableDisableActions() {
//        boolean currentDocumentIsNull = documentsTabs.getCurrentDocument() == null;
        SingleDocumentModel currentDocument = documentsTabs.getCurrentDocument();
        if (currentDocument == null) {
            saveAction.setEnabled(false);
            saveAsAction.setEnabled(false);
            closeAction.setEnabled(false);
            cutAction.setEnabled(false);
            copyAction.setEnabled(false);
            pasteAction.setEnabled(false);
            summaryAction.setEnabled(false);
            uppercaseAction.setEnabled(false);
            lowercaseAction.setEnabled(false);
            invertcaseAction.setEnabled(false);
            ascendingAction.setEnabled(false);
            descendingAction.setEnabled(false);
            uniqueAction.setEnabled(false);
        } else {
            saveAsAction.setEnabled(true);
            closeAction.setEnabled(true);
            cutAction.setEnabled(true);
            copyAction.setEnabled(true);
            pasteAction.setEnabled(true);
            pasteAction.setEnabled(true);
            summaryAction.setEnabled(true);

            saveAction.setEnabled(currentDocument.isModified());

            /* Calculate number of selected characters */
            boolean someAreSelected = this.sel != 0;

            uppercaseAction.setEnabled(someAreSelected);
            lowercaseAction.setEnabled(someAreSelected);
            invertcaseAction.setEnabled(someAreSelected);
            ascendingAction.setEnabled(someAreSelected);
            descendingAction.setEnabled(someAreSelected);
            uniqueAction.setEnabled(someAreSelected);
        }


    }


    /**
     * Action that is used for localization. Each supported language will have one button that changes language of the
     * application to that one.
     */
    private static class ChangeLangAction extends AbstractAction {

        private final String language;

        public ChangeLangAction(String language) {
            this.language = language;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            LocalizationProvider instance = LocalizationProvider.getInstance();
            if (!instance.getCurrentLanguage().equals(language))
                instance.setLanguage(language);
        }
    }

    private final Action changeLangHrAction = new ChangeLangAction("hr");
    private final Action changeLangEnAction = new ChangeLangAction("en");
    private final Action changeLangDeAction = new ChangeLangAction("de");


    /**
     * Helper method that inserts given values into Action keys.
     *
     * @param action    that is enriched with values
     * @param keyStroke ACCELERATOR_KEY value
     * @param keyEvent  MNEMONIC_KEY value
     * @param imageIcon LARGE_ICON_KEY value
     */
    private void putActionValues(Action action, KeyStroke keyStroke, int keyEvent, ImageIcon imageIcon) {
        action.putValue(Action.ACCELERATOR_KEY, keyStroke);
        action.putValue(Action.MNEMONIC_KEY, keyEvent);

        if (imageIcon == null)
            return;
        action.putValue(Action.LARGE_ICON_KEY, imageIcon);
    }

    /**
     * Helper method that fills already created Actions with parameters (accelerator, mnemonic, icon etc.).
     */
    private void createActions() {
        /*
        newAction.putValue(
                Action.ACCELERATOR_KEY,
                KeyStroke.getKeyStroke("control N"));
        newAction.putValue(
                Action.MNEMONIC_KEY,
                KeyEvent.VK_N);
        ImageIcon newIcon = new ImageIcon(JNotepadPP.class.getResource("/icons/new.png"));
        newAction.putValue(
                Action.LARGE_ICON_KEY,
                newIcon);
         */
        putActionValues(newAction, KeyStroke.getKeyStroke("control N"), KeyEvent.VK_N, Icons.NEW);
        putActionValues(openAction, KeyStroke.getKeyStroke("control O"), KeyEvent.VK_O, Icons.OPEN);
        putActionValues(saveAction, KeyStroke.getKeyStroke("control S"), KeyEvent.VK_S, Icons.SAVE);
        putActionValues(saveAsAction, KeyStroke.getKeyStroke("control A"), KeyEvent.VK_A, Icons.SAVE_AS);
        putActionValues(closeAction, KeyStroke.getKeyStroke("control W"), KeyEvent.VK_E, Icons.CLOSE);
        putActionValues(exitAction, KeyStroke.getKeyStroke("alt f4"), KeyEvent.VK_X, Icons.EXIT);

        putActionValues(cutAction, KeyStroke.getKeyStroke("control X"), KeyEvent.VK_X, Icons.CUT);
        putActionValues(copyAction, KeyStroke.getKeyStroke("control C"), KeyEvent.VK_C, Icons.COPY);
        putActionValues(pasteAction, KeyStroke.getKeyStroke("control V"), KeyEvent.VK_P, Icons.PASTE);

        putActionValues(summaryAction, KeyStroke.getKeyStroke("control I"), KeyEvent.VK_I, Icons.SUMMARY);

        putActionValues(uppercaseAction, KeyStroke.getKeyStroke("control U"), KeyEvent.VK_U, Icons.UPPERCASE);
        putActionValues(lowercaseAction, KeyStroke.getKeyStroke("control L"), KeyEvent.VK_L, Icons.LOWERCASE);
        putActionValues(invertcaseAction, KeyStroke.getKeyStroke("control E"), KeyEvent.VK_E, Icons.INVERT_CASE);
        putActionValues(ascendingAction, KeyStroke.getKeyStroke("control M"), KeyEvent.VK_M, Icons.ASCENDING);
        putActionValues(descendingAction, KeyStroke.getKeyStroke("control B"), KeyEvent.VK_B, Icons.DESCENDING);
        putActionValues(uniqueAction, KeyStroke.getKeyStroke("control Q"), KeyEvent.VK_Q, Icons.UNIQUE);

        changeLangHrAction.putValue(Action.NAME, "hr");
        changeLangEnAction.putValue(Action.NAME, "en");
        changeLangDeAction.putValue(Action.NAME, "de");
    }

    /**
     * Helper method that creates menu bar, menus, modifies menus with actions and properties.
     * <ul>
     *     <li>
     *     <ul>
     *     File
     *             <li>New</li>
     *             <li>Open</li>
     *             <li>Save</li>
     *             <li>Save As...</li>
     *             <li>Close</li>
     *             <li>Exit</li>
     *     </ul>
     *     </li>
     *     <li>
     *     <ul>
     *     Edit
     *             <li>Cut</li>
     *             <li>Copy</li>
     *             <li>Paste</li>
     *     </ul>
     *     </li>
     *     <li>
     *     <ul>
     *     View
     *             <li>Summary</li>
     *     </ul>
     *     </li>
     * </ul>
     */
    private void createMenus() {
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new LJMenu("file", flp);
        fileMenu.setMnemonic(KeyEvent.VK_F);
        fileMenu.add(new JMenuItem(newAction));
        fileMenu.add(new JMenuItem(openAction));
        fileMenu.add(new JMenuItem(saveAction));
        fileMenu.add(new JMenuItem(saveAsAction));
        fileMenu.add(new JMenuItem(closeAction));
        fileMenu.addSeparator();
        fileMenu.add(new JMenuItem(exitAction));

        JMenu editMenu = new LJMenu("edit", flp);
        editMenu.setMnemonic(KeyEvent.VK_E);
        editMenu.add(new JMenuItem(cutAction));
        editMenu.add(new JMenuItem(copyAction));
        editMenu.add(new JMenuItem(pasteAction));

        JMenu viewMenu = new LJMenu("view", flp);
        viewMenu.setMnemonic(KeyEvent.VK_V);
        viewMenu.add(new JMenuItem(summaryAction));
        JMenu langMenu = new LJMenu("language", flp);
        langMenu.setMnemonic(KeyEvent.VK_L);
        langMenu.add(new JMenuItem(changeLangHrAction));
        langMenu.add(new JMenuItem(changeLangEnAction));
        langMenu.add(new JMenuItem(changeLangDeAction));
        viewMenu.add(langMenu);

        JMenu toolsMenu = new LJMenu("tools", flp);
        toolsMenu.setMnemonic(KeyEvent.VK_H);
        JMenu changeCaseMenu = new LJMenu("change_case", flp);
        changeCaseMenu.add(new JMenuItem(uppercaseAction));
        changeCaseMenu.add(new JMenuItem(lowercaseAction));
        changeCaseMenu.add(new JMenuItem(invertcaseAction));
        JMenu sortMenu = new LJMenu("sort", flp);
        sortMenu.add(new JMenuItem(ascendingAction));
        sortMenu.add(new JMenuItem(descendingAction));
        toolsMenu.add(changeCaseMenu);
        toolsMenu.add(sortMenu);
        toolsMenu.add(new JMenuItem(uniqueAction));

        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(viewMenu);
        menuBar.add(toolsMenu);

        setJMenuBar(menuBar);
    }

    /**
     * Helper method that creates tool bar, and adds buttons with actions to it.
     */
    private void createToolbars() {
        JToolBar toolBar = new JToolBar("Tools");
        toolBar.setFloatable(true);

        toolBar.add(new JButton(newAction));
        toolBar.add(new JButton(openAction));
        toolBar.add(new JButton(saveAction));
        toolBar.add(new JButton(saveAsAction));
        toolBar.add(new JButton(closeAction));
        toolBar.addSeparator();
        toolBar.add(new JButton(cutAction));
        toolBar.add(new JButton(copyAction));
        toolBar.add(new JButton(pasteAction));
        toolBar.addSeparator();
        toolBar.add(new JButton(uppercaseAction));
        toolBar.add(new JButton(lowercaseAction));
        toolBar.add(new JButton(invertcaseAction));
        toolBar.addSeparator();
        toolBar.add(new JButton(ascendingAction));
        toolBar.add(new JButton(descendingAction));
        toolBar.add(new JButton(uniqueAction));
        toolBar.addSeparator();
        toolBar.add(new JButton(summaryAction));
        toolBar.add(new JButton(exitAction));

        getContentPane().add(toolBar, BorderLayout.PAGE_START);
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() ->
                new JNotepadPP().setVisible(true));
    }
}
