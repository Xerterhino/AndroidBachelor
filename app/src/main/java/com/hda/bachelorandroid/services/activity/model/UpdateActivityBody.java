package com.hda.bachelorandroid.services.activity.model;

public class UpdateActivityBody {
    final String duration;
    final String name;
    final boolean finished;


    public UpdateActivityBody(String name, String duration, boolean finished) {
        this.duration = duration;
        this.name = name;
        this.finished = finished;
    }
}
