package cs.workspace.controllers;

import djf.modules.AppGUIModule;
import djf.ui.dialogs.AppDialogsFacade;
import javafx.collections.ObservableList;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import cs.CourseSiteApp;
import static cs.CourseSitePropertyType.OH_EMAIL_TEXT_FIELD;
import static cs.CourseSitePropertyType.OH_FOOLPROOF_SETTINGS;
import static cs.CourseSitePropertyType.OH_NAME_TEXT_FIELD;
import static cs.CourseSitePropertyType.OH_NO_TA_SELECTED_CONTENT;
import static cs.CourseSitePropertyType.OH_NO_TA_SELECTED_TITLE;
import static cs.CourseSitePropertyType.OH_OFFICE_HOURS_TABLE_VIEW;
import static cs.CourseSitePropertyType.OH_TAS_TABLE_VIEW;
import static cs.CourseSitePropertyType.OH_TA_EDIT_DIALOG;
import cs.data.CourseSiteData;
import cs.data.MeetingTime;
import cs.data.TAType;
import cs.data.TeachingAssistantPrototype;
import cs.data.TimeSlot;
import cs.data.TimeSlot.DayOfWeek;
import cs.transactions.AddMeetingTime_Transaction;
import cs.transactions.AddTA_Transaction;
import cs.transactions.EditMeetingTime_Transaction;
import cs.transactions.EditScheduleMeetingTime_Transaction;
import cs.transactions.EditTA_Transaction;
import cs.transactions.EditTextArea_Transaction;
import cs.transactions.EditTextField_Transaction;
import cs.transactions.RemoveMeetingTime_Transaction;
import cs.transactions.RemoveTA_Transaction;
import cs.transactions.ToggleOfficeHours_Transaction;
import cs.transactions.ToggleWorkTime_Transaction;
import cs.workspace.dialogs.TADialog;
import javafx.scene.control.TextArea;

/**
 *
 * @author McKillaGorilla
 * @author Haolin Yu
 */
public class CourseSiteController {

    CourseSiteApp app;
    public static String originalOHStartTime = "9:00am";
    public static String originalOHEndTime = "9:00pm";
    public static boolean isAllMode = true;
    private String oldText;

    public CourseSiteController(CourseSiteApp initApp) {
        app = initApp;
    }

    public void processAddTA() {
        AppGUIModule gui = app.getGUIModule();
        TextField nameTF = (TextField) gui.getGUINode(OH_NAME_TEXT_FIELD);
        String name = nameTF.getText();
        TextField emailTF = (TextField) gui.getGUINode(OH_EMAIL_TEXT_FIELD);
        String email = emailTF.getText();
        CourseSiteData data = (CourseSiteData) app.getDataComponent();
        TAType type = data.getSelectedType();
        if (data.isLegalNewTA(name, email)) {
            TeachingAssistantPrototype ta = new TeachingAssistantPrototype(name.trim(), email.trim(), type);
            AddTA_Transaction addTATransaction = new AddTA_Transaction(data, ta);
            app.processTransaction(addTATransaction);

            // NOW CLEAR THE TEXT FIELDS
            nameTF.setText("");
            emailTF.setText("");
            nameTF.requestFocus();
        }
        app.getFoolproofModule().updateControls(OH_FOOLPROOF_SETTINGS);
    }

    public void processVerifyTA() {

    }

    public void processRemoveTA() {
        AppGUIModule gui = app.getGUIModule();
        TableView<TeachingAssistantPrototype> taTableView = (TableView)gui.getGUINode(OH_TAS_TABLE_VIEW);
        TeachingAssistantPrototype ta = taTableView.getSelectionModel().getSelectedItem();
        CourseSiteData data = (CourseSiteData) app.getDataComponent();
        if (ta != null) {
            RemoveTA_Transaction removeTATransaction = new RemoveTA_Transaction(data, ta);
            app.processTransaction(removeTATransaction);
        }
        else {
            Stage window = app.getGUIModule().getWindow();
            AppDialogsFacade.showMessageDialog(window, OH_NO_TA_SELECTED_TITLE, OH_NO_TA_SELECTED_CONTENT);
        }
    }
    
    public void processToggleOfficeHours() {
        AppGUIModule gui = app.getGUIModule();
        TableView<TimeSlot> officeHoursTableView = (TableView) gui.getGUINode(OH_OFFICE_HOURS_TABLE_VIEW);
        ObservableList<TablePosition> selectedCells = officeHoursTableView.getSelectionModel().getSelectedCells();
        if (selectedCells.size() > 0) {
            TablePosition cell = selectedCells.get(0);
            int cellColumnNumber = cell.getColumn();
            CourseSiteData data = (CourseSiteData)app.getDataComponent();
            if (data.isDayOfWeekColumn(cellColumnNumber)) {
                DayOfWeek dow = data.getColumnDayOfWeek(cellColumnNumber);
                TableView<TeachingAssistantPrototype> taTableView = (TableView)gui.getGUINode(OH_TAS_TABLE_VIEW);
                TeachingAssistantPrototype ta = taTableView.getSelectionModel().getSelectedItem();
                if (ta != null) {
                    TimeSlot timeSlot = officeHoursTableView.getSelectionModel().getSelectedItem();
                    ToggleOfficeHours_Transaction transaction = new ToggleOfficeHours_Transaction(data, timeSlot, dow, ta);
                    app.processTransaction(transaction);
                }
                else {
                    Stage window = app.getGUIModule().getWindow();
                    AppDialogsFacade.showMessageDialog(window, OH_NO_TA_SELECTED_TITLE, OH_NO_TA_SELECTED_CONTENT);
                }
            }
            int row = cell.getRow();
            cell.getTableView().refresh();
        }
    }

    public void processTypeTA() {
        app.getFoolproofModule().updateControls(OH_FOOLPROOF_SETTINGS);
    }

    public void processEditTA() {
        CourseSiteData data = (CourseSiteData)app.getDataComponent();
        if (data.isTASelected()) {
            TeachingAssistantPrototype taToEdit = data.getSelectedTA();
            TADialog taDialog = (TADialog)app.getGUIModule().getDialog(OH_TA_EDIT_DIALOG);
            taDialog.showEditDialog(taToEdit);
            TeachingAssistantPrototype editTA = taDialog.getEditTA();
            if (editTA != null) {
                EditTA_Transaction transaction = new EditTA_Transaction(taToEdit, editTA.getName(), editTA.getEmail(), editTA.getType());
                app.processTransaction(transaction);
            }
        }
    }

    public void processSelectAllTAs() {
        CourseSiteData data = (CourseSiteData)app.getDataComponent();
        data.selectTAs(TAType.All);
    }

    public void processSelectGradTAs() {
        CourseSiteData data = (CourseSiteData)app.getDataComponent();
        data.selectTAs(TAType.Graduate);
    }

    public void processSelectUndergradTAs() {
        CourseSiteData data = (CourseSiteData)app.getDataComponent();
        data.selectTAs(TAType.Undergraduate);
    }

    public void processSelectTA() {
        AppGUIModule gui = app.getGUIModule();
        TableView<TimeSlot> officeHoursTableView = (TableView) gui.getGUINode(OH_OFFICE_HOURS_TABLE_VIEW);
        officeHoursTableView.refresh();
    }
    
    public void processTypeExport() {
        app.getFoolproofModule().updateControls(OH_FOOLPROOF_SETTINGS);
    }
    
    public void UpdateStartEndTime (String newStartTime, String newEndTime) {
        originalOHStartTime = newStartTime;
        originalOHEndTime = newEndTime;
    }
    
    public void processUpdateOH (String newStartTime, String newEndTime) {
        ToggleWorkTime_Transaction transaction = new ToggleWorkTime_Transaction((CourseSiteData)app.getDataComponent(),
               originalOHStartTime, originalOHEndTime, newStartTime, newEndTime);
        app.processTransaction(transaction);
        UpdateStartEndTime(newStartTime, newEndTime);
    }
    
    public void processEditTextArea(boolean done, String oldText, String newText, TextArea textArea) {
        if(!done) {
            this.oldText = oldText;
        }
        else {
            if(!this.oldText.equals(newText)) {
                EditTextArea_Transaction transaction = new EditTextArea_Transaction(textArea, this.oldText, newText);
                app.processTransaction(transaction);
            }
        }
    }
    
    public void processEditTextField(boolean done, String oldText, String newText, TextField textField) {
        if(!done) {
            this.oldText = oldText;
        }
        else {
            if(!this.oldText.equals(newText)) {
                EditTextField_Transaction transaction = new EditTextField_Transaction(this.oldText, newText, textField);
                app.processTransaction(transaction);
            }
        }
    }
    
    public void processAddMeetingTime(int mode) {
        CourseSiteData data = (CourseSiteData)app.getDataComponent();
        AddMeetingTime_Transaction transaction = new AddMeetingTime_Transaction(mode, data);
        app.processTransaction(transaction);
    }
    
    public void processAddScheduleMeetingTime(String type, String date, String title, String topic, String link) {
        CourseSiteData data = (CourseSiteData)app.getDataComponent();
        AddMeetingTime_Transaction transaction = new AddMeetingTime_Transaction(4, data, type,
                        date, title, topic, link);
        app.processTransaction(transaction);
    }
    
    public void processRemoveMeetingTime(int mode, MeetingTime mt) {
        CourseSiteData data = (CourseSiteData)app.getDataComponent();
        RemoveMeetingTime_Transaction transaction = new RemoveMeetingTime_Transaction(data, mt, mode);
        app.processTransaction(transaction);
    }
    
    public void processEditMeetingTime(MeetingTime mt, String oldValue, String newValue, int row, int mode, TableView<MeetingTime> table) {
        CourseSiteData data = (CourseSiteData) app.getDataComponent();
        EditMeetingTime_Transaction transaction = new EditMeetingTime_Transaction(data, mt, oldValue, newValue, row, mode, table);
        app.processTransaction(transaction);
    }
    
    public void processEditScheduleMeetingTime(MeetingTime mt, String type, String date, String title, String topic, String link) {
        EditScheduleMeetingTime_Transaction transaction = new EditScheduleMeetingTime_Transaction(mt, type, date, title, topic, link);
        app.processTransaction(transaction);
    }
}