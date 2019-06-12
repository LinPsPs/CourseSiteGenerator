
package cs.transactions;

import cs.data.CourseSiteData;
import cs.data.MeetingTime;
import jtps.jTPS_Transaction;

/**
 *
 * @author Jxsry
 */
public class AddMeetingTime_Transaction implements jTPS_Transaction{
    int mode;
    CourseSiteData data;
    MeetingTime lec = new MeetingTime("?","?","?","?");
    MeetingTime lab = new MeetingTime("?", "?", "?", "?", "?");
    MeetingTime sch;
    public AddMeetingTime_Transaction(int mode, CourseSiteData data) {
        this.mode = mode;
        this.data = data;
    }
    
    public AddMeetingTime_Transaction(int mode, CourseSiteData data, String type, String date, String title, String topic, String link) {
        this.mode = mode;
        this.data = data;
        sch = new MeetingTime(type, date, title, topic, link, true);
   }
    
    @Override
    public void doTransaction() {
        switch(mode) {
            case 1: data.addMeetingTime(lec, mode);
            break;
            case 2: ;
            case 3: data.addMeetingTime(lab, mode);
            break;
            case 4: data.addMeetingTime(sch, mode);
            break;
        }
    }
    
    @Override
    public void undoTransaction() {
        switch(mode) {
            case 1: data.removeMeetingTime(lec, mode);
            break;
            case 2: ;
            case 3: data.removeMeetingTime(lab, mode);
            break;
            case 4: data.removeMeetingTime(sch, mode);
            break;
        }
    }
}
