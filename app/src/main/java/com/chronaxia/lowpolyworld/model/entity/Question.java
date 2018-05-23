package com.chronaxia.lowpolyworld.model.entity;

/**
 * Created by 一非 on 2018/5/23.
 */

public class Question {
    private String questionName;
    private String questionPicture;
    private String questionPhonetic;
    private String questionEnglish;
    private String leftAnswerName;
    private String leftAnswerPicture;
    private String rightAnswerName;
    private String rightAnswerPicture;
    private String answer;

    public String getQuestionName() {
        return questionName;
    }

    public void setQuestionName(String questionName) {
        this.questionName = questionName;
    }

    public String getQuestionPicture() {
        return questionPicture;
    }

    public void setQuestionPicture(String questionPicture) {
        this.questionPicture = questionPicture;
    }

    public String getLeftAnswerName() {
        return leftAnswerName;
    }

    public void setLeftAnswerName(String leftAnswerName) {
        this.leftAnswerName = leftAnswerName;
    }

    public String getLeftAnswerPicture() {
        return leftAnswerPicture;
    }

    public void setLeftAnswerPicture(String leftAnswerPicture) {
        this.leftAnswerPicture = leftAnswerPicture;
    }

    public String getRightAnswerName() {
        return rightAnswerName;
    }

    public void setRightAnswerName(String rightAnswerName) {
        this.rightAnswerName = rightAnswerName;
    }

    public String getRightAnswerPicture() {
        return rightAnswerPicture;
    }

    public void setRightAnswerPicture(String rightAnswerPicture) {
        this.rightAnswerPicture = rightAnswerPicture;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getQuestionPhonetic() {
        return questionPhonetic;
    }

    public void setQuestionPhonetic(String questionPhonetic) {
        this.questionPhonetic = questionPhonetic;
    }

    public String getQuestionEnglish() {
        return questionEnglish;
    }

    public void setQuestionEnglish(String questionEnglish) {
        this.questionEnglish = questionEnglish;
    }

    @Override
    public String toString() {
        return "Question{" +
                "questionName='" + questionName + '\'' +
                ", questionPicture='" + questionPicture + '\'' +
                ", questionPhonetic='" + questionPhonetic + '\'' +
                ", questionEnglish='" + questionEnglish + '\'' +
                ", leftAnswerName='" + leftAnswerName + '\'' +
                ", leftAnswerPicture='" + leftAnswerPicture + '\'' +
                ", rightAnswerName='" + rightAnswerName + '\'' +
                ", rightAnswerPicture='" + rightAnswerPicture + '\'' +
                ", answer='" + answer + '\'' +
                '}';
    }
}
