package org.phoenicis.scripts.wizard;

import org.phoenicis.scripts.ui.BrowserControl;

public interface BrowserWizard {
    BrowserControl createBrowser(String textToShow);
}
