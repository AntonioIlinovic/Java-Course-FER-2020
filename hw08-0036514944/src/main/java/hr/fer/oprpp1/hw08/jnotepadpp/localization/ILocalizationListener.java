package hr.fer.oprpp1.hw08.jnotepadpp.localization;

/**
 * A listener for LocalizationProvider is modeled with ILocalizationListener and has a single method
 */
public interface ILocalizationListener {

    /**
     * Method that is called when localization has changed from one language to another.
     */
    void localizationChanged();

}
