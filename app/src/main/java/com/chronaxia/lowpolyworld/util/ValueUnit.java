package com.chronaxia.lowpolyworld.util;

/**
 * Created by 一非 on 2018/5/17.
 */

public class ValueUnit {

    private static volatile ValueUnit instance;

    private ValueUnit() {
    }

    public static ValueUnit getInstance() {
        if (instance == null) {
            synchronized (ValueUnit.class) {
                if (instance == null) {
                    instance = new ValueUnit();
                }
            }
        }
        return instance;
    }

    public static String turnToXmlName(String name) {
        String idName = "";
        if ("亚洲".equals(name)) {
            idName = "asia_scenic_spot";
        } else if ("非洲".equals(name)) {
            idName = "africa_scenic_spot";
        } else if ("欧洲".equals(name)) {
            idName = "europe_scenic_spot";
        } else if ("北美洲".equals(name)) {
            idName = "north_america_scenic_spot";
        } else if ("南美洲".equals(name)) {
            idName = "south_america_scenic_spot";
        } else if ("南极洲".equals(name)) {
            idName = "antarctica_scenic_spot";
        } else if ("大洋洲".equals(name)) {
            idName = "oceania_scenic_spot";
        }
        return idName;
    }
}
