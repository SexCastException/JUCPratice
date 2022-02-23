package com.huazai.juc.test.volatiles;

import org.openjdk.jcstress.annotations.Result;

import java.io.Serializable;

/**
 * @author pyh
 * @datetime 2022/2/21 23:15
 * @description
 * @className Result
 */
@Result
public class CResult implements Serializable {
    int r1;

    Object jcstress_trap;

    public int getR1() {
        return r1;
    }

    public void setR1(int r1) {
        this.r1 = r1;
    }
}
