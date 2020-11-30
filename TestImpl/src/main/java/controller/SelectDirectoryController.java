package controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.stage.DirectoryChooser;
import view.MainWindow;
import view.StartupScene;

import java.io.File;

public class SelectDirectoryController implements EventHandler<ActionEvent> {
    @Override
    public void handle(ActionEvent actionEvent) {
        DirectoryChooser chooser = new DirectoryChooser();
        File directory = chooser.showDialog(MainWindow.getInstance().getMainStage());
        ((StartupScene)((Node)actionEvent.getSource()).getScene()).setDirectory(directory);
    }
}
