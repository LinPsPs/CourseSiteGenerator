package cs.workspace;

import com.sun.org.apache.bcel.internal.classfile.Utility;
import djf.components.AppWorkspaceComponent;
import djf.modules.AppFoolproofModule;
import djf.modules.AppGUIModule;
import static djf.modules.AppGUIModule.ENABLED;
import djf.ui.AppNodesBuilder;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import properties_manager.PropertiesManager;
import cs.CourseSiteApp;
import static cs.CourseSitePropertyType.*;
import cs.data.CourseSiteData;
import cs.data.MeetingTime;
import cs.data.TeachingAssistantPrototype;
import cs.data.TimeSlot;
import cs.transactions.AddComboBoxItem_Transaction;
import cs.transactions.AddMeetingTime_Transaction;
import cs.transactions.ChangeImage_Transaction;
import cs.transactions.EditDatePicker_Transaction;
import cs.transactions.RemoveMeetingTime_Transaction;
import cs.transactions.SelectComboBoxItem_Transaction;
import cs.workspace.controllers.CourseSiteController;
import cs.workspace.dialogs.TADialog;
import cs.workspace.foolproof.CourseSiteFoolproofDesign;
import static cs.workspace.style.CSStyle.*;
import static djf.AppPropertyType.LOAD_IMAGE_TITLE;
import static djf.AppTemplate.PATH_STYLE;
import static djf.modules.AppLanguageModule.FILE_PROTOCOL;
import djf.ui.dialogs.AppDialogsFacade;
import djf.ui.dialogs.AppWebDialog;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TextArea;
import javafx.scene.control.TitledPane;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;

import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javax.imageio.ImageIO;

/**
 *
 * @author McKillaGorilla
 * @author Haolin Yu
 */
public class CourseSiteWorkspace extends AppWorkspaceComponent {

    public CourseSiteWorkspace(CourseSiteApp app) {
        super(app);
        
        // LAYOUT THE APP
        initLayout();
        
        // INIT THE EVENT HANDLERS
        initControllers();

        // 
        initFoolproofDesign();

        // INIT DIALOGS
        initDialogs();
    }

    private void initDialogs() {
        TADialog taDialog = new TADialog((CourseSiteApp) app);
        app.getGUIModule().addDialog(OH_TA_EDIT_DIALOG, taDialog);
    }
    
    private void initLayout() {
        AppNodesBuilder csBuilder = app.getGUIModule().getNodesBuilder();
        workspace = new BorderPane();
        TabPane tabPane = csBuilder.buildTabPane(CS_TAB_PANE, null, CLASS_TAB_PANE, ENABLED);
        initSite(tabPane);
        initSyllabus(tabPane);
        initMT(tabPane);
        initOh(tabPane);
        initSchedule(tabPane);
        ((BorderPane)workspace).setCenter(tabPane);
    }

    // THIS HELPER METHOD INITIALIZES ALL THE CONTROLS IN THE WORKSPACE
    private void initSite(TabPane tabPane) {
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        
        AppNodesBuilder csBuilder = app.getGUIModule().getNodesBuilder();
        
        Tab siteTab = csBuilder.buildTab(SITE_TAB, tabPane, CLASS_TAB, ENABLED);
        GridPane sitePane = csBuilder.buildGridPane(SITE_PANE, null, CLASS_PANE, ENABLED);
        sitePane.setVgap(10);
        sitePane.setStyle("-fx-background-color: #f0b20a");
        ScrollPane siteScrollPane = new ScrollPane();
        sitePane.prefWidthProperty().bind(siteScrollPane.widthProperty());
        siteScrollPane.setContent(sitePane);
        siteScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        siteScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        siteTab.setContent(siteScrollPane);
        initSite_BannerPane(sitePane);
        initSite_PagesPane(sitePane);
        initSite_StylePane(sitePane);
        initSite_InstructorPane(sitePane);
    }
    
    private void initSyllabus(TabPane tabPane) {
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        
        AppNodesBuilder csBuilder = app.getGUIModule().getNodesBuilder();
        
        Tab syllabusTab = csBuilder.buildTab(SYLLABUS_TAB, tabPane, CLASS_TAB, ENABLED);
        
        ScrollPane syllabusScrollPane = new ScrollPane();
        syllabusTab.setContent(syllabusScrollPane);
        GridPane syllabusPane = csBuilder.buildGridPane(SYLLABUS_PANE, null, CLASS_PANE, ENABLED);
        syllabusPane.setVgap(10);
        syllabusPane.setStyle("-fx-background-color: #f0b20a");
        syllabusPane.prefWidthProperty().bind(syllabusScrollPane.widthProperty());
        syllabusPane.prefHeightProperty().bind(syllabusScrollPane.heightProperty());
        syllabusScrollPane.setContent(syllabusPane);
        setupTitledPane(syllabusPane, SYLLABUS_DESCRIPTION_TITLED_PANE, SYLLABUS_DESCRIPTION_TEXT_AREA, 0, 0, 1, 1);
        setupTitledPane(syllabusPane, SYLLABUS_TOPICS_TITLED_PANE, SYLLABUS_TOPICS_TEXT_AREA, 0, 1, 1, 1);
        setupTitledPane(syllabusPane, SYLLABUS_PREREQUISITES_TITLED_PANE, SYLLABUS_PREREQUISITES_TEXT_AREA, 0, 2, 1, 1);
        setupTitledPane(syllabusPane, SYLLABUS_OUTCOMES_TITLED_PANE, SYLLABUS_OUTCOMES_TEXT_AREA, 0, 3, 1, 1);
        setupTitledPane(syllabusPane, SYLLABUS_TEXTBOOKS_TITLED_PANE, SYLLABUS_TEXTBOOKS_TEXT_AREA, 0, 4, 1, 1);
        setupTitledPane(syllabusPane, SYLLABUS_GRADED_COMPONENTS_TITLED_PANE, SYLLABUS_GRADED_COMPONENTS_TEXT_AREA, 0, 5, 1, 1);
        setupTitledPane(syllabusPane, SYLLABUS_GRADING_NOTE_TITLED_PANE, SYLLABUS_GRADING_NOTE_TEXT_AREA, 0, 6, 1, 1);
        setupTitledPane(syllabusPane, SYLLABUS_ACADEMIC_DISHONESTY_TITLED_PANE, SYLLABUS_ACADEMIC_DISHONESTY_TEXT_AREA, 0, 7, 1, 1);
        setupTitledPane(syllabusPane, SYLLABUS_SPECIAL_ASSISTANCE_TITLED_PANE, SYLLABUS_SPECIAL_ASSISTANCE_TEXT_AREA, 0, 8, 1, 1);
    }
    
    private void initMT(TabPane tabPane) {
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        
        AppNodesBuilder csBuilder = app.getGUIModule().getNodesBuilder();
        
        Tab mtTab = csBuilder.buildTab(MT_TAB, tabPane, CLASS_TAB, ENABLED);
        
        ScrollPane mtScrollPane = new ScrollPane();
        mtTab.setContent(mtScrollPane);
        GridPane mtPane = csBuilder.buildGridPane(MT_PANE, null, CLASS_PANE, ENABLED);
        mtPane.setVgap(10);
        mtPane.setStyle("-fx-background-color: #f0b20a");
        mtPane.prefWidthProperty().bind(mtScrollPane.widthProperty());
        mtPane.prefHeightProperty().bind(mtScrollPane.heightProperty());
        mtScrollPane.setContent(mtPane);
        
        setupMeetingTime(4, 0, 0, mtPane, MT_LECTURE_ADD_BUTTON, MT_LECTURE_REMOVE_BUTTON, MT_LECTURES_LABEL,
                MT_LECTURES_TABLE_VIEW, MT_LECTURES_SECTION_TABLE_COLUMN, MT_LECTURES_DAYS_TABLE_COLUMN, MT_LECTURES_TIME_TABLE_COLUMN,
                null, MT_LECTURES_ROOM_TABLE_COLUMN, null, null);
        setupMeetingTime(5, 0, 1, mtPane, MT_RECITATIONS_ADD_BUTTON, MT_RECITATIONS_REMOVE_BUTTON, MT_RECITATIONS_LABEL,
                MT_RECITATIONS_TABLE_VIEW, MT_RECITATIONS_SECTION_TABLE_COLUMN, null, null, MT_RECITATIONS_EXACTTIME_TABLE_COLUMN,
                MT_RECITATIONS_ROOM_TABLE_COLUMN, MT_RECITATIONS_TA1_TABLE_COLUMN, MT_RECITATIONS_TA2_TABLE_COLUMN);
        setupMeetingTime(5, 0, 2, mtPane, MT_LABS_ADD_BUTTON, MT_LABS_REMOVE_BUTTON, MT_LABS_LABEL,
                MT_LABS_TABLE_VIEW, MT_LABS_SECTION_TABLE_COLUMN, null, null, MT_LABS_EXACTTIME_TABLE_COLUMN,
                MT_LABS_ROOM_TABLE_COLUMN, MT_LABS_TA1_TABLE_COLUMN, MT_LABS_TA2_TABLE_COLUMN);
    }
    
    private void initSchedule(TabPane tabPane) {
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        
        AppNodesBuilder csBuilder = app.getGUIModule().getNodesBuilder();
        
        AppGUIModule gui = app.getGUIModule();
        
        CourseSiteController controller = new CourseSiteController((CourseSiteApp) app);
        
        Tab scheduleTab = csBuilder.buildTab(SCHEDULE_TAB, tabPane, CLASS_TAB, ENABLED);
        
        ScrollPane scheduleScrollPane = new ScrollPane();
        scheduleScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scheduleTab.setContent(scheduleScrollPane);
        GridPane schedulePane = csBuilder.buildGridPane(SCHEDULE_PANE, null, CLASS_PANE, ENABLED);
        schedulePane.setVgap(10);
        schedulePane.setStyle("-fx-background-color: #f0b20a");
        schedulePane.prefWidthProperty().bind(scheduleScrollPane.widthProperty());
        scheduleScrollPane.setContent(schedulePane);
        
        GridPane calenderPane = csBuilder.buildGridPane(SCHEDULE_CALENDAR_PANE, null, CLASS_PANE, ENABLED);
        calenderPane.setVgap(10);
        calenderPane.setHgap(20);
        Label calenderLabel = csBuilder.buildLabel(SCHEDULE_CALENDAR_LABEL, calenderPane, 0, 0, 2, 1, CLASS_HEADER_LABEL, ENABLED);
        Label startLabel = csBuilder.buildLabel(SCHEDULE_START_LABEL, calenderPane, 0, 1, 1, 1, CLASS_LABEL, ENABLED);
        Label endLabel = csBuilder.buildLabel(SCHEDULE_END_LABEL, calenderPane, 2, 1, 1, 1, CLASS_LABEL, ENABLED);
        
        DatePicker startDate = csBuilder.buildDatePicker(SCHEDULE_START_DATEPICKER, calenderPane, 1, 1, CLASS_DATE_PICKER, ENABLED);
        startDate.setEditable(false);
        
        DatePicker endDate = csBuilder.buildDatePicker(SCHEDULE_END_DATEPICKER, calenderPane, 3, 1, CLASS_DATE_PICKER, ENABLED);
        endDate.setEditable(false);
        
        schedulePane.add(calenderPane, 0, 0);
        
        GridPane itemsPane = csBuilder.buildGridPane(SCHEDULE_ITEMS_PANE, null, CLASS_PANE, ENABLED);
        itemsPane.setVgap(10);
        itemsPane.setHgap(20);
        Button removeButton = csBuilder.buildTextButton(SCHEDULE_REMOVE_BUTTON, null, CLASS_BUTTON, ENABLED);
        itemsPane.add(removeButton, 0, 0);
        
        Label itemsLabel = csBuilder.buildLabel(SCHEDULE_ITEMS_LABEL, null, CLASS_LABEL, ENABLED);
        itemsPane.add(itemsLabel, 1, 0);
        
        TableView<MeetingTime> itemsTable = csBuilder.buildTableView(SCHEDULE_ITEMS_TABLE_VIEW, null, CLASS_TABLE_VIEW, ENABLED);
        itemsTable.prefWidthProperty().bind(schedulePane.widthProperty());
        itemsPane.add(itemsTable, 0, 1, 10, 1);
        
        TableColumn typeColumn = csBuilder.buildTableColumn(SCHEDULE_TYPE_TABLE_COLUMN, itemsTable, CLASS_COLUMN);
        typeColumn.setCellValueFactory(new PropertyValueFactory<String, String>("type"));
        typeColumn.prefWidthProperty().bind(schedulePane.widthProperty().multiply(1.0 / 5.0));
        
        TableColumn dateColumn = csBuilder.buildTableColumn(SCHEDULE_DATE_TABLE_COLUMN, itemsTable, CLASS_COLUMN);
        dateColumn.setCellValueFactory(new PropertyValueFactory<String, String>("date"));
        dateColumn.prefWidthProperty().bind(schedulePane.widthProperty().multiply(1.0 / 5.0));
        
        TableColumn titleColumn = csBuilder.buildTableColumn(SCHEDULE_TITLE_TABLE_COLUMN, itemsTable, CLASS_COLUMN);
        titleColumn.setCellValueFactory(new PropertyValueFactory<String, String>("title"));
        titleColumn.prefWidthProperty().bind(schedulePane.widthProperty().multiply(1.0 / 5.0));
        
        TableColumn topicColumn = csBuilder.buildTableColumn(SCHEDULE_TOPIC_TABLE_COLUMN, itemsTable, CLASS_COLUMN);
        topicColumn.setCellValueFactory(new PropertyValueFactory<String, String>("topic"));
        topicColumn.prefWidthProperty().bind(schedulePane.widthProperty().multiply(2.0 / 5.0));
        
        schedulePane.add(itemsPane, 0, 1);
        
        GridPane addPane = csBuilder.buildGridPane(SCHEDULE_ADD_PANE, null, CLASS_PANE, ENABLED);
        addPane.setHgap(30);
        addPane.setVgap(10);
        schedulePane.add(addPane, 0, 2);
        Label addLabel = csBuilder.buildLabel(SCHEDULE_ADD_LABEL, null, CLASS_HEADER_LABEL, ENABLED);
        addPane.add(addLabel, 0, 0);
        Label typeLabel = csBuilder.buildLabel(SCHEDULE_TYPE_LABEL, null, CLASS_LABEL, ENABLED);
        addPane.add(typeLabel, 0, 1);
        
        ObservableList<String> typeOpitions = FXCollections.observableArrayList();
        typeOpitions.addAll("Holiday", "HW", "Lecture", "Recitation", "Reference");
        final ComboBox typeComboBox = csBuilder.buildComboBox(SCHEDULE_TYPE_COMBOBOX, typeOpitions, null, CLASS_COMBOBOX, ENABLED);
        typeComboBox.setEditable(false);
        setupComboBoxListener(typeComboBox);
        addPane.add(typeComboBox, 1, 1);
        
        Label dateLabel = csBuilder.buildLabel(SCHEDULE_DATE_LABEL, null, CLASS_LABEL, ENABLED);
        addPane.add(dateLabel, 0, 2);
        
        DatePicker datePicker = csBuilder.buildDatePicker(SCHEDULE_DATE_DATEPICKER, addPane, 1, 2, CLASS_DATE_PICKER, ENABLED);
        datePicker.setEditable(false);
        
        setupLabelTextFieldCombo(addPane, 0, 3, SCHEDULE_TITLE_LABEL, SCHEDULE_TITLE_TEXT_FIELD);
        
        setupLabelTextFieldCombo(addPane, 0, 4, SCHEDULE_TOPIC_LABEL, SCHEDULE_TOPIC_TEXT_FIELD);
        
        setupLabelTextFieldCombo(addPane, 0, 5, SCHEDULE_LINK_LABEL, SCHEDULE_LINK_TEXT_FIELD);
        
        Button addButton = csBuilder.buildTextButton(SCHEDULE_ADD_BUTTON, null, CLASS_BUTTON, ENABLED);
        
        addPane.add(addButton, 0, 6);
        Button clearButton = csBuilder.buildTextButton(SCHEDULE_CLEAR_BUTTON, null, CLASS_BUTTON, ENABLED);
        addPane.add(clearButton, 1, 6);
        
               
        startDate.valueProperty().addListener(new ChangeListener<LocalDate>() {
            @Override
            public void changed(ObservableValue ov, LocalDate oldValue, LocalDate newValue) {
                if(startDate.isFocused() && oldValue != newValue) {
                    EditDatePicker_Transaction transaction = new EditDatePicker_Transaction(startDate, oldValue, newValue);
                    app.processTransaction(transaction);
                }
            }
        });
        
        endDate.valueProperty().addListener(new ChangeListener<LocalDate>() {
            @Override
            public void changed(ObservableValue ov, LocalDate oldValue, LocalDate newValue) {
                if(endDate.isFocused() && oldValue != newValue) {
                    EditDatePicker_Transaction transaction = new EditDatePicker_Transaction(endDate, oldValue, newValue);
                    app.processTransaction(transaction);
                }
            }
        });
        
        datePicker.valueProperty().addListener(new ChangeListener<LocalDate>() {
            @Override
            public void changed(ObservableValue ov, LocalDate oldValue, LocalDate newValue) {
                if(datePicker.isFocused() && oldValue != newValue) {
                    EditDatePicker_Transaction transaction = new EditDatePicker_Transaction(endDate, oldValue, newValue);
                    app.processTransaction(transaction);
                }
            }
        });
        
        startDate.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.getDayOfWeek() == DayOfWeek.TUESDAY || date.getDayOfWeek() == DayOfWeek.WEDNESDAY ||
                        date.getDayOfWeek() == DayOfWeek.THURSDAY || date.getDayOfWeek() == DayOfWeek.FRIDAY || 
                        date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY);
            }
        });
        
        endDate.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.getDayOfWeek() == DayOfWeek.TUESDAY || date.getDayOfWeek() == DayOfWeek.WEDNESDAY ||
                        date.getDayOfWeek() == DayOfWeek.THURSDAY || date.getDayOfWeek() == DayOfWeek.MONDAY || 
                        date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY ||
                        date.compareTo(startDate.getValue()) < 0);
            }
        });
        
        datePicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.compareTo(startDate.getValue()) < 0 ||
                        date.compareTo(endDate.getValue()) > 0);
            }
        });
        
        itemsTable.setOnMouseClicked( e -> { 
            if(itemsTable.getSelectionModel().getSelectedItem() != null) {
                addButton.setText("Update");
                MeetingTime mt = itemsTable.getSelectionModel().getSelectedItem();
                typeComboBox.getSelectionModel().select(mt.getType());
                ((TextField) gui.getGUINode(SCHEDULE_TITLE_TEXT_FIELD)).setText(mt.getTitle());
                ((TextField) gui.getGUINode(SCHEDULE_TOPIC_TEXT_FIELD)).setText(mt.getTopic());
                ((TextField) gui.getGUINode(SCHEDULE_LINK_TEXT_FIELD)).setText(mt.getLink());
                datePicker.setValue(getLocalDate(mt.getDate()));
            }
        });
        
        clearButton.setOnMouseClicked(e -> {
            if(itemsTable.getSelectionModel().getSelectedItem() != null) {
                addButton.setText("Add");
            }
            typeComboBox.setValue(null);
            ((DatePicker) gui.getGUINode(SCHEDULE_DATE_DATEPICKER)).setValue(null);
            ((TextField) gui.getGUINode(SCHEDULE_TITLE_TEXT_FIELD)).clear();
            ((TextField) gui.getGUINode(SCHEDULE_TOPIC_TEXT_FIELD)).clear();
            ((TextField) gui.getGUINode(SCHEDULE_LINK_TEXT_FIELD)).clear();
            itemsTable.getSelectionModel().clearSelection();
        });
        
        addButton.setOnMouseClicked(e -> { 
            String type = (String) typeComboBox.getValue();
            String date = getLocalDateString(datePicker.getValue());
            String title = ((TextField) gui.getGUINode(SCHEDULE_TITLE_TEXT_FIELD)).getText();
            String topic = ((TextField) gui.getGUINode(SCHEDULE_TOPIC_TEXT_FIELD)).getText();
            String link = ((TextField) gui.getGUINode(SCHEDULE_LINK_TEXT_FIELD)).getText();
            if(itemsTable.getSelectionModel().getSelectedItem() == null) {
                controller.processAddScheduleMeetingTime(type, date, title, topic, link);
            }
            else {
                MeetingTime mt = itemsTable.getSelectionModel().getSelectedItem();
                controller.processEditScheduleMeetingTime(mt, type, date, title, topic, link);
            }
            itemsTable.refresh();
        });
        
        removeButton.setOnMouseClicked(e -> {
            if(itemsTable.getSelectionModel().getSelectedItem() != null) {
                MeetingTime mt = itemsTable.getSelectionModel().getSelectedItem();
                controller.processRemoveMeetingTime(4, mt);
            }
        });
    }
    
    private void initOh(TabPane tabPane) {
        // FIRST LOAD THE FONT FAMILIES FOR THE COMBO BOX
        PropertiesManager props = PropertiesManager.getPropertiesManager();

        // THIS WILL BUILD ALL OF OUR JavaFX COMPONENTS FOR US
        AppNodesBuilder csBuilder = app.getGUIModule().getNodesBuilder();
        
        Tab ohTab = csBuilder.buildTab(OH_TAB, tabPane, CLASS_TAB, ENABLED);
        ScrollPane ohScrollPane = new ScrollPane();
        ohScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        GridPane ohPane = csBuilder.buildGridPane(OH_PANE, null, CLASS_PANE, ENABLED);
        ohPane.setStyle("-fx-background-color: #f0b20a;");
        ohScrollPane.setContent(ohPane);
        ohPane.prefWidthProperty().bind(ohScrollPane.widthProperty());
        ohTab.setContent(ohScrollPane);
        
        GridPane typeHeaderBox = csBuilder.buildGridPane(OH_GRAD_UNDERGRAD_TAS_PANE, null, CLASS_PANE, ENABLED);
        ohPane.add(typeHeaderBox, 0, 0);
        typeHeaderBox.setVgap(50);
        typeHeaderBox.setHgap(100);
        typeHeaderBox.prefWidthProperty().bind(ohPane.widthProperty());
        Button removeTAButton = csBuilder.buildTextButton(OH_REMOVE_TA_BUTTON, null, CLASS_BUTTON, ENABLED);
        typeHeaderBox.add(removeTAButton, 0, 0);
        ToggleGroup tg = new ToggleGroup();
        RadioButton all = csBuilder.buildRadioButton(OH_ALL_RADIO_BUTTON, null, CLASS_RADIO_BUTTON, ENABLED, tg, true);
        typeHeaderBox.add(all, 1, 0);
        RadioButton grad = csBuilder.buildRadioButton(OH_GRAD_RADIO_BUTTON, null, CLASS_RADIO_BUTTON, ENABLED, tg, false);
        typeHeaderBox.add(grad, 2, 0);
        RadioButton undergrad = csBuilder.buildRadioButton(OH_UNDERGRAD_RADIO_BUTTON, null, CLASS_RADIO_BUTTON, ENABLED, tg, false);
        typeHeaderBox.add(undergrad, 3, 0);

        // MAKE THE TABLE AND SETUP THE DATA MODEL
        TableView<TeachingAssistantPrototype> taTable = csBuilder.buildTableView(OH_TAS_TABLE_VIEW, null, CLASS_TABLE_VIEW, ENABLED);
        taTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        TableColumn nameColumn = csBuilder.buildTableColumn(OH_NAME_TABLE_COLUMN, taTable, CLASS_COLUMN);
        TableColumn emailColumn = csBuilder.buildTableColumn(OH_EMAIL_TABLE_COLUMN, taTable, CLASS_COLUMN);
        TableColumn slotsColumn = csBuilder.buildTableColumn(OH_SLOTS_TABLE_COLUMN, taTable, CLASS_CENTERED_COLUMN);
        TableColumn typeColumn = csBuilder.buildTableColumn(OH_TYPE_TABLE_COLUMN, taTable, CLASS_COLUMN);
        nameColumn.setCellValueFactory(new PropertyValueFactory<String, String>("name"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<String, String>("email"));
        slotsColumn.setCellValueFactory(new PropertyValueFactory<String, String>("slots"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<String, String>("type"));
        nameColumn.prefWidthProperty().bind(taTable.widthProperty().multiply(1.0 / 5.0));
        emailColumn.prefWidthProperty().bind(taTable.widthProperty().multiply(2.0 / 5.0));
        slotsColumn.prefWidthProperty().bind(taTable.widthProperty().multiply(1.0 / 5.0));
        typeColumn.prefWidthProperty().bind(taTable.widthProperty().multiply(1.0 / 5.0));
        ohPane.add(taTable, 0, 1);

        // ADD BOX FOR ADDING A TA
        HBox taBox = csBuilder.buildHBox(OH_ADD_TA_PANE, null, CLASS_PANE, ENABLED);
        csBuilder.buildTextField(OH_NAME_TEXT_FIELD, taBox, CLASS_TEXT_FIELD, ENABLED);
        csBuilder.buildTextField(OH_EMAIL_TEXT_FIELD, taBox, CLASS_TEXT_FIELD, ENABLED);
        csBuilder.buildTextButton(OH_ADD_TA_BUTTON, taBox, CLASS_BUTTON, !ENABLED);
        ohPane.add(taBox, 0, 2);

        GridPane ohHeaderPane = csBuilder.buildGridPane(OH_HEADER_PANE, null, CLASS_PANE, ENABLED);
        ohHeaderPane.setVgap(50);
        ohHeaderPane.setHgap(100);
        ohPane.add(ohHeaderPane, 0, 3);
        Label ohLabel = csBuilder.buildLabel(OH_OFFICE_HOURS_HEADER_LABEL, null, CLASS_HEADER_LABEL, ENABLED);
        ohHeaderPane.add(ohLabel, 0, 0);
        
        Label startTimeLabel =csBuilder.buildLabel(OH_START_TIME_LABEL, null, CLASS_LABEL, ENABLED);
        ohHeaderPane.add(startTimeLabel, 1, 0);
        ObservableList<String> startTime = FXCollections.observableArrayList();
        final ComboBox startTimeComboBox = csBuilder.buildComboBox(OH_START_TIME_COMBOBOX, startTime, null, CLASS_COMBOBOX, ENABLED);
        setupOHComboBoxContent(startTimeComboBox);
        startTimeComboBox.getSelectionModel().selectFirst();
        ohHeaderPane.add(startTimeComboBox, 2, 0);
        
        Label endTimeLabel =csBuilder.buildLabel(OH_END_TIME_LABEL, null, CLASS_LABEL, ENABLED);
        ohHeaderPane.add(endTimeLabel, 3, 0);
        ObservableList<String> endTime = FXCollections.observableArrayList();
        final ComboBox endTimeComboBox = csBuilder.buildComboBox(OH_END_TIME_COMBOBOX, endTime, null, CLASS_COMBOBOX, ENABLED);
        setupOHComboBoxContent(endTimeComboBox);
        endTimeComboBox.getSelectionModel().selectLast();
        ohHeaderPane.add(endTimeComboBox, 4, 0);
        
        // SETUP THE OFFICE HOURS TABLE
        TableView<TimeSlot> officeHoursTable = csBuilder.buildTableView(OH_OFFICE_HOURS_TABLE_VIEW, null, CLASS_OFFICE_HOURS_TABLE_VIEW, ENABLED);
        setupOfficeHoursColumn(OH_START_TIME_TABLE_COLUMN, officeHoursTable, CLASS_TIME_COLUMN, "startTime");
        setupOfficeHoursColumn(OH_END_TIME_TABLE_COLUMN, officeHoursTable, CLASS_TIME_COLUMN, "endTime");
        setupOfficeHoursColumn(OH_MONDAY_TABLE_COLUMN, officeHoursTable, CLASS_DAY_OF_WEEK_COLUMN, "monday");
        setupOfficeHoursColumn(OH_TUESDAY_TABLE_COLUMN, officeHoursTable, CLASS_DAY_OF_WEEK_COLUMN, "tuesday");
        setupOfficeHoursColumn(OH_WEDNESDAY_TABLE_COLUMN, officeHoursTable, CLASS_DAY_OF_WEEK_COLUMN, "wednesday");
        setupOfficeHoursColumn(OH_THURSDAY_TABLE_COLUMN, officeHoursTable, CLASS_DAY_OF_WEEK_COLUMN, "thursday");
        setupOfficeHoursColumn(OH_FRIDAY_TABLE_COLUMN, officeHoursTable, CLASS_DAY_OF_WEEK_COLUMN, "friday");
        ohPane.add(officeHoursTable, 0, 4);
        
        setupOHComboBoxListener(endTimeComboBox);
        setupOHComboBoxListener(startTimeComboBox);
    }
    
    private void initSite_BannerPane(GridPane sitePane) {
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        
        AppNodesBuilder csBuilder = app.getGUIModule().getNodesBuilder();

        GridPane bannerPane = csBuilder.buildGridPane(SITE_BANNER_PANE, null, CLASS_PANE, ENABLED);
        sitePane.add(bannerPane, 0, 0);
        bannerPane.setVgap(20);
        bannerPane.setHgap(50);
        
        Label bannerLabel = csBuilder.buildLabel(SITE_BANNER_LABEL, null, CLASS_HEADER_LABEL, ENABLED);
        bannerPane.add(bannerLabel, 0, 0);
        
        //SUBJECT
        Label subjectLabel = csBuilder.buildLabel(SITE_SUBJECT_LABEL, null, CLASS_HEADER_LABEL, ENABLED);
        bannerPane.add(subjectLabel, 0, 1);
        
        ObservableList<String> subjectOpitions = FXCollections.observableArrayList();
        final ComboBox subjectComboBox = csBuilder.buildComboBox(SITE_SUBJECT_COMBOBOX, subjectOpitions, null, CLASS_COMBOBOX, ENABLED);
        subjectComboBox.setEditable(true);
        setupComboBoxListener(subjectComboBox);
        bannerPane.add(subjectComboBox, 1 , 1);
        
        //NUMBER
        Label numberLabel = csBuilder.buildLabel(SITE_NUMBER_LABEL, null, CLASS_HEADER_LABEL, ENABLED);
        bannerPane.add(numberLabel, 2, 1);
        
        ObservableList<String> numberOpitions = FXCollections.observableArrayList();
        final ComboBox numberComboBox = csBuilder.buildComboBox(SITE_NUMBER_COMBOBOX, numberOpitions, null, CLASS_COMBOBOX, ENABLED);
        numberComboBox.setEditable(true);
        setupComboBoxListener(numberComboBox);
        bannerPane.add(numberComboBox, 3, 1);
        
        //SEMESTER
        Label semesterLabel = csBuilder.buildLabel(SITE_SEMESTER_LABEL, null, CLASS_HEADER_LABEL, ENABLED);
        bannerPane.add(semesterLabel, 0, 2);
        
        ObservableList<String> semesterOpitions = FXCollections.observableArrayList();
        semesterOpitions.addAll("Spring", "Summer", "Fall", "Winter");
        final ComboBox semesterComboBox = csBuilder.buildComboBox(SITE_SEMESTER_COMBOBOX, semesterOpitions, null, CLASS_COMBOBOX, ENABLED);
        semesterComboBox.getSelectionModel().selectFirst();
        setupComboBoxListener(semesterComboBox);
        bannerPane.add(semesterComboBox, 1, 2);
        
        //YEAR
        Label yearLabel = csBuilder.buildLabel(SITE_YEAR_LABEL, null, CLASS_HEADER_LABEL, ENABLED);
        bannerPane.add(yearLabel, 2, 2);
        
        ObservableList<String> yearOpitions = FXCollections.observableArrayList();
        int year = Calendar.getInstance().get(Calendar.YEAR);
        yearOpitions.add(Integer.toString(year));
        yearOpitions.add(Integer.toString(year+1));
        final ComboBox yearComboBox = csBuilder.buildComboBox(SITE_YEAR_COMBOBOX, yearOpitions, null, CLASS_COMBOBOX, ENABLED);
        yearComboBox.getSelectionModel().selectFirst();
        setupComboBoxListener(yearComboBox);
        bannerPane.add(yearComboBox, 3, 2);
        
        //TITLE
        Label titleLabel = csBuilder.buildLabel(SITE_TITLE_LABEL, null, CLASS_HEADER_LABEL, ENABLED);
        bannerPane.add(titleLabel, 0, 3);
        
        TextField titleTextfield = csBuilder.buildTextField(SITE_TITLE_TEXT_FIELD, null, CLASS_TEXT_FIELD, ENABLED);
        bannerPane.add(titleTextfield, 1, 3, 4, 1);
        
        CourseSiteController controller = new CourseSiteController((CourseSiteApp) app);
        titleTextfield.focusedProperty().addListener(e -> { 
            if(titleTextfield.isFocused()) {
                controller.processEditTextField(false, titleTextfield.getText(), null, titleTextfield);
            }
            else {
                controller.processEditTextField(true, null, titleTextfield.getText(), titleTextfield);
            }
        });
        
        //EXPORT DIR
        Label exportLabel = csBuilder.buildLabel(SITE_EXPORTDIR_LABEL, null, CLASS_HEADER_LABEL, ENABLED);
        bannerPane.add(exportLabel, 0, 4);
        
        Label exportTargetLabel = csBuilder.buildLabel(SITE_EXPORTDIR_TARGET_LABEL, null, CLASS_LABEL, ENABLED);
        bannerPane.add(exportTargetLabel, 1, 4);
    }
    
    private void initSite_PagesPane(GridPane sitePane) {
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        
        AppNodesBuilder csBuilder = app.getGUIModule().getNodesBuilder();
        
        GridPane pagesPane = csBuilder.buildGridPane(SITE_PAGES_PANE, null, CLASS_PANE, ENABLED);
        sitePane.add(pagesPane, 0, 1);
        pagesPane.setVgap(50);
        pagesPane.setHgap(100);
        
        Label pagesLabel = csBuilder.buildLabel(SITE_PAGES_LABEL, null, CLASS_HEADER_LABEL, ENABLED);
        pagesPane.add(pagesLabel, 0, 0);
        
        CheckBox homeCheckBox = csBuilder.buildCheckBox(SITE_HOME_CHECKBOX, null, CLASS_CHECKBOX, ENABLED);
        homeCheckBox.setSelected(true);
        pagesPane.add(homeCheckBox, 1, 0);
        
        CheckBox syllabusCheckBox = csBuilder.buildCheckBox(SITE_SYLLABUS_CHECKBOX, null, CLASS_CHECKBOX, ENABLED);
        syllabusCheckBox.setSelected(true);
        pagesPane.add(syllabusCheckBox, 2, 0);
        
        CheckBox scheduleCheckBox = csBuilder.buildCheckBox(SITE_SCHEDULE_CHECKBOX, null, CLASS_CHECKBOX, ENABLED);
        scheduleCheckBox.setSelected(true);
        pagesPane.add(scheduleCheckBox, 3, 0);
        
        CheckBox HWsCheckBox = csBuilder.buildCheckBox(SITE_HWS_CHECKBOX, null, CLASS_CHECKBOX, ENABLED);
        HWsCheckBox.setSelected(true);
        pagesPane.add(HWsCheckBox, 4, 0);
    }
    
    private void initSite_StylePane(GridPane sitePane) {
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        
        AppNodesBuilder csBuilder = app.getGUIModule().getNodesBuilder();

        GridPane stylePane = csBuilder.buildGridPane(SITE_STYLE_PANE, null, CLASS_PANE, ENABLED);
        sitePane.add(stylePane, 0, 2);
        stylePane.setVgap(20);
        stylePane.setHgap(50);
        
        Label style = csBuilder.buildLabel(SITE_STYLE_LABEL, null, CLASS_HEADER_LABEL, ENABLED);
        stylePane.add(style, 0, 0);
        
        Button faviconButton = csBuilder.buildTextButton(SITE_FAVICON_BUTTON, null, CLASS_BUTTON, ENABLED);
        stylePane.add(faviconButton, 0, 1);
        
        Button navbarButton = csBuilder.buildTextButton(SITE_NAVBAR_BUTTON, null, CLASS_BUTTON, ENABLED);
        
        stylePane.add(navbarButton, 0, 2);
        
        Button LeftFooterButton = csBuilder.buildTextButton(SITE_LEFT_FOOTER_BUTTON, null, CLASS_BUTTON, ENABLED);
        stylePane.add(LeftFooterButton, 0, 3);
        
        Button RightFooterButton = csBuilder.buildTextButton(SITE_RIGHT_FOOTER_BUTTON, null, CLASS_BUTTON, ENABLED);
        stylePane.add(RightFooterButton, 0, 4);
        
        Label cssLabel = csBuilder.buildLabel(SITE_CSS_LABEL, null, CLASS_LABEL, ENABLED);
        cssLabel.setStyle("-fx-font-weight: bold");
        stylePane.add(cssLabel, 0, 5);
        
        Label cssNoteLabel =csBuilder.buildLabel(SITE_CSS_NOTE_LABEL, null, CLASS_LABEL, ENABLED);
        cssNoteLabel.setStyle("-fx-font-weight: bold");
        stylePane.add(cssNoteLabel, 0, 6);
        
        ObservableList<String> cssOpitions = FXCollections.observableArrayList();
        final ComboBox cssComboBox = csBuilder.buildComboBox(SITE_CSS_COMBOBOX, cssOpitions, null, CLASS_COMBOBOX, ENABLED);
        stylePane.add(cssComboBox, 1, 5);
        
        ImageView favicon = csBuilder.buildImageView(SITE_FAVICON_IMAGE_VIEW, "file:./original_web/images/Favicon.ico", stylePane, 1, 1, CLASS_IMAGE_VIEW, ENABLED);
        setupLoadImageButton(faviconButton, favicon);
        
        ImageView navbar = csBuilder.buildImageView(SITE_NAVBAR_IMAGE_VIEW, "file:./original_web/images/SBUDarkRedShieldLogo.png", stylePane, 1, 2, CLASS_IMAGE_VIEW, ENABLED);
        setupLoadImageButton(navbarButton, navbar);
        
        ImageView leftFooter = csBuilder.buildImageView(SITE_LEFT_FOOTER_IMAGE_VIEW, "file:./original_web/images/SBUWhiteShieldLogo.jpg", stylePane, 1, 3, CLASS_IMAGE_VIEW, ENABLED);
        setupLoadImageButton(LeftFooterButton, leftFooter);
        
        ImageView rightFooter = csBuilder.buildImageView(SITE_RIGHT_FOOTER_IMAGE_VIEW, "file:./original_web/images/SBUCSLogo.png", stylePane, 1, 4, CLASS_IMAGE_VIEW, ENABLED);
        setupLoadImageButton(RightFooterButton, rightFooter);
    }
    
    private void initSite_InstructorPane(GridPane sitePane) {
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        
        AppNodesBuilder csBuilder = app.getGUIModule().getNodesBuilder();

        GridPane instructorPane = csBuilder.buildGridPane(SITE_INSTRUCTOR_PANE, null, CLASS_PANE, ENABLED);
        sitePane.add(instructorPane, 0, 3);
        instructorPane.setVgap(20);
        instructorPane.setHgap(50);
        
        Label instructorLabel = csBuilder.buildLabel(SITE_INSTRUCTOR_LABEL, null, CLASS_HEADER_LABEL, ENABLED);
        instructorPane.add(instructorLabel, 0, 0);
        
        setupLabelTextFieldCombo(instructorPane, 0, 1, SITE_INSTRUCTOR_NAME_LABEL, SITE_INSTRUCTOR_NAME_TEXT_FIELD);
        
        setupLabelTextFieldCombo(instructorPane, 2, 1, SITE_INSTRUCTOR_ROOM_LABEL, SITE_INSTRUCTOR_ROOM_TEXT_FIELD);
        
        setupLabelTextFieldCombo(instructorPane, 0, 2, SITE_INSTRUCTOR_EMAIL_LABEL, SITE_INSTRUCTOR_EMAIL_TEXT_FIELD);
        
        setupLabelTextFieldCombo(instructorPane, 2, 2, SITE_INSTRUCTOR_HOME_PAGE_LABEL, SITE_INSTRUCTOR_HOME_PAGE_TEXT_FIELD);
        
        setupTitledPane(instructorPane, SITE_INSTRUCTOR_TITLED_PANE, SITE_INSTRUCTOR_OH_TEXT_AREA, 0, 3, 10, 1);
        instructorPane.prefWidthProperty().bind(sitePane.widthProperty());
    }
    
    private void setupOfficeHoursColumn(Object columnId, TableView tableView, String styleClass, String columnDataProperty) {
        AppNodesBuilder builder = app.getGUIModule().getNodesBuilder();
        TableColumn<TeachingAssistantPrototype, String> column = builder.buildTableColumn(columnId, tableView, styleClass);
        column.setCellValueFactory(new PropertyValueFactory<TeachingAssistantPrototype, String>(columnDataProperty));
        column.prefWidthProperty().bind(tableView.widthProperty().multiply(1.0 / 7.0));
        column.setCellFactory(col -> {
            return new TableCell<TeachingAssistantPrototype, String>() {
                @Override
                protected void updateItem(String text, boolean empty) {
                    super.updateItem(text, empty);
                    if (text == null || empty) {
                        setText(null);
                        setStyle("");
                    } else {
                        // CHECK TO SEE IF text CONTAINS THE NAME OF
                        // THE CURRENTLY SELECTED TA
                        setText(text);
                        TableView<TeachingAssistantPrototype> tasTableView = (TableView) app.getGUIModule().getGUINode(OH_TAS_TABLE_VIEW);
                        TeachingAssistantPrototype selectedTA = tasTableView.getSelectionModel().getSelectedItem();
                        if (selectedTA == null) {
                            setStyle("");
                        } else if (text.contains(selectedTA.getName())) {
                            setStyle("-fx-background-color: yellow");
                        } else {
                            setStyle("");
                        }
                    }
                }
            };
        });
    }

    private void setupLoadImageButton(Button bt, ImageView iv) {
        bt.setOnMouseClicked(e -> {
            File selectedFile = AppDialogsFacade.showOpenImageDialog(app.getGUIModule().getWindow(), LOAD_IMAGE_TITLE);
            if (selectedFile != null) {
                String newPath = selectedFile.getPath();
                ChangeImage_Transaction transaction = new ChangeImage_Transaction(iv, iv.getImage().impl_getUrl(), FILE_PROTOCOL+newPath);
                app.processTransaction(transaction);
            }
        });
    }
    
    private void setupComboBoxListener(ComboBox cb) {
        cb.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue ov, String oldValue, String newValue) {
                if(!cb.getItems().contains(newValue) && newValue != null) {
                    AddComboBoxItem_Transaction transaction = new AddComboBoxItem_Transaction(oldValue, newValue, cb);
                    app.processTransaction(transaction);
                }
                else if(cb.isFocused() && newValue != null) {
                    SelectComboBoxItem_Transaction transaction = new SelectComboBoxItem_Transaction(oldValue, newValue, cb);
                    app.processTransaction(transaction);
                }
                updateExportDir();
            }
        });
    }
    
    private void setupLabelTextFieldCombo(GridPane pane, int col, int row, Object labelId, Object textFieldId) {
        CourseSiteController controller = new CourseSiteController((CourseSiteApp) app);
        AppNodesBuilder csBuilder = app.getGUIModule().getNodesBuilder();
        
        Label label = csBuilder.buildLabel(labelId, null, CLASS_LABEL, ENABLED);
        pane.add(label, col, row);
        TextField textField = csBuilder.buildTextField(textFieldId, null, CLASS_TEXT_FIELD, ENABLED);
        pane.add(textField, col+1, row);
        textField.focusedProperty().addListener(e -> { 
            if(textField.isFocused()) {
                controller.processEditTextField(false, textField.getText(), null, textField);
            }
            else {
                controller.processEditTextField(true, null, textField.getText(), textField);
            }
        });
    }
    
    private void setupOHComboBoxContent(ComboBox cb) {
        for(int i = CourseSiteData.MIN_START_HOUR; i<=11; i++) {
            cb.getItems().add(i+":00am");
            cb.getItems().add(i+":30am");
        }
        cb.getItems().add("12:00pm");
        cb.getItems().add("12:30pm");
        for(int i = 1; i<=CourseSiteData.MAX_END_HOUR-12; i++) {
            cb.getItems().add(i+":00pm");
            cb.getItems().add(i+":30pm");
        }
        if(CourseSiteData.MAX_END_HOUR == 23) {
           cb.getItems().add("11:59pm"); 
        }
        else {
            cb.getItems().add(CourseSiteData.MAX_END_HOUR-11+":00pm");
        }
    }
    
    private void setupOHComboBoxListener(ComboBox cb) {
        CourseSiteController controller = new CourseSiteController((CourseSiteApp) app);
        AppGUIModule gui = app.getGUIModule();
        final ComboBox startTimeComboBox = (ComboBox) gui.getGUINode(OH_START_TIME_COMBOBOX);
        final ComboBox endTimeComboBox = (ComboBox) gui.getGUINode(OH_END_TIME_COMBOBOX);
        cb.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue ov, String s, String s1) {
                String startHour = (String) startTimeComboBox.getValue();
                String endHour = (String) endTimeComboBox.getValue();
                controller.processUpdateOH(startHour, endHour);
            }
        });
    }
    
    private void setupTitledPane(GridPane pane, Object titledPaneNodeId, Object textAreaNodeId, int col, int row, int colspan, int rowspan) {
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        
        CourseSiteController controller = new CourseSiteController((CourseSiteApp) app);
        
        AppNodesBuilder csBuilder = app.getGUIModule().getNodesBuilder();
        
        TitledPane titledPane = csBuilder.buildTitledPane(titledPaneNodeId, null, CLASS_TITLED_PANE, ENABLED);
        titledPane.setExpanded(false);
        TextArea textArea = csBuilder.buildTextArea(textAreaNodeId, null, CLASS_TEXT_FIELD, ENABLED);
        textArea.prefWidthProperty().bind(pane.widthProperty());
        titledPane.setContent(textArea);
        pane.add(titledPane, col, row, colspan, rowspan);
        
        textArea.focusedProperty().addListener(e -> {
            if(textArea.isFocused()) {
                controller.processEditTextArea(false, textArea.getText(), null, textArea);
            }
            else {
                controller.processEditTextArea(true, null, textArea.getText(), textArea);
            }
        });
    }
    
    private void setupMeetingTime(int count, int col, int row, GridPane parentpane, Object addButtonId, Object removeButtonId, Object labelId,
            Object tableId, Object sectionId, Object daysId, Object timeId, Object exactTimeId, Object roomId,
            Object ta1Id, Object ta2Id) {
        
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        
        AppNodesBuilder csBuilder = app.getGUIModule().getNodesBuilder();
        
        CourseSiteController controller = new CourseSiteController((CourseSiteApp) app);
        
        GridPane pane = csBuilder.buildGridPane(MT_LECTURES_PANE, null, CLASS_PANE, ENABLED);
        
        HBox controllerPane = new HBox();
        pane.add(controllerPane, 0, 0);
        
        Button addButton = csBuilder.buildTextButton(addButtonId, null, CLASS_BUTTON, ENABLED);
        controllerPane.getChildren().add(addButton);
        
        Button removeButton = csBuilder.buildTextButton(removeButtonId, null, CLASS_BUTTON, false);
        controllerPane.getChildren().add(removeButton);
        
        Label label = csBuilder.buildLabel(labelId, null, CLASS_HEADER_LABEL, ENABLED);
        controllerPane.getChildren().add(label);
        
        int mode;
        if(daysId != null) {
            mode = 0;
        }
        else {
            mode = 1;
        }
        
        TableView<MeetingTime> mtTable = csBuilder.buildTableView(tableId, null, CLASS_TABLE_VIEW, ENABLED);
        mtTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        if(sectionId != null) {
            TableColumn sectionColumn = csBuilder.buildTableColumn(sectionId, mtTable, CLASS_COLUMN);
            sectionColumn.setCellValueFactory(new PropertyValueFactory<String, String>("section"));
            sectionColumn.setCellFactory(TextFieldTableCell.forTableColumn());
            sectionColumn.prefWidthProperty().bind(parentpane.widthProperty().multiply(1.0 / count));
            setupMeetingTimeColumn(mtTable, sectionColumn, controller, mode);
        }
        if(daysId != null) {
            TableColumn daysColumn = csBuilder.buildTableColumn(daysId, mtTable, CLASS_COLUMN);
            daysColumn.setCellValueFactory(new PropertyValueFactory<String, String>("days"));
            daysColumn.setCellFactory(TextFieldTableCell.forTableColumn());
            daysColumn.prefWidthProperty().bind(parentpane.widthProperty().multiply(1.0 / count));
            setupMeetingTimeColumn(mtTable, daysColumn, controller, mode);
        }
        if(timeId != null) {
            TableColumn timeColumn = csBuilder.buildTableColumn(timeId, mtTable, CLASS_COLUMN);
            timeColumn.setCellValueFactory(new PropertyValueFactory<String, String>("time"));
            timeColumn.setCellFactory(TextFieldTableCell.forTableColumn());
            timeColumn.prefWidthProperty().bind(parentpane.widthProperty().multiply(1.0 / count));
            setupMeetingTimeColumn(mtTable, timeColumn, controller, mode);
        }
        if(exactTimeId != null) {
            TableColumn exactTimeColumn = csBuilder.buildTableColumn(exactTimeId, mtTable, CLASS_COLUMN);
            exactTimeColumn.setCellValueFactory(new PropertyValueFactory<String, String>("exactTime"));
            exactTimeColumn.setCellFactory(TextFieldTableCell.forTableColumn());
            exactTimeColumn.prefWidthProperty().bind(parentpane.widthProperty().multiply(1.0 / count));
            setupMeetingTimeColumn(mtTable, exactTimeColumn, controller, mode);
        }
        if(roomId != null) {
            TableColumn roomColumn = csBuilder.buildTableColumn(roomId, mtTable, CLASS_COLUMN);
            roomColumn.setCellValueFactory(new PropertyValueFactory<String, String>("room"));
            roomColumn.setCellFactory(TextFieldTableCell.forTableColumn());
            roomColumn.prefWidthProperty().bind(parentpane.widthProperty().multiply(1.0 / count));
            setupMeetingTimeColumn(mtTable, roomColumn, controller, mode);
        }
        if(ta1Id != null) {
            TableColumn ta1Column = csBuilder.buildTableColumn(ta1Id, mtTable, CLASS_COLUMN);
            ta1Column.setCellValueFactory(new PropertyValueFactory<String, String>("ta1"));
            ta1Column.setCellFactory(TextFieldTableCell.forTableColumn());
            ta1Column.prefWidthProperty().bind(parentpane.widthProperty().multiply(1.0 / count));
            setupMeetingTimeColumn(mtTable, ta1Column, controller, mode);
        }
        if(ta2Id != null) {
            TableColumn ta2Column = csBuilder.buildTableColumn(ta2Id, mtTable, CLASS_COLUMN);
            ta2Column.setCellValueFactory(new PropertyValueFactory<String, String>("ta2"));
            ta2Column.setCellFactory(TextFieldTableCell.forTableColumn());
            ta2Column.prefWidthProperty().bind(parentpane.widthProperty().multiply(1.0 / count));
            setupMeetingTimeColumn(mtTable, ta2Column, controller, mode);
        }
        
        mtTable.setOnMouseClicked(e -> {
                if(mtTable.getSelectionModel().getSelectedItem() != null) {
                    removeButton.setDisable(false);
                    mtTable.setEditable(ENABLED);
                }
            });
         
        pane.add(mtTable, 0, 1);
        mtTable.prefWidthProperty().bind(parentpane.widthProperty());
        parentpane.add(pane, col, row);
        
        if(addButtonId == MT_LECTURE_ADD_BUTTON) {
            addButton.setOnMouseClicked(e -> {
                controller.processAddMeetingTime(1);
                mtTable.refresh();
            });
            removeButton.setOnMouseClicked(e -> {
               MeetingTime mt = mtTable.getSelectionModel().getSelectedItem();
               if(mt != null) {
                   controller.processRemoveMeetingTime(1, mt);
                   mtTable.refresh();
                   removeButton.setDisable(true);
               }
            });
        }
        else if(addButtonId == MT_RECITATIONS_ADD_BUTTON) {
            addButton.setOnMouseClicked(e -> {
                controller.processAddMeetingTime(2);
                mtTable.refresh();
            });
            removeButton.setOnMouseClicked(e -> {
               MeetingTime mt = mtTable.getSelectionModel().getSelectedItem();
               if(mt != null) {
                   controller.processRemoveMeetingTime(2, mt);
                   mtTable.refresh();
                   removeButton.setDisable(true);
               }
            });
        }
        else if(addButtonId == MT_LABS_ADD_BUTTON) {
            addButton.setOnMouseClicked(e -> {
                controller.processAddMeetingTime(3);
                mtTable.refresh();
            });
            removeButton.setOnMouseClicked(e -> {
               MeetingTime mt = mtTable.getSelectionModel().getSelectedItem();
               if(mt != null) {
                   controller.processRemoveMeetingTime(3, mt);
                   mtTable.refresh();
                   removeButton.setDisable(true);
               }
            });
        }
    }
    
    private void updateExportDir() {
        AppGUIModule gui = (AppGUIModule)app.getGUIModule();
        
        Label exportTargLabel = (Label) gui.getGUINode(SITE_EXPORTDIR_TARGET_LABEL);
        
        ComboBox subject = (ComboBox) gui.getGUINode(SITE_SUBJECT_COMBOBOX);
        ComboBox number = (ComboBox) gui.getGUINode(SITE_NUMBER_COMBOBOX);
        ComboBox semester = (ComboBox) gui.getGUINode(SITE_SEMESTER_COMBOBOX);
        ComboBox year = (ComboBox) gui.getGUINode(SITE_YEAR_COMBOBOX);
        
        exportTargLabel.setText(".\\export\\"+subject.getValue()+"_"+number.getValue()+"_"
                +semester.getValue()+"_"+year.getValue());
    }
    
    public void initControllers() {
        CourseSiteController controller = new CourseSiteController((CourseSiteApp) app);
        AppGUIModule gui = app.getGUIModule();

        // FOOLPROOF DESIGN STUFF
        TextField nameTextField = ((TextField) gui.getGUINode(OH_NAME_TEXT_FIELD));
        TextField emailTextField = ((TextField) gui.getGUINode(OH_EMAIL_TEXT_FIELD));

        nameTextField.textProperty().addListener(e -> {
            controller.processTypeTA();
        });
        emailTextField.textProperty().addListener(e -> {
            controller.processTypeTA();
        });

        // FIRE THE ADD EVENT ACTION
        nameTextField.setOnAction(e -> {
            controller.processAddTA();
        });
        emailTextField.setOnAction(e -> {
            controller.processAddTA();
        });
        ((Button) gui.getGUINode(OH_ADD_TA_BUTTON)).setOnAction(e -> {
            controller.processAddTA();
        });
        
        CheckBox homeCheckBox = (CheckBox) gui.getGUINode(SITE_HOME_CHECKBOX);
        CheckBox syllabusCheckBox = (CheckBox) gui.getGUINode(SITE_SYLLABUS_CHECKBOX);
        CheckBox scheduleCheckBox = (CheckBox) gui.getGUINode(SITE_SCHEDULE_CHECKBOX);
        CheckBox HWsCheckBox = (CheckBox) gui.getGUINode(SITE_HWS_CHECKBOX);
        
        homeCheckBox.setOnMouseClicked(e -> {
            controller.processTypeExport();
        });
        syllabusCheckBox.setOnMouseClicked(e -> {
            controller.processTypeExport();
        });
        scheduleCheckBox.setOnMouseClicked(e -> {
            controller.processTypeExport();
        });
        HWsCheckBox.setOnMouseClicked(e -> {
            controller.processTypeExport();
        });
        
        Button removeTAButton = (Button) gui.getGUINode(OH_REMOVE_TA_BUTTON);
        removeTAButton.setOnMouseClicked(e -> {
            controller.processRemoveTA();
        });

        TableView officeHoursTableView = (TableView) gui.getGUINode(OH_OFFICE_HOURS_TABLE_VIEW);
        officeHoursTableView.getSelectionModel().setCellSelectionEnabled(true);
        officeHoursTableView.setOnMouseClicked(e -> {
            controller.processToggleOfficeHours();
        });

        // DON'T LET ANYONE SORT THE TABLES
        TableView tasTableView = (TableView) gui.getGUINode(OH_TAS_TABLE_VIEW);
        for (int i = 0; i < officeHoursTableView.getColumns().size(); i++) {
            ((TableColumn) officeHoursTableView.getColumns().get(i)).setSortable(false);
        }
        for (int i = 0; i < tasTableView.getColumns().size(); i++) {
            ((TableColumn) tasTableView.getColumns().get(i)).setSortable(false);
        }

        tasTableView.setOnMouseClicked(e -> {
            app.getFoolproofModule().updateAll();
            if (e.getClickCount() == 2) {
                controller.processEditTA();
            }
            controller.processSelectTA();
        });

        RadioButton allRadio = (RadioButton) gui.getGUINode(OH_ALL_RADIO_BUTTON);
        allRadio.setOnAction(e -> {
            CourseSiteController.isAllMode = true;
            controller.processSelectAllTAs();
            officeHoursTableView.refresh();
        });
        RadioButton gradRadio = (RadioButton) gui.getGUINode(OH_GRAD_RADIO_BUTTON);
        gradRadio.setOnAction(e -> {
            CourseSiteController.isAllMode = false;
            controller.processSelectGradTAs();
            officeHoursTableView.refresh();
        });
        RadioButton undergradRadio = (RadioButton) gui.getGUINode(OH_UNDERGRAD_RADIO_BUTTON);
        undergradRadio.setOnAction(e -> {
            CourseSiteController.isAllMode = false;
            controller.processSelectUndergradTAs();
            officeHoursTableView.refresh();
        });
    }

    public void initFoolproofDesign() {
        AppGUIModule gui = app.getGUIModule();
        AppFoolproofModule foolproofSettings = app.getFoolproofModule();
        foolproofSettings.registerModeSettings(OH_FOOLPROOF_SETTINGS,
                new CourseSiteFoolproofDesign((CourseSiteApp) app));
    }
    
    @Override
    public void processWorkspaceKeyEvent(KeyEvent ke) {
        // WE AREN'T USING THIS FOR THIS APPLICATION
    }

    @Override
    public void showNewDialog() {
        // WE AREN'T USING THIS FOR THIS APPLICATION
    } 
    
    public void setupMeetingTimeColumn(TableView<MeetingTime> table, TableColumn col, CourseSiteController controller, int mode) {
        col.setOnEditCommit(new EventHandler<CellEditEvent<MeetingTime, String>>() {
                @Override
                public void handle(CellEditEvent<MeetingTime ,String> event) {
                    String oldValue = event.getOldValue();
                    String newValue = event.getNewValue();
                    int col = event.getTablePosition().getColumn();
                    MeetingTime mt = event.getRowValue();
                    controller.processEditMeetingTime(mt, oldValue, newValue, col, mode, table);
                }
            });
    }
    
    public LocalDate getLocalDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        LocalDate localDate = LocalDate.parse(date, formatter);
        return localDate;
    }
    
    public String getLocalDateString(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        String dateString = date.format(formatter);
        return dateString;
    }
}
