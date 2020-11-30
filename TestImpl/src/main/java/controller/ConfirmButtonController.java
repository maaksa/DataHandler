package controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import storage.StorageManager;
import view.MainWindow;
import view.StartupScene;

import java.io.File;

public class ConfirmButtonController implements EventHandler<ActionEvent> {
    private StartupScene view;

    public ConfirmButtonController(StartupScene startupScene){
        this.view = startupScene;
    }

    @Override
    public void handle(ActionEvent actionEvent) {
        File file = view.getDirectory();
        if (file == null){
            return;//error
        }
        String path = file.getPath();
        file.mkdir();
        boolean generateId = view.getGenerateIDCheckBox().isSelected();
        int maxCount = Integer.parseInt(view.getMaxObjectsPerFileTextField().getText());
        if(maxCount == 0){
            return;//error
        }
        try {
            Class.forName("YAMLimpl");
            File[] filesList = file.listFiles();
            if(filesList != null) {
                for (File file1 : filesList) {
                    String filePath = file1.getPath();
                    if(!filePath.endsWith(".yml")){
                        Alert alert = new Alert(Alert.AlertType.ERROR, "Directory contains unsupported file type!");
                        alert.showAndWait();
                        return;
                    }
                }
            }
            MainWindow.getInstance().getAppCore().setStorage(StorageManager.getStorage(path, generateId, maxCount));
            MainWindow.getInstance().initialiseMainScene();
            MainWindow.getInstance().getAppCore().initialiseAppCore();
            MainWindow.getInstance().getAppCore().setGenerateId(generateId);
        }catch (ClassNotFoundException e){
            e.printStackTrace();
            MainWindow.getInstance().getMainStage().close();
            return;
        }
    }
}
