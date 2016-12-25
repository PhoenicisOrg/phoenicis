package com.playonlinux.javafx.views.mainwindow.containers;

import com.playonlinux.containers.dto.WinePrefixDTO;
import com.playonlinux.javafx.views.common.ColumnConstraintsWithPercentage;
import com.playonlinux.javafx.views.common.TextWithStyle;
import javafx.scene.control.Tab;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import static com.playonlinux.configuration.localisation.Localisation.translate;

public class WinePrefixContainerPanel extends AbstractContainerPanel<WinePrefixDTO> {
    private static final String CAPTION_TITLE_CSS_CLASS = "captionTitle";
    private static final String CONFIGURATION_PANE_CSS_CLASS = "containerConfigurationPane";
    private static final String TITLE_CSS_CLASS = "title";

    public WinePrefixContainerPanel(WinePrefixDTO containerEntity) {
        super(containerEntity);
    }

    @Override
    Tab drawInformationTab(WinePrefixDTO container) {
        final Tab informationTab = new Tab(translate("Information"));
        final VBox informationPane = new VBox();
        final Text title = new TextWithStyle(translate("Information"), TITLE_CSS_CLASS);

        informationPane.getStyleClass().add(CONFIGURATION_PANE_CSS_CLASS);
        informationPane.getChildren().add(title);

        final GridPane informationContentPane = new GridPane();
        informationContentPane.getStyleClass().add("grid");

        informationContentPane.add(new TextWithStyle(translate("Name:"), CAPTION_TITLE_CSS_CLASS), 0, 0);
        informationContentPane.add(new Text(container.getName()), 1, 0);

        informationContentPane.add(new TextWithStyle(translate("Path:"), CAPTION_TITLE_CSS_CLASS), 0, 1);
        informationContentPane.add(new Text(container.getPath()), 1, 1);

        informationContentPane.add(new TextWithStyle(translate("Wine version:"), CAPTION_TITLE_CSS_CLASS), 0, 2);
        informationContentPane.add(new Text("FIXME"), 1, 2);

        informationContentPane.add(new TextWithStyle(translate("Wine architecture:"), CAPTION_TITLE_CSS_CLASS), 0, 3);
        informationContentPane.add(new Text(container.getPrefixArchitecture().toString()), 1, 3);

        informationContentPane.add(new TextWithStyle(translate("Wine distribution:"), CAPTION_TITLE_CSS_CLASS), 0, 4);
        informationContentPane.add(new Text("FIXME"), 1, 4);

        informationContentPane.getRowConstraints().addAll(
                new RowConstraints(20.),
                new RowConstraints(20.),
                new RowConstraints(20.),
                new RowConstraints(20.),
                new RowConstraints(20.)
        );

        informationContentPane.getColumnConstraints().addAll(
                new ColumnConstraintsWithPercentage(20),
                new ColumnConstraintsWithPercentage(80)
        );

        informationPane.getChildren().addAll(informationContentPane);
        informationTab.setContent(informationPane);
        informationTab.setClosable(false);
        return informationTab;
    }


}
