package com.chronaxia.lowpolyworld.model;

import com.chronaxia.lowpolyworld.model.entity.Question;

import org.xmlpull.v1.XmlPullParser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 一非 on 2018/5/23.
 */

public class QuestionModel {
    public List<Question> loadQuestions(XmlPullParser parser) throws Exception{
        return pullQuestionsToXml(parser);
    }

    public List<Question> pullQuestionsToXml(XmlPullParser parser) throws Exception {
        List<Question> list = null;
        Question question = null;
        int type = parser.getEventType();
        while (type != XmlPullParser.END_DOCUMENT) {
            switch (type) {
                case XmlPullParser.START_TAG:
                    if ("questions".equals(parser.getName())) {
                        list = new ArrayList<>();
                    } else if ("question".equals(parser.getName())) {
                        question = new Question();
                    } else if ("question_name".equals(parser.getName())) {
                        question.setQuestionName(parser.nextText());
                    } else if ("question_picture".equals(parser.getName())) {
                        question.setQuestionPicture(parser.nextText());
                    } else if ("left_answer_name".equals(parser.getName())) {
                        question.setLeftAnswerName(parser.nextText());
                    } else if ("left_answer_picture".equals(parser.getName())) {
                        question.setLeftAnswerPicture(parser.nextText());
                    } else if ("right_answer_name".equals(parser.getName())) {
                        question.setRightAnswerName(parser.nextText());
                    } else if ("right_answer_picture".equals(parser.getName())) {
                        question.setRightAnswerPicture(parser.nextText());
                    } else if ("answer".equals(parser.getName())) {
                        question.setAnswer(parser.nextText());
                    } else if ("question_phonetic".equals(parser.getName())) {
                        question.setQuestionPhonetic(parser.nextText());
                    } else if ("question_english".equals(parser.getName())) {
                        question.setQuestionEnglish(parser.nextText());
                    }
                    break;
                case XmlPullParser.END_TAG:
                    if ("question".equals(parser.getName())) {
                        list.add(question);
                    }
                    break;
            }
            //继续往下读取标签类型
            type = parser.next();
        }
        return list;
    }
}
