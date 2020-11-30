
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import model.Entity;
import storage.Storage;
import storage.StorageManager;

import java.io.*;
import java.util.*;

public class YAMLimpl extends Storage {

    private int fileCount = 1;
    private String filePath;
    private List<Entity> entities;

    public YAMLimpl(){
        entities = new ArrayList<Entity>();
    }

    static {
        StorageManager.registerStorage(new YAMLimpl());
    }

    public boolean save(Entity entity) {
        if(generateId){
            entity.setId(currId++);
            takenId.add(currId - 1);
            if(entity.getFields() != null){
                Set<Object> keys = entity.getFields().keySet();
                for (Object key : keys) {
                    if (entity.getFields().get(key) instanceof Entity){
                        Entity inner = (Entity) entity.getFields().get(key);
                        inner.setId(currId++);
                        takenId.add(currId - 1);
                    }
                }
            }
        }else{
            if(checkID(entity) == false){
                return false;
            }
        }

        if(filePath == null){
            filePath = path + "\\data" + fileCount + ".yml";
        }

        entities.add(entity);
        try {
            List<Entity> entityList = readFileToEntityList(filePath);
            if (entityList == null){
                entityList = new ArrayList<>();
            }
            if(entityList.size() >= maxCount){
                entityList = new ArrayList<>();
                fileCount++;
                filePath = path + "\\data" + fileCount + ".yml";
            }
            entityList.add(entity);
            writeEntityListToFile(entityList, filePath);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean save(List<Entity> list) {
        List<Integer> oldTakenId = takenId;
        try {
            List<Entity> entityList = readFileToEntityList(filePath);
            for (Entity entity : list) {
                if(!generateId && !checkID(entity)){
                    takenId = oldTakenId;
                    saveAll(entities);
                    //ako nije uspeo da snimi sve vraca na staro
                    return false;
                }
                if (generateId) {
                    entity.setId(currId++);
                    takenId.add(currId - 1);
                    if(entity.getFields() != null){
                        Set<Object> keys = entity.getFields().keySet();
                        for (Object key : keys) {
                            if (entity.getFields().get(key) instanceof Entity){
                                Entity inner = (Entity) entity.getFields().get(key);
                                inner.setId(currId++);
                                takenId.add(currId - 1);
                            }
                        }
                    }
                }
                if (entityList.size() >= maxCount) {
                    writeEntityListToFile(entityList, filePath);
                    entityList = new ArrayList<Entity>();
                    fileCount++;
                    filePath = path + "\\data" + fileCount + ".yml";
                }
                entityList.add(entity);
            }
            entities.addAll(list);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean writeEntityListToFile(List<Entity> entityList, String path) throws Exception {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory().disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER));
        String yaml = mapper.writeValueAsString(entityList);
        stringToFile(path, yaml);
        return true;
    }

    private void stringToFile(String destinationPath, String data) {
        File file = new File(destinationPath);
        file.setWritable(true);
        try (FileOutputStream fos = new FileOutputStream(file);
             BufferedOutputStream bos = new BufferedOutputStream(fos)) {
            byte[] bytes = data.getBytes();
            bos.write(bytes);
            bos.close();
            fos.close();
            System.out.print("Data written to file successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private List<Entity> readFileToEntityList(String path) throws IOException{
        if(! new File(path).exists()){
            return null;
        }
        ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory().disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER));
        List<Entity> toReturn = objectMapper.readValue(new File(path), new TypeReference<List<Entity>>() {
        });
        return toReturn;
    }

    public List<Entity> loadAll() {
        int count = 1;
        entities.clear();
        try {
            do {
                String currPath = path + "\\data" + count + ".yml";
                List<Entity> entityList = readFileToEntityList(currPath);
                if (entityList == null || entityList.size() == 0){
                    if(takenId.size() != 0) {
                        Collections.sort(takenId);
                        currId = takenId.get(takenId.size() - 1) + 1;
                    }
                    return entities;
                }
                for (Entity entity : entityList) {
                    takenId.add(entity.getId());
                    if(entity.getFields() != null){
                        Set<Object> keys = entity.getFields().keySet();
                        for (Object key : keys) {
                            if (entity.getFields().get(key) instanceof Entity){
                                Entity inner = (Entity) entity.getFields().get(key);
                                if(takenId.contains(inner.getId())){
                                    inner.setId(currId++);
                                }
                                takenId.add(inner.getId());
                            }
                        }
                    }
                }
                entities.addAll(entityList);
                count++; //menja trenutni fajl 
            }while(true);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    //koristi se kada se uklone neki elementi iz liste da bi se popunili fajlovi do maxcount-a
    private void saveAll(List<Entity> entityList){
        int count = 1;
        String currPath = path + "\\data" + count + ".yml";
        List<Entity> toSave = new ArrayList<>();
        try {
            for (Entity entity : entityList) {
                toSave.add(entity);
                if (toSave.size() == maxCount) {
                    writeEntityListToFile(toSave, currPath);
                    count++;
                    currPath = path + "\\data" + count + ".yml";
                    toSave.clear();
                }
            }
            if(toSave.size() != 0){
                writeEntityListToFile(toSave, currPath);
            }
            while(fileCount > count){
                File file = new File(path + "\\data" + fileCount + ".yml");
                file.delete();
                fileCount--;
            }
            fileCount = count;
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //sklanja element iz liste entities i onda ponovo snima sve fajlove
    public void remove(int i) {
        int count = 1;
        try {
            do {
                String currPath = path + "\\data" + count + ".yml";
                List<Entity> entityList = readFileToEntityList(currPath);
                if (entityList == null || entityList.size() == 0){
                    return;
                }
                for(int j = 0; j < entityList.size(); j++){
                    if(entityList.get(j).getId() == i){
                        removeEntityFromEntities(i);
                        removeID(i);
                        saveAll(entities);
                        return;
                    }
                }
                count++; //menja trenutni fajl
            }while(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void remove(Entity entity) {
        int count = 1;
        try {
            do {
                String currPath = path + "\\data" + count + ".yml";
                List<Entity> entityList = readFileToEntityList(currPath);
                if (entityList == null || entityList.size() == 0){
                    return;
                }
                for(int j = 0; j < entityList.size(); j++){
                    if(entityList.get(j).getId() == entity.getId()){
                        if(entityList.size() == 1) {
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
                count++; //menja trenutni fajl
            }while(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void remove(List<Entity> list) {
        int count = 1;
        try {
            do {
                String currPath = path + "\\data" + count + ".yml";
                List<Entity> entityList = readFileToEntityList(currPath);
                if (entityList == null || entityList.size() == 0){
                    saveAll(entities);
                    return;
                }
                for(int j = 0; j < entityList.size(); j++){
                    for (Entity entity : list) {
                        if(entityList.get(j).equals(entity)){
                            removeEntityFromEntities(entity.getId());
                            removeID(entity);
                        }
                    }
                }
                count++; //menja trenutni fajl
            }while(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void remove(String s, Object o, Object o1) {
        int count = 1;
        try {
            do {
                String currPath = path + "\\data" + count + ".yml";
                List<Entity> entityList = readFileToEntityList(currPath);
                if (entityList == null || entityList.size() == 0){
                    saveAll(entities);
                    return;
                }
                for(int j = 0; j < entityList.size(); j++){
                    Entity entity = entityList.get(j);
                    if(entity.getName().equals(s)){
                        if(entity.getFields() != null && entity.getFields().get(o) != null &&
                        entity.getFields().get(o).equals(o1)){
                            removeID(entity);
                            removeEntityFromEntities(entity.getId());
                        }
                    }
                }
                count++;
            }while(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void removeEntityFromEntities(int id){
        Iterator iterator = entities.iterator();
        while (iterator.hasNext()){
            Entity entity = (Entity) iterator.next();
            if (entity.getId() == id){
                iterator.remove();
                return;
            }
        }
    }

    public void update(Entity entity) {
        int count = 1;
        try {
            do {
                String currPath = path + "\\data" + count + ".yml";
                List<Entity> entityList = readFileToEntityList(currPath);
                if (entityList == null || entityList.size() == 0){
                    return;
                }
                for(int j = 0; j < entityList.size(); j++){
                    if(entityList.get(j).getId() == entity.getId()){
                        entityList.get(j).setFields(entity.getFields());
                        entityList.get(j).setName(entity.getName());
                        writeEntityListToFile(entityList, currPath);

                        for (Entity entity1 : entities) {
                            if(entity1.getId() == entity.getId()){
                                entity1.setName(entity.getName());
                                entity1.setFields(entity.getFields());
                            }
                        }
                    }
                }
                count++;
            }while(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void update(List<Entity> list) {
        int count = 1;
        try {
            do {
                String currPath = path + "\\data" + count + ".yml";
                List<Entity> entityList = readFileToEntityList(currPath);
                if (entityList == null || entityList.size() == 0){
                    return;
                }
                for(int j = 0; j < entityList.size(); j++){
                    if(list == null || list.size() == 0){
                        return;
                    }
                    Iterator<Entity> i = list.iterator();
                    while(i.hasNext()){
                        Entity entity = i.next();
                        if(entity.getId() == entityList.get(j).getId()){
                            entityList.get(j).setFields(entity.getFields());
                            entityList.get(j).setName(entity.getName());
                            writeEntityListToFile(entityList, currPath);
                            i.remove();

                            for (Entity entity1 : entities) {
                                if(entity1.getId() == entity.getId()){
                                    entity1.setName(entity.getName());
                                    entity1.setFields(entity.getFields());
                                }
                            }
                        }
                    }
                }
                count++; //menja trenutni fajl
            }while(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void update(int i, String s) {
        int count = 1;
        try {
            do {
                String currPath = path + "\\data" + count + ".yml";
                List<Entity> entityList = readFileToEntityList(currPath);
                if (entityList == null || entityList.size() == 0){
                    return;
                }
                for(int j = 0; j < entityList.size(); j++){
                    if(entityList.get(j).getId() == i){
                        entityList.get(j).setName(s);
                        writeEntityListToFile(entityList, currPath);

                        for (Entity entity : entities) {
                            if(entity.getId() == entityList.get(j).getId()){
                                entity.setName(s);
                                break;
                            }
                        }

                        return;
                    }
                }
                count++;
            }while(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void update(int i, String s, Map<Object, Object> map) {
        int count = 1;
        List<Integer> oldTakenId = takenId;
        try {
            do {
                String currPath = path + "\\data" + count + ".yml";
                List<Entity> entityList = readFileToEntityList(currPath);
                if (entityList == null || entityList.size() == 0){
                    return;
                }
                for(int j = 0; j < entityList.size(); j++){
                    if(entityList.get(j).getId() == i){
                        entityList.get(j).setName(s);

                        Map<Object, Object> fields = entityList.get(j).getFields();
                        fields.putAll(map);
                        for (Object value : fields.values()) {
                            if (value instanceof Entity){
                                Entity toAdd = (Entity) value;
                                if (generateId) {
                                    toAdd.setId(currId++);
                                }else if(checkID(toAdd) == false){
                                    takenId = oldTakenId;
                                    return; //error
                                }
                                takenId.add(toAdd.getId());
                            }
                        }
                        entityList.get(j).setFields(fields);
                        writeEntityListToFile(entityList, currPath);

                        for (Entity entity : entities) {
                            if(entity.getId() == entityList.get(j).getId()){
                                entity.setName(s);
                                entity.setFields(fields);
                                break;
                            }
                        }

                        return;
                    }
                }
                count++; //menja trenutni fajl
            }while(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void update(int i, Map<Object, Object> map) {
        int count = 1;
        List<Integer> oldTakenId = takenId;
        try {
            do {
                String currPath = path + "\\data" + count + ".yml";
                List<Entity> entityList = readFileToEntityList(currPath);
                if (entityList == null || entityList.size() == 0){
                    return;
                }
                for(int j = 0; j < entityList.size(); j++){
                    if(entityList.get(j).getId() == i){

                        Map<Object, Object> fields = entityList.get(j).getFields();
                        fields.putAll(map);
                        for (Object value : fields.values()) {
                            if (value instanceof Entity){
                                Entity toAdd = (Entity) value;
                                if (generateId) {
                                    toAdd.setId(currId++);
                                }else if(checkID(toAdd) == false){
                                    takenId = oldTakenId;
                                    return; //error
                                }
                                takenId.add(toAdd.getId());
                            }
                        }
                        entityList.get(j).setFields(fields);
                        writeEntityListToFile(entityList, currPath);

                        for (Entity entity : entities) {
                            if(entity.getId() == entityList.get(j).getId()){
                                entity.setFields(fields);
                                break;
                            }
                        }
                        return;
                    }
                }
                count++; //menja trenutni fajl
            }while(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void update(int i, Object o, Object o1) {
        int count = 1;
        try {
            do {
                String currPath = path + "\\data" + count + ".yml";
                List<Entity> entityList = readFileToEntityList(currPath);
                if (entityList == null || entityList.size() == 0){
                    return;
                }
                for(int j = 0; j < entityList.size(); j++){
                    if(entityList.get(j).getId() == i){

                        Map<Object, Object> fields = entityList.get(j).getFields();
                        fields.put(o, o1);
                        if (o1 instanceof Entity){
                            Entity toAdd = (Entity) o1;
                            if (generateId) {
                                toAdd.setId(currId++);
                            }else if(checkID(toAdd) == false){
                                return; //error
                            }
                            takenId.add(toAdd.getId());
                        }
                        entityList.get(j).setFields(fields);
                        writeEntityListToFile(entityList, currPath);

                        for (Entity entity : entities) {
                            if(entity.getId() == entityList.get(j).getId()){
                                entity.setFields(fields);
                                break;
                            }
                        }
                        return;
                    }
                }
                count++; //menja trenutni fajl
            }while(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void update(String s, String s1, Map<Object, Object> map) {
        int count = 1;
        try {
            do {
                String currPath = path + "\\data" + count + ".yml";
                List<Entity> entityList = readFileToEntityList(currPath);
                if (entityList == null || entityList.size() == 0){
                    return;
                }
                for(int j = 0; j < entityList.size(); j++){
                    if(entityList.get(j).getName().equals(s)){
                        entityList.get(j).setName(s1);

                        Map<Object, Object> fields = entityList.get(j).getFields();
                        fields.putAll(map);
                        for (Object value : map.values()) {
                            if (value instanceof Entity){
                                Entity toAdd = (Entity) value;
                                if (generateId) {
                                    toAdd.setId(currId++);
                                }else{
                                    return; //error zato sto ce u vise entiteta da bude isti id za ugnjezdeni entitet
                                }
                                takenId.add(toAdd.getId());
                            }
                        }
                        entityList.get(j).setFields(fields);
                        writeEntityListToFile(entityList, currPath);

                        for (Entity entity : entities) {
                            if(entity.getId() == entityList.get(j).getId()){
                                entity.setName(s1);
                                entity.setFields(fields);
                            }
                        }
                    }
                }
                count++; //menja trenutni fajl
            }while(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void update(String s, Object o, Object o1) {
        int count = 1;
        try {
            do {
                String currPath = path + "\\data" + count + ".yml";
                List<Entity> entityList = readFileToEntityList(currPath);
                if (entityList == null || entityList.size() == 0){
                    return;
                }
                for(int j = 0; j < entityList.size(); j++){
                    if(entityList.get(j).getName().equals(s)){

                        Map<Object, Object> fields = entityList.get(j).getFields();
                        fields.put(o, o1);
                        if (o1 instanceof Entity){
                            Entity toAdd = (Entity) o1;
                            if (generateId) {
                                toAdd.setId(currId++);
                            }else if(checkID(toAdd) == false){
                                return; //error
                            }
                            takenId.add(toAdd.getId());
                        }
                        entityList.get(j).setFields(fields);
                        writeEntityListToFile(entityList, currPath);

                        for (Entity entity : entities) {
                            if(entity.getId() == entityList.get(j).getId()){
                                entity.setFields(fields);
                                break;
                            }
                        }
                    }
                }
                count++;
            }while(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(int i, Set<Object> keysToRemove) {
        int count = 1;
        try {
            do {
                String currPath = path + "\\data" + count + ".yml";
                List<Entity> entityList = readFileToEntityList(currPath);
                if (entityList == null || entityList.size() == 0){
                    return;
                }
                for(int j = 0; j < entityList.size(); j++){
                    if(entityList.get(j).getId() == i){

                        Map<Object, Object> fields = entityList.get(j).getFields();
                        if (fields != null){
                            for (Object key : keysToRemove) {
                                if (fields.get(key) instanceof Entity){
                                    removeID((Integer) fields.get(key));
                                }
                                fields.remove(key);
                            }
                        }
                        entityList.get(j).setFields(fields);
                        writeEntityListToFile(entityList, currPath);

                        for (Entity entity : entities) {
                            if(entity.getId() == entityList.get(j).getId()){
                                entity.setFields(fields);
                                break;
                            }
                        }
                    }
                }
                count++; //menja trenutni fajl
            }while(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Entity search(int i) {
        int count = 1;
        try {
            do {
                String currPath = path + "\\data" + count + ".yml";
                List<Entity> entityList = readFileToEntityList(currPath);
                if (entityList == null || entityList.size() == 0){
                    return null;
                }
                for(int j = 0; j < entityList.size(); j++){
                    if(entityList.get(j).getId() == i){
                        return entityList.get(j);
                    }
                }
                count++; //menja trenutni fajl
            }while(true);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Entity> search(String s, boolean sort, boolean isSortByNameOrId, boolean asc) {
        int count = 1;
        List<Entity> toReturn = new ArrayList<>();
        try {
            do {
                String currPath = path + "\\data" + count + ".yml";
                List<Entity> entityList = readFileToEntityList(currPath);
                if (entityList == null || entityList.size() == 0){
                    if (sort){
                        toReturn = sort(toReturn, isSortByNameOrId, asc);
                    }
                    return toReturn;
                }
                for(int j = 0; j < entityList.size(); j++){
                    if(entityList.get(j).getName().equals(s)){
                        toReturn.add(entityList.get(j));
                    }
                }
                count++; //menja trenutni fajl
            }while(true);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Entity> search(Object o, boolean sort, boolean isSortByNameOrId, boolean asc) {
        int count = 1;
        List<Entity> toReturn = new ArrayList<>();
        try {
            do {
                String currPath = path + "\\data" + count + ".yml";
                List<Entity> entityList = readFileToEntityList(currPath);
                if (entityList == null || entityList.size() == 0){
                    if (sort){
                        toReturn = sort(toReturn, isSortByNameOrId, asc);
                    }
                    return toReturn;
                }
                for(int j = 0; j < entityList.size(); j++){
                    if(entityList.get(j).getFields() != null && entityList.get(j).getFields().containsKey(o)){
                        toReturn.add(entityList.get(j));
                    }
                }
                count++; //menja trenutni fajl
            }while(true);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Entity> search(Map<Object, Object> map, boolean sort, boolean isSortByNameOrId, boolean asc) {
        int count = 1;
        List<Entity> toReturn = new ArrayList<>();
        if(map == null || map.isEmpty()) {
            return null;
        }
        try {
            do {
                String currPath = path + "\\data" + count + ".yml";
                List<Entity> entityList = readFileToEntityList(currPath);
                if (entityList == null || entityList.size() == 0){
                    if (sort){
                        toReturn = sort(toReturn, isSortByNameOrId, asc);
                    }
                    return toReturn;
                }
                for(int j = 0; j < entityList.size(); j++){
                    Map<Object, Object> fields = entityList.get(j).getFields();
                    Set<Object> keys = map.keySet();
                    boolean isEqual = true;
                    for (Object key : keys) {
                        if(fields.get(key) == null || !fields.get(key).equals(map.get(key))){
                            isEqual = false;
                            break;
                        }
                    }
                    if(isEqual){
                        toReturn.add(entityList.get(j));
                    }
                }
                count++; //menja trenutni fajl
            }while(true);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Entity> search(String s, Map<Object, Object> map, boolean sort, boolean isSortByNameOrId, boolean asc) {
        int count = 1;
        List<Entity> toReturn = new ArrayList<>();
        if(map == null || map.isEmpty()) {
            return null;
        }
        try {
            do {
                String currPath = path + "\\data" + count + ".yml";
                List<Entity> entityList = readFileToEntityList(currPath);
                if (entityList == null || entityList.size() == 0){
                    if (sort){
                        toReturn = sort(toReturn, isSortByNameOrId, asc);
                    }
                    return toReturn;
                }
                for(int j = 0; j < entityList.size(); j++){
                    if(!entityList.get(j).getName().equals(s)){
                        continue;
                    }
                    Map<Object, Object> fields = entityList.get(j).getFields();
                    Set<Object> keys = map.keySet();
                    boolean isEqual = true;
                    for (Object key : keys) {
                        if(fields.get(key) == null || !fields.get(key).equals(map.get(key))){
                            isEqual = false;
                            break;
                        }
                    }
                    if(isEqual){
                        toReturn.add(entityList.get(j));
                    }
                }
                count++; //menja trenutni fajl
            }while(true);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Entity> search(Object field, Object innerKey, Object innerValue, boolean sort, boolean isSortByNameOrId, boolean asc) {
        int count = 1;
        List<Entity> toReturn = new ArrayList<>();
        try {
            do {
                String currPath = path + "\\data" + count + ".yml";
                List<Entity> entityList = readFileToEntityList(currPath);
                if (entityList == null || entityList.size() == 0){
                    if (sort){
                        toReturn = sort(toReturn, isSortByNameOrId, asc);
                    }
                    return toReturn;
                }
                for(int j = 0; j < entityList.size(); j++){
                    Entity entity = entityList.get(j);
                    if(entity.getFields() != null && entity.getFields().get(field) instanceof Entity){
                        Entity inner = (Entity) entity.getFields().get(field);
                        if (inner.getFields() != null && inner.getFields().
                                get(innerKey) != null && inner.getFields().get(innerKey).equals(innerValue)) {
                            toReturn.add(entity);
                        }
                    }
                }
                count++; //menja trenutni fajl
            }while(true);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Entity> search(Object field, String innerName, boolean sort, boolean isSortByNameOrId, boolean asc) {
        int count = 1;
        List<Entity> toReturn = new ArrayList<>();
        try {
            do {
                String currPath = path + "\\data" + count + ".yml";
                List<Entity> entityList = readFileToEntityList(currPath);
                if (entityList == null || entityList.size() == 0){
                    if (sort){
                        toReturn = sort(toReturn, isSortByNameOrId, asc);
                    }
                    return toReturn;
                }
                for(int j = 0; j < entityList.size(); j++){
                    Entity entity = entityList.get(j);
                    if(entity.getFields() != null && entity.getFields().get(field) instanceof Entity){
                        Entity inner = (Entity) entity.getFields().get(field);
                        if (inner.getName().equals(innerName)) {
                            toReturn.add(entity);
                        }
                    }
                }
                count++; //menja trenutni fajl
            }while(true);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Entity> search(String name, Object fieldKey, char startsWith, boolean sort, boolean isSortByNameOrId, boolean asc) {
        int count = 1;
        List<Entity> toReturn = new ArrayList<>();
        try {
            do {
                String currPath = path + "\\data" + count + ".yml";
                List<Entity> entityList = readFileToEntityList(currPath);
                if (entityList == null || entityList.size() == 0){
                    if (sort){
                        toReturn = sort(toReturn, isSortByNameOrId, asc);
                    }
                    return toReturn;
                }
                for(int j = 0; j < entityList.size(); j++){
                    Entity entity = entityList.get(j);
                    if(entity.getFields() != null && entity.getFields().get(fieldKey) != null &&
                     entity.getFields().get(fieldKey) instanceof String){
                        String value = (String) entity.getFields().get(fieldKey);
                        if (!value.isEmpty() && value.charAt(0) == startsWith){
                            toReturn.add(entity);
                        }
                    }
                }
                count++; //menja trenutni fajl
            }while(true);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Entity> search(String name, Object field, Object innerKey, Object innerValue, boolean sort, boolean isSortByNameOrId, boolean asc) {
        int count = 1;
        List<Entity> toReturn = new ArrayList<>();
        try {
            do {
                String currPath = path + "\\data" + count + ".yml";
                List<Entity> entityList = readFileToEntityList(currPath);
                if (entityList == null || entityList.size() == 0){
                    if (sort){
                        toReturn = sort(toReturn, isSortByNameOrId, asc);
                    }
                    return toReturn;
                }
                for(int j = 0; j < entityList.size(); j++){
                    Entity entity = entityList.get(j);
                    if(!entity.getName().equals(name)){
                        continue;
                    }
                    if(entity.getFields() != null && entity.getFields().get(field) instanceof Entity){
                        Entity inner = (Entity) entity.getFields().get(field);
                        if (inner.getFields() != null && inner.getFields().
                                get(innerKey) != null && inner.getFields().get(innerKey).equals(innerValue)) {
                            toReturn.add(entity);
                        }
                    }
                }
                count++; //menja trenutni fajl
            }while(true);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Entity> sort(List<Entity> toSort, boolean isSortByNameOrId, boolean asc) {
        if(isSortByNameOrId){
            if (asc){
                Collections.sort(toSort, Entity.EntNameComparatorAsc);
            }else{
                Collections.sort(toSort, Entity.EntNameComparatorDsc);
            }
        }else {
            if (asc){
                Collections.sort(toSort, Entity.EntIdComparatorAsc);
            }else {
                Collections.sort(toSort, Entity.EntIdComparatorDsc);

            }
        }
        return toSort;
    }

    @Override
    protected boolean checkID(Entity entity) {
        if(takenId.contains(entity.getId())){
            return false;
        }
        takenId.add(entity.getId());
        Map<Object, Object> fields = entity.getFields();
        for (Object o : fields.keySet()) {
            if(fields.get(o) instanceof Entity) {
                return checkID((Entity) fields.get(o));
            }
        }
        return true;
    }
}
