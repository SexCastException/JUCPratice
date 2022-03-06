package com.huazai.juc.test.atomic.comparison;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 悲观锁，自旋锁
 *
 * @author pyh
 * @datetime 2022/3/6 10:45
 * @description
 * @className DecimalAccountSafeCas
 */
public class DecimalAccountSafeCas implements  DecimalAccount{
    AtomicReference<BigDecimal> ref;

    public DecimalAccountSafeCas(BigDecimal account) {
        ref = new AtomicReference<>(account);
    }

    @Override
    public BigDecimal getBalance() {
        return ref.get();
    }

    @Override
    public void withdraw(BigDecimal amount) {
        while (true) {
            BigDecimal bigDecimal = ref.get();
            if (ref.compareAndSet(bigDecimal, bigDecimal.subtract(amount))) {
                return;
            }
        }
    }

    public static void main(String[] args) {
        DecimalAccountSafeCas decimalAccountSafeCas = new DecimalAccountSafeCas(new BigDecimal("10000"));
        decimalAccountSafeCas.test();
    }
}
