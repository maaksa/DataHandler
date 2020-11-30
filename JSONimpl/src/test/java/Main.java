import model.Entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {

    public static void main(String[] args) {
        JSONimpl json = new JSONimpl();
        Map<Object, Object> capitalCities = new HashMap<Object, Object>();
        Map<Object, Object> predmeti = new HashMap<Object, Object>();

        capitalCities.put("England", "London");
        capitalCities.put("Germany", "Berlin");
        capitalCities.put("Norway", "Oslo");
        capitalCities.put("USA", "Washington DC");
        capitalCities.put("BG", "Belgrade");

        predmeti.put("UI", "Upravljanje informacijama");
        predmeti.put("SK", "Softverske komponente");
        predmeti.put("UUP", "Uvod u programiranje");
        predmeti.put("OOP", "Objektno orjentisano programiranje");
        predmeti.put("ASP", "Algoritmi i strukture podataka");

        //Entity entity1 = new Entity(1,"Student", capitalCities);
        //Entity entity2 = new Entity(2,"Grad", predmeti);
        //Entity entity3 = new Entity(3,"Student", capitalCities);

        Entity entity2_id = new Entity();
        entity2_id.setId(2);
        entity2_id.setName("Student");
        entity2_id.addField("brIndex", "RN43/18");
        entity2_id.addField("ime", "Milos");
        entity2_id.addField("prezime", "Maksimovic");
        entity2_id.addField("godine", 21);

        Entity entity1_id = new Entity();
        entity1_id.setId(2);
        entity1_id.setName("Student");
        entity1_id.addField("brIndex", "RN43/18");
        entity1_id.addField("ime", "Nikola");
        entity1_id.addField("prezime", "Paunovic");
        entity1_id.addField("godine", 21);

        Entity entity3_id = new Entity();
        entity3_id.setId(3);
        entity3_id.setName("Student");
        entity3_id.addField("brIndex", "RN43/18");
        entity3_id.addField("ime", "Luka");
        entity3_id.addField("prezime", "Lukovic");
        entity3_id.addField("godine", 21);


        //BEZ ID
        Entity entity1 = new Entity();
        entity1.setName("Studijski Program");
        entity1.addField("UI", "Upravljanje informacijama");
        entity1.addField("SK", "Softverske komponente");
        entity1.addField("UUP", "Uvod u programiranje");

        Entity entity2 = new Entity();
        entity2.setName("Studijski Program");
        entity2.addField("UI", "Upravljanje informacijama");
        entity2.addField("SK", "Softverske komponente");
        entity2.addField("UUP", "Uvod u programiranje");
        entity2.addField("student", entity2_id);

        Entity entity3 = new Entity();
        entity3.setName("Student");
        entity3.addField("ime", "Dragoslav");

        Entity entity4 = new Entity();
        entity4.setName("Studijski Program");

        Entity entity5 = new Entity();
        entity5.setName("Student");
        entity5.addField("ime", "Jovan");

        Entity entity6 = new Entity();
        entity6.setName("Student");

        Entity entity7 = new Entity();
        entity7.setName("Student");
        entity7.addField("ime", "Tore");


        //SAVE LIST
        List<Entity> entities_id = new ArrayList<>();
        entities_id.add(entity1_id);
        entities_id.add(entity2_id);
        entities_id.add(entity3_id);

        List<Entity> entities = new ArrayList<>();
        entities.add(entity1);
        entities.add(entity2);
        entities.add(entity3);
        entities.add(entity4);
        entities.add(entity5);
        entities.add(entity6);
        entities.add(entity7);

        List<Entity> entities2 = new ArrayList<>();
        entities2.add(entity1);
        entities2.add(entity2);
        entities2.add(entity3);
        entities2.add(entity4);

        //json.save(entity1);

        //json.save(3, "Student", predmeti);

        //json.save(entities2);

        // SAVE entity, kreira novi file ako se predje maxEntityCount
        //json.save(entity1_id);
        //json.save(entity2_id);
        //json.save(entity3_id);
        //json.save(entity4);
        //json.save(entity5);
        //json.save(entity22);


        //json.save(entities);

        //SAVE id name fields
        //json.save(1, "Student", predmeti);

        //READ name
        //System.out.println(json.load("Student"));

        //READ id
        // System.out.println(json.load(6));

        //READ all
        //System.out.println(json.loadAll());

        //DELETE id
        //json.remove(6);

        //DELETE entity
        //json.remove(entity4);

        //DELETE entities todo ne radi
        //json.remove(entities2);

        //UPDATE entity
        /*Entity ent = new Entity();
        ent.setId(4);
        ent.setName("Student");
        ent.setFields(predmeti);
        json.update(ent);*/

        //UPDATE entities
        /*List<Entity> entities22= new ArrayList<>();
        Entity ent = new Entity();
        ent.setId(4);
        ent.setName("Student");
        ent.setFields(predmeti);
        json.update(ent);
        Entity ent1 = new Entity();
        ent.setId(6);
        ent.setName("Student");
        ent.setFields(predmeti);
        json.update(ent);
        entities.add(ent);
        entities.add(ent1);
        json.update(entities22);*/

        //UPDATE name
        //json.update(4, "Nikola");

        //UPDATE name fields
        //json.update(4, "Predmet", capitalCities);

        //UPDATE fields
        //json.update(4, predmeti);

        //UPDATE add newField newKey
        //json.update(4, "Tore", "Torakic");

        //SEARCH id
        //System.out.println(json.search(6));

        //SEARCH name
        //System.out.println(json.search("Studijski Program"));

        //SEARCH RETURN ENTITES BY NAME, KEY AND STARTs WITH CHAR npr 'a'
        //System.out.println(json.search("Studijski Program", "UUP", 'U'));

        //earch(String name, Object fieldKeyOfInnerEntity, Object innerKey, Object innerValue)
        //System.out.println(json.search("Studijski Program", "student", "ime", "Milos"));

        //SORT BY ID
        //System.out.println(JSONimpl.getInstance().sort(true, false, true));

        //SORT BY NAME
        //System.out.println(JSONimpl.getInstance().sort(false, true, true));

        //UPDATE VALUE FIELD BY ID AND KEY VALUE
        //JSONimpl.getInstance().update(1, "UI", entity4);

        //REMOVE ENTITY WITH NAME = "STUDENT" AND KEYVALUE = "RM"
        //JSONimpl.getInstance().remove("Student", "ime", "Nikola");

     /*   //search(Object fieldKey) preusmeri ovu metodu kao search(String name);
        String fieldKey = "UUP";
        System.out.println(JSONimpl.getInstance().search(fieldKey));*/

        //remove(int id, boolean removeName, boolean removeFields)


    }

}
