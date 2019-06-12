
package cs.transactions;

import javafx.scene.control.TextArea;
import jtps.jTPS_Transaction;

/**
 *
 * @author Jxsry
 */
public class EditTextArea_Transaction implements jTPS_Transaction {
    String oldText;
    String newText;
    TextArea textArea;
    public EditTextArea_Transaction(TextArea textArea, String oldText, String newText) {
        this.oldText = oldText;
        this.newText = newText;
        this.textArea = textArea;
    }
    public void doTransaction() {
        textArea.setText(newText);
    }
    public void undoTransaction() {
        textArea.setText(oldText);
    }
}
