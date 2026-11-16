package com.pathtojavaarchitect.patterns;

import com.pathtojavaarchitect.patterns.singleton.LazySingleton;

public class LazySingletonDemo {

    static void main() throws InterruptedException {

        Thread t1 = new Thread(() -> {
            LazySingleton instance3 = LazySingleton.getInstance();
            System.out.println(instance3);
        });

        Thread t2 = new Thread(() -> {
            LazySingleton instance4 = LazySingleton.getInstance();
            System.out.println(instance4);
        });

        t1.start();
        t2.start();

        LazySingleton instance = LazySingleton.getInstance();
        System.out.println(instance);

        LazySingleton instance2 = LazySingleton.getInstance();
        System.out.println(instance2);

        t1.join();
        t2.join();
    }
}
