package app;

import javafx.application.Application;
import javafx.stage.Stage;
import model.Entity;
import view.MainWindow;

import java.io.File;
import java.nio.file.Files;
import java.util.*;

public class App extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        MainWindow.getInstance().setAppCore(new AppCore());
        MainWindow.getInstance().initialise();
        Map<Object, Object> fields = new HashMap<>();
        fields.put("ime", "Boris");
        fields.put("prezime", "Stojanovic");
        Map<Object, Object> fields2 = new HashMap<>();
        fields2.put("profesor", new Entity(1, "profesor", fields));
        fields2.put("ime", "Milos");
        fields2.put("prezime", "Maksimovic");
        List<Entity> entityList = new ArrayList<>();
        entityList.add(new Entity(3, "Student", fields));
        entityList.add(new Entity(4, "Student", fields2));

        MainWindow.getInstance().getAppCore().setEntities(entityList);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
