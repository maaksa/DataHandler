package controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import view.MainWindow;

public class UpdateEntityEventHandler implements EventHandler<ActionEvent> {
    @Override
    public void handle(ActionEvent actionEvent) {
        if(MainWindow.getInstance().getScene().getEntityView().getSelectionModel().getSelectedItem() != null) {
            MainWindow.getInstance().initialiseUpdateStage();
        }
    }
}
