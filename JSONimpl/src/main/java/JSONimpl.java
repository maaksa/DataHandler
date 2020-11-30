import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import model.Entity;
import storage.Storage;
import storage.StorageManager;

import java.io.*;
import java.lang.reflect.Type;
import java.util.*;

public class JSONimpl extends Storage {

    private int fileCount = 1;
    private String filePath;
    private List<Entity> entities;

    public JSONimpl() {
        entities = new ArrayList<Entity>();
    }

    static {
        StorageManager.registerStorage(new JSONimpl());
    }

    public boolean save(Entity entity) {
        if (generateId) {
            entity.setId(currId++);
            takenId.add(currId - 1);
            if (entity.getFields() != null) {
                Set<Object> keys = entity.getFields().keySet();
                for (Object key : keys) {
                    if (entity.getFields().get(key) instanceof Entity) {
                        Entity inner = (Entity) entity.getFields().get(key);
                        inner.setId(currId++);
                        takenId.add(currId - 1);
                    }
                }
            }
        } else {
            if (checkID(entity) == false) {
                return false;
            }
        }

        if (filePath == null) {
            filePath = path + "\\data" + fileCount + ".json";
        }

        entities.add(entity);
        try {
            List<Entity> entities_from_file = fileToEntityAll(filePath);
            if (entities_from_file == null) {
                entities_from_file = new ArrayList<>();
            }
            if (entities_from_file.size() >= maxCount) {
                entities_from_file = new ArrayList<>();
                fileCount++;
                filePath = path + "\\data" + fileCount + ".json";
            }
            entities_from_file.add(entity);
            entityToJson(entities_from_file, filePath);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean save(List<Entity> list) {
        List<Integer> oldTakenId = takenId;
        try {
            List<Entity> entities_from_file = fileToEntityAll(filePath);
            for (Entity entity : list) {
                if (!generateId && !checkID(entity)) {
                    takenId = oldTakenId;
                    saveAll(entities);
                    return false;
                }

                if (generateId) {
                    entity.setId(currId++);
                    takenId.add(currId - 1);
                    if (entity.getFields() != null) {
                        Set<Object> keys = entity.getFields().keySet();
                        for (Object key : keys) {
                            if (entity.getFields().get(key) instanceof Entity) {
                                Entity inner = (Entity) entity.getFields().get(key);
                                inner.setId(currId++);
                                takenId.add(currId - 1);
                            }
                        }
                    }
                }

                if (entities_from_file.size() >= maxCount) {
                    entityToJson(entities_from_file, filePath);
                    entities_from_file = new ArrayList<Entity>();
                    fileCount++;
                    filePath = path + "\\data" + fileCount + ".json";
                }
                entities_from_file.add(entity);
            }
            entities.addAll(list);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean save(int id, String name, Map<Object, Object> fields) {

        return super.save(id, name, fields);
    }

    public List<Entity> loadAll() {
        int count = 1;
        entities.clear();
        do {
            String currPath = path + "\\data" + count + ".json";
            List<Entity> entityList = fileToEntityAll(currPath);
            if (entityList == null || entityList.size() == 0) {
                if (takenId.size() != 0) {
                    Collections.sort(takenId);
                    currId = takenId.get(takenId.size() - 1) + 1;
                }
                return entities;
            }

            for (Entity entity : entityList) {
                takenId.add(entity.getId());
                if (entity.getFields() != null) {
                    Set<Object> keys = entity.getFields().keySet();
                    for (Object key : keys) {
                        if (entity.getFields().get(key) instanceof Entity) {
                            Entity inner = (Entity) entity.getFields().get(key);
                            if (takenId.contains(inner.getId())) {
                                inner.setId(currId++);
                            }
                            takenId.add(inner.getId());
                        }
                    }
                }
            }
            entities.addAll(entityList);
            count++;
        } while (true);
    }

    public void update(Entity entity) {
        int count = 1;
        try {
            do {
                String currPath = path + "\\data" + count + ".json";
                List<Entity> entityList = fileToEntityAll(currPath);
                if (entityList == null || entityList.size() == 0) {
                    return;
                }
                for (int j = 0; j < entityList.size(); j++) {
                    if (entityList.get(j).getId() == entity.getId()) {
                        entityList.get(j).setFields(entity.getFields());
                        entityList.get(j).setName(entity.getName());
                        entityToJson(entityList, currPath);

                        for (Entity entity1 : entities) {
                            if (entity1.getId() == entity.getId()) {
                                entity1.setName(entity.getName());
                                entity1.setFields(entity.getFields());
                            }
                        }
                    }
                }
                count++;
            } while (true);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void update(List<Entity> list) {
        for (Entity e : entities) {
            for (Entity ee : list) {
                if (e.getId() == ee.getId()) {
                    e.setName(ee.getName());
                    e.setFields(ee.getFields());
                }
            }
        }
        saveAll(entities);
    }

    @Override
    public void update(int id, String newName) {
        int count = 1;
        try {
            do {
                String currPath = path + "\\data" + count + ".json";
                List<Entity> entityList = fileToEntityAll(currPath);
                if (entityList == null || entityList.size() == 0) {
                    return;
                }
                for (int j = 0; j < entityList.size(); j++) {
                    if (entityList.get(j).getId() == id) {
                        entityList.get(j).setName(newName);
                        entityToJson(entityList, currPath);

                        for (Entity entity : entities) {
                            if (entity.getId() == entityList.get(j).getId()) {
                                entity.setName(newName);
                                break;
                            }
                        }

                        return;
                    }
                }
                count++;
            } while (true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(int id, String newName, Map<Object, Object> addFields) {
        int count = 1;
        List<Integer> oldTakenId = takenId;
        try {
            do {
                String currPath = path + "\\data" + count + ".json";
                List<Entity> entityList = fileToEntityAll(currPath);
                if (entityList == null || entityList.size() == 0) {
                    return;
                }
                for (int j = 0; j < entityList.size(); j++) {
                    if (entityList.get(j).getId() == id) {
                        entityList.get(j).setName(newName);

                        Map<Object, Object> fields = entityList.get(j).getFields();
                        fields.putAll(addFields);
                        for (Object value : fields.values()) {
                            if (value instanceof Entity) {
                                Entity toAdd = (Entity) value;
                                if (generateId) {
                                    toAdd.setId(currId++);
                                } else if (checkID(toAdd) == false) {
                                    takenId = oldTakenId;
                                    return;
                                }
                                takenId.add(toAdd.getId());
                            }
                        }
                        entityList.get(j).setFields(fields);
                        entityToJson(entityList, currPath);

                        for (Entity entity : entities) {
                            if (entity.getId() == entityList.get(j).getId()) {
                                entity.setName(newName);
                                entity.setFields(fields);
                                break;
                            }
                        }
                        return;
                    }
                }
                count++;
            } while (true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(int id, Map<Object, Object> addFields) {
        int count = 1;
        List<Integer> oldTakenId = takenId;
        try {
            do {
                String currPath = path + "\\data" + count + ".json";
                List<Entity> entityList = fileToEntityAll(currPath);
                if (entityList == null || entityList.size() == 0) {
                    return;
                }
                for (int j = 0; j < entityList.size(); j++) {
                    if (entityList.get(j).getId() == id) {

                        Map<Object, Object> fields = entityList.get(j).getFields();
                        fields.putAll(addFields);
                        for (Object value : fields.values()) {
                            if (value instanceof Entity) {
                                Entity toAdd = (Entity) value;
                                if (generateId) {
                                    toAdd.setId(currId++);
                                } else if (checkID(toAdd) == false) {
                                    takenId = oldTakenId;
                                    return;
                                }
                                takenId.add(toAdd.getId());
                            }
                        }
                        entityList.get(j).setFields(fields);
                        entityToJson(entityList, currPath);

                        for (Entity entity : entities) {
                            if (entity.getId() == entityList.get(j).getId()) {
                                entity.setFields(fields);
                                break;
                            }
                        }
                        return;
                    }
                }
                count++;
            } while (true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(int i, Object o, Object o1) {
        int count = 1;
        try {
            do {
                String currPath = path + "\\data" + count + ".json";
                List<Entity> entityList = fileToEntityAll(currPath);
                if (entityList == null || entityList.size() == 0) {
                    return;
                }
                for (int j = 0; j < entityList.size(); j++) {
                    if (entityList.get(j).getId() == i) {

                        Map<Object, Object> fields = entityList.get(j).getFields();
                        fields.put(o, o1);
                        if (o1 instanceof Entity) {
                            Entity toAdd = (Entity) o1;
                            if (generateId) {
                                toAdd.setId(currId++);
                            } else if (checkID(toAdd) == false) {
                                return; //error
                            }
                            takenId.add(toAdd.getId());
                        }
                        entityList.get(j).setFields(fields);
                        entityToJson(entityList, currPath);

                        for (Entity entity : entities) {
                            if (entity.getId() == entityList.get(j).getId()) {
                                entity.setFields(fields);
                                break;
                            }
                        }
                        return;
                    }
                }
                count++;
            } while (true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(String s, String s1, Map<Object, Object> map) {
        int count = 1;
        try {
            do {
                String currPath = path + "\\data" + count + ".json";
                List<Entity> entityList = fileToEntityAll(currPath);
                if (entityList == null || entityList.size() == 0) {
                    return;
                }
                for (int j = 0; j < entityList.size(); j++) {
                    if (entityList.get(j).getName().equals(s)) {
                        entityList.get(j).setName(s1);

                        Map<Object, Object> fields = entityList.get(j).getFields();
                        fields.putAll(map);
                        for (Object value : map.values()) {
                            if (value instanceof Entity) {
                                Entity toAdd = (Entity) value;
                                if (generateId) {
                                    toAdd.setId(currId++);
                                } else {
                                    return;
                                }
                                takenId.add(toAdd.getId());
                            }
                        }
                        entityList.get(j).setFields(fields);
                        entityToJson(entityList, currPath);

                        for (Entity entity : entities) {
                            if (entity.getId() == entityList.get(j).getId()) {
                                entity.setName(s1);
                                entity.setFields(fields);
                            }
                        }
                    }
                }
                count++;
            } while (true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(String s, Object o, Object o1) {
        int count = 1;
        try {
            do {
                String currPath = path + "\\data" + count + ".json";
                List<Entity> entityList = fileToEntityAll(currPath);
                if (entityList == null || entityList.size() == 0) {
                    return;
                }
                for (int j = 0; j < entityList.size(); j++) {
                    if (entityList.get(j).getName().equals(s)) {

                        Map<Object, Object> fields = entityList.get(j).getFields();
                        fields.put(o, o1);
                        if (o1 instanceof Entity) {
                            Entity toAdd = (Entity) o1;
                            if (generateId) {
                                toAdd.setId(currId++);
                            } else if (checkID(toAdd) == false) {
                                return; //error
                            }
                            takenId.add(toAdd.getId());
                        }
                        entityList.get(j).setFields(fields);
                        entityToJson(entityList, currPath);

                        for (Entity entity : entities) {
                            if (entity.getId() == entityList.get(j).getId()) {
                                entity.setFields(fields);
                                break;
                            }
                        }
                    }
                }
                count++;
            } while (true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(int i, Set<Object> keysToRemove) {
        int count = 1;
        try {
            do {
                String currPath = path + "\\data" + count + ".json";
                List<Entity> entityList = fileToEntityAll(currPath);
                if (entityList == null || entityList.size() == 0) {
                    return;
                }
                for (int j = 0; j < entityList.size(); j++) {
                    if (entityList.get(j).getId() == i) {

                        Map<Object, Object> fields = entityList.get(j).getFields();
                        if (fields != null) {
                            for (Object key : keysToRemove) {
                                if (fields.get(key) instanceof Entity) {
                                    removeID((Integer) fields.get(key));
                                }
                                fields.remove(key);
                            }
                        }
                        entityList.get(j).setFields(fields);
                        entityToJson(entityList, currPath);

                        for (Entity entity : entities) {
                            if (entity.getId() == entityList.get(j).getId()) {
                                entity.setFields(fields);
                                break;
                            }
                        }
                    }
                }
                count++;
            } while (true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void remove(int id) {
        int count = 1;
        try {
            do {
                String currPath = path + "\\data" + count + ".json";
                List<Entity> entityList = fileToEntityAll(currPath);
                if (entityList == null || entityList.size() == 0) {
                    return;
                }
                for (Entity entity : entityList) {
                    if (entity.getId() == id) {
                        removeEntityFromEntities(id);
                        removeID(id);
                        saveAll(entities);
                        return;
                    }
                }
                count++;
            } while (true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void remove(Entity entity) {
        int count = 1;
        try {
            do {
                String currPath = path + "\\data" + count + ".json";
                List<Entity> entityList = fileToEntityAll(currPath);
                if (entityList == null || entityList.size() == 0) {
                    return;
                }
                for (int j = 0; j < entityList.size(); j++) {
                    if (entityList.get(j).getId() == entity.getId()) {
                        if (entityList.size() == 1) {
                            fileCount--;
                            if (fileCount == 0)
                                fileCount = 1;
                            File file = new File(currPath);
                            file.delete();
                        }
                        removeEntityFromEntities(entity.getId());
                        removeID(entity);
                        saveAll(entities);
                        return;
                    }
                }
                count++;
            } while (true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void remove(List<Entity> list) {
        int count = 1;
        try {
            do {
                String currPath = path + "\\data" + count + ".json";
                List<Entity> entityList = fileToEntityAll(currPath);
                if (entityList == null || entityList.size() == 0) {
                    saveAll(entities);
                    return;
                }
                for (Entity value : entityList) {
                    for (Entity entity : list) {
                        if (value.equals(entity)) {
                            removeEntityFromEntities(entity.getId());
                            removeID(entity);
                        }
                    }
                }
                count++;
            } while (true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void remove(String s, Object o, Object o1) {
        int count = 1;
        do {
            String currPath = path + "\\data" + count + ".json";
            List<Entity> entityList = fileToEntityAll(currPath);
            if (entityList == null || entityList.size() == 0) {
                saveAll(entities);
                return;
            }
            for (Entity entity : entityList) {
                if (entity.getName().equals(s)) {
                    if (entity.getFields() != null && entity.getFields().get(o) != null &&
                            entity.getFields().get(o).equals(o1)) {
                        removeID(entity);
                        removeEntityFromEntities(entity.getId());
                    }
                }
            }
            count++;
        } while (true);
    }

    @Override
    public Entity search(int id) {
        int count = 1;
        do {
            String currPath = path + "\\data" + count + ".json";
            List<Entity> entityList = fileToEntityAll(currPath);

            if (entityList == null || entityList.size() == 0) {
                return null;
            }
            for (Entity entity : entityList) {
                if (entity.getId() == id) {
                    return entity;
                }
            }
            count++;
        } while (true);
    }

    @Override
    public List<Entity> search(String s, boolean sort, boolean isSortByNameOrId, boolean asc) {
        int count = 1;
        List<Entity> toReturn = new ArrayList<>();
        do {
            String currPath = path + "\\data" + count + ".json";
            List<Entity> entityList = fileToEntityAll(currPath);
            if (entityList == null || entityList.size() == 0) {
                if (sort) {
                    toReturn = sort(toReturn, isSortByNameOrId, asc);
                }
                return toReturn;
            }
            for (int j = 0; j < entityList.size(); j++) {
                if (entityList.get(j).getName().equals(s)) {
                    toReturn.add(entityList.get(j));
                }
            }
            count++;
        } while (true);
    }

    @Override
    public List<Entity> search(Object o, boolean sort, boolean isSortByNameOrId, boolean asc) {
        int count = 1;
        List<Entity> toReturn = new ArrayList<>();
        do {
            String currPath = path + "\\data" + count + ".json";
            List<Entity> entityList = fileToEntityAll(currPath);
            if (entityList == null || entityList.size() == 0) {
                if (sort) {
                    toReturn = sort(toReturn, isSortByNameOrId, asc);
                }
                return toReturn;
            }
            for (int j = 0; j < entityList.size(); j++) {
                if (entityList.get(j).getFields() != null && entityList.get(j).getFields().containsKey(o)) {
                    toReturn.add(entityList.get(j));
                }
            }
            count++;
        } while (true);
    }

    @Override
    public List<Entity> search(Map<Object, Object> map, boolean sort, boolean isSortByNameOrId, boolean asc) {
        int count = 1;
        List<Entity> toReturn = new ArrayList<>();
        if (map == null || map.isEmpty()) {
            return null;
        }
        do {
            String currPath = path + "\\data" + count + ".json";
            List<Entity> entityList = fileToEntityAll(currPath);
            if (entityList == null || entityList.size() == 0) {
                if (sort) {
                    toReturn = sort(toReturn, isSortByNameOrId, asc);
                }
                return toReturn;
            }
            for (int j = 0; j < entityList.size(); j++) {
                Map<Object, Object> fields = entityList.get(j).getFields();
                Set<Object> keys = map.keySet();
                boolean isEqual = true;
                for (Object key : keys) {
                    if (fields.get(key) == null || !fields.get(key).equals(map.get(key))) {
                        isEqual = false;
                        break;
                    }
                }
                if (isEqual) {
                    toReturn.add(entityList.get(j));
                }
            }
            count++;
        } while (true);
    }

    @Override
    public List<Entity> search(String s, Map<Object, Object> map, boolean sort, boolean isSortByNameOrId, boolean asc) {
        int count = 1;
        List<Entity> toReturn = new ArrayList<>();
        if (map == null || map.isEmpty()) {
            return null;
        }
        do {
            String currPath = path + "\\data" + count + ".json";
            List<Entity> entityList = fileToEntityAll(currPath);
            if (entityList == null || entityList.size() == 0) {
                if (sort) {
                    toReturn = sort(toReturn, isSortByNameOrId, asc);
                }
                return toReturn;
            }
            for (int j = 0; j < entityList.size(); j++) {
                if (!entityList.get(j).getName().equals(s)) {
                    continue;
                }
                Map<Object, Object> fields = entityList.get(j).getFields();
                Set<Object> keys = map.keySet();
                boolean isEqual = true;
                for (Object key : keys) {
                    if (fields.get(key) == null || !fields.get(key).equals(map.get(key))) {
                        isEqual = false;
                        break;
                    }
                }
                if (isEqual) {
                    toReturn.add(entityList.get(j));
                }
            }
            count++;
        } while (true);
    }

    @Override
    public List<Entity> search(Object field, Object innerKey, Object innerValue, boolean sort, boolean isSortByNameOrId, boolean asc) {
        int count = 1;
        List<Entity> toReturn = new ArrayList<>();
        do {
            String currPath = path + "\\data" + count + ".json";
            List<Entity> entityList = fileToEntityAll(currPath);
            if (entityList == null || entityList.size() == 0) {
                if (sort) {
                    toReturn = sort(toReturn, isSortByNameOrId, asc);
                }
                return toReturn;
            }
            for (int j = 0; j < entityList.size(); j++) {
                Entity entity = entityList.get(j);
                if (entity.getFields() != null && entity.getFields().get(field) instanceof Entity) {
                    Entity inner = (Entity) entity.getFields().get(field);
                    if (inner.getFields() != null && inner.getFields().
                            get(innerKey) != null && inner.getFields().get(innerKey).equals(innerValue)) {
                        toReturn.add(entity);
                    }
                }
            }
            count++;
        } while (true);
    }

    @Override
    public List<Entity> search(Object field, String innerName, boolean sort, boolean isSortByNameOrId, boolean asc) {
        int count = 1;
        List<Entity> toReturn = new ArrayList<>();
        do {
            String currPath = path + "\\data" + count + ".json";
            List<Entity> entityList = fileToEntityAll(currPath);
            if (entityList == null || entityList.size() == 0) {
                if (sort) {
                    toReturn = sort(toReturn, isSortByNameOrId, asc);
                }
                return toReturn;
            }
            for (int j = 0; j < entityList.size(); j++) {
                Entity entity = entityList.get(j);
                if (entity.getFields() != null && entity.getFields().get(field) instanceof Entity) {
                    Entity inner = (Entity) entity.getFields().get(field);
                    if (inner.getName().equals(innerName)) {
                        toReturn.add(entity);
                    }
                }
            }
            count++;
        } while (true);
    }

    public List<Entity> search(String name, Object fieldKey, char startsWith, boolean sort, boolean isSortByNameOrId, boolean asc) {
        int count = 1;
        List<Entity> toReturn = new ArrayList<>();
        do {
            String currPath = path + "\\data" + count + ".json";
            List<Entity> entityList = fileToEntityAll(currPath);
            if (entityList == null || entityList.size() == 0) {
                if (sort) {
                    toReturn = sort(toReturn, isSortByNameOrId, asc);
                }
                return toReturn;
            }
            for (int j = 0; j < entityList.size(); j++) {
                Entity entity = entityList.get(j);
                if (entity.getFields() != null && entity.getFields().get(fieldKey) != null &&
                        entity.getFields().get(fieldKey) instanceof String) {
                    String value = (String) entity.getFields().get(fieldKey);
                    if (!value.isEmpty() && value.charAt(0) == startsWith) {
                        toReturn.add(entity);
                    }
                }
            }
            count++;
        } while (true);
    }

    public List<Entity> search(String name, Object field, Object innerKey, Object innerValue, boolean sort, boolean isSortByNameOrId, boolean asc) {
        int count = 1;
        List<Entity> toReturn = new ArrayList<>();
        do {
            String currPath = path + "\\data" + count + ".json";
            List<Entity> entityList = fileToEntityAll(currPath);
            if (entityList == null || entityList.size() == 0) {
                if (sort) {
                    toReturn = sort(toReturn, isSortByNameOrId, asc);
                }
                return toReturn;
            }
            for (int j = 0; j < entityList.size(); j++) {
                Entity entity = entityList.get(j);
                if (!entity.getName().equals(name)) {
                    continue;
                }
                if (entity.getFields() != null && entity.getFields().get(field) instanceof Entity) {
                    Entity inner = (Entity) entity.getFields().get(field);
                    if (inner.getFields() != null && inner.getFields().
                            get(innerKey) != null && inner.getFields().get(innerKey).equals(innerValue)) {
                        toReturn.add(entity);
                    }
                }
            }
            count++;
        } while (true);
    }

    public List<Entity> sort(List<Entity> toSort, boolean isSortByNameOrId, boolean asc) {
        if (isSortByNameOrId) {
            if (asc) {
                Collections.sort(toSort, Entity.EntNameComparatorAsc);
            } else {
                Collections.sort(toSort, Entity.EntNameComparatorDsc);
            }
        } else {
            if (asc) {
                Collections.sort(toSort, Entity.EntIdComparatorAsc);
            } else {
                Collections.sort(toSort, Entity.EntIdComparatorDsc);

            }
        }
        return toSort;
    }


    public boolean entityToJson(Object entities, String path) {
        try {
            String json = new GsonBuilder().setPrettyPrinting().create().toJson(entities);
            jsonToFile(path, json);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private void jsonToFile(String path, String entity_data) {
        File file = new File("");
        file = new File(path);
        file.setWritable(true);

        try (FileOutputStream fos = new FileOutputStream(file);
             BufferedOutputStream bos = new BufferedOutputStream(fos)) {
            byte[] bytes = entity_data.getBytes();
            bos.write(bytes);
            bos.close();
            fos.close();
            System.out.print("Data written to file successfully.\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Entity> fileToEntityAll(String path) {
        try {
            File f = new File(path);
            if (f.exists()) {
                JsonReader reader = new JsonReader(new FileReader(path));
                Gson gson = new Gson();
                Type listType = new TypeToken<ArrayList<Entity>>() {
                }.getType();
                ArrayList<Entity> entities = gson.fromJson(reader, listType);
                return entities;
            } else {
                return null;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void saveAll(List<Entity> entityList) {
        int count = 1;
        String currPath = path + "\\data" + count + ".json";
        List<Entity> toSave = new ArrayList<>();
        try {
            for (Entity entity : entityList) {
                toSave.add(entity);
                if (toSave.size() == maxCount) {
                    entityToJson(toSave, currPath);
                    count++;
                    currPath = path + "\\data" + count + ".json";
                    toSave.clear();
                }
            }
            if (toSave.size() != 0) {
                entityToJson(toSave, currPath);
            }
            while (fileCount > count) {
                File file = new File(path + "\\data" + fileCount + ".json");
                file.delete();
                fileCount--;
            }
            fileCount = count;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected boolean checkID(Entity entity) {
        if (takenId.contains(entity.getId())) {
            return false;
        }
        Map<Object, Object> fields = entity.getFields();
        for (Object o : fields.keySet()) {
            if (fields.get(o) instanceof Entity) {
                return checkID((Entity) fields.get(o));
            }
        }
        return true;
    }

    private void removeEntityFromEntities(int id) {
        Iterator iterator = entities.iterator();
        while (iterator.hasNext()) {
            Entity entity = (Entity) iterator.next();
            if (entity.getId() == id) {
                iterator.remove();
                return;
            }
        }
    }


}
