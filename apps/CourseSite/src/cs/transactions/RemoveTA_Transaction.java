package cs.transactions;

import jtps.jTPS_Transaction;
import cs.data.CourseSiteData;
import cs.data.TeachingAssistantPrototype;

/**
 *
 * @author haoli
 */
public class RemoveTA_Transaction implements jTPS_Transaction {
    CourseSiteData data;
    TeachingAssistantPrototype ta;
    public RemoveTA_Transaction (CourseSiteData initData, TeachingAssistantPrototype initTA) {
        data = initData;
        ta = initTA;
    }
    
    @Override
    public void doTransaction() {
        data.removeTA(ta);
    }
    
    @Override
    public void undoTransaction() {
        data.addTA(ta);
    }
}
