package client.net;

import java.io.Serializable;

import client.presenter.IPresenter;

public abstract class Subject implements Serializable {
    public abstract void addObserver(IPresenter o);
    public abstract void removeObserver(IPresenter o);
    public abstract void notifyObservers();
}
