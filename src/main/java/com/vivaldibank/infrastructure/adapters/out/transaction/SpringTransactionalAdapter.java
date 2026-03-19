package com.vivaldibank.infrastructure.adapters.out.transaction;

import com.vivaldibank.domain.ports.out.TransactionalPort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;

@Component
public class SpringTransactionalAdapter implements TransactionalPort {

    private final TransactionTemplate transactionTemplate;

    public SpringTransactionalAdapter(TransactionTemplate transactionTemplate) {
        this.transactionTemplate = transactionTemplate;
    }

    @Override
    public void execute(Runnable operation) {
        transactionTemplate.executeWithoutResult(status -> operation.run());
    }
}
