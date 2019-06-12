package cs.transactions;

import cs.data.CourseSiteData;
import jtps.jTPS_Transaction;

/**
 *
 * @author Jxsry
 */

public class ToggleWorkTime_Transaction implements jTPS_Transaction{
    CourseSiteData data;
    String originalStartTime;
    String originalEndTime;
    String newStartTime;
    String newEndTime;
    public ToggleWorkTime_Transaction (CourseSiteData initData, String originalStartTime, String originalEndTime,
            String newStartTime, String newEndTime) {
        data = initData;
        this.originalStartTime = originalStartTime;
        this.originalEndTime = originalEndTime;
        this.newStartTime = newStartTime;
        this.newEndTime = newEndTime;
    }
    
    @Override
    public void doTransaction() {
        data.updateOH(newStartTime, newEndTime);
    }
    
    @Override
    public void undoTransaction() {
        data.updateOH(originalStartTime, originalEndTime);
    }
}
