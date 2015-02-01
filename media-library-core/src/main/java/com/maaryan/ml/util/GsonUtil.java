package com.maaryan.ml.util;

import com.google.gson.Gson;

public class GsonUtil {
	public static String toJson(Object obj){
		Gson gson = new Gson();
		return gson.toJson(obj);
	}
	public static <T> T fromJson(String json, Class<T> classOfT){
		Gson gson = new Gson();
		return gson.fromJson(json, classOfT);
	}
}
