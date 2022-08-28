package com.jwj.community.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;

public class JSONReadUtils {

    // Static으로만 사용할 클래스는 클래스의 생성자를 외부에서 호출할 수 없도록 막기
    private JSONReadUtils() {
        throw new AssertionError();
    }

    public static <T> List<T> readJSONList(String filePath, Class<T> classOfT){
        Gson gson = new Gson();
        List<T> result = null;

        try(InputStream in = gson.getClass().getClassLoader().getResourceAsStream(filePath)) {
            ObjectMapper mapper = new ObjectMapper();
            List<Map> list = gson.fromJson(new String(in.readAllBytes(), UTF_8), List.class);
            result = new ArrayList<>();

            for (Map map : list){
                result.add(mapper.convertValue(map, classOfT));
            }
        }catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    public static <T> T readJSON(String filePath, Class<T> classOfT){
        Gson gson = new Gson();
        T result = null;

        try(InputStream in = gson.getClass().getClassLoader().getResourceAsStream(filePath)) {
            result = gson.fromJson(new String(in.readAllBytes(), UTF_8), classOfT);
        }catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }
}
