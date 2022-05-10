package com.lespayne.smartwear.dataHelper;

import com.google.gson.Gson;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

public class GsonUtil {
    private static final Gson gson;

    static {
        gson = new Gson();
    }

    private GsonUtil() {
    }

    /**
     * 对象转成Json字符串
     */
    public static String objectToJson(Object object) {
        return gson.toJson(object);
    }

    /**
     * List对象转成Json字符串
     *
     * @param list List对象
     * @param <T>  泛型
     * @return Json字符串
     */
    public static <T> String listToJson(List<T> list) {
        return gson.toJson(list);
    }

    /**
     * Json字符串转成对象
     *
     * @param json  Json字符串
     * @param clazz 对象class
     * @param <T>   泛型
     * @return 对象
     */
    public static <T> T jsonToObject(String json, Class<T> clazz) {
        return gson.fromJson(json, clazz);
    }

    /**
     * Json字符串转成List对象
     *
     * @param json  Json字符串
     * @param clazz 对象class
     * @param <T>   泛型
     * @return List对象
     */
    public static <T> List<T> jsonToList(String json, Class<T> clazz) {
        Type type = new ParameterizedTypeImpl(clazz);
        return gson.fromJson(json, type);
    }

    private static class ParameterizedTypeImpl implements ParameterizedType {
        Class clazz;

        ParameterizedTypeImpl(Class clz) {
            clazz = clz;
        }

        @Override
        public Type[] getActualTypeArguments() {
            return new Type[]{clazz};
        }

        @Override
        public Type getRawType() {
            return List.class;
        }

        @Override
        public Type getOwnerType() {
            return null;
        }
    }
}