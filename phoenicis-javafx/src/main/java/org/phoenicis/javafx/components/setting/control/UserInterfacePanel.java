package org.phoenicis.javafx.components.setting.control;

import javafx.beans.property.*;
import javafx.collections.ObservableList;
import org.phoenicis.javafx.components.common.control.ControlBase;
import org.phoenicis.javafx.components.setting.skin.UserInterfacePanelSkin;
import org.phoenicis.javafx.views.common.themes.Theme;

/**
 * A panel containing the user interface settings
 */
public class UserInterfacePanel extends ControlBase<UserInterfacePanel, UserInterfacePanelSkin> {
    /**
     * A list containing all available themes
     */
    private final ObservableList<Theme> themes;

    /**
     * The currently selected theme. This value should never be null
     */
    private final ObjectProperty<Theme> selectedTheme;

    /**
     * True if the source repository of a script should be shown
     */
    private final BooleanProperty showScriptSource;

    /**
     * The user interface scaling
     */
    private final DoubleProperty scaling;

    /**
     * Callback for the restore settings button
     */
    private final ObjectProperty<Runnable> onRestoreSettings;

    /**
     * Constructor
     *
     * @param themes A list containing all available themes
     * @param selectedTheme The currently selected theme. This value should never be null
     * @param showScriptSource True if the source repository of a script should be shown
     * @param scaling The user interface scaling
     * @param onRestoreSettings Callback for the restore settings button
     */
    public UserInterfacePanel(ObservableList<Theme> themes, ObjectProperty<Theme> selectedTheme,
            BooleanProperty showScriptSource, DoubleProperty scaling, ObjectProperty<Runnable> onRestoreSettings) {
        super();

        this.themes = themes;
        this.selectedTheme = selectedTheme;
        this.showScriptSource = showScriptSource;
        this.scaling = scaling;
        this.onRestoreSettings = onRestoreSettings;
    }

    /**
     * Constructor
     *
     * @param themes A list containing all available themes
     */
    public UserInterfacePanel(ObservableList<Theme> themes) {
        this(themes, new SimpleObjectProperty<>(), new SimpleBooleanProperty(), new SimpleDoubleProperty(),
                new SimpleObjectProperty<>());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserInterfacePanelSkin createSkin() {
        return new UserInterfacePanelSkin(this);
    }

    public ObservableList<Theme> getThemes() {
        return this.themes;
    }

    public Theme getSelectedTheme() {
        return this.selectedTheme.get();
    }

    public ObjectProperty<Theme> selectedThemeProperty() {
        return this.selectedTheme;
    }

    public void setSelectedTheme(Theme selectedTheme) {
        this.selectedTheme.set(selectedTheme);
    }

    public boolean isShowScriptSource() {
        return this.showScriptSource.get();
    }

    public BooleanProperty showScriptSourceProperty() {
        return this.showScriptSource;
    }

    public void setShowScriptSource(boolean showScriptSource) {
        this.showScriptSource.set(showScriptSource);
    }

    public double getScaling() {
        return this.scaling.get();
    }

    public DoubleProperty scalingProperty() {
        return this.scaling;
    }

    public void setScaling(double scaling) {
        this.scaling.set(scaling);
    }

    public Runnable getOnRestoreSettings() {
        return this.onRestoreSettings.get();
    }

    public ObjectProperty<Runnable> onRestoreSettingsProperty() {
        return this.onRestoreSettings;
    }

    public void setOnRestoreSettings(Runnable onRestoreSettings) {
        this.onRestoreSettings.set(onRestoreSettings);
    }
}
