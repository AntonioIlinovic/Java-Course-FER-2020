package hr.fer.oprpp1.hw08.jnotepadpp;

import javax.swing.*;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Class with all used icons.
 */
public class Icons {

    /* Icons are stored as static final ImageIcon */
    public static final ImageIcon NEW = initializeIcon("new.png");
    public static final ImageIcon OPEN = initializeIcon("open.png");
    public static final ImageIcon SAVE = initializeIcon("save.png");
    public static final ImageIcon SAVE_AS = initializeIcon("save_as.png");
    public static final ImageIcon CLOSE = initializeIcon("close.png");
    public static final ImageIcon EXIT = initializeIcon("exit.png");
    public static final ImageIcon CUT = initializeIcon("cut.png");
    public static final ImageIcon COPY = initializeIcon("copy.png");
    public static final ImageIcon PASTE = initializeIcon("paste.png");
    public static final ImageIcon SUMMARY = initializeIcon("summary.png");
    public static final ImageIcon ASCENDING = initializeIcon("ascending.png");
    public static final ImageIcon DESCENDING = initializeIcon("descending.png");
    public static final ImageIcon INVERT_CASE = initializeIcon("invert_case.png");
    public static final ImageIcon LOWERCASE = initializeIcon("lowercase.png");
    public static final ImageIcon UPPERCASE = initializeIcon("uppercase.png");
    public static final ImageIcon UNMODIFIED = initializeIcon("unmodified.png");
    public static final ImageIcon MODIFIED = initializeIcon("modified.png");
    public static final ImageIcon UNIQUE = initializeIcon("unique.png");

    /**
     * Method that returns icon with: hardcoded path + given filename.
     *
     * @param iconName
     * @return
     */
    private static ImageIcon initializeIcon(String iconName) {
        // TODO maybe change this according to homework documentation
        /*
        InputStream is = this.getClass().getResourceAsStream("icons/redDisk.png");
        if(is==null) error!!!
        byte[] bytes = readAllBytes(is);
        close input stream
        return new ImageIcon(bytes);
         */
        return new ImageIcon(Icons.class.getResource("/hr.fer.oprpp1.hw08.jnotepadpp.icons/" + iconName));
    }

}
