package storage;

//import jdk.nashorn.internal.runtime.PrototypeObject;

import model.Entity;

import java.util.*;

public abstract class Storage {
    protected String path;
    protected boolean generateId;
    protected int currId = 1;
    protected int maxCount;
    protected List<Integer> takenId = new ArrayList<>();

    /**
     * Pravljenje novog entiteta i provera da li je prosledjeni id vec zauzet
     *
     * @param id     id koji ce biti dodeljen entitetu
     * @param name   name koji ce biti dodeljen entitetu
     * @param fields polja koja ce biti dodeljena entitetu
     * @return null ako je id vec zauzet ili novi entitet kreiran od ovih parametara
     */
    public Entity createEntity(int id, String name, Map<Object, Object> fields) {
        if (!generateId) {
            if (takenId.contains(id)) {
                return null;
            }
        }
        return new Entity(id, name, fields);
    }

    //CRUD Operacije

    /**
     * Cuvanje jedne instance entiteta u skladiste
     *
     * @param entity etitet koji treba da se sacuva
     * @return true ako je uspesno upisano u skladiste
     */
    public abstract boolean save(Entity entity);

    /**
     * Cuvanje liste entiteta u bazu
     *
     * @param entities lista entiteta koje cuvamo
     * @return true ako je uspesno sacuvan u skladistu
     */
    public abstract boolean save(List<Entity> entities);

    /**
     * Kreiranje jedne instance entiteta i cuvanje iste u skladiste
     *
     * @param id     id koji ce biti dodeljen entitetu
     * @param name   name koji ce biti dodeljen entitetu
     * @param fields polja koja ce biti dodeljena entitetu
     * @return true ako je uspesno kreiran entitet i sacuvan u skladistu
     */
    public boolean save(int id, String name, Map<Object, Object> fields) {
        Entity entity = new Entity(id, name, fields);
        return save(entity);
    }

    /**
     * Iscitavanje svih entiteta iz skladista
     *
     * @return listu entiteta iz baze
     */
    public abstract List<Entity> loadAll();

    /**
     * Brisanje entiteta iz baze koji se podudara sa prosledjenim identifikatorom
     *
     * @param id identifikator po kom ce se traziti entitet za brisanje
     */
    public abstract void remove(int id);

    /**
     * Brisanje konkretnog prosledjenog entiteta iz skladista
     *
     * @param entity entitet koji ce biti izbrisan
     */
    public abstract void remove(Entity entity);

    /**
     * Birisanje liste entiteta iz skladista
     *
     * @param entities lista entiteta koji ce biti izbrisani
     */
    public abstract void remove(List<Entity> entities);

    /**
     * Birsanje entiteta iz skladista po kljucu i vrednosti njegovih atributa
     *
     * @param name          naziv entiteta kojeg trazimo u skaldistu
     * @param keyField      vrednost kljuca u entitetu
     * @param valueField    vrednost koja odgovara kljucu keyField u entitetu
     */
    public abstract void remove(String name, Object keyField, Object valueField);

    /**
     * Postvaljanje novih vrednosti za prosledjeni entitet koji se podudara sa entitetom u skladistu
     *
     * @param entity entitet koje ce biti dodeljene nove vrednosti u skladistu
     */
    public abstract void update(Entity entity);

    /**
     * Postvaljanje novih vrednosti za prosledjenu listu entiteta koji se podudaraju sa entitetima u skladistu
     *
     * @param entities lista entiteta kojima ce biti dodeljene nove vrednosti u skladistu
     */
    public abstract void update(List<Entity> entities);

    /**
     * Postavljanje nove vrednosti za polje ime entiteta koji ce biti trazen u skaldistu po prosledjenom identifikatoru
     *
     * @param id      identifikator po kojem ce biti trazen entitet u skladistu
     * @param newName nova vrednost za polje ime entiteta
     */
    public abstract void update(int id, String newName);

    /**
     * Postavljanje novih vrednosti za polje ime i atribute entiteta koji ce biti trazen u skaldistu po prosledjenom identifikatoru
     *
     * @param id        identifikator po kojem ce biti trazen entitet u skladistu
     * @param newName   nova vrednost za polje ime entiteta
     * @param newFields nova vrednost za atribute entiteta
     */
    public abstract void update(int id, String newName, Map<Object, Object> newFields);

    /**
     * Postavljanje nove vrednosti za atribute entiteta koji ce biti trazen u skaldistu po prosledjenom identifikatoru
     *
     * @param id        identifikator po kojem ce biti trazen entitet u skladistu
     * @param newFields nova vrednost za atribute entiteta
     */
    public abstract void update(int id, Map<Object, Object> newFields);

    /**
     * Dodavanje novog (key,value) para za atribute entiteta koji se podudara sa prosledjenim identifikatorom
     *
     * @param id       identifikator po kojem ce biti trazen entitet u skladistui
     * @param newKey   novi kljuc koji ce bitit dodat u atribute
     * @param newValue nova vrednost koja ce biti dodeljena kljucu
     */
    public abstract void update(int id, Object newKey, Object newValue);

    /**
     * Dodavanje novog imena i novih atributa na postojece za entitet koji ce biti trazen u skaldistu po prosledjenom imenu
     *
     * @param currName  ime po kojem ce biti trazen entitet u skladistu
     * @param newName   novo ime koji ce bitit dodeljeno entitetu
     * @param newFields atributi koji ce biti dodati entitetu
     */
    public abstract void update(String currName, String newName, Map<Object, Object> newFields);

    /**
     * Dodavanje novog (key,value) para za atribute entiteta koji se podudara sa prosledjenim imenom
     *
     * @param currName ime po kojem ce biti trazen entitet u skladistu
     * @param newKey   novi kljuc koji ce bitit dodat u atribute
     * @param newField nova vrednost koja ce biti dodeljena kljucu
     */
    public abstract void update(String currName, Object newKey, Object newField);

    //koristi se za uklanjanje polja iz entiteta

    /**
     * Uklanjanje (key,value) parova za atribute entiteta koji se podudara sa prosledjenim identifikatorom
     *
     * @param id           identifikator po kojem ce biti trazen entitet u skladistu
     * @param keysToRemove kljucevi koji ce biti uklonjeni iz atributa entiteta
     */
    public abstract void update(int id, Set<Object> keysToRemove);

    //CRUDOPERACIJEKRAJ

    /**
     * Pronalazenje entiteta po prosledjenom identifikatoru i vracanje istog
     *
     * @param id identifikator po kojem ce biti trazen entitet u skladistu
     * @return entitet ukoliko je prondjen u suprotnom null vrednost
     */
    public abstract Entity search(int id);

    /**
     * Pronalazenje jednog ili vise entiteta po prosledjenom imenu i vracanje istog.
     * Moguce vracanj soritiranih entiteta rastuce ili opadajuce po nekom polju
     *
     * @param name             ime po kojem ce biti trazeni entitet u skladistu
     * @param sort             true ukoliko zelimo da nam se pronadjeni entiteti vrate u sortiranom redosledu, false ako ne zelimo
     * @param isSortByNameOrId true ako zelimo da sortiramo pronadjene entitete po polju ime, false po polju id
     * @param asc              true ako zelimo da budu sortirani u rastucem redosledu, flase u opadajucem
     * @return listu entiteta ukoliko je pronadjen jedan ili vise u suprotnom vraca null vrednost
     */
    public abstract List<Entity> search(String name, boolean sort, boolean isSortByNameOrId, boolean asc);

    //vraca sve entitete koji kao kljuc imaju prosledjeni objekat

    /**
     * Pronalazenje jednog ili vise entiteta koji kao kljuc imaju prosledjeni objekat.
     * Moguce vracanj soritiranih entiteta rastuce ili opadajuce po nekom polju
     *
     * @param fieldKey         kljuc po kojem ce biti trazeni entitet u skladistu
     * @param sort             true ukoliko zelimo da nam se pronadjeni entiteti vrate u sortiranom redosledu, false ako ne zelimo
     * @param isSortByNameOrId true ako zelimo da sortiramo pronadjene entitete po polju ime, false po polju id
     * @param asc              true ako zelimo da budu sortirani u rastucem redosledu, flase u opadajucem
     * @return listu entiteta ukoliko je pronadjen jedan ili vise u suprotnom vraca null vrednost
     */
    public abstract List<Entity> search(Object fieldKey, boolean sort, boolean isSortByNameOrId, boolean asc);

    //vraca sve entitete koji kao key/value parove imaju sve prosledjene u mapi values

    /**
     * Pronalazenje jednog ili vise entiteta koji kao (key,value) parove imaju sve prosledjene u mapi values.
     * Moguce vracanj soritiranih entiteta rastuce ili opadajuce po nekom polju
     *
     * @param values           (key,value) vrednosti u mapi po kojima ce biti trazeni entiteti u skladistu
     * @param sort             true ukoliko zelimo da nam se pronadjeni entiteti vrate u sortiranom redosledu, false ako ne zelimo
     * @param isSortByNameOrId true ako zelimo da sortiramo pronadjene entitete po polju ime, false po polju id
     * @param asc              true ako zelimo da budu sortirani u rastucem redosledu, flase u opadajucem
     * @return listu entiteta ukoliko je pronadjen jedan ili vise u suprotnom vraca null vrednost
     */
    public abstract List<Entity> search(Map<Object, Object> values, boolean sort, boolean isSortByNameOrId, boolean asc);

    //vraca sve entitete sa imenom name koji kao key/value parove imaju sve prosledjene u mapi values

    /**
     * Pronalazenje jednog ili vise entiteta koji kao prosledjeno ime i (key,value) parove imaju sve prosledjene u mapi values.
     * Moguce vracanje soritiranih entiteta rastuce ili opadajuce po nekom polju
     *
     * @param name             ime po kojem ce biti trazeni entitet u skladistu
     * @param values           (key,value) vrednosti u mapi po kojima ce biti trazeni entiteti u skladistu
     * @param sort             true ukoliko zelimo da nam se pronadjeni entiteti vrate u sortiranom redosledu, false ako ne zelimo
     * @param isSortByNameOrId true ako zelimo da sortiramo pronadjene entitete po polju ime, false po polju id
     * @param asc              true ako zelimo da budu sortirani u rastucem redosledu, flase u opadajucem
     * @return listu entiteta ukoliko je pronadjen jedan ili vise u suprotnom vraca null vrednost
     */
    public abstract List<Entity> search(String name, Map<Object, Object> values, boolean sort, boolean isSortByNameOrId, boolean asc);

    //pretrazuje ugnjezdene entitete

    /**
     * Pronalazenje jednog ili vise entiteta koji za prosledjen key imaju entitet koji kao (key, value) par ima prosledjen par (innerKey, inner)
     * Moguce vracanje soritiranih entiteta rastuce ili opadajuce po imenu ili id-u
     *
     * @param fieldKey         kljuc u entitetu cija je vrednost ugnjezdeni entitet
     * @param innerKey         kljuc u ugnjezdenom entitetu
     * @param value            vrednost koja odgovara kljucu innerKey u ugnjezdenom entitetu
     * @param sort             true ukoliko zelimo da nam se pronadjeni entiteti vrate u sortiranom redosledu, false ako ne zelimo
     * @param isSortByNameOrId true ako zelimo da sortiramo pronadjene entitete po polju ime, false po polju id
     * @param asc              true ako zelimo da budu sortirani u rastucem redosledu, flase u opadajucem
     * @return listu entiteta ukoliko je pronadjen jedan ili vise u suprotnom vraca null vrednost
     */
    public abstract List<Entity> search(Object fieldKey, Object innerKey, Object value, boolean sort, boolean isSortByNameOrId, boolean asc);

    //vraca entitete ciji ugnjezdeni entiteti imaju ime kao innerName

    /**
     * Pronalazenje jednog ili vise entiteta koji kao ugnjezdeni entitet imaju ime kao prosledjeni innerName.
     * Moguce vracanj soritiranih entiteta rastuce ili opadajuce po nekom polju
     *
     * @param fieldKey         kljuc u entitetu cija je vrednost ugnjezdeni entitet
     * @param innerName        ime ugnjezdenog entiteta po kojem ce biti trazeni entitet u skladistu
     * @param sort             true ukoliko zelimo da nam se pronadjeni entiteti vrate u sortiranom redosledu, false ako ne zelimo
     * @param isSortByNameOrId true ako zelimo da sortiramo pronadjene entitete po polju ime, false po polju id
     * @param asc              true ako zelimo da budu sortirani u rastucem redosledu, flase u opadajucem
     * @return listu entiteta ukoliko je pronadjen jedan ili vise u suprotnom vraca null vrednost
     */
    public abstract List<Entity> search(Object fieldKey, String innerName, boolean sort, boolean isSortByNameOrId, boolean asc);

    /**
     * Pronalazenje jednog ili vise entiteta kojima vrednost za kljuc pocinje nekim karakterom.
     * Moguce vracanj soritiranih entiteta rastuce ili opadajuce po nekom polju
     *
     * @param name             ime po kojem ce biti trazeni entitet u skladistu
     * @param fieldKey         kljuc po kojem ce biti trazeni entitet u skladistu
     * @param valueStartsWith  vrednost za prosledjeni kljuc po kojem ce biti trazeni entiteti u skladistu
     * @param sort             true ukoliko zelimo da nam se pronadjeni entiteti vrate u sortiranom redosledu, false ako ne zelimo
     * @param isSortByNameOrId true ako zelimo da sortiramo pronadjene entitete po polju ime, false po polju id
     * @param asc              true ako zelimo da budu sortirani u rastucem redosledu, flase u opadajucem
     * @return listu entiteta ukoliko je pronadjen jedan ili vise u suprotnom vraca null vrednost
     */
    public abstract List<Entity> search(String name, Object fieldKey, char valueStartsWith, boolean sort, boolean isSortByNameOrId, boolean asc);

    /**
     * Pronalazenje jednog ili vise entiteta koji za vrednost filedKey imaju ugnjezdeni entitet koji za vrednost njegovog innerKey ima vrednsot innerValue.
     * Moguce vracanj soritiranih entiteta rastuce ili opadajuce po nekom polju
     *
     * @param name             ime po kojem ce biti trazeni entitet u skladistu
     * @param fieldKey         kljuc po kojem ce biti trazeni entitet u skladistu
     * @param innerKey         kljuc za ugnjezdeni entitet po kojem ce biti trazeni entiteti iz skladista
     * @param innerValue       vrednost za prosledjeni kljuc ugnjezdengo entiteta po kojem ce biti trazeni entiteti u skladistu
     * @param sort             true ukoliko zelimo da nam se pronadjeni entiteti vrate u sortiranom redosledu, false ako ne zelimo
     * @param isSortByNameOrId true ako zelimo da sortiramo pronadjene entitete po polju ime, false po polju id
     * @param asc              true ako zelimo da budu sortirani u rastucem redosledu, flase u opadajucem
     * @return listu entiteta ukoliko je pronadjen jedan ili vise u suprotnom vraca null vrednost
     */
    public abstract List<Entity> search(String name, Object fieldKey, Object innerKey, Object innerValue, boolean sort, boolean isSortByNameOrId, boolean asc);

    // prima listu entiteta koji se sortiraju i vraca ih sortirane

    /**
     * Prosledjuje se lista entiteta koji se sortiraju i vracaju se sortirani.
     *
     * @param toSort lista entiteta koja treba biti sortirana
     * @param byId   true ako zelimo da sortiramo primljene entitete po polju ime, false po polju id
     * @param asc    true ako zelimo da budu sortirani u rastucem redosledu, flase u opadajucem
     * @return listu sortiranih entiteta
     */
    public abstract List<Entity> sort(List<Entity> toSort, boolean byId, boolean asc);

    /**
     * Vrsi proveru da li je identifikator za prosledjeni entitet vec zauzet
     *
     * @param entity entitet za koji proveravamo validnost identifikatora
     * @return true ukoliko je identifikator validan, false ukoliko nije
     */
    protected abstract boolean checkID(Entity entity);

    protected void setPath(String path) {
        this.path = path;
    }

    protected void setGenerateId(boolean generateId) {
        this.generateId = generateId;
    }

    protected void setMaxCount(int maxCount) {
        this.maxCount = maxCount;
    }

    protected void removeID(int id) {
        Iterator iterator = takenId.iterator();
        while (iterator.hasNext()) {
            int next = (int) iterator.next();
            if (next == id) {
                iterator.remove();
                return;
            }
        }
    }

    //uklanja iz liste zauzetih id entiteta i svih njegovih ugnjezdenih entiteta

    /**
     * @param entity
     */
    protected void removeID(Entity entity) {
        if (entity.getFields() != null) {
            Set<Object> keys = entity.getFields().keySet();
            for (Object key : keys) {
                if (entity.getFields().get(key) instanceof Entity) {
                    removeID(((Entity) entity.getFields().get(key)).getId());
                }
            }
        }
        removeID(entity.getId());
    }


}
