package org.phoenicis.javafx.components.setting.skin;

import javafx.beans.binding.Bindings;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.StringConverter;
import org.phoenicis.javafx.components.common.skin.SkinBase;
import org.phoenicis.javafx.components.setting.control.UserInterfacePanel;
import org.phoenicis.javafx.themes.Theme;

import java.util.Optional;

import static org.phoenicis.configuration.localisation.Localisation.tr;

/**
 * The skin for the {@link UserInterfacePanel} component
 */
public class UserInterfacePanelSkin extends SkinBase<UserInterfacePanel, UserInterfacePanelSkin> {
    /**
     * Constructor
     *
     * @param control The control belonging to the skin
     */
    public UserInterfacePanelSkin(UserInterfacePanel control) {
        super(control);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialise() {
        final Text title = new Text(tr("User Interface Settings"));
        title.getStyleClass().add("title");

        final GridPane themeGrid = new GridPane();
        themeGrid.getStyleClass().add("grid");
        themeGrid.setHgap(20);
        themeGrid.setVgap(10);

        // change theme
        final Text themeLabel = new Text(tr("Theme"));
        themeLabel.getStyleClass().add("captionTitle");

        final ComboBox<Theme> themeSelection = createThemeSelection();

        // view script sources
        final Label showScriptSourceLabel = new Label(tr("View the scripts’ source repository"));
        showScriptSourceLabel.getStyleClass().add("captionTitle");

        final CheckBox showScriptSourceSelection = new CheckBox();
        showScriptSourceSelection.selectedProperty().bindBidirectional(getControl().showScriptSourceProperty());

        // scale UI
        final Label scalingLabel = new Label(tr("Scale the user interface"));
        scalingLabel.getStyleClass().add("captionTitle");

        final Slider scalingSelection = new Slider();
        scalingSelection.setMin(8);
        scalingSelection.setMax(16);
        scalingSelection.valueProperty().bindBidirectional(getControl().scalingProperty());

        // restore default
        final Button restoreDefaultButton = new Button(tr("Restore defaults (requires restart)"));
        restoreDefaultButton.setOnAction(
                event -> Optional.ofNullable(getControl().getOnRestoreSettings()).ifPresent(Runnable::run));

        themeGrid.addRow(0, themeLabel, themeSelection);
        themeGrid.addRow(1, showScriptSourceLabel, showScriptSourceSelection);
        themeGrid.addRow(2, scalingLabel, scalingSelection);
        themeGrid.add(restoreDefaultButton, 1, 3);

        final VBox container = new VBox(title, themeGrid);

        container.getStyleClass().add("containerConfigurationPane");

        getChildren().setAll(container);
    }

    /**
     * Create the {@link ComboBox} containing the known themes
     *
     * @return A {@link ComboBox} containing the known themes
     */
    private ComboBox<Theme> createThemeSelection() {
        final ComboBox<Theme> themeSelection = new ComboBox<>();

        Bindings.bindContent(themeSelection.getItems(), getControl().getThemes());

        themeSelection.valueProperty().bindBidirectional(getControl().selectedThemeProperty());
        themeSelection.setConverter(new StringConverter<>() {
            @Override
            public String toString(Theme theme) {
                return theme.getName();
            }

            @Override
            public Theme fromString(String themeName) {
                final Optional<Theme> foundTheme = getControl().getThemes().stream()
                        .filter(theme -> themeName.equals(theme.getName()))
                        .findFirst();

                return foundTheme.orElseThrow(
                        () -> new IllegalArgumentException("Couldn't find theme with name \"" + themeName + "\""));
            }
        });

        return themeSelection;
    }
}
