
package cs.transactions;

import java.time.LocalDate;
import javafx.scene.control.DatePicker;
import jtps.jTPS_Transaction;

/**
 *
 * @author haoli
 */
public class EditDatePicker_Transaction implements jTPS_Transaction{
    DatePicker date;
    LocalDate oldDate;
    LocalDate newDate;
    
    public EditDatePicker_Transaction(DatePicker date, LocalDate oldDate, LocalDate newDate) {
        this.date = date;
        this.oldDate = oldDate;
        this.newDate = newDate;
    }
    
    @Override
    public void doTransaction() {
        date.setValue(newDate);
    }
    
    @Override
    public void undoTransaction() {
        date.setValue(oldDate);
    }
}
