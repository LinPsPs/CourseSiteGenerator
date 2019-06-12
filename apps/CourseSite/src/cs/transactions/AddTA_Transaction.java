package cs.transactions;

import jtps.jTPS_Transaction;
import cs.data.CourseSiteData;
import cs.data.TeachingAssistantPrototype;

/**
 *
 * @author McKillaGorilla
 */
public class AddTA_Transaction implements jTPS_Transaction {
    CourseSiteData data;
    TeachingAssistantPrototype ta;
    
    public AddTA_Transaction(CourseSiteData initData, TeachingAssistantPrototype initTA) {
        data = initData;
        ta = initTA;
    }

    @Override
    public void doTransaction() {
        data.addTA(ta);        
    }

    @Override
    public void undoTransaction() {
        data.removeTA(ta);
    }
}
