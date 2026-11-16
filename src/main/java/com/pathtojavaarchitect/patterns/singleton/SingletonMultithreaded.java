package com.pathtojavaarchitect.patterns.singleton;

public class SingletonMultithreaded {

    private static SingletonMultithreaded instance;

    private SingletonMultithreaded() {

    }


    public static synchronized SingletonMultithreaded getInstance() {
        if(instance == null) {
            instance = new SingletonMultithreaded();
        }
        return instance;
    }

}
