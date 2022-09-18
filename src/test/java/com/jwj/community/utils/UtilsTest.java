package com.jwj.community.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

public class UtilsTest {

    @Test
    @DisplayName("최소값 테스트")
    public void test1() throws Exception{
        int views = 100;
        views = Math.min(++views, Integer.MAX_VALUE);

        assertThat(views).isEqualTo(101);
    }

    @Test
    @DisplayName("날짜 테스트")
    public void test2() throws Exception{
        Date date = new Date(0);

        System.out.println("date = " + date);
        System.out.println("date = " + date.before(new Date()));

    }

}
