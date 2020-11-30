package view;

import app.AppCore;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Entity;
import observer.MyObserver;

import java.util.Iterator;
import java.util.List;

public class MainWindow implements MyObserver {

    private static MainWindow instance = null;

    private Stage currStage;
    private Stage mainStage;
    private MainScene scene;

    private AppCore appCore;

    private MainWindow(){
    }

    public static MainWindow getInstance() {
        if(instance == null){
            instance = new MainWindow();
        }
        return instance;
    }

    public void initialise(){
        initialiseFirstScene();
        mainStage.show();
    }

    private void initialiseFirstScene(){
        StartupScene scene = new StartupScene(new StackPane());
        mainStage = new Stage();
        mainStage.setScene(scene);
    }

    public void initialiseMainScene(){
        scene = new MainScene(new StackPane());
        mainStage.close();
        mainStage = new Stage();
        mainStage.setScene(scene);
        mainStage.show();
    }

    public void initialiseCreateEntityStage(){
        currStage = new Stage();
        currStage.setScene(new CreateEntityScene(new StackPane()));
        currStage.initModality(Modality.APPLICATION_MODAL);
        currStage.show();
    }

    public void initialiseUpdateStage(){
        currStage = new Stage();
        currStage.setScene(new UpdateEntityScene(new StackPane()));
        currStage.initModality(Modality.APPLICATION_MODAL);
        currStage.show();
    }

    public void initialiseSearchStage() {
        currStage = new Stage();
        SearchEntityScene searchEntityScene = new SearchEntityScene(new ScrollPane());
        currStage.setScene(searchEntityScene);
        currStage.initModality(Modality.APPLICATION_MODAL);
        currStage.show();
    }

    public AppCore getAppCore() {
        return appCore;
    }

    public void setAppCore(AppCore appCore) {
        this.appCore = appCore;
        appCore.addObserver(this);
    }

    public MainScene getScene() {
        return scene;
    }

    public void setScene(MainScene scene) {
        this.scene = scene;
    }


    public Stage getMainStage() {
        return mainStage;
    }

    public void setMainStage(Stage mainStage) {
        this.mainStage = mainStage;
    }

    public Stage getCurrStage() {
        return currStage;
    }

    public void setCurrStage(Stage currStage) {
        this.currStage = currStage;
    }

    @Override
    public void update(Object object, String string) {
        if(object instanceof Entity){
            Entity newEntity = (Entity) object;
            List<Entity> entities = getScene().getEntityView().getItems();
            Iterator iterator = entities.iterator();
            if(string.equals("remove")){
                while(iterator.hasNext()) {
                    Entity entity = (Entity) iterator.next();
                    if(newEntity.getId() == entity.getId()){
                        getScene().getEntityView().getItems().remove(entity);
                        break;
                    }
                }
            }else if(string.equals("update")){
                while(iterator.hasNext()) {
                    Entity entity = (Entity) iterator.next();
                    if(newEntity.getId() == entity.getId()){
                        getScene().getEntityView().getItems().remove(entity);
                        getScene().getEntityView().getItems().add(newEntity);
                        break;
                    }
                }
            }
        }else if(object instanceof List){
            List<Entity> entityList = (List<Entity>) object;
            if(getScene() != null && getScene().getEntityView() != null) {
                getScene().getEntityView().getItems().clear();
                for (Entity entity : entityList) {
                    getScene().getEntityView().getItems().add(entity);
                }
            }
        }
    }

}
