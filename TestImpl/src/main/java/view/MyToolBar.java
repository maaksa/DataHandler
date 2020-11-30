package view;

import controller.*;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;

public class MyToolBar extends ToolBar {
    private Button addButton;
    private Button updateButton;
    private Button searchButton;
    private Button removeButton;

    public MyToolBar(){

        addButton = new Button("Add");
        addButton.setPrefSize(100, 20);
        addButton.setOnAction(new CreateEntityEventHandler());

        updateButton = new Button("Update");
        updateButton.setPrefSize(100, 20);
        updateButton.setOnAction(new UpdateEntityEventHandler());

        searchButton = new Button("Search");
        searchButton.setPrefSize(100, 20);
        searchButton.setOnAction(new SearchEntityEventHandler());

        removeButton = new Button("Remove");
        removeButton.setPrefSize(100, 20);
        removeButton.setOnAction(new RemoveEntityEventHandler());

        this.setPadding(new Insets(10));
        this.getItems().add(addButton);
        this.getItems().add(updateButton);
        this.getItems().add(searchButton);
        this.getItems().add(removeButton);
    }

    public Button getAddButton() {
        return addButton;
    }

    public Button getRemoveButton() {
        return removeButton;
    }

    public Button getSearchButton() {
        return searchButton;
    }

    public Button getUpdateButton() {
        return updateButton;
    }
}
