package controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import view.MainWindow;

public class CreateEntityEventHandler implements EventHandler<ActionEvent> {
    @Override
    public void handle(ActionEvent actionEvent) {
        MainWindow.getInstance().initialiseCreateEntityStage();
    }
}
