package app;

import model.Entity;
import observer.ObservableImplementation;
import storage.Storage;
import view.MainWindow;

import java.util.*;

public class AppCore extends ObservableImplementation {

    private Storage storage;

    private List<Entity> entities;

    private boolean generateId;

    public AppCore(){
        entities = new ArrayList<>();
    }

    public void initialiseAppCore(){
        entities = storage.loadAll();
        notifyObservers(entities);
    }

    public boolean save(int id, String name, Map<Object, Object> fields){
        return storage.save(id, name, fields);
    }

    public void search(Map<Object, Object> fields, boolean sort, boolean isSearchByNameOrID, boolean asc) {
        entities = storage.search(fields, sort, isSearchByNameOrID, asc);
        notifyObservers(entities);
    }

    public void search(int id){
        Entity entity = storage.search(id);
        entities.clear();
        entities.add(entity);
        notifyObservers(entities);
    }

    public Entity createEntity(int id, String name, Map<Object, Object> fields){
        return storage.createEntity(id, name, fields);
    }

    public void search(String name, Map<Object, Object> fields, boolean sort, boolean isSearchByNameOrID, boolean asc){
        entities = storage.search(name, fields, sort, isSearchByNameOrID, asc);
        notifyObservers(entities);
    }

    public void search(String name, boolean sort, boolean isSearchByNameOrID, boolean asc){
        entities = storage.search(name, sort, isSearchByNameOrID, asc);
        notifyObservers(entities);
    }

    public void update(int id, String name){
        storage.update(id, name);
        entities = storage.loadAll();
        notifyObservers(entities);
    }

    public void update(int id, Map<Object, Object> fields){
        storage.update(id, fields);
        entities = storage.loadAll();
        notifyObservers(entities);
    }

    public void update(int id, String name, Map<Object, Object> fields){
        storage.update(id, name, fields);
        entities = storage.loadAll();
        notifyObservers(entities);
    }

    public List<Entity> getEntities() {
        return entities;
    }

    public void setEntities(List<Entity> entities) {
        this.entities = entities;
        notifyObservers(entities);
    }

    public boolean isGenerateId() {
        return generateId;
    }

    public void setGenerateId(boolean generateId) {
        this.generateId = generateId;
    }

    public Storage getStorage() {
        return storage;
    }

    public void setStorage(Storage storage) {
        this.storage = storage;
    }

    public void remove(Entity entity) {
        storage.remove(entity);
        entities.remove(entity);
        notifyObservers(entity, "remove");
    }

}
