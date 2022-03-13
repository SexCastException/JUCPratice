package com.huazai.juc.test.immutable;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * DateTimeFormatter和基本类型包装类都是不可变类，所以不存在线程不安全问题
 *
 * @author pyh
 * @datetime 2022/3/13 13:06
 * @description
 * @className DateTimeFormatterTest
 */
@Slf4j
public class DateTimeFormatterTest {
    public static void main(String[] args) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        for (int i = 0; i < 100; i++) {
            LocalDate localDate = dateTimeFormatter.parse("1951-04-21", LocalDate::from);
            log.debug("date:{}", localDate);
        }
    }
}
