
package cs.transactions;

import cs.data.CourseSiteData;
import cs.data.MeetingTime;
import jtps.jTPS_Transaction;

/**
 *
 * @author Jxsry
 */
public class RemoveMeetingTime_Transaction implements jTPS_Transaction{
    CourseSiteData data;
    int mode;
    MeetingTime mt;
    
    public RemoveMeetingTime_Transaction(CourseSiteData data, MeetingTime mt, int mode) {
        this.data = data;
        this.mode = mode;
        this.mt = mt;
    }
    
    @Override
    public void doTransaction() {
        data.removeMeetingTime(mt, mode);
    }
    
    @Override
    public void undoTransaction() {
        data.addMeetingTime(mt, mode);
    }
}
