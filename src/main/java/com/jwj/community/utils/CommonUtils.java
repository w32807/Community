package com.jwj.community.utils;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;

import java.time.LocalDate;
import java.util.UUID;
import java.util.function.Supplier;

import static java.time.format.DateTimeFormatter.ofPattern;

// Guava Library 주소
// https://github.com/google/guava/wiki
public class CommonUtils {

	// Static으로만 사용할 클래스는 클래스의 생성자를 외부에서 호출할 수 없도록 막기
	private CommonUtils() {
		throw new AssertionError();
	}

	public static LocalDate getToday(){
		return LocalDate.now();
	}

	public static String getTodayString(){
		return LocalDate.now().format(ofPattern("yyyy-MM-dd"));
	}

	public static String getDateString(LocalDate localDate){
		return localDate.format(ofPattern("yyyy-MM-dd"));
	}

	public static String getUUID(){
		return UUID.randomUUID().toString();
	}

	public static BooleanBuilder nullSafeBuilder(Supplier<BooleanExpression> f) {
		try {
			return new BooleanBuilder(f.get());
		} catch (IllegalArgumentException e) {
			return new BooleanBuilder();
		}
	}
}
