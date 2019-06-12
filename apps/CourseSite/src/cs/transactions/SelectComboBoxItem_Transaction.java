package cs.transactions;

import javafx.scene.control.ComboBox;

/**
 *
 * @author haoli
 */
public class SelectComboBoxItem_Transaction implements jtps.jTPS_Transaction{
    String oldValue;
    String newValue;
    ComboBox cb;

    public SelectComboBoxItem_Transaction(String oldValue, String newValue, ComboBox cb) {
        this.oldValue = oldValue;
        this.newValue = newValue;
        this.cb = cb;
    }
    public void doTransaction() {
        cb.getSelectionModel().select(newValue);
    }
    public void undoTransaction() {
        cb.getSelectionModel().select(oldValue);
    }
}
