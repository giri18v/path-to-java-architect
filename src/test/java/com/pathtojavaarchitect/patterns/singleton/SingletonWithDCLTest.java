package com.pathtojavaarchitect.patterns.singleton;

class SingletonWithDCLTest extends SingletonContractTest<SingletonWithDCL> {

    @Override
    protected SingletonWithDCL getInstance() {
        return SingletonWithDCL.getInstance();
    }
}
