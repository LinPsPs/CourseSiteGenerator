package cs.transactions;

import cs.data.CourseSiteData;
import cs.data.MeetingTime;
import javafx.scene.control.TableView;
import jtps.jTPS_Transaction;

public class EditMeetingTime_Transaction implements jTPS_Transaction{
    MeetingTime mt;
    int mode;
    CourseSiteData data;
    String oldValue;
    String newValue;
    int col;
    TableView<MeetingTime> table;
    
    public EditMeetingTime_Transaction(CourseSiteData data, MeetingTime mt, String oldValue, String newValue, int col, int mode, TableView<MeetingTime> table) {
        this.mt = mt;
        this.data = data;
        this.mode = mode;
        this.oldValue = oldValue;
        this.newValue = newValue;
        this.col = col;
        this.table = table;
   }
    
    @Override
    public void doTransaction() {
        data.editMeetingTime(mt, newValue, col, mode);
        table.refresh();
    }
    
    @Override
    public void undoTransaction() {
        data.editMeetingTime(mt, oldValue, col, mode);
        table.refresh();
    }
}