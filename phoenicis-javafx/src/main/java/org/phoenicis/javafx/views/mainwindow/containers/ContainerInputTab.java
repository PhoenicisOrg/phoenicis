package org.phoenicis.javafx.views.mainwindow.containers;

import com.sun.javafx.collections.ObservableListWrapper;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Tab;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.phoenicis.containers.dto.WinePrefixContainerDTO;
import org.phoenicis.containers.wine.parameters.MouseWarpOverride;
import org.phoenicis.javafx.views.common.ColumnConstraintsWithPercentage;
import org.phoenicis.javafx.views.common.TextWithStyle;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import static org.phoenicis.configuration.localisation.Localisation.tr;

/**
 * Created by marc on 27.05.17.
 */
public class ContainerInputTab extends Tab {
    private static final String CAPTION_TITLE_CSS_CLASS = "captionTitle";
    private static final String CONFIGURATION_PANE_CSS_CLASS = "containerConfigurationPane";
    private static final String TITLE_CSS_CLASS = "title";

    private final WinePrefixContainerDTO container;

    private final List<Node> lockableElements = new ArrayList<>();

    public ContainerInputTab(WinePrefixContainerDTO container) {
        super(tr("Input"));

        this.container = container;

        this.setClosable(false);

        this.populate();
    }

    private void populate() {
        final VBox inputPane = new VBox();
        final Text title = new TextWithStyle(tr("Input settings"), TITLE_CSS_CLASS);

        inputPane.getStyleClass().add(CONFIGURATION_PANE_CSS_CLASS);
        inputPane.getChildren().add(title);

        final GridPane inputContentPane = new GridPane();
        inputContentPane.getStyleClass().add("grid");

        final ComboBox<MouseWarpOverride> mouseWarpOverrideComboBox = new ComboBox<>();
        mouseWarpOverrideComboBox.setValue(container.getMouseWarpOverride());
        addItems(mouseWarpOverrideComboBox, MouseWarpOverride.class);
        inputContentPane.add(new TextWithStyle(tr("Mouse Warp Override"), CAPTION_TITLE_CSS_CLASS), 0, 0);
        inputContentPane.add(mouseWarpOverrideComboBox, 1, 0);

        inputContentPane.getColumnConstraints().addAll(new ColumnConstraintsWithPercentage(30),
                new ColumnConstraintsWithPercentage(70));

        inputPane.getChildren().addAll(inputContentPane);

        this.setContent(inputPane);

        lockableElements.add(mouseWarpOverrideComboBox);
    }

    private <T extends Enum> void addItems(ComboBox<T> comboBox, Class<T> clazz) {
        final List<T> possibleValues = new ArrayList<>(EnumSet.allOf(clazz));

        final ObservableList<T> possibleValuesObservable = new ObservableListWrapper<>(possibleValues);
        comboBox.setItems(possibleValuesObservable);
    }
}
