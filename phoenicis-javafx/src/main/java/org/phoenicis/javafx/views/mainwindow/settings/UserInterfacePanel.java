package org.phoenicis.javafx.views.mainwindow.settings;

import javafx.animation.PauseTransition;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Duration;
import org.phoenicis.javafx.views.common.TextWithStyle;
import org.phoenicis.javafx.views.common.Theme;
import org.phoenicis.javafx.views.common.ThemeManager;
import org.phoenicis.settings.SettingsManager;

import java.net.URL;

import static org.phoenicis.configuration.localisation.Localisation.translate;

/**
 * This class represents the "User Interface" settings category
 *
 * @author marc
 * @since 23.04.17
 */
public class UserInterfacePanel extends VBox {
    private SettingsManager settingsManager;
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
     *
     * @param settingsManager The settings manager
     * @param themeManager The theme manager
     */
    public UserInterfacePanel(SettingsManager settingsManager, ThemeManager themeManager) {
        super();

        this.settingsManager = settingsManager;
        this.themeManager = themeManager;

        this.getStyleClass().add("containerConfigurationPane");

        this.populate();

        this.getChildren().setAll(title, themeGrid);
    }

    private void populate() {
        this.title = new TextWithStyle(translate("User Interface Settings"), "title");

        this.themeGrid = new GridPane();
        this.themeGrid.getStyleClass().add("grid");
        this.themeGrid.setHgap(20);
        this.themeGrid.setVgap(10);

        // Change Theme
        this.themeTitle = new TextWithStyle(translate("Theme:"), "captionTitle");

        this.themes = new ComboBox<>();
        this.themes.getItems().setAll(Theme.values());
        this.themes.setValue(Theme.fromShortName(settingsManager.getTheme()));
        this.themes.setOnAction(event -> {
            this.handleThemeChange();
            this.save();
        });

        // View Script Sources
        this.showScriptSource = new CheckBox();
        this.showScriptSource.setSelected(settingsManager.isViewScriptSource());
        this.showScriptSource.setOnAction(event -> this.save());

        this.showScriptDescription = new Label(
                translate("Select, if you want to view the source repository of the scripts"));

        // Scale UI
        this.scale = new Slider(8, 16, settingsManager.getScale());
        this.scaleDescription = new Label(translate("Scale the user interface."));
        this.scale.valueProperty().addListener((observableValue, oldValue, newValue) -> {
            this.pause.setOnFinished(event -> {
                getScene().getRoot().setStyle(String.format("-fx-font-size: %.2fpt;", newValue));
                this.save();
            });
            this.pause.playFromStart();
        });

        this.themeGrid.add(themeTitle, 0, 0);
        this.themeGrid.add(themes, 1, 0);
        this.themeGrid.add(showScriptSource, 0, 1);
        this.themeGrid.add(showScriptDescription, 1, 1);
        this.themeGrid.add(scale, 0, 2);
        this.themeGrid.add(scaleDescription, 1, 2);
    }

    private void handleThemeChange() {
        final Theme theme = themes.getSelectionModel().getSelectedItem();
        themeManager.setCurrentTheme(theme);
        final String shortName = theme.getShortName();
        final String url = String.format("/org/phoenicis/javafx/themes/%s/main.css", shortName);
        final URL style = this.getClass().getResource(url);

        getScene().getStylesheets().setAll(themeManager.getDefaultCategoryIconsCss(),
                themeManager.getDefaultEngineIconsCss(), style.toExternalForm());
    }

    private void save() {
        settingsManager.setTheme(themes.getSelectionModel().getSelectedItem().getShortName());
        settingsManager.setScale(scale.getValue());
        settingsManager.setViewScriptSource(showScriptSource.isSelected());

        settingsManager.save();
    }
}
