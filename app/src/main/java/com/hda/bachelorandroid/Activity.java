package com.hda.bachelorandroid;

public class Activity {
    private String name;
    private String duration;
    private Boolean finished;
    private String _id ;

    public Activity(String name, String duration, String _id) {
        this.name = name;
        this.duration = duration;
        this.finished = false;
        this._id = _id;
    }
    public String get_id() { return _id;}

    public void set_id(String _id) {this._id = _id;
    }
    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public Boolean getFinished() {
        return finished;
    }

    public void setFinished(Boolean finished) {
        this.finished = finished;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
