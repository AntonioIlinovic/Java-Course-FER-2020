package hr.fer.oprpp1.hw08.jnotepadpp.localization;

import javax.swing.*;

/**
 * This class extends the AbstractAction and defines a single constructor.
 */
public abstract class LocalizableAction extends AbstractAction {

    /**
     * Please observe that the first argument is not a text for action (so called action-name); it is a key. The second
     * argument is a reference to localization provider. In all our examples, we create actions in frames, so the second
     * argument will be the flp reference.
     *
     * @param key
     * @param lp
     */
    public LocalizableAction(String key, ILocalizationProvider lp) {
        /*
        In LocalizableAction constructor you must ask lp for translation of the key and then call on Action
        object putValue(NAME, translation). You must also register an instance of anonymous class as a listener
        for localization changes on lp. When you receive a notification, you must again ask lp to give you a new
        translation of action's key and you must again call putValue(NAME, translation). Since this method
        changes action's properties, the action itself will notify all interested listeners about the change, and then all
        GUI components (buttons, menu items) will automatically refresh itself.
         */
        String translation = lp.getString(key);
        putValue(NAME, translation);
        putValue(SHORT_DESCRIPTION, translation);

        lp.addLocalizationListener(new ILocalizationListener() {
            @Override
            public void localizationChanged() {
                String translation = lp.getString(key);
                putValue(NAME, translation);
                putValue(SHORT_DESCRIPTION, translation);
            }
        });
    }

}
