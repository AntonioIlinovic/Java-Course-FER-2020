package hr.fer.oprpp1.hw08.jnotepadpp.localization;

import java.util.ArrayList;
import java.util.List;

/**
 * The AbstractLocalizationProvider class implements ILocalizationProvider interface and adds it the ability to
 * register, de-register and inform (fire() method) listeners. It is an abstract class – it does not implement
 * getString(...) method.
 */
public abstract class AbstractLocalizationProvider implements ILocalizationProvider {

    private final List<ILocalizationListener> listeners;

    public AbstractLocalizationProvider() {
        this.listeners = new ArrayList<>();
    }

    @Override
    public void addLocalizationListener(ILocalizationListener localizationListener) {
        listeners.add(localizationListener);
    }

    @Override
    public void removeLocalizationListener(ILocalizationListener localizationListener) {
        listeners.remove(localizationListener);
    }

    /**
     * Informs all listeners that localization has changed.
     */
    public void fire() {
        listeners.forEach(ILocalizationListener::localizationChanged);
    }

}
