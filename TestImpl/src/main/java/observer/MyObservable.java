package observer;

public interface MyObservable {
    void addObserver(MyObserver observer);
    void removeObserver(MyObserver observer);
    void notifyObservers(Object object);
    void notifyObservers(Object object, String string);
}
