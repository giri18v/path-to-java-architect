package com.pathtojavaarchitect.patterns.singleton;

public class SingletonWithDCL {

    private static volatile SingletonWithDCL instance;

    private SingletonWithDCL() {

    }

    public static SingletonWithDCL getInstance() {
        if(instance == null){
            synchronized (SingletonWithDCL.class){
                if(instance == null){
                    instance = new SingletonWithDCL();
                }
            }
        }
        return instance;
    }
}
