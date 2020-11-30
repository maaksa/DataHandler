package controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import model.Entity;
import view.MainWindow;


public class RemoveEntityEventHandler implements EventHandler<ActionEvent> {
    @Override
    public void handle(ActionEvent actionEvent) {
        if(MainWindow.getInstance().getScene().getEntityView().getSelectionModel().getSelectedItem() != null) {
            Entity entity = (Entity) MainWindow.getInstance().getScene().
                    getEntityView().getSelectionModel().getSelectedItem();
            MainWindow.getInstance().getAppCore().remove(entity);
            MainWindow.getInstance().getScene().getEntityView().refresh();
        }
    }
}
