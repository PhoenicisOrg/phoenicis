package org.phoenicis.javafx.views.mainwindow.containers;

import com.sun.javafx.collections.ImmutableObservableList;
import com.sun.javafx.collections.ObservableListWrapper;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Tab;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.phoenicis.containers.dto.WinePrefixContainerDTO;
import org.phoenicis.containers.wine.WinePrefixContainerController;
import org.phoenicis.containers.wine.parameters.*;
import org.phoenicis.javafx.views.common.ErrorMessage;
import org.phoenicis.javafx.views.common.TextWithStyle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;

import static org.phoenicis.configuration.localisation.Localisation.tr;

/**
 * Created by marc on 27.05.17.
 */
public class WinePrefixContainerDisplayTab extends Tab {
    private static final String CAPTION_TITLE_CSS_CLASS = "captionTitle";
    private static final String CONFIGURATION_PANE_CSS_CLASS = "containerConfigurationPane";
    private static final String TITLE_CSS_CLASS = "title";

    private final WinePrefixContainerDTO container;
    private final WinePrefixContainerController winePrefixContainerController;

    private final List<Node> lockableElements = new ArrayList<>();

    public WinePrefixContainerDisplayTab(WinePrefixContainerDTO container,
            WinePrefixContainerController winePrefixContainerController) {
        super(tr("Display"));

        this.container = container;
        this.winePrefixContainerController = winePrefixContainerController;

        this.setClosable(false);

        this.populate();
    }

    private void populate() {
        final VBox displayPane = new VBox();
        final Text title = new TextWithStyle(tr("Display settings"), TITLE_CSS_CLASS);

        displayPane.getStyleClass().add(CONFIGURATION_PANE_CSS_CLASS);
        displayPane.getChildren().add(title);

        final GridPane displayContentPane = new GridPane();
        displayContentPane.getStyleClass().add("grid");

        final ComboBox<UseGLSL> glslComboBox = new ComboBox<>();
        glslComboBox.setMaxWidth(Double.MAX_VALUE);
        glslComboBox.setValue(container.getUseGlslValue());
        glslComboBox.valueProperty().addListener((observable, oldValue, newValue) -> this.changeSettings(newValue));
        addItems(glslComboBox, UseGLSL.class);
        displayContentPane.add(new TextWithStyle(tr("GLSL support"), CAPTION_TITLE_CSS_CLASS), 0, 0);
        displayContentPane.add(glslComboBox, 1, 0);

        final ComboBox<DirectDrawRenderer> directDrawRendererComboBox = new ComboBox<>();
        directDrawRendererComboBox.setMaxWidth(Double.MAX_VALUE);
        directDrawRendererComboBox.setValue(container.getDirectDrawRenderer());
        directDrawRendererComboBox.valueProperty()
                .addListener((observable, oldValue, newValue) -> this.changeSettings(newValue));
        addItems(directDrawRendererComboBox, DirectDrawRenderer.class);
        displayContentPane.add(new TextWithStyle(tr("Direct Draw Renderer"), CAPTION_TITLE_CSS_CLASS), 0, 1);
        displayContentPane.add(directDrawRendererComboBox, 1, 1);

        final ComboBox<VideoMemorySize> videoMemorySizeComboBox = new ComboBox<>();
        videoMemorySizeComboBox.setMaxWidth(Double.MAX_VALUE);
        videoMemorySizeComboBox.setValue(container.getVideoMemorySize());
        videoMemorySizeComboBox.valueProperty()
                .addListener((observable, oldValue, newValue) -> this.changeSettings(newValue));
        addItemsVideoMemorySize(videoMemorySizeComboBox);
        displayContentPane.add(new TextWithStyle(tr("Video memory size"), CAPTION_TITLE_CSS_CLASS), 0, 2);
        displayContentPane.add(videoMemorySizeComboBox, 1, 2);

        final ComboBox<OffscreenRenderingMode> offscreenRenderingModeComboBox = new ComboBox<>();
        offscreenRenderingModeComboBox.setMaxWidth(Double.MAX_VALUE);
        offscreenRenderingModeComboBox.setValue(container.getOffscreenRenderingMode());
        offscreenRenderingModeComboBox.valueProperty()
                .addListener((observable, oldValue, newValue) -> this.changeSettings(newValue));
        addItems(offscreenRenderingModeComboBox, OffscreenRenderingMode.class);
        displayContentPane.add(new TextWithStyle(tr("Offscreen rendering mode"), CAPTION_TITLE_CSS_CLASS), 0, 3);
        displayContentPane.add(offscreenRenderingModeComboBox, 1, 3);

        final ComboBox<RenderTargetModeLock> renderTargetModeLockComboBox = new ComboBox<>();
        renderTargetModeLockComboBox.setMaxWidth(Double.MAX_VALUE);
        renderTargetModeLockComboBox.setValue(container.getRenderTargetModeLock());
        renderTargetModeLockComboBox.valueProperty()
                .addListener((observable, oldValue, newValue) -> this.changeSettings(newValue));
        addItems(renderTargetModeLockComboBox, RenderTargetModeLock.class);
        displayContentPane.add(new TextWithStyle(tr("Render target lock mode"), CAPTION_TITLE_CSS_CLASS), 0, 4);
        displayContentPane.add(renderTargetModeLockComboBox, 1, 4);

        final ComboBox<Multisampling> multisamplingComboBox = new ComboBox<>();
        multisamplingComboBox.setMaxWidth(Double.MAX_VALUE);
        multisamplingComboBox.setValue(container.getMultisampling());
        multisamplingComboBox.valueProperty()
                .addListener((observable, oldValue, newValue) -> this.changeSettings(newValue));
        addItems(multisamplingComboBox, Multisampling.class);
        displayContentPane.add(new TextWithStyle(tr("Multisampling"), CAPTION_TITLE_CSS_CLASS), 0, 5);
        displayContentPane.add(multisamplingComboBox, 1, 5);

        final ComboBox<StrictDrawOrdering> strictDrawOrderingComboBox = new ComboBox<>();
        strictDrawOrderingComboBox.setMaxWidth(Double.MAX_VALUE);
        strictDrawOrderingComboBox.setValue(container.getStrictDrawOrdering());
        strictDrawOrderingComboBox.valueProperty()
                .addListener((observable, oldValue, newValue) -> this.changeSettings(newValue));
        addItems(strictDrawOrderingComboBox, StrictDrawOrdering.class);
        displayContentPane.add(new TextWithStyle(tr("Strict Draw Ordering"), CAPTION_TITLE_CSS_CLASS), 0, 6);
        displayContentPane.add(strictDrawOrderingComboBox, 1, 6);

        final ComboBox<AlwaysOffscreen> alwaysOffscreenComboBox = new ComboBox<>();
        alwaysOffscreenComboBox.setMaxWidth(Double.MAX_VALUE);
        alwaysOffscreenComboBox.setValue(container.getAlwaysOffscreen());
        alwaysOffscreenComboBox.valueProperty()
                .addListener((observable, oldValue, newValue) -> this.changeSettings(newValue));
        addItems(alwaysOffscreenComboBox, AlwaysOffscreen.class);
        displayContentPane.add(new TextWithStyle(tr("Always Offscreen"), CAPTION_TITLE_CSS_CLASS), 0, 7);
        displayContentPane.add(alwaysOffscreenComboBox, 1, 7);

        Region spacer = new Region();
        GridPane.setHgrow(spacer, Priority.ALWAYS);
        displayContentPane.add(spacer, 2, 0);

        displayPane.getChildren().addAll(displayContentPane);
        this.setContent(displayPane);

        lockableElements.addAll(Arrays.asList(glslComboBox, directDrawRendererComboBox, offscreenRenderingModeComboBox,
                renderTargetModeLockComboBox, multisamplingComboBox, strictDrawOrderingComboBox,
                alwaysOffscreenComboBox, videoMemorySizeComboBox));
    }

    private void changeSettings(RegistryParameter newValue) {
        this.lockAll();
        winePrefixContainerController.changeSetting(container, newValue, this::unlockAll,
                e -> Platform.runLater(() -> new ErrorMessage(tr("Error"), e).show()));
    }

    private <T extends Enum> void addItems(ComboBox<T> comboBox, Class<T> clazz) {
        final List<T> possibleValues = new ArrayList<>(EnumSet.allOf(clazz));

        final ObservableList<T> possibleValuesObservable = new ObservableListWrapper<>(possibleValues);
        comboBox.setItems(possibleValuesObservable);
    }

    private void addItemsVideoMemorySize(ComboBox<VideoMemorySize> videoMemorySizeComboBox) {
        videoMemorySizeComboBox.setItems(new ImmutableObservableList<>(VideoMemorySize.possibleValues()));
    }

    public void unlockAll() {
        for (Node element : lockableElements) {
            element.setDisable(false);
        }
    }

    private void lockAll() {
        for (Node element : lockableElements) {
            element.setDisable(true);
        }
    }
}
