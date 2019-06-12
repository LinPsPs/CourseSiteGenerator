/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cs.transactions;

import javafx.scene.control.ComboBox;
import jtps.jTPS_Transaction;

/**
 *
 * @author Jxsry
 */
public class AddComboBoxItem_Transaction implements jTPS_Transaction{
    String oldValue;
    String newValue;
    ComboBox cb;
    public AddComboBoxItem_Transaction(String oldValue, String newValue, ComboBox cb) {
        this.oldValue = oldValue;
        this.newValue = newValue;
        this.cb = cb;
    }
    public void doTransaction() {
        cb.getItems().add(newValue);
        cb.getSelectionModel().select(newValue);
        
    }
    public void undoTransaction() {
        cb.getItems().remove(newValue);
        cb.getSelectionModel().select(oldValue);
    }
}
