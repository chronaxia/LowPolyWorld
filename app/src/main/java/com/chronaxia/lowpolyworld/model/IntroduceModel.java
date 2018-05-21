package com.chronaxia.lowpolyworld.model;

import com.chronaxia.lowpolyworld.model.entity.ScenicSpot;

import org.xmlpull.v1.XmlPullParser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 一非 on 2018/5/21.
 */

public class IntroduceModel {

    public List<ScenicSpot> loadIntroduceScenicSpots(XmlPullParser parser) throws Exception{
        return pullScenicSpotsToXml(parser);
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
