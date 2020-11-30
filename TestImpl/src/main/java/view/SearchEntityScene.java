package view;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

public class SearchEntityScene extends Scene {

    private VBox vBox;
    private HBox hBoxOptions;
    private HBox hBoxButtons;
    private HBox hBoxSort;
    private ScrollPane root;
    private ComboBox<String> comboBoxOptions;
    private TextField textFieldId;
    private TextField textFieldName;
    private TextField textFieldKey;
    private TextField textFieldValue;
    private Button searchButton;
    private Button cancelButton;
    private Button andButton;
    private CheckBox sortCheckBox;
    private ComboBox<String> sortByComboBox;
    private ComboBox<String> sortAscComboBox;

    private int id;
    private String name;
    private Map<Object, Object> fields;

    public SearchEntityScene(ScrollPane root) {
        super(root);
        this.root = root;
        this.fields = new HashMap<>();
        Text searchByText = new Text("Search by: ");
        searchByText.prefWidth(100);
        searchByText.prefHeight(20);
        comboBoxOptions = new ComboBox<>();
        comboBoxOptions.setPrefSize(100, 20);
        comboBoxOptions.getItems().add("id");
        comboBoxOptions.getItems().add("name");
        comboBoxOptions.getItems().add("field");
        hBoxOptions = new HBox(searchByText, comboBoxOptions);
        hBoxOptions.setSpacing(20);
        hBoxOptions.setPadding(new Insets(10));
        comboBoxOptions.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String selected = comboBoxOptions.getSelectionModel().getSelectedItem();
                switch (selected){
                    case "id":
                        searchByID();
                        break;
                    case "name":
                        searchByName();
                        break;
                    case "field":
                        searchByField();
                        break;
                }
            }
        });
        comboBoxOptions.getSelectionModel().selectFirst();

        sortByComboBox = new ComboBox<>();
        sortByComboBox.setPrefSize(100, 20);
        sortByComboBox.getItems().add("name");
        sortByComboBox.getItems().add("id");
        sortByComboBox.getSelectionModel().selectFirst();

        sortAscComboBox = new ComboBox<>();
        sortAscComboBox.setPrefSize(100, 20);
        sortAscComboBox.getItems().add("asc");
        sortAscComboBox.getItems().add("desc");
        sortAscComboBox.getSelectionModel().selectFirst();

        sortCheckBox = new CheckBox("Sort");
        sortCheckBox.setSelected(false);
        sortCheckBox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String selected = comboBoxOptions.getSelectionModel().getSelectedItem();
                switch (selected){
                    case "id":
                        searchByID();
                        break;
                    case "name":
                        searchByName();
                        break;
                    case "field":
                        searchByField();
                        break;
                }
            }
        });

        hBoxSort = new HBox();
        hBoxSort.getChildren().add(sortByComboBox);
        hBoxSort.getChildren().add(sortAscComboBox);
        hBoxSort.setPadding(new Insets(10));
        hBoxSort.setSpacing(20);

        searchButton = new Button("Search");
        searchButton.setPrefSize(100, 20);
        searchButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                boolean sort = sortCheckBox.isSelected();
                boolean isSortByNameOrId = false;
                if(sortByComboBox.getSelectionModel().getSelectedItem().equals("name")){
                    isSortByNameOrId = true;
                }
                boolean asc = false;
                if(sortAscComboBox.getSelectionModel().getSelectedItem().equals("asc")){
                    asc = true;
                }
                Node source = (Node) actionEvent.getSource();
                Stage stage = (Stage) source.getScene().getWindow();
                String selected = comboBoxOptions.getSelectionModel().getSelectedItem();
                switch (selected){
                    case "id":
                        if(textFieldId.getText().isBlank()){
                            return;
                        }
                        id = Integer.parseInt(textFieldId.getText());
                        break;
                    case "name":
                        if(textFieldName.getText().isBlank()){
                            return;
                        }
                        name = textFieldName.getText();
                        break;
                    case "field":
                        if(textFieldKey.getText().isBlank() || textFieldValue.getText().isBlank()){
                            return;
                        }
                        fields.put(textFieldKey.getText(), textFieldValue.getText());
                        break;
                }
                if(id == 0){
                    if(name == null || name.isBlank()){
                        if(fields.isEmpty()){
                            return;
                        }else{
                            MainWindow.getInstance().getAppCore().search(fields, sort, isSortByNameOrId, asc);
                        }
                    }else if(fields.isEmpty()){
                            MainWindow.getInstance().getAppCore().search(name, sort, isSortByNameOrId, asc);
                    }else{
                            MainWindow.getInstance().getAppCore().search(name, fields, sort, isSortByNameOrId, asc);
                    }
                }else{
                    MainWindow.getInstance().getAppCore().search(id);
                }
                stage.close();
            }
        });

        cancelButton = new Button("Cancel");
        cancelButton.setPrefSize(100, 20);
        cancelButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Node source = (Node) actionEvent.getSource();
                Stage stage = (Stage) source.getScene().getWindow();
                stage.close();
            }
        });

        andButton = new Button("And");
        andButton.setPrefSize(100, 20);
        andButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String selected = comboBoxOptions.getSelectionModel().getSelectedItem();
                switch (selected){
                    case "id":
                        if(textFieldId.getText().isBlank()){
                            return;
                        }
                        id = Integer.parseInt(textFieldId.getText());
                        comboBoxOptions.getItems().remove("id");
                        comboBoxOptions.getSelectionModel().selectFirst();
                        break;
                    case "name":
                        if(textFieldName.getText().isBlank()){
                            return;
                        }
                        name = textFieldName.getText();
                        comboBoxOptions.getItems().remove("name");
                        comboBoxOptions.getSelectionModel().selectFirst();
                        break;
                    case "field":
                        if(textFieldKey.getText().isBlank() || textFieldValue.getText().isBlank()){
                            return;
                        }
                        fields.put(textFieldKey.getText(), textFieldValue.getText());
                        textFieldValue.setText("");
                        textFieldKey.setText("");
                        break;
                }
            }
        });

        textFieldId = new TextField();
        textFieldId.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
                if (!newValue.matches("\\d*")) {
                    textFieldId.setText(newValue.replaceAll("[^\\d]", ""));
                }
                if(newValue.length() > 10){
                    textFieldId.setText(oldValue);
                }

            }
        });

        hBoxButtons = new HBox(searchButton, andButton, cancelButton);
        hBoxButtons.setPadding(new Insets(10));
        hBoxButtons.setSpacing(20);

        textFieldKey = new TextField();
        textFieldName = new TextField();
        textFieldValue = new TextField();

        vBox = new VBox();
        vBox.setSpacing(10);
        vBox.setPadding(new Insets(10));
        root.setContent(vBox);
        searchByID();
    }

    private void searchByID(){
        andButton.setDisable(true);
        vBox.getChildren().clear();
        vBox.getChildren().add(hBoxOptions);
        vBox.getChildren().add(textFieldId);
        HBox hBoxSortCB = new HBox();
        hBoxSortCB.getChildren().add(sortCheckBox);
        hBoxSortCB.setSpacing(20);
        hBoxSortCB.setPadding(new Insets(10));
        vBox.getChildren().add(hBoxSortCB);
        if (sortCheckBox.isSelected()){
            addSortView();
        }
        vBox.getChildren().add(hBoxButtons);
    }

    private void searchByName(){
        andButton.setDisable(false);
        vBox.getChildren().clear();
        vBox.getChildren().add(hBoxOptions);
        vBox.getChildren().add(textFieldName);
        HBox hBoxSortCB = new HBox();
        hBoxSortCB.getChildren().add(sortCheckBox);
        hBoxSortCB.setSpacing(20);
        hBoxSortCB.setPadding(new Insets(10));
        vBox.getChildren().add(hBoxSortCB);
        if (sortCheckBox.isSelected()){
            addSortView();
        }
        vBox.getChildren().add(hBoxButtons);
    }

    private void searchByField(){
        andButton.setDisable(false);
        vBox.getChildren().clear();
        vBox.getChildren().add(hBoxOptions);
        Text keyText = new Text("Key: ");
        keyText.prefHeight(20);
        keyText.prefWidth(100);
        Text valueText = new Text("Value: ");
        valueText.prefHeight(20);
        valueText.prefWidth(100);
        HBox hBox = new HBox(keyText, textFieldKey, valueText, textFieldValue);
        hBox.setSpacing(20);
        vBox.getChildren().add(hBox);
        HBox hBoxSortCB = new HBox();
        hBoxSortCB.getChildren().add(sortCheckBox);
        hBoxSortCB.setSpacing(20);
        hBoxSortCB.setPadding(new Insets(10));
        vBox.getChildren().add(hBoxSortCB);
        if (sortCheckBox.isSelected()){
            addSortView();
        }
        vBox.getChildren().add(hBoxButtons);
    }

    private void addSortView(){
        vBox.getChildren().add(hBoxSort);
    }

    private void removeSortView(){
        vBox.getChildren().remove(hBoxSort);
    }

}
