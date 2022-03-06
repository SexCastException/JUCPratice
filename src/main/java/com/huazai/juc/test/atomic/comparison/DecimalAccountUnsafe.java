package com.huazai.juc.test.atomic.comparison;

import java.math.BigDecimal;

/**
 * @author pyh
 * @datetime 2022/3/6 10:36
 * @description
 * @className DecimalAccountUnsafe
 */
public class DecimalAccountUnsafe implements DecimalAccount {

    BigDecimal balance;

    public DecimalAccountUnsafe(BigDecimal balance) {
        this.balance = balance;
    }

    @Override
    public BigDecimal getBalance() {
        return balance;
    }

    @Override
    public void withdraw(BigDecimal amount) {
        BigDecimal balance = this.getBalance();
        this.balance = balance.subtract(amount);
    }

    public static void main(String[] args) {
        DecimalAccountUnsafe decimalAccountUnsafe = new DecimalAccountUnsafe(new BigDecimal("10000"));
        decimalAccountUnsafe.test();
    }
}
