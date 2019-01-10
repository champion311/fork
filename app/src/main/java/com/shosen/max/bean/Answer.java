package com.shosen.max.bean;

public class Answer {

    private String text;
    private int id;
    private boolean checked;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    @Override
    public String toString() {
        return text;
    }

    //
//      "text":"续航能力 公里数",
//              "id":1301,
//              "checked":false
}