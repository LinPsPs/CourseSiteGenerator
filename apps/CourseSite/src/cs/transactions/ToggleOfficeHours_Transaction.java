package cs.transactions;

import jtps.jTPS_Transaction;
import cs.data.CourseSiteData;
import cs.data.TeachingAssistantPrototype;
import cs.data.TimeSlot;
import cs.data.TimeSlot.DayOfWeek;

/**
 *
 * @author McKillaGorilla
 */
public class ToggleOfficeHours_Transaction implements jTPS_Transaction {
    CourseSiteData data;
    TimeSlot timeSlot;
    DayOfWeek dow;
    TeachingAssistantPrototype ta;
    
    public ToggleOfficeHours_Transaction(   CourseSiteData initData, 
                                            TimeSlot initTimeSlot,
                                            DayOfWeek initDOW,
                                            TeachingAssistantPrototype initTA) {
        data = initData;
        timeSlot = initTimeSlot;
        dow = initDOW;
        ta = initTA;
    }

    @Override
    public void doTransaction() {
        timeSlot.toggleTA(dow, ta);
    }

    @Override
    public void undoTransaction() {
        timeSlot.toggleTA(dow, ta);
    }
}