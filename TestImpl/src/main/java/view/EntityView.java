package view;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Entity;

public class EntityView extends TableView {

    public EntityView() {
        TableColumn nameColumn = new TableColumn("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn idColumn = new TableColumn("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn fieldsColumn = new TableColumn("Fields");
        fieldsColumn.setCellValueFactory(new PropertyValueFactory<>("fields"));

        this.getColumns().add(idColumn);
        this.getColumns().add(nameColumn);
        this.getColumns().add(fieldsColumn);

    }
}
