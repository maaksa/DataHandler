package view;

import controller.CreateNestedEntityEventHandler;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.Window;
import model.Entity;

import java.util.HashMap;
import java.util.Map;

public class UpdateEntityScene extends Scene {

    private Map<Object, Object> newFields;

    public UpdateEntityScene(Pane root) {
        super(root);
        Entity entity = (Entity) MainWindow.getInstance().getScene().getEntityView().getSelectionModel().getSelectedItem();

        Text nameText = new Text("name: ");
        nameText.prefHeight(20);
        nameText.prefWidth(100);
        TextField nameTextField = new TextField(entity.getName());
        HBox nameHBox = new HBox(nameText, nameTextField);
        nameHBox.setSpacing(20);
        nameHBox.setPadding(new Insets(10));

        Map<Object, Object> fields = entity.getFields();
        Map<Object, TextField> textFieldMap = new HashMap<>();

        VBox valueUpdateVBox = new VBox();
        valueUpdateVBox.setSpacing(10);
        valueUpdateVBox.setPadding(new Insets(10));

        for (Object o : fields.keySet()) {
            Text keyUpdateText = new Text("key: " + o.toString());
            keyUpdateText.prefHeight(20);
            keyUpdateText.prefWidth(100);
            Object object = fields.get(o);
            TextField valueUpdateTextField = new TextField();
            if(object != null){
                valueUpdateTextField.setText(object.toString());
            }
            Text valueUpdateText = new Text("value: ");
            valueUpdateText.prefHeight(20);
            valueUpdateText.prefWidth(100);

            HBox valueUpdateHBox = new HBox(keyUpdateText, valueUpdateText, valueUpdateTextField);
            valueUpdateHBox.setSpacing(20);
            valueUpdateVBox.getChildren().add(valueUpdateHBox);
            textFieldMap.put(o, valueUpdateTextField);
        }
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

        newFields = new HashMap<>();

        addFieldButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if(keyTextField.getText().isBlank() || valueTextField.getText().isBlank()){
                    //todo error
                    return;
                }
                newFields.put(keyTextField.getText(), valueTextField.getText());
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
                int id = entity.getId();
                String name = entity.getName();

                //ime ne moze da bude prazno
                if(nameTextField.getText().isBlank()){
                    return;
                }
                String newName = "";
                if(!nameTextField.getText().equals(name)){
                    newName = nameTextField.getText();
                }
                for (Object o : fields.keySet()) {
                    if (fields.get(o) != null && !fields.get(o).toString().equals(textFieldMap.get(o).getText())) {
                        if (textFieldMap.get(o).getText().isBlank()) {
                            newFields.put(o, null);
                        } else {
                            newFields.put(o, textFieldMap.get(o).getText());
                        }
                    }
                }
                if(newName.isBlank()) {
                    if(!newFields.isEmpty()) {
                        MainWindow.getInstance().getAppCore().update(id, newFields);
                    }else{
                        return; //error
                    }
                }else{
                    if(!newFields.isEmpty()) {
                        MainWindow.getInstance().getAppCore().update(id, newName, newFields);
                    }else{
                        MainWindow.getInstance().getAppCore().update(id, newName);
                    }
                }
                Node source = (Node) actionEvent.getSource();
                Stage stage = (Stage) source.getScene().getWindow();
                MainWindow.getInstance().getScene().getEntityView().refresh();
                stage.close();
            }
        });

        HBox hBoxButtons = new HBox(confirmButton, cancelButton);
        hBoxButtons.setSpacing(20);
        hBoxButtons.setPadding(new Insets(10));

        Button addNestedButton = new Button("Add nested entity");
        addNestedButton.setPrefSize(200, 20);
        addNestedButton.setOnAction(new CreateNestedEntityEventHandler());

        HBox hBoxAddNested = new HBox(addNestedButton);
        hBoxAddNested.setPadding(new Insets(10));

        VBox vBoxUpdate = new VBox();
        vBoxUpdate.getChildren().add(nameHBox);
        vBoxUpdate.getChildren().add(valueUpdateVBox);
        vBoxUpdate.getChildren().add(fieldHBox);
        vBoxUpdate.getChildren().add(hBoxAddNested);
        vBoxUpdate.getChildren().add(hBoxButtons);

        root.getChildren().add(vBoxUpdate);
    }

    public Map<Object, Object> getNewFields() {
        return newFields;
    }
}
