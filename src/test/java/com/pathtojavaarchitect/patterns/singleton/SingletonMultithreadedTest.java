package com.pathtojavaarchitect.patterns.singleton;

class SingletonMultithreadedTest extends SingletonContractTest<SingletonMultithreaded> {

    @Override
    protected SingletonMultithreaded getInstance() {
        return SingletonMultithreaded.getInstance();
    }
}
