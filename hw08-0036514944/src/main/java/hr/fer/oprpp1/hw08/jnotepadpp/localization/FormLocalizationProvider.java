package hr.fer.oprpp1.hw08.jnotepadpp.localization;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * FormLocalizationProvider is a class derived from LocalizationProviderBridge; in its constructor it registeres itself
 * as a WindowListener to its JFrame; when frame is opened, it calls connect and when frame is closed, it calls
 * disconnect.
 */
public class FormLocalizationProvider extends LocalizationProviderBridge {

    public FormLocalizationProvider(ILocalizationProvider localizationProvider, JFrame frame) {
        super(localizationProvider);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                connect();
            }

            @Override
            public void windowClosed(WindowEvent e) {
                disconnect();
            }
        });
    }
}
