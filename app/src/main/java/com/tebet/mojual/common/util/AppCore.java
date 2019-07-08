package com.tebet.mojual.common.util;

import com.google.gson.Gson;

import java.util.Arrays;
import java.util.List;

/**
 * Created by kal on 6/9/2017.
 */

public class AppCore {

    public static <T> List<T> objectToArray(List list, Class<T[]> clazz) {
        String s = new Gson().toJson(list);
        T[] arr = new Gson().fromJson(s, clazz);
        return Arrays.asList(arr); //or return Arrays.asList(new Gson().fromJson(s, clazz)); for a one-liner
    }
}
