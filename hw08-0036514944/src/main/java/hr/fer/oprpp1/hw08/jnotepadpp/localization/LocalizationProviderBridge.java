package hr.fer.oprpp1.hw08.jnotepadpp.localization;


/**
 * LocalizationProviderBridge which is a decorator for some other IlocalizationProvider. This class offers two
 * additional methods: connect() and disconnect(), and it manages a connection status (so that you can not connect if
 * you are already connected). Here is the idea: this class is ILocalizationProvider which, when asked to resolve a key
 * delegates this request to wrapped (decorated) ILocalizationProvider object. When user calls connect() on it, the
 * method will register an instance of anonimous ILocalizationListener on the decorated object. When user calls
 * disconnect(), this object will be deregistered from decorated object. The LocalizationProviderBridge must listen for
 * localization changes so that, when it receives the notification, it will notify all listeners that are registered as
 * its listeners.
 */
public class LocalizationProviderBridge extends AbstractLocalizationProvider {

    /* LocalizationProvider that will be asked for translation (this class is Decorator over this object). */
    private final ILocalizationProvider localizationProvider;
    private final ILocalizationListener listener;
    private boolean connected;

    /**
     * Constructor which accepts ILocalizationProvider which it will Decorate over.
     *
     * @param localizationProvider
     */
    public LocalizationProviderBridge(ILocalizationProvider localizationProvider) {
        this.localizationProvider = localizationProvider;
        this.listener = this::fire;
    }

    /**
     * Registers an instance of anonymous ILocalizationListener on the decorated object.
     */
    public void connect() {
        connected = true;
        LocalizationProvider.getInstance().addLocalizationListener(listener);
    }

    /**
     * This object will be unregistered from decorated object.
     */
    public void disconnect() {
        connected = false;
        LocalizationProvider.getInstance().addLocalizationListener(listener);
    }

    @Override
    public String getString(String key) {
        return localizationProvider.getString(key);
    }

    @Override
    public String getCurrentLanguage() {
        return localizationProvider.getCurrentLanguage();
    }
}
