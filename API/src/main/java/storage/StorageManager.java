package storage;

public class StorageManager {

    private static Storage storage;

    public static void registerStorage(Storage theStorage) {
        storage = theStorage;
    }

    public static Storage getStorage(String filePath, boolean generateId, int maxCount){
        storage.setPath(filePath);
        storage.setGenerateId(generateId);
        storage.setMaxCount(maxCount);
        return storage;
    }
}
