package com.vip.textmemo.model;

public class Memo {

    private int index;
    private String heading;
    private String text;
    private String finalDate;

    public Memo() {

    }

    public Memo(int index, String heading, String text, String finalDate) {
        this.index = index;
        this.heading = heading;
        this.text = text;
        this.finalDate = finalDate;
    }

    public Memo(String heading, String text, String finalDate) {
        this.heading = heading;
        this.text = text;
        this.finalDate = finalDate;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getFinalDate() {
        return finalDate;
    }

    public void setFinalDate(String finalDate) {
        this.finalDate = finalDate;
    }
}
