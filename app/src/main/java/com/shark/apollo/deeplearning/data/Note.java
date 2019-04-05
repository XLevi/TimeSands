package com.shark.apollo.deeplearning.data;

import java.util.Date;
import java.util.Locale;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

@Entity
public class Note {
    @Id
    public long id;

    private int mins;

    private Date date;

    private long timeKeeping;

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }


    public void setMins(int mins) {
        this.mins = mins;
    }

    public int getMins() {
        return mins;
    }

    public void setTimeKeeping(long timeKeeping) {
        this.timeKeeping = timeKeeping;
    }

    public long getTimeKeeping() {
        return timeKeeping;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getDate() {
        return date;
    }

    @Override
    public String toString() {
        return "Note - id = " + id  + ", time = " + mins;
    }
}
