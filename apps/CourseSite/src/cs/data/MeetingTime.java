/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cs.data;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Haolin Yu
 */
public class MeetingTime {
    private StringProperty section;
    private StringProperty days;
    private StringProperty time;
    private StringProperty room;
    private StringProperty ta1;
    private StringProperty ta2;
    private StringProperty exactTime;
    private StringProperty type;
    private StringProperty date;
    private StringProperty title;
    private StringProperty topic;
    private StringProperty link;
    
    public MeetingTime (String section, String room) {
        this.section = new SimpleStringProperty(section);
        this.room = new SimpleStringProperty(room);
    }
    
    public MeetingTime (String section, String days, String time, String room) {
        this(section, room);
        this.days = new SimpleStringProperty(days);
        this.time = new SimpleStringProperty(time);
    }
    
    public MeetingTime (String section, String exactTime, String room, String ta1, String ta2) {
        this(section, room);
        this.exactTime = new SimpleStringProperty(exactTime);
        this.ta1 = new SimpleStringProperty(ta1);
        this.ta2 = new SimpleStringProperty(ta2);
    }
    
    public MeetingTime (String type, String date, String title, String topic, String link, boolean isSchedule) {
        this.type = new SimpleStringProperty(type);
        this.date = new SimpleStringProperty(date);
        this.title = new SimpleStringProperty(title);
        this.topic = new SimpleStringProperty(topic);
        this.link = new SimpleStringProperty(link);
    }
    
    public void setDate(String date) {
        this.date.set(date);
    }

    public void setSection(String section) {
        this.section.set(section);
    }

    public void setDays(String days) {
        this.days.set(days);
    }

    public void setTime(String time) {
        this.time.set(time);
    }

    public void setRoom(String room) {
        this.room.set(room);
    }

    public void setTa1(String ta1) {
        this.ta1.set(ta1);
    }

    public void setTa2(String ta2) {
        this.ta2.set(ta2);
    }

    public void setExactTime(String exactTime) {
        this.exactTime.set(exactTime);
    }

    public void setType(String type) {
        this.type.set(type);
    }

    public void setTitle(String title) {
        this.title.set(title);
    }

    public void setTopic(String topic) {
        this.topic.set(topic);
    }
    
    public void setLink(String link) {
        this.link.set(link);
    }

    public String getLink() {
        return link.get();
    }
    
    public String getSection() {
        return section.get();
    }

    public String getDays() {
        return days.get();
    }

    public String getTime() {
        return time.get();
    }

    public String getRoom() {
        return room.get();
    }

    public String getTa1() {
        return ta1.get();
    }

    public String getTa2() {
        return ta2.get();
    }

    public String getExactTime() {
        return exactTime.get();
    }

    public String getType() {
        return type.get();
    }

    public String getDate() {
        return date.get();
    }

    public String getTitle() {
        return title.get();
    }

    public String getTopic() {
        return topic.get();
    }
    
    
}
