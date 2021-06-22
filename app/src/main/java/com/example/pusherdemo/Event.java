package com.example.pusherdemo;

public class Event {
    private String name;
    private String id;
    private String data;

    public Event(String name, String evenId, String data){
        this.name = name;
        this.id = evenId;
        this.data = data;
    }

    public String getName(){
        return name;
    }
    public String getId(){
        return id;
    }
    public  String getData(){
        return data;
    }
    public void setName(String name){
        this.name = name;
    }
}
