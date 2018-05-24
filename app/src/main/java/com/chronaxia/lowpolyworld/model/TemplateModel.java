package com.chronaxia.lowpolyworld.model;

import com.chronaxia.lowpolyworld.model.entity.ScenicSpot;

import org.xmlpull.v1.XmlPullParser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 一非 on 2018/5/24.
 */

public class TemplateModel {

    public List<String> loadTemplates(XmlPullParser parser) throws Exception{
        return pullTemplateToXml(parser);
    }

    public List<String> pullTemplateToXml(XmlPullParser parser) throws Exception {
        List<String> list = null;
        int type = parser.getEventType();
        while (type != XmlPullParser.END_DOCUMENT) {
            switch (type) {
                case XmlPullParser.START_TAG:
                    if ("templates".equals(parser.getName())) {
                        list = new ArrayList<>();
                    }else if ("template".equals(parser.getName())) {
                        list.add(parser.nextText());
                    }
                    break;
            }
            //继续往下读取标签类型
            type = parser.next();
        }
        return list;
    }

}
