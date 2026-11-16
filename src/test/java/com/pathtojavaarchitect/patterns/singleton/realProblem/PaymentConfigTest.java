package com.pathtojavaarchitect.patterns.singleton.realProblem;

import com.pathtojavaarchitect.patterns.singleton.SingletonContractTest;

class PaymentConfigTest extends SingletonContractTest<PaymentConfig> {

    @Override
    protected PaymentConfig getInstance() {
        return PaymentConfig.getPaymentConfig();
    }
}
