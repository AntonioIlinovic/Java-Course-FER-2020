package hr.fer.oprpp1.hw08.jnotepadpp.localization;

import javax.swing.*;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * LocalizationProvider is a class that is singleton (so it has private constructor, private static instance reference
 * and public static getter method); it also extends AbstractLocalizationProvider. Constructor sets the language to “en”
 * by default. It also loads the resource bundle for this language and stores reference to it. Method getString uses
 * loaded resource bundle to translate the requested key.
 */
public class LocalizationProvider extends AbstractLocalizationProvider {

    /**
     * Creates singleton object of LocalizationProvider.
     */
    private static final LocalizationProvider instance = new LocalizationProvider();
    private String language;
    private ResourceBundle bundle;

    /**
     * Sets default language of localization to English.
     */
    private LocalizationProvider() {
        super();
        setLanguage("en");
    }

    @Override
    public String getString(String key) {
        return bundle.getString(key);
    }

    @Override
    public String getCurrentLanguage() {
        return language;
    }

    /**
     * Returns singleton instance of LocalizationProvider.
     *
     * @return
     */
    public static LocalizationProvider getInstance() {
        return instance;
    }

    /**
     * Sets language of localization and notifies that localization has changed.
     *
     * @param language
     */
    public void setLanguage(String language) {
        if (this.language != null && this.language.equals(language))
            return;

        this.language = language;
        setBundle();

        fire();
    }

    /**
     * Sets bundle of translations depending on current language.
     */
    public void setBundle() {
        Locale locale = Locale.forLanguageTag(language);
        bundle = ResourceBundle.getBundle("prijevodi", locale);
    }
}
