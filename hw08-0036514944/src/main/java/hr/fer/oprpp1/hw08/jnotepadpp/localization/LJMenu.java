package hr.fer.oprpp1.hw08.jnotepadpp.localization;

import javax.swing.*;

/**
 * Subclass of JMenu. Only difference is that it has constructor which accepts a key for localization and localization
 * provider. It will register itself so that it changes JMenu name depending on current language.
 */
public class LJMenu extends JMenu {

    public LJMenu(String key, ILocalizationProvider lp) {
        String translation = lp.getString(key);
        setText(translation);

        lp.addLocalizationListener(() -> setText(lp.getString(key)));
    }

}
