package com.chronaxia.lowpolyworld.model;

import com.chronaxia.lowpolyworld.R;
import com.chronaxia.lowpolyworld.app.LowPolyWorldApp;
import com.chronaxia.lowpolyworld.model.entity.Continent;
import com.chronaxia.lowpolyworld.util.XmlUtils;

import org.xmlpull.v1.XmlPullParser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 一非 on 2018/5/19.
 */

public class ContinentModel {

    private static volatile ContinentModel instance;

    private ContinentModel() {
        initData();
    }

    public static ContinentModel getInstance() {
        if (instance == null) {
            synchronized (ContinentModel.class) {
                if (instance == null) {
                    instance = new ContinentModel();
                }
            }
        }
        return instance;
    }

    private List<Continent> continentList;

    private void initData() {
        continentList = new ArrayList<>();
        try {
            continentList = pullContinentToXml(LowPolyWorldApp.getInstance().getResources().getXml(R.xml.continent));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Continent> getContinentList() {
        return continentList;
    }

    public Continent getContinent(String name) {
        for (Continent continent : continentList) {
            if (name.equals(continent.getName())) {
                return continent;
            }
        }
        return null;
    }

    public List<Continent> pullContinentToXml(XmlPullParser parser) throws Exception {
        List<Continent> list = null;
        Continent continent = null;
        int type = parser.getEventType();
        while (type != XmlPullParser.END_DOCUMENT) {
            switch (type) {
                case XmlPullParser.START_TAG:
                    if ("continents".equals(parser.getName())) {
                        list = new ArrayList<>();
                    } else if ("continent".equals(parser.getName())) {
                        continent = new Continent();
                    } else if ("name".equals(parser.getName())) {
                        continent.setName(parser.nextText());
                    } else if ("phonetic".equals(parser.getName())) {
                        continent.setPhonetic(parser.nextText());
                    } else if ("english".equals(parser.getName())) {
                        continent.setEnglish(parser.nextText());
                    }
                    break;
                case XmlPullParser.END_TAG:
                    if ("continent".equals(parser.getName())) {
                        list.add(continent);
                    }
                    break;
            }
            //继续往下读取标签类型
            type = parser.next();
        }
        return list;
    }

}
