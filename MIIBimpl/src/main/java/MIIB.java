import model.Entity;
import org.apache.commons.lang3.math.NumberUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Map;

public class MIIB {

    public MIIB() {

    }

    public StringBuilder toMIIB(Object obj) {
        boolean inner = false;
        StringBuilder miib = new StringBuilder();
        ArrayList<Entity> entities = (ArrayList<Entity>) obj;

        for (Entity ent : entities) {
            miib.append("<");
            miib.append("\n  ");
            miib.append("id" + "|").append(ent.getId());
            miib.append(";\n  ");
            if (ent.getName().isEmpty()) {
                miib.append("name" + "|" + "-");
            } else {
                miib.append("name" + "|").append(ent.getName());
            }
            miib.append(";\n  ");
            Map<Object, Object> map = ent.getFields();
            if (map.isEmpty()) {
                miib.append("fields" + "|" + "-");
                miib.append("\n" + ">");
            } else {
                miib.append("fields" + "=" + "\n    ");
                int size = map.size();
                for (Map.Entry<Object, Object> entry : map.entrySet()) {
                    size -= 1;
                    Object key = entry.getKey();
                    Object value = entry.getValue();
                    if (value instanceof Entity) {
                        inner = true;
                        Entity inner_ent = (Entity) value;
                        System.out.println("ugnjezdeni entitet!");
                        miib.append(key);
                        miib.append("\n      [");
                        miib.append("\n        ");
                        miib.append("id" + "|").append(inner_ent.getId());
                        miib.append(";\n        ");
                        if (ent.getName().isEmpty()) {
                            miib.append("name" + "|" + "-");
                        } else {
                            miib.append("name" + "|").append(inner_ent.getName());
                        }
                        miib.append(";\n        ");
                        Map<Object, Object> inner_map = inner_ent.getFields();
                        if (inner_map.isEmpty()) {
                            miib.append("fields" + "|" + "-");
                            miib.append("\n      " + ">");
                        } else {
                            miib.append("fields" + "=" + "\n          ");
                            int size_inner = inner_map.size();
                            for (Map.Entry<Object, Object> entry_inner : inner_map.entrySet()) {
                                size_inner -= 1;
                                Object key_inner = entry_inner.getKey();
                                Object value_inner = entry_inner.getValue();
                                miib.append(key_inner).append("|").append(value_inner);
                                miib.append(";");
                                if (size_inner == 0) {
                                    miib.append("\n      ]\n");
                                } else {
                                    miib.append("\n          ");
                                }
                            }
                        }
                    } else {
                        if (inner) {
                            miib.append("    ");
                            miib.append(key).append("|").append(value);
                            miib.append(";");
                            System.out.println("USAAAO U INNER");
                        } else {
                            miib.append(key).append("|").append(value);
                            miib.append(";");
                        }
                        if (size == 0) {
                            miib.append("\n>\n");
                        } else {
                            miib.append("\n    ");
                        }
                    }
                }
            }

        }
        if(inner) {
            miib.append(">\n");
            inner = false;
        }
        return miib;
    }

    ArrayList<Entity> miibToEntity(String path) throws IOException {

        File f = new File(path);
        if (f.exists()) {

            boolean numeric = true;
            int num = 0;

            ArrayList<Entity> entities = new ArrayList<>();

            String content = new String(Files.readAllBytes(Paths.get(path)));

            content = content.replaceAll(" ", "");
            content = content.replaceAll("\\n", "");
            //content = content.trim();

            String[] entityArr = content.split("<");

            for(String sss: entityArr) {
                System.out.println(sss);
            }


            for (String ent : entityArr) {
                ent = ent.replace(">", "");
                ent = ent.replace("=", "");
                if (ent.contains("[")) {
                    String innerEntKey = "";
                    Entity entity = new Entity();
                    //ent.replace("]", "");
                    String[] innerEnt = ent.split("\\[");
                    //System.out.println(innerEnt[0]);
                    //System.out.println(innerEnt[1]);

                    String[] lookForeInnerKeyArr = innerEnt[0].split(";");
                    innerEntKey = lookForeInnerKeyArr[lookForeInnerKeyArr.length - 1];

                    String[] lookForFieldsAfterInner = innerEnt[1].split("]");

                    innerEnt[0] = innerEnt[0].replace(innerEntKey, "");//brisemo key za inner enty

                    if(lookForFieldsAfterInner.length == 2){
                        innerEnt[0] += lookForFieldsAfterInner[1];
                    }

                    //innerEnt[0] += lookForFieldsAfterInner[1]; //dodajemo ostatak fields koji je dosao posle innerEnty u filds

                    //System.out.println(innerEnt[1]);
                    entity = returnEnt(innerEnt[0]);

                    Entity innerEntity = new Entity();
                    innerEntity = returnEnt(lookForFieldsAfterInner[0]);//prosledjujemo unutrasnji enty za parsiranje

                    entity.addField(innerEntKey, innerEntity);

                    if (entity.getId() == 0) {

                    } else {
                        entities.add(entity);
                    }
                } else {

                    Entity entity = new Entity();

                    entity = returnEnt(ent);

                    if (entity.getId() == 0) {

                    } else {
                        entities.add(entity);
                    }
                }
            }
            return entities;
        }
        else {
            return null;
        }
    }


    protected Entity returnEnt(String ent) {
        String[] attrFieldArr = ent.split("fields");

        for (String s : attrFieldArr) {
            if (s.length() == 0) {
                continue;
            }
            s = s.substring(0, s.length() - 1);
        }

        String[] entKeyValueAttrArr = attrFieldArr[0].split(";");
        String[] entKeyValueFieldArr = attrFieldArr[entKeyValueAttrArr.length - 1].split(";");

        for (String s : entKeyValueAttrArr) {
            // System.out.println(s);
        }

        for (String s : entKeyValueFieldArr) {
            //System.out.println(s);
        }

        Entity entity = new Entity();

        //atributi entiteta
        for (String keyValueAttr : entKeyValueAttrArr) {
            String[] keyValuePairAttrArr = keyValueAttr.split("\\|");
            for (String kvPairAttr : keyValuePairAttrArr) {
                if (kvPairAttr.equals("id") || kvPairAttr.equals("name")) {
                    continue;
                }
                //da li je int ako jeste onda je id za entity
                if (NumberUtils.isDigits(kvPairAttr)) {
                    entity.setId(Integer.parseInt(kvPairAttr));
                } else { //inace je name za entity
                    entity.setName(kvPairAttr);
                }
            }
        }

        //fields entiteta
        for (String keyValueField : entKeyValueFieldArr) {
            String[] keyValuePairFieldArr = keyValueField.split("\\|");
            //dodajemo fileds u entity
            entity.addField(keyValuePairFieldArr[0], keyValuePairFieldArr[keyValuePairFieldArr.length - 1]);
        }

        return entity;
    }

}


