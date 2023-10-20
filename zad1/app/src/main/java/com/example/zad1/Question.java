package com.example.zad1;

public class Question {
    private int questionId;
    private boolean trueAnswer;

    public Question(int questionId, boolean trueAnswer) {
        this.questionId = questionId;
        this.trueAnswer = trueAnswer;
    }
    public int getQuestionId() { return this.questionId; }
    public boolean getTrueAnswer() { return this.trueAnswer; }
}
