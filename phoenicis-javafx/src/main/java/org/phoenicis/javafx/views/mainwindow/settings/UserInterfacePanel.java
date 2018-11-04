package org.phoenicis.javafx.views.mainwindow.settings;

import javafx.animation.PauseTransition;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Duration;
import org.phoenicis.javafx.settings.JavaFxSettingsManager;
import org.phoenicis.javafx.views.common.TextWithStyle;
import org.phoenicis.javafx.views.common.ThemeManager;
import org.phoenicis.javafx.views.common.themes.Theme;
import org.phoenicis.javafx.views.common.themes.Themes;

import static org.phoenicis.configuration.localisation.Localisation.tr;

/**
 * This class represents the "User Interface" settings category
 *
 * @author marc
 * @since 23.04.17
 */
public class UserInterfacePanel extends VBox {
    private JavaFxSettingsManager javaFxSettingsManager;
    private ThemeManager themeManager;

    private Text title;

    private GridPane themeGrid;

    private Text themeTitle;
    private ComboBox<Theme> themes;

    private Label showScriptDescription;
    private CheckBox showScriptSource;

    private Slider scale;
    private Label scaleDescription;
    private PauseTransition pause = new PauseTransition(Duration.seconds(0.5));

    /**
     * Constructor
     * @param javaFxSettingsManager The settings manager
     * @param themeManager The theme manager
     */
    public UserInterfacePanel(JavaFxSettingsManager javaFxSettingsManager, ThemeManager themeManager) {
        super();

        this.javaFxSettingsManager = javaFxSettingsManager;
        this.themeManager = themeManager;

        this.getStyleClass().add("containerConfigurationPane");

        this.populate();

        this.getChildren().setAll(title, themeGrid);
    }

    private void populate() {
        this.title = new TextWithStyle(tr("User Interface Settings"), "title");

        this.themeGrid = new GridPane();
        this.themeGrid.getStyleClass().add("grid");
        this.themeGrid.setHgap(20);
        this.themeGrid.setVgap(10);

        // Change Theme
        this.themeTitle = new TextWithStyle(tr("Theme:"), "captionTitle");

        this.themes = new ComboBox<>();
        this.themes.getItems().setAll(Themes.all());
        this.themes.setValue(Themes.fromShortName(javaFxSettingsManager.getTheme()).orElse(Themes.DEFAULT));
        this.themes.setOnAction(event -> {
            this.handleThemeChange();
            this.save();
        });

        // View Script Sources
        this.showScriptSource = new CheckBox();
        this.showScriptSource.setSelected(javaFxSettingsManager.isViewScriptSource());
        this.showScriptSource.setOnAction(event -> this.save());

        this.showScriptDescription = new Label(tr("View the scripts’ source repository"));

        // Scale UI
        this.scale = new Slider(8, 16, javaFxSettingsManager.getScale());
        this.scaleDescription = new Label(tr("Scale the user interface."));
        this.scale.valueProperty().addListener((observableValue, oldValue, newValue) -> {
            this.pause.setOnFinished(event -> {
                getScene().getRoot().setStyle(String.format("-fx-font-size: %.2fpt;", newValue));
                this.save();
            });
            this.pause.playFromStart();
        });

        // restore default
        ToggleButton restoreDefault = new ToggleButton(tr("Restore defaults (requires restart)"));
        restoreDefault.setOnAction(event -> {
            this.javaFxSettingsManager.restoreDefault();
        });

        this.themeGrid.add(themeTitle, 0, 0);
        this.themeGrid.add(themes, 1, 0);
        this.themeGrid.add(showScriptSource, 0, 1);
        this.themeGrid.add(showScriptDescription, 1, 1);
        this.themeGrid.add(scale, 0, 2);
        this.themeGrid.add(scaleDescription, 1, 2);
        this.themeGrid.add(restoreDefault, 0, 3);
    }

    private void handleThemeChange() {
        this.themeManager.setCurrentTheme(this.themes.getSelectionModel().getSelectedItem());
    }

    private void save() {
        this.javaFxSettingsManager.setTheme(themes.getSelectionModel().getSelectedItem().getShortName());
        this.javaFxSettingsManager.setScale(scale.getValue());
        this.javaFxSettingsManager.setViewScriptSource(showScriptSource.isSelected());

        this.javaFxSettingsManager.save();
    }
}
