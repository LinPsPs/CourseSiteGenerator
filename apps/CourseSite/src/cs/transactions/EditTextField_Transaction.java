
package cs.transactions;

import javafx.scene.control.TextField;
import jtps.jTPS_Transaction;

/**
 *
 * @author Jxsry
 */
public class EditTextField_Transaction implements jTPS_Transaction{
    String oldText;
    String newText;
    TextField textField;
    public EditTextField_Transaction(String oldText, String newText, TextField textField) {
        this.oldText = oldText;
        this.newText = newText;
        this.textField = textField;
    }
    public void doTransaction() {
        textField.setText(newText);
    }
    public void undoTransaction() {
        textField.setText(oldText);
    }
}
