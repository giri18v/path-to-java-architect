package com.pathtojavaarchitect.patterns.singleton.realProblem;

import com.pathtojavaarchitect.patterns.singleton.SingletonContractTest;

class PaymentConfigWithDCLTest extends SingletonContractTest<PaymentConfigWithDCL> {

    @Override
    protected PaymentConfigWithDCL getInstance() {
        return PaymentConfigWithDCL.getPaymentConfig();
    }
}
