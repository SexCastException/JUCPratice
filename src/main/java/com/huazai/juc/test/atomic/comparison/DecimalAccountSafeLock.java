package com.huazai.juc.test.atomic.comparison;

import java.math.BigDecimal;

/**
 *
 * 悲观锁、重量级锁
 *
 * @author pyh
 * @datetime 2022/3/6 10:42
 * @description
 * @className DecimalAccountSafeLock
 */
public class DecimalAccountSafeLock implements DecimalAccount {
    BigDecimal balance;

    public DecimalAccountSafeLock(BigDecimal balance) {
        this.balance = balance;
    }

    @Override
    public BigDecimal getBalance() {
        return balance;
    }

    @Override
    public void withdraw(BigDecimal amount) {
        synchronized (this) {
            BigDecimal balance = this.getBalance();
            this.balance = balance.subtract(amount);
        }
    }

    public static void main(String[] args) {
        DecimalAccountSafeLock decimalAccountSafeLock = new DecimalAccountSafeLock(new BigDecimal("10000"));
        decimalAccountSafeLock.test();
    }
}
