
package cs.transactions;

import cs.data.MeetingTime;
import jtps.jTPS_Transaction;

/**
 *
 * @author Jxsry
 */
public class EditScheduleMeetingTime_Transaction implements jTPS_Transaction{
    MeetingTime mt;
    String newType;
    String newDate;
    String newTitle;
    String newTopic;
    String newLink;
    String oldType;
    String oldDate;
    String oldTitle;
    String oldTopic;
    String oldLink;
    public EditScheduleMeetingTime_Transaction(MeetingTime mt, String newType, String newDate, String newTitle, String newTopic, String newLink) {
        this.mt = mt;
        this.newType = newType;
        this.newDate = newDate;
        this.newTitle = newTitle;
        this.newTopic = newTopic;
        this.newLink = newLink;
        this.oldType = mt.getType();
        this.oldDate = mt.getDate();
        this.oldTitle = mt.getTitle();
        this.oldTopic = mt.getTopic();
        this.oldLink = mt.getLink();
    }
    
    public void doTransaction() {
        mt.setDate(newDate);
        mt.setType(newType);
        mt.setTitle(newTitle);
        mt.setTopic(newTopic);
        mt.setLink(newLink);
    }
    
    public void undoTransaction() {
        mt.setDate(oldDate);
        mt.setType(oldType);
        mt.setTitle(oldTitle);
        mt.setTopic(oldTopic);
        mt.setLink(oldLink);
    }
}
