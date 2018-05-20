package com.chronaxia.lowpolyworld.model;

import com.chronaxia.lowpolyworld.R;
import com.chronaxia.lowpolyworld.app.LowPolyWorldApp;
import com.chronaxia.lowpolyworld.model.entity.ScenicSpot;

import org.xmlpull.v1.XmlPullParser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 一非 on 2018/5/19.
 */

public class ScenicSpotsModel {

    private static volatile ScenicSpotsModel instance;

    private ScenicSpotsModel() {
        scenicSpotList = new ArrayList<>();
    }

    public static ScenicSpotsModel getInstance() {
        if (instance == null) {
            synchronized (ScenicSpotsModel.class) {
                if (instance == null) {
                    instance = new ScenicSpotsModel();
                }
            }
        }
        return instance;
    }

    private List<ScenicSpot> scenicSpotList;

    public void initData(int id){
        try {
            if (id != 0) {
                scenicSpotList = pullScenicSpotsToXml(LowPolyWorldApp.getInstance().getResources().getXml(id));
            } else {
                scenicSpotList = new ArrayList<>();
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    public List<ScenicSpot> getScenicSpotList() {
        return scenicSpotList;
    }

    public ScenicSpot getScenicSpot(String name) {
        for (ScenicSpot scenicSpot : scenicSpotList) {
            if (name.equals(scenicSpot.getName())) {
                return scenicSpot;
            }
        }
        return null;
    }

    public List<ScenicSpot> pullScenicSpotsToXml(XmlPullParser parser) throws Exception {
        List<ScenicSpot> list = null;
        ScenicSpot scenicSpot = null;
        int type = parser.getEventType();
        while (type != XmlPullParser.END_DOCUMENT) {
            switch (type) {
                case XmlPullParser.START_TAG:
                    if ("scenic_spots".equals(parser.getName())) {
                        list = new ArrayList<>();
                    } else if ("scenic_spot".equals(parser.getName())) {
                        scenicSpot = new ScenicSpot();
                    } else if ("name".equals(parser.getName())) {
                        scenicSpot.setName(parser.nextText());
                    } else if ("phonetic".equals(parser.getName())) {
                        scenicSpot.setPhonetic(parser.nextText());
                    } else if ("english".equals(parser.getName())) {
                        scenicSpot.setEnglish(parser.nextText());
                    } else if ("continent".equals(parser.getName())) {
                        scenicSpot.setContinent(parser.nextText());
                    } else if ("location".equals(parser.getName())) {
                        scenicSpot.setLocation(parser.nextText());
                    } else if ("describe".equals(parser.getName())) {
                        scenicSpot.setDescribe(parser.nextText());
                    } else if ("picture".equals(parser.getName())) {
                        scenicSpot.setPicture(parser.nextText());
                    }
                    break;
                case XmlPullParser.END_TAG:
                    if ("scenic_spot".equals(parser.getName())) {
                        list.add(scenicSpot);
                    }
                    break;
            }
            //继续往下读取标签类型
            type = parser.next();
        }
        return list;
    }

}
