/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cs.transactions;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import jtps.jTPS_Transaction;

/**
 *
 * @author Jxsry
 */
public class ChangeImage_Transaction implements jTPS_Transaction{
    ImageView iv;
    String oldPath;
    String newPath;
    public ChangeImage_Transaction(ImageView iv, String oldPath, String newPath) {
        this.iv = iv;
        this.oldPath = oldPath;
        this.newPath = newPath;
    }
    
    public void doTransaction() {
        iv.setImage(new Image(newPath));
    }
    
    public void undoTransaction() {
        iv.setImage(new Image(oldPath));
    }
}
