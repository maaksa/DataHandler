package view;

import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class MainScene extends Scene {

    private EntityView entityView;

    public MainScene(Pane root) {
        super(root);
        entityView = new EntityView();

        VBox vBoxEntity = new VBox();
        MyToolBar toolBarEntity = new MyToolBar();
        vBoxEntity.getChildren().add(entityView);
        vBoxEntity.getChildren().add(toolBarEntity);

        root.getChildren().add(vBoxEntity);
    }

    public EntityView getEntityView() {
        return entityView;
    }
}
