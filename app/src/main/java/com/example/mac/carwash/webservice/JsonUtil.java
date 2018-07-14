package com.example.mac.carwash.webservice;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;

/**
 * Created by xk on 2017/7/4.
 */

public class JsonUtil {

    /**
     * 将JSON字符串转成对象
     * @see {ZJResponse zjResponse = JsonUtil.fromJson(jsonStr, ZJResponse.class);}
     * @param str-要转换的JSON字符串
     * @param   -要转换的对象类型
     * @return 对象
     */
    public static <T> T fromJson(String str, Type type) {
        Gson gson = new Gson();
        try {//扑捉一下  如果返回的json格式是错误的
            T t = gson.fromJson(str, type);
            return  t;
        }catch (Exception e){
            e.printStackTrace();
            return  null;
        }
    }

    /**
     * 将对象转为JSON
     * @param obj 需要转换成JSON的类
     * @return JSON数据
     */
    public static String toJson(Object obj) {
        Gson gson = new Gson();
        return gson.toJson(obj);
    }

    /**
     * 将对象转为JsonObject
     * @param str 字符串
     * @return JSONObject
     */
    public static JSONObject toJsonObject(String str) {
        JSONObject obj = null;
        try{
            obj = new JSONObject(str);
        }catch (Exception e){
            e.printStackTrace();
        }
        return obj;
    }

    /**
     * 将对象转为JSON
     * @param str 字符串
     * @return JsonArray
     */
    public static JSONArray toJsonArray(String str) {
        JSONArray list = null;
        try{
            list = new JSONArray(str);
        }catch (Exception e){
            e.printStackTrace();
        }
        return list;
    }
}
