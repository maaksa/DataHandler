package view;

import controller.CreateNestedEntityEventHandler;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

public class CreateEntityScene extends Scene {

    private Map<Object, Object> fields;

    public CreateEntityScene(Pane root) {
        super(root);
        fields = new HashMap<>();
        boolean generateId = MainWindow.getInstance().getAppCore().isGenerateId();
        Text idText = new Text("id: ");
        idText.prefWidth(100);
        idText.prefHeight(20);
        TextField idTextField = new TextField();
        idTextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
                if (!newValue.matches("\\d*")) {
                    idTextField.setText(newValue.replaceAll("[^\\d]", ""));
                }
                if(newValue.length() > 10){
                    idTextField.setText(oldValue);
                }

            }
        });
        HBox hBoxId = new HBox(idText, idTextField);
        hBoxId.setSpacing(20);
        hBoxId.setPadding(new Insets(10));

        Text nameText = new Text("name: ");
        nameText.prefHeight(20);
        nameText.prefWidth(100);
        TextField nameTextField = new TextField();
        HBox nameHBox = new HBox(nameText, nameTextField);
        nameHBox.setSpacing(20);
        nameHBox.setPadding(new Insets(10));

        Text keyText = new Text("key: ");
        keyText.prefWidth(100);
        keyText.prefHeight(20);
        Text valueText = new Text("value: ");
        valueText.prefWidth(100);
        valueText.prefHeight(20);
        TextField keyTextField = new TextField();
        keyTextField.setPrefSize(200, 20);
        TextField valueTextField = new TextField();
        valueTextField.setPrefSize(200, 20);
        Button addFieldButton = new Button("Add Field");
        addFieldButton.setPrefSize(100, 20);
        HBox fieldHBox = new HBox(keyText, keyTextField, valueText, valueTextField, addFieldButton);
        fieldHBox.setSpacing(20);
        fieldHBox.setPadding(new Insets(10));

        addFieldButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if(keyTextField.getText().isBlank() || valueTextField.getText().isBlank()){
                    //todo error
                    return;
                }
                fields.put(keyTextField.getText(), valueTextField.getText());
                keyTextField.setText("");
                valueTextField.setText("");
            }
        });

        Button cancelButton = new Button("Cancel");
        cancelButton.setPrefSize(100, 20);
        cancelButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
                stage.close();
            }
        });

        Button confirmButton = new Button("Confirm");
        confirmButton.setPrefSize(100, 20);
        confirmButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                int id = 0;
                String name = "";
                if(!generateId){
                    if(idTextField.getText().isBlank()){
                        return;//todo error
                    }
                    id = Integer.parseInt(idTextField.getText());
                }
                if(nameTextField.getText().isBlank()){
                    return;
                }
                name = nameTextField.getText();
                if(MainWindow.getInstance().getAppCore().save(id, name, fields)) {
                    MainWindow.getInstance().getAppCore().setEntities(MainWindow.getInstance().getAppCore().getStorage().loadAll());
                    Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                    stage.close();
                }else{
                    //todo error
                }
            }
        });

        Button addNestedButton = new Button("Add nested entity");
        addNestedButton.setPrefSize(200, 20);
        addNestedButton.setOnAction(new CreateNestedEntityEventHandler());

        HBox hBoxAddNested = new HBox(addNestedButton);
        hBoxAddNested.setPadding(new Insets(10));

        HBox hBoxButtons = new HBox(confirmButton, cancelButton);
        hBoxButtons.setSpacing(20);
        hBoxButtons.setPadding(new Insets(10));

        VBox vBoxAdd = new VBox();
        if(!generateId){
            vBoxAdd.getChildren().add(hBoxId);
        }
        vBoxAdd.getChildren().add(nameHBox);
        vBoxAdd.getChildren().add(fieldHBox);
        vBoxAdd.getChildren().add(hBoxAddNested);
        vBoxAdd.getChildren().add(hBoxButtons);

        root.getChildren().add(vBoxAdd);
    }

    public Map<Object, Object> getFields() {
        return fields;
    }
}
