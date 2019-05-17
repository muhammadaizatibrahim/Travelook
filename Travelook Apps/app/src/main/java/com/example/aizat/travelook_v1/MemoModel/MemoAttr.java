package com.example.aizat.travelook_v1.MemoModel;

public class MemoAttr {

    public String date;
    public String title;
    public String text;
    public String cuid;
    public String memoid;

    public MemoAttr (){

    }


    public MemoAttr(String date, String title, String text, String cuid, String memoid) {
        this.date = date;
        this.title = title;
        this.text = text;
        this.cuid = cuid;
        this.memoid = memoid;
    }


    public String getDate() { return date; }

    public void setDate(String date) { this.date = date; }

    public String getTitle() { return title; }

    public void setTitle(String title){ this.title = title; }

    public String getText() { return text; }

    public void setText (String text){this.text = text; }

    public String getCuid() { return cuid; }

    public void setCuid (String cuid) {this.cuid = cuid; }

    public String getMemoid() { return memoid; }

    public void setMemoid(String memoid) { this.memoid = memoid; }
}
