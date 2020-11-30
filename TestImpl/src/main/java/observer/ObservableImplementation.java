package observer;

import java.util.ArrayList;
import java.util.List;

public class ObservableImplementation implements MyObservable{

    private List<MyObserver> observerList;

    @Override
    public void addObserver(MyObserver observer) {
        if(this.observerList == null){
            observerList = new ArrayList<>();
        }
        if(observer != null && !observerList.contains(observer)) {
            observerList.add(observer);
        }
    }

    @Override
    public void removeObserver(MyObserver observer) {
        if(this.observerList == null){
            return;
        }
        this.observerList.remove(observer);
    }

    @Override
    public void notifyObservers(Object object) {
        for (MyObserver observer : observerList) {
            observer.update(object, null);
        }
    }

    @Override
    public void notifyObservers(Object object, String string) {
        for (MyObserver myObserver : observerList) {
            myObserver.update(object, string);
        }
    }
}
