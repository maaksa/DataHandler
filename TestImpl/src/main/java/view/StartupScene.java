package view;

import controller.ConfirmButtonController;
import controller.SelectDirectoryController;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

import java.io.File;

public class StartupScene extends Scene {
    private Button selectDirectoryButton;
    private CheckBox generateIDCheckBox;
    private Button confirmButton;
    private TextField maxObjectsPerFileTextField;

    private File directory;

    public StartupScene(Pane root){
        super(root);

        selectDirectoryButton = new Button("Choose Directory");
        selectDirectoryButton.setPrefSize(150, 20);
        selectDirectoryButton.setOnAction(new SelectDirectoryController());

        confirmButton = new Button("Confirm");
        confirmButton.setPrefSize(100, 20);
        confirmButton.setOnAction(new ConfirmButtonController(this));

        maxObjectsPerFileTextField = new TextField();
        maxObjectsPerFileTextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
                if (!newValue.matches("\\d*")) {
                    maxObjectsPerFileTextField.setText(newValue.replaceAll("[^\\d]", ""));
                }
                if(newValue.length() > 10){
                    maxObjectsPerFileTextField.setText(oldValue);
                }

            }
        });
        maxObjectsPerFileTextField.setPrefSize(200, 20);

        generateIDCheckBox = new CheckBox("Generate ID");

        HBox hBox = new HBox();
        Text text = new Text("Enter Objects Per File:");
        text.prefHeight(20);
        text.prefWidth(100);
        hBox.getChildren().add(text);
        hBox.getChildren().add(maxObjectsPerFileTextField);
        hBox.setSpacing(10);

        VBox vBox = new VBox();
        vBox.getChildren().add(generateIDCheckBox);
        vBox.getChildren().add(selectDirectoryButton);
        vBox.getChildren().add(hBox);
        vBox.getChildren().add(confirmButton);

        vBox.setSpacing(20);
        vBox.setPadding(new Insets(10));

        root.getChildren().add(vBox);
    }

    public File getDirectory() {
        return directory;
    }

    public void setDirectory(File directory) {
        this.directory = directory;
    }

    public CheckBox getGenerateIDCheckBox() {
        return generateIDCheckBox;
    }

    public TextField getMaxObjectsPerFileTextField() {
        return maxObjectsPerFileTextField;
    }

}
