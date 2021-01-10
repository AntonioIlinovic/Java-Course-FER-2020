package hr.fer.oprpp1.hw08.jnotepadpp.localization;

/**
 * Interface named ILocalizationProvider. Object which are instances of classes that implement this interface will be
 * able to give us the translations for given keys. For this reason there is a declared method String getString(String
 * key); It takes a key and gives back the localization. Since we would like to support the dynamical change of selected
 * language, here we also utilize the Observer pattern: each ILocalizationProvider will be automatically the Subject
 * that will notify all registered listeners when a selected language has changed. For that purpose, the
 * ILocalizationProvider interface also declares a method for registration and a method for de-registration of
 * listeners
 */
public interface ILocalizationProvider {

    /**
     * Adds listener who is interested in localization.
     *
     * @param localizationListener
     */
    void addLocalizationListener(ILocalizationListener localizationListener);

    /**
     * Removes listener who is no longer interested in localization.
     *
     * @param localizationListener
     */
    void removeLocalizationListener(ILocalizationListener localizationListener);

    /**
     * Method that takes a key and gives back the localization
     *
     * @param key
     * @return
     */
    String getString(String key);

    /**
     * Returns current language of localization. For example: "hr", "en", "de"...
     *
     * @return
     */
    String getCurrentLanguage();

}
