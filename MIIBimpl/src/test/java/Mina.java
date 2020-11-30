import model.Entity;
import storage.Storage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Mina {

    public static void main(String[] args) {

        MIIBimpl miib = new MIIBimpl();
        Map<Object, Object> capitalCities = new HashMap<Object, Object>();
        Map<Object, Object> predmeti = new HashMap<Object, Object>();


        miib.setPath("C:\\Users\\maxa\\IdeaProjects\\Maven\\SoftKompMIIBimpl\\data");

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

        Entity entity2 = new Entity();
        entity2.setId(2);
        entity2.setName("Student");
        entity2.addField("brIndex", "RN43/18");
        entity2.addField("ime", "Milos");
        entity2.addField("prezime", "Maksimovic");
        entity2.addField("godine", 21);

        Entity entity22 = new Entity();
        entity22.setId(6);
        entity22.setName("Student");
        entity22.addField("brIndex", "RN43/18");
        entity22.addField("ime", "Milos");
        entity22.addField("prezime", "Maksimovic");
        entity22.addField("godine", 21);

        Entity entity1 = new Entity(1, "Studijski Program");
        entity1.addField("UI", "Upravljanje informacijama");
        entity1.addField("SK", "Softverske komponente");
        entity1.addField("UUP", "Uvod u programiranje");
        entity1.addField("student", entity2);

        Entity entity5 = new Entity(5, "Studijski Program");
        entity5.addField("UI", "bbbbbbb");
        entity5.addField("SK", "Softverske komponente");
        entity5.addField("UUP", "Udgdsgsdgdsgsdgsdg");
        entity5.addField("student", entity2);

        Entity entity3 = new Entity(3, "Student");

        Entity entity4 = new Entity(4, "Student");
        entity4.addField("ime", "Nikola");
        entity4.addField("prezime", "Paunovic");


        //SAVE LIST
/*        List<Entity> entities = new ArrayList<>();
        entities.add(entity1);
        entities.add(entity3);
        entities.add(entity4);
        entities.add(entity5);
        System.out.println(json.save(entities));*/

        // SAVE entity, kreira novi file ako se predje maxEntityCount
        /*miib.save(entity1);
        miib.save(entity2);
        miib.save(entity3);
        miib.save(entity4);
        miib.save(entity5);
        miib.save(entity22);*/

      //  miib.save(entity22);


       // miib.save(entity22);

       // miib.save(entity22);

        List<Entity> entities = new ArrayList<>();
        entities.add(entity5);
        entities.add(entity22);


        //add entity
        //ystem.out.println(MIIBimpl.getInstance().save(entity4));

        //add list of entity
        //System.out.println(miib.save(entities));

        //add entity prosledjivanje id name
        //miib.save(1, "Skola");

        //vracamo samo jedan entitet po nazivu
        //System.out.println(miib.load("Studijski Program"));

        //vracamo listu entiteta po nazivu
        System.out.println(miib.loadAll());

        //vracamo entitet po id-u
        //System.out.println(miib.load(6));

        //brisemo entitet po id-u
        //MIIBimpl.getInstance().remove(3);

        //brisanje entiteta
        //miib.remove(entity22);

        //birsanje liste entiteta
        //MIIBimpl.getInstance().remove(entities_to_remove);

        //brisanje imena i polja prosledjujemo id
        //MIIBimpl.getInstance().remove(1, true, true);

        //brisanje fields prosledjuje se name
        //miib.remove("Student", true);

        //birsanje prosledjuje se naziv key i value koji se matchuje da se brise
        //MIIBimpl.getInstance().remove("Student", "ime", "Milos");

        //update name prosledjujemo id
        //miib.update(3, "tore");

        //update name i fields prosledjujemo id
        //miib.update(1,"baki", capitalCities);

        //update fields prosledjujemo id
        //MIIBimpl.getInstance().update(2, capitalCities);

        //update vrednost prosledjujemo kljuc i id
        //MIIBimpl.getInstance().update(1, "student", entity4);

        //update name i fields prosledjuje se name
        //MIIBimpl.getInstance().update("Studijski Program", "program", capitalCities);

        //update value prosledjuje se name i key
        //MIIBimpl.getInstance().update("Studijski Program", "UI", "upravljanje");

        //search by id
       //System.out.println("\n"+miib.search(3));

        //search by fieldKey
        //Object obj = "ime";
        //System.out.println(MIIBimpl.getInstance().search(obj));

        //valueStartsWith
        //System.out.println(MIIBimpl.getInstance().search("Student", "ime", 'M'));

    }
}
