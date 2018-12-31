package org.phoenicis.scripts.wizard;

import org.phoenicis.scripts.ui.BrowserControl;

/**
 * SetupWizard that supports browser creation
 */
public interface BrowserWizard {
    BrowserControl createBrowser(String textToShow);
}
