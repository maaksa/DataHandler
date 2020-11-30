package controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import view.CreateNestedEntityScene;

public class CreateNestedEntityEventHandler implements EventHandler<ActionEvent> {
    @Override
    public void handle(ActionEvent actionEvent) {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        CreateNestedEntityScene scene = new CreateNestedEntityScene(new StackPane());
        stage.setScene(scene);
        stage.show();
    }
}
