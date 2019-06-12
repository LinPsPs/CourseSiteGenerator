package cs.workspace.foolproof;

import djf.modules.AppGUIModule;
import djf.ui.foolproof.FoolproofDesign;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import cs.CourseSiteApp;
import static cs.CourseSitePropertyType.*;
import cs.data.CourseSiteData;
import static cs.workspace.style.CSStyle.CLASS_TEXT_FIELD;
import static cs.workspace.style.CSStyle.CLASS_TEXT_FIELD_ERROR;
import static djf.AppPropertyType.EXPORT_BUTTON;
import javafx.scene.control.CheckBox;

public class CourseSiteFoolproofDesign implements FoolproofDesign {

    CourseSiteApp app;

    public CourseSiteFoolproofDesign(CourseSiteApp initApp) {
        app = initApp;
    }

    @Override
    public void updateControls() {
        updateAddTAFoolproofDesign();
        updateEditTAFoolproofDesign();
        exportFoolproofDesign();
    }

    private void updateAddTAFoolproofDesign() {
        AppGUIModule gui = app.getGUIModule();
        
        // FOOLPROOF DESIGN STUFF FOR ADD TA BUTTON
        TextField nameTextField = ((TextField) gui.getGUINode(OH_NAME_TEXT_FIELD));
        TextField emailTextField = ((TextField) gui.getGUINode(OH_EMAIL_TEXT_FIELD));
        String name = nameTextField.getText();
        String email = emailTextField.getText();
        CourseSiteData data = (CourseSiteData) app.getDataComponent();
        Button addTAButton = (Button) gui.getGUINode(OH_ADD_TA_BUTTON);

        // FIRST, IF NO TYPE IS SELECTED WE'LL JUST DISABLE
        // THE CONTROLS AND BE DONE WITH IT
        boolean isTypeSelected = data.isTATypeSelected();
        if (!isTypeSelected) {
            nameTextField.setDisable(true);
            emailTextField.setDisable(true);
            addTAButton.setDisable(true);
            return;
        } // A TYPE IS SELECTED SO WE'LL CONTINUE
        else {
            nameTextField.setDisable(false);
            emailTextField.setDisable(false);
            addTAButton.setDisable(false);
        }

        // NOW, IS THE USER-ENTERED DATA GOOD?
        boolean isLegalNewTA = data.isLegalNewTA(name, email);

        // ENABLE/DISABLE THE CONTROLS APPROPRIATELY
        addTAButton.setDisable(!isLegalNewTA);
        if (isLegalNewTA) {
            nameTextField.setOnAction(addTAButton.getOnAction());
            emailTextField.setOnAction(addTAButton.getOnAction());
        } else {
            nameTextField.setOnAction(null);
            emailTextField.setOnAction(null);
        }

        // UPDATE THE CONTROL TEXT DISPLAY APPROPRIATELY
        boolean isLegalNewName = data.isLegalNewName(name);
        boolean isLegalNewEmail = data.isLegalNewEmail(email);
        foolproofTextField(nameTextField, isLegalNewName);
        foolproofTextField(emailTextField, isLegalNewEmail);
    }
    
    private void updateEditTAFoolproofDesign() {
        
    }
    
    public void foolproofTextField(TextField textField, boolean hasLegalData) {
        if (hasLegalData) {
            textField.getStyleClass().remove(CLASS_TEXT_FIELD_ERROR);
            if (!textField.getStyleClass().contains(CLASS_TEXT_FIELD)) {
                textField.getStyleClass().add(CLASS_TEXT_FIELD);
            }
        } else {
            textField.getStyleClass().remove(CLASS_TEXT_FIELD);
            if (!textField.getStyleClass().contains(CLASS_TEXT_FIELD_ERROR)) {
                textField.getStyleClass().add(CLASS_TEXT_FIELD_ERROR);
            }
        }
    }
    
    public void exportFoolproofDesign() {
        AppGUIModule gui = app.getGUIModule();
        
        CheckBox homeCheckBox = (CheckBox) gui.getGUINode(SITE_HOME_CHECKBOX);
        CheckBox syllabusCheckBox = (CheckBox) gui.getGUINode(SITE_SYLLABUS_CHECKBOX);
        CheckBox scheduleCheckBox = (CheckBox) gui.getGUINode(SITE_SCHEDULE_CHECKBOX);
        CheckBox HWsCheckBox = (CheckBox) gui.getGUINode(SITE_HWS_CHECKBOX);
        
        if(homeCheckBox.isSelected() || syllabusCheckBox.isSelected() ||
                scheduleCheckBox.isSelected() || HWsCheckBox.isSelected()) {
            gui.getGUINode(EXPORT_BUTTON).setDisable(false);
        }
        else {
            gui.getGUINode(EXPORT_BUTTON).setDisable(true);
        }
    }
}
