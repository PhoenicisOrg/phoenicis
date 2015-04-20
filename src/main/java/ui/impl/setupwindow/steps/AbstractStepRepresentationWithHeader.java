package ui.impl.setupwindow.steps;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import ui.impl.setupwindow.JavaFXSetupWindowImplementation;
import utils.messages.CancelerMessage;

abstract class AbstractStepRepresentationWithHeader extends AbstractStepRepresentation {
    Pane contentPanel;

    AbstractStepRepresentationWithHeader(JavaFXSetupWindowImplementation parent, CancelerMessage messageWaitingForResponse) {
        super(parent, messageWaitingForResponse);
    }

    public Pane getContentPanel() {
        return contentPanel;
    }

    public void addToContentPanel(Node contentToAdd) {
        this.contentPanel.getChildren().add(contentToAdd);
    }
    /**
     * Draw the header at the top of the window
     */
    private void drawHeader() {
        String title = this.getParentWizardTitle(); // FIXME: use this variable to draw the title of the window
        Pane header = new Pane();
        header.setId("header");
        header.setPrefSize(522, 65);
        header.setLayoutX(-1);
        header.setLayoutY(-1);
        header.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));

        ImageView topImage = new ImageView(this.createTopImage());
        topImage.setLayoutX(426);

        header.getChildren().add(topImage);
        this.addToStep(header);
    }

    private void drawPanelForTopHeader(Pane root) {
        Pane panel = new Pane();
        panel.setId("panelForTopheader");
        panel.setPrefSize(522, 294);
        panel.setLayoutX(-1);
        panel.setLayoutY(63);
        this.addToStep(panel);
        this.contentPanel = panel;
    }

    private Image createTopImage() {
        return new Image("file:" + this.getParentTopImage().getAbsolutePath());
    }

    public void installStep() {
        this.clearAllOnParent();
        this.drawHeader();
        this.drawPanelForTopHeader(this.getParentRoot());
        this.drawFooter();

        this.setStepEvents();
        this.drawStepContent();
    }

}
