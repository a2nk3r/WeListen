package com.example.welisten;

public class InputHistory {
    private String mInputText;
    private String mDate;

    public InputHistory(){

    }
    public InputHistory(String inputText, String date) {
        mInputText = inputText;
        mDate = date;
    }

    public String getInputText() {
        return mInputText;
    }

    public void setInputText(String inputText) {
        mInputText = inputText;
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(String date) {
        mDate = date;
    }
}
