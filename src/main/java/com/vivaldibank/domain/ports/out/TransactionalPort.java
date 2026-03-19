package com.vivaldibank.domain.ports.out;

public interface TransactionalPort {
    void execute(Runnable operation);
}
