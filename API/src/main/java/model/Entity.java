package model;

import java.lang.reflect.Field;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Entity {

    private int id;
    private String name;
    private Map<Object, Object> fields;

    public Entity() {
        fields = new HashMap<Object, Object>();
    }

    public Entity(int id, String name) {
        this.id = id;
        this.name = name;
        fields = new HashMap<Object, Object>();
    }

    public Entity(int id, String name, Map<Object, Object> fields) {
        /*
        Field[] objFields = object.getClass().getFields();
        for (Field objField : objFields) {
            fields.put(objField.getName(), objField.get(object));
        }
        */
        this.fields = fields;
        this.id = id;
        this.name = name;
    }

    public void addField(Object key, Object value) {
        fields.put(key, value);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<Object, Object> getFields() {
        return fields;
    }

    public void setFields(Map<Object, Object> fields) {
        this.fields = fields;
    }

    public static Comparator<Entity> EntIdComparatorAsc = new Comparator<Entity>() {
        @Override
        public int compare(Entity o1, Entity o2) {
            int id1 = o1.getId();
            int id2 = o2.getId();

            return id1 - id2;
        }
    };

    public static Comparator<Entity> EntIdComparatorDsc = new Comparator<Entity>() {
        @Override
        public int compare(Entity o1, Entity o2) {
            int id1 = o1.getId();
            int id2 = o2.getId();

            return id2 - id1;
        }
    };

    public static Comparator<Entity> EntNameComparatorAsc = new Comparator<Entity>() {
        @Override
        public int compare(Entity o1, Entity o2) {
            String name1 = o1.getName().toUpperCase();
            String name2 = o2.getName().toUpperCase();

            return name1.compareTo(name2);
        }
    };

    public static Comparator<Entity> EntNameComparatorDsc = new Comparator<Entity>() {
        @Override
        public int compare(Entity o1, Entity o2) {
            String name1 = o1.getName().toUpperCase();
            String name2 = o2.getName().toUpperCase();

            return name2.compareTo(name1);
        }
    };

    @Override
    public String toString() {
        return "Entity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", fields=" + fields +
                '}';
    }

}
