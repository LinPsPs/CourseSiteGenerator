package cs.files;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import djf.components.AppDataComponent;
import djf.components.AppFileComponent;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonWriter;
import javax.json.JsonWriterFactory;
import javax.json.stream.JsonGenerator;
import cs.CourseSiteApp;
import static cs.CourseSitePropertyType.*;
import cs.data.CourseSiteData;
import cs.data.MeetingTime;
import cs.data.TAType;
import cs.data.TeachingAssistantPrototype;
import cs.data.TimeSlot;
import cs.data.TimeSlot.DayOfWeek;
import djf.AppTemplate;
import static djf.AppTemplate.PATH_EXPORT;
import djf.modules.AppGUIModule;
import static djf.modules.AppLanguageModule.FILE_PROTOCOL;
import djf.ui.dialogs.AppDialogsFacade;
import java.io.File;
import java.io.FileWriter;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javafx.collections.ObservableList;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * This class serves as the file component for the TA
 * manager app. It provides all saving and loading 
 * services for the application.
 * 
 * @author Richard McKenna
 */
public class CourseSiteFiles implements AppFileComponent {
    // THIS IS THE APP ITSELF
    CourseSiteApp app;
    
    // THESE ARE USED FOR IDENTIFYING JSON TYPES
    static final String JSON_GRAD_TAS = "grad_tas";
    static final String JSON_UNDERGRAD_TAS = "undergrad_tas";
    static final String JSON_NAME = "name";
    static final String JSON_EMAIL = "email";
    static final String JSON_TYPE = "type";
    static final String JSON_OFFICE_HOURS = "officeHours";
    static final String JSON_START_HOUR = "startHour";
    static final String JSON_END_HOUR = "endHour";
    static final String JSON_START_TIME = "time";
    static final String JSON_DAY_OF_WEEK = "day";
    static final String JSON_MONDAY = "monday";
    static final String JSON_TUESDAY = "tuesday";
    static final String JSON_WEDNESDAY = "wednesday";
    static final String JSON_THURSDAY = "thursday";
    static final String JSON_FRIDAY = "friday";
    static final String JSON_SUBJECT = "subject";
    static final String JSON_NUMBER = "number";
    static final String JSON_SEMESTER = "semester";
    static final String JSON_YEAR = "year";
    static final String JSON_TITLE = "title";
    static final String JSON_CSS = "css";
    static final String JSON_INSTRUCTOR_NAME = "instructor_name";
    static final String JSON_INSTRUCTOR_ROOM = "instructor_room";
    static final String JSON_INSTRUCTOR_EMAIL = "instructor_email";
    static final String JSON_INSTRUCTOR_HOMEPAGE = "instructor_homepage";
    static final String JSON_INSTRUCTOR_OH = "instructor_oh";
    static final String JSON_SITE = "site";
    static final String JSON_SYLLABUS = "syllabus";
    static final String JSON_DESCRIPTION = "description";
    static final String JSON_TOPICS = "topics";
    static final String JSON_PREREQUISITES = "prerequisites";
    static final String JSON_OUTCOMES = "outcomes";
    static final String JSON_TEXTBOOKS = "textbooks";
    static final String JSON_GRADED = "graded";
    static final String JSON_GRADING_NOTE = "grading_note";
    static final String JSON_GRADINGNOTE = "gradingNote";
    static final String JSON_ACADEMIC_DISHONESTY = "academic_dishonesty";
    static final String JSON_ACADEMICDISHONESTY = "academicDishonesty";
    static final String JSON_SPECIAL_ASSISTANCE = "special_assistance";
    static final String JSON_SPECIALASSISTANCE = "specialAssistance";
    static final String JSON_LECTURES = "lectures";
    static final String JSON_RECITATIONS = "recitations";
    static final String JSON_LABS = "labs";
    static final String JSON_SECTION = "section";
    static final String JSON_DAYS = "days";
    static final String JSON_TIME = "time";
    static final String JSON_ROOM = "room";
    static final String JSON_EXACTDATE = "exact";
    static final String JSON_TA1 = "ta1";
    static final String JSON_TA2 = "ta2";
    static final String JSON_CALENDAR = "calendar";
    static final String JSON_CALENDAR_START = "calendar_start";
    static final String JSON_CALENDAR_END = "calendar_end";
    static final String JSON_SCHEDULE_ITEMS = "schedule_items";
    static final String JSON_TOPIC = "topic";
    static final String JSON_DATE = "date";
    static final String JSON_FAVICON = "favicon";
    static final String JSON_NAVBAR = "navbar";
    static final String JSON_LEFT = "bottom_left";
    static final String JSON_RIGHT = "bottom_right";
    static final String JSON_LINK = "link";
    static final String JSON_CODE = "code";
    static final String JSON_HREF = "href";
    static final String JSON_SRC = "src";
    static final String JSON_ALT = "alt";
    static final String JSON_SBU = "Stony Brook University";
    static final String JSON_SBU_CS = "Stony Brook University Computer Science Department";
    static final String JSON_SBU_WEB = "http://www.stonybrook.edu";
    static final String JSON_SBU_CS_WEB = "http://www.cs.stonybrook.edu";
    static final String JSON_AUTHOR = "author";
    static final String JSON_LOGOS = "logos";
    static final String JSON_PAGES = "pages";
    static final String JSON_PHOTO = "photo";
    static final String JSON_HOURS = "hours";
    static final String JSON_INSTRUCTOR = "instructor";
    static final String JSON_GRADED_COM = "gradedComponents";
    static final String JSON_DAY_TIME = "day_time";
    static final String JSON_LOCATION = "location";
    static final String JSON_TA_1 = "ta_1";
    static final String JSON_TA_2 = "ta_2";
    static final String JSON_START_MONTH = "startingMondayMonth";
    static final String JSON_START_DAY = "startingMondayDay";
    static final String JSON_END_MONTH = "endingFridayMonth";
    static final String JSON_END_DAY = "endingFridayDay";
    static final String JSON_HOLIDAYS = "holidays";
    static final String JSON_HWS = "hws";
    static final String JSON_MONTH = "month";
    static final String JSON_DAY = "day";
    static final String JSON_REFERENCES = "references";
    
    public CourseSiteFiles(CourseSiteApp initApp) {
        app = initApp;
    }

    @Override
    public void loadData(AppDataComponent data, String filePath) throws IOException {
	// CLEAR THE OLD DATA OUT
	CourseSiteData dataManager = (CourseSiteData)data;
        dataManager.reset();

	// LOAD THE JSON FILE WITH ALL THE DATA
	JsonObject json = loadJSONFile(filePath);

        // FIX OH BUG
        RadioButton rb = (RadioButton) ((AppGUIModule) app.getGUIModule()).getGUINode(OH_ALL_RADIO_BUTTON);
        rb.setSelected(true);
        
	// LOAD THE START AND END HOURS
	String startHour = json.getString(JSON_START_HOUR);
        String endHour = json.getString(JSON_END_HOUR);
        dataManager.initHours(startHour, endHour);
        
        // LOAD ALL THE GRAD TAs
        loadTAs(dataManager, json, JSON_GRAD_TAS);
        loadTAs(dataManager, json, JSON_UNDERGRAD_TAS);

        // AND THEN ALL THE OFFICE HOURS
        JsonArray jsonOfficeHoursArray = json.getJsonArray(JSON_OFFICE_HOURS);
        for (int i = 0; i < jsonOfficeHoursArray.size(); i++) {
            JsonObject jsonOfficeHours = jsonOfficeHoursArray.getJsonObject(i);
            String startTime = jsonOfficeHours.getString(JSON_START_TIME);
            DayOfWeek dow = DayOfWeek.valueOf(jsonOfficeHours.getString(JSON_DAY_OF_WEEK));
            String name = jsonOfficeHours.getString(JSON_NAME);
            TeachingAssistantPrototype ta = dataManager.getTAWithName(name);
            TimeSlot timeSlot = dataManager.getTimeSlot(startTime);
            timeSlot.toggleTA(dow, ta);
        }
        
        loadMeetingTime(dataManager, json);
        
        loadSchedulesStartEndTime(app.getGUIModule(), json);
        
        AppGUIModule gui = app.getGUIModule();
        JsonArray jsonSiteArray = json.getJsonArray(JSON_SITE);
        JsonObject jsonSite = jsonSiteArray.getJsonObject(0);
        loadSitePage(gui, jsonSite);
        JsonArray jsonSyllabusArray = json.getJsonArray(JSON_SYLLABUS);
        JsonObject jsonSyllabus = jsonSyllabusArray.getJsonObject(0);
        loadSyllabusPage(gui, jsonSyllabus);
    }
    
    private void loadMeetingTime(CourseSiteData data, JsonObject json) {
        JsonArray jsonLecturesArray = json.getJsonArray(JSON_LECTURES);
        for (int i = 0; i < jsonLecturesArray.size(); i++) {
            JsonObject jsonMt = jsonLecturesArray.getJsonObject(i);
            String section = jsonMt.getString(JSON_SECTION);
            String days = jsonMt.getString(JSON_DAYS);
            String time = jsonMt.getString(JSON_TIME);
            String room = jsonMt.getString(JSON_ROOM);
            MeetingTime mt = new MeetingTime(section, days, time, room);
            data.addMeetingTime(mt, 1);
        }
        
        JsonArray JsonRecitationsArray = json.getJsonArray(JSON_RECITATIONS);
        for (int i = 0; i < JsonRecitationsArray.size(); i++) {
            JsonObject jsonMt = JsonRecitationsArray.getJsonObject(i);
            String section = jsonMt.getString(JSON_SECTION);
            String exactTime = jsonMt.getString(JSON_EXACTDATE);
            String room = jsonMt.getString(JSON_ROOM);
            String ta1 = jsonMt.getString(JSON_TA1);
            String ta2 = jsonMt.getString(JSON_TA2);
            MeetingTime mt = new MeetingTime(section, exactTime, room, ta1, ta2);
            data.addMeetingTime(mt, 2);
        }
        
        JsonArray JsonLabsArray = json.getJsonArray(JSON_LABS);
        for (int i = 0; i < JsonLabsArray.size(); i++) {
            JsonObject jsonMt = JsonLabsArray.getJsonObject(i);
            String section = jsonMt.getString(JSON_SECTION);
            String exactTime = jsonMt.getString(JSON_EXACTDATE);
            String room = jsonMt.getString(JSON_ROOM);
            String ta1 = jsonMt.getString(JSON_TA1);
            String ta2 = jsonMt.getString(JSON_TA2);
            MeetingTime mt = new MeetingTime(section, exactTime, room, ta1, ta2);
            data.addMeetingTime(mt, 3);
        }
        
        JsonArray JsonSchedulesArray = json.getJsonArray(JSON_SCHEDULE_ITEMS);
        for (int i = 0; i < JsonSchedulesArray.size(); i++) {
            JsonObject jsonMt = JsonSchedulesArray.getJsonObject(i);
            String type = jsonMt.getString(JSON_TYPE);
            String date = jsonMt.getString(JSON_DATE);
            String title = jsonMt.getString(JSON_TITLE);
            String topic = jsonMt.getString(JSON_TOPIC);
            String link = jsonMt.getString(JSON_LINK);
            MeetingTime mt = new MeetingTime(type, date, title, topic, link, true);
            data.addMeetingTime(mt, 4);
        }
    }
    
    private void loadTAs(CourseSiteData data, JsonObject json, String tas) {
        JsonArray jsonTAArray = json.getJsonArray(tas);
        for (int i = 0; i < jsonTAArray.size(); i++) {
            JsonObject jsonTA = jsonTAArray.getJsonObject(i);
            String name = jsonTA.getString(JSON_NAME);
            String email = jsonTA.getString(JSON_EMAIL);
            TAType type = TAType.valueOf(jsonTA.getString(JSON_TYPE));
            TeachingAssistantPrototype ta = new TeachingAssistantPrototype(name, email, type);
            data.addTA(ta);
        }     
    }
    
    private void loadSitePage(AppGUIModule gui, JsonObject json) {
        loadComboBox(JSON_SUBJECT, SITE_SUBJECT_COMBOBOX, json, gui);
        loadComboBox(JSON_NUMBER, SITE_NUMBER_COMBOBOX, json, gui);
        loadComboBox(JSON_SEMESTER, SITE_SEMESTER_COMBOBOX, json, gui);
        loadComboBox(JSON_YEAR, SITE_YEAR_COMBOBOX, json, gui);
        
        loadTextField(JSON_TITLE, SITE_TITLE_TEXT_FIELD, json, gui);
        
        loadComboBox(JSON_CSS, SITE_CSS_COMBOBOX, json, gui);
        
        loadTextField(JSON_INSTRUCTOR_NAME, SITE_INSTRUCTOR_NAME_TEXT_FIELD, json, gui);
        loadTextField(JSON_INSTRUCTOR_ROOM, SITE_INSTRUCTOR_ROOM_TEXT_FIELD, json, gui);
        loadTextField(JSON_INSTRUCTOR_EMAIL, SITE_INSTRUCTOR_EMAIL_TEXT_FIELD, json, gui);
        loadTextField(JSON_INSTRUCTOR_HOMEPAGE, SITE_INSTRUCTOR_HOME_PAGE_TEXT_FIELD, json, gui);
        
        loadTextArea(JSON_INSTRUCTOR_OH, SITE_INSTRUCTOR_OH_TEXT_AREA, json, gui);
        
        loadImageView(JSON_FAVICON, SITE_FAVICON_IMAGE_VIEW, json, gui);
        loadImageView(JSON_NAVBAR, SITE_NAVBAR_IMAGE_VIEW, json, gui);
        loadImageView(JSON_LEFT, SITE_LEFT_FOOTER_IMAGE_VIEW, json, gui);
        loadImageView(JSON_RIGHT, SITE_RIGHT_FOOTER_IMAGE_VIEW, json, gui);
    }
    
    private void loadSyllabusPage(AppGUIModule gui, JsonObject json) {
        loadTextArea(JSON_DESCRIPTION, SYLLABUS_DESCRIPTION_TEXT_AREA, json, gui);
        loadTextArea(JSON_TOPICS, SYLLABUS_TOPICS_TEXT_AREA, json, gui);
        loadTextArea(JSON_PREREQUISITES, SYLLABUS_PREREQUISITES_TEXT_AREA, json, gui);
        loadTextArea(JSON_OUTCOMES, SYLLABUS_OUTCOMES_TEXT_AREA, json, gui);
        loadTextArea(JSON_TEXTBOOKS, SYLLABUS_TEXTBOOKS_TEXT_AREA, json, gui);
        loadTextArea(JSON_GRADED, SYLLABUS_GRADED_COMPONENTS_TEXT_AREA, json, gui);
        loadTextArea(JSON_GRADING_NOTE, SYLLABUS_GRADING_NOTE_TEXT_AREA, json, gui);
        loadTextArea(JSON_ACADEMIC_DISHONESTY, SYLLABUS_ACADEMIC_DISHONESTY_TEXT_AREA, json, gui);
        loadTextArea(JSON_SPECIAL_ASSISTANCE, SYLLABUS_SPECIAL_ASSISTANCE_TEXT_AREA, json, gui);
    }
    
    private void loadTextArea(String jsonId, Object nodeId, JsonObject json, AppGUIModule gui) {
        String str = json.getString(jsonId);
        ((TextArea) gui.getGUINode(nodeId)).setText(str);
    }
    
    private void loadTextField(String jsonId, Object nodeId, JsonObject json, AppGUIModule gui) {
        String str = json.getString(jsonId);
        ((TextField) gui.getGUINode(nodeId)).setText(str);
    }
      
    private void loadComboBox(String jsonId, Object nodeId, JsonObject json, AppGUIModule gui) {
        String str = json.getString(jsonId);
        ((ComboBox) gui.getGUINode(nodeId)).getSelectionModel().select(str);
    }
    
    private void loadImageView(String jsonId, Object nodeId, JsonObject json, AppGUIModule gui) {
        String str = json.getString(jsonId);
        ((ImageView) gui.getGUINode(nodeId)).setImage(new Image(str));
    }
    
    public void loadSchedulesStartEndTime(AppGUIModule gui, JsonObject json){
            DatePicker startDatePicker = (DatePicker) gui.getGUINode(SCHEDULE_START_DATEPICKER);
            DatePicker endDatePicker = (DatePicker) gui.getGUINode(SCHEDULE_END_DATEPICKER);
            JsonArray dp = json.getJsonArray(JSON_CALENDAR);
            JsonObject odp = dp.getJsonObject(0);
            startDatePicker.setValue(getLocalDate(odp.getString(JSON_CALENDAR_START)));
            endDatePicker.setValue(getLocalDate(odp.getString(JSON_CALENDAR_END)));
    }
    
    // HELPER METHOD FOR LOADING DATA FROM A JSON FORMAT
    private JsonObject loadJSONFile(String jsonFilePath) throws IOException {
	InputStream is = new FileInputStream(jsonFilePath);
	JsonReader jsonReader = Json.createReader(is);
	JsonObject json = jsonReader.readObject();
	jsonReader.close();
	is.close();
	return json;
    }

    @Override
    public void saveData(AppDataComponent data, String filePath) throws IOException {
	// GET THE DATA
	CourseSiteData dataManager = (CourseSiteData)data;
        
        // GET THE GUI
        AppGUIModule gui = app.getGUIModule();

	// NOW BUILD THE TA JSON OBJCTS TO SAVE
	JsonArrayBuilder gradTAsArrayBuilder = Json.createArrayBuilder();
        JsonArrayBuilder undergradTAsArrayBuilder = Json.createArrayBuilder();
	Iterator<TeachingAssistantPrototype> tasIterator = dataManager.teachingAssistantsIterator();
        while (tasIterator.hasNext()) {
            TeachingAssistantPrototype ta = tasIterator.next();
	    JsonObject taJson = Json.createObjectBuilder()
		    .add(JSON_NAME, ta.getName())
		    .add(JSON_EMAIL, ta.getEmail())
                    .add(JSON_TYPE, ta.getType().toString()).build();
            if (ta.getType().equals(TAType.Graduate.toString()))
                gradTAsArrayBuilder.add(taJson);
            else
                undergradTAsArrayBuilder.add(taJson);
	}
        JsonArray gradTAsArray = gradTAsArrayBuilder.build();
	JsonArray undergradTAsArray = undergradTAsArrayBuilder.build();

	// NOW BUILD THE OFFICE HOURS JSON OBJCTS TO SAVE
	JsonArrayBuilder officeHoursArrayBuilder = Json.createArrayBuilder();
        Iterator<TimeSlot> timeSlotsIterator = dataManager.officeHoursIterator();
        while (timeSlotsIterator.hasNext()) {
            TimeSlot timeSlot = timeSlotsIterator.next();
            for (int i = 0; i < DayOfWeek.values().length; i++) {
                DayOfWeek dow = DayOfWeek.values()[i];
                tasIterator = timeSlot.getTAsIterator(dow);
                while (tasIterator.hasNext()) {
                    TeachingAssistantPrototype ta = tasIterator.next();
                    JsonObject tsJson = Json.createObjectBuilder()
                        .add(JSON_START_TIME, timeSlot.getStartTime().replace(":", "_"))
                        .add(JSON_DAY_OF_WEEK, dow.toString())
                        .add(JSON_NAME, ta.getName()).build();
                    officeHoursArrayBuilder.add(tsJson);
                }
            }
	}
	JsonArray officeHoursArray = officeHoursArrayBuilder.build();
        
        // NOW BUILD THE SITE PAGE
        JsonArray sitePageArray = saveSiteData(gui);
        
        // NOW BUILD THE SYLLABUS PAGE
        JsonArray syllabusArray = saveSyllabus(gui);
        
        // NOW BUILD THE MEETING TIME PAGE
        JsonArray lecturesArray = saveLecturesMeetingTime(dataManager);
        
        JsonArray recitationsArray = saveRecitationsMeetingTime(dataManager);
        
        JsonArray labsArray = saveLabsMeetingTime(dataManager);
        
        // NOW BUILD THE SCHEDULE MEETING TIME
        JsonArray datePickerArray = saveSchedulesStartEndTime(gui);
        
        JsonArray schedulesArray = saveSchedulesMeetingTime(dataManager);
        
	// THEN PUT IT ALL TOGETHER IN A JsonObject
	JsonObject dataManagerJSO = Json.createObjectBuilder()
		.add(JSON_START_HOUR, "" + dataManager.getStartHour())
		.add(JSON_END_HOUR, "" + dataManager.getEndHour())
                .add(JSON_GRAD_TAS, gradTAsArray)
                .add(JSON_UNDERGRAD_TAS, undergradTAsArray)
                .add(JSON_OFFICE_HOURS, officeHoursArray)
                .add(JSON_SITE, sitePageArray)
                .add(JSON_SYLLABUS, syllabusArray)
                .add(JSON_LECTURES, lecturesArray)
                .add(JSON_RECITATIONS, recitationsArray)
                .add(JSON_LABS, labsArray)
                .add(JSON_CALENDAR, datePickerArray)
                .add(JSON_SCHEDULE_ITEMS, schedulesArray)
		.build();
	
	// AND NOW OUTPUT IT TO A JSON FILE WITH PRETTY PRINTING
	Map<String, Object> properties = new HashMap<>(1);
	properties.put(JsonGenerator.PRETTY_PRINTING, true);
	JsonWriterFactory writerFactory = Json.createWriterFactory(properties);
	StringWriter sw = new StringWriter();
	JsonWriter jsonWriter = writerFactory.createWriter(sw);
	jsonWriter.writeObject(dataManagerJSO);
	jsonWriter.close();

	// INIT THE WRITER
	OutputStream os = new FileOutputStream(filePath);
	JsonWriter jsonFileWriter = Json.createWriter(os);
	jsonFileWriter.writeObject(dataManagerJSO);
	String prettyPrinted = sw.toString();
	PrintWriter pw = new PrintWriter(filePath);
	pw.write(prettyPrinted);
	pw.close();
        
        saveCourseInfoIuput();
    }
    
    public JsonArray saveSiteData(AppGUIModule gui) {
        JsonArrayBuilder siteArrayBuilder = Json.createArrayBuilder();
        JsonObject siteJson = Json.createObjectBuilder()
                .add(JSON_SUBJECT, (String)((ComboBox) gui.getGUINode(SITE_SUBJECT_COMBOBOX)).getValue())
                .add(JSON_NUMBER, (String)((ComboBox) gui.getGUINode(SITE_NUMBER_COMBOBOX)).getValue())
                .add(JSON_SEMESTER, (String)((ComboBox) gui.getGUINode(SITE_SEMESTER_COMBOBOX)).getValue())
                .add(JSON_YEAR, (String)((ComboBox) gui.getGUINode(SITE_YEAR_COMBOBOX)).getValue())
                .add(JSON_TITLE, (String)((TextField) gui.getGUINode(SITE_TITLE_TEXT_FIELD)).getText())
                .add(JSON_CSS, (String)((ComboBox) gui.getGUINode(SITE_CSS_COMBOBOX)).getValue())
                .add(JSON_INSTRUCTOR_NAME, (String)((TextField) gui.getGUINode(SITE_INSTRUCTOR_NAME_TEXT_FIELD)).getText())
                .add(JSON_INSTRUCTOR_ROOM, (String)((TextField) gui.getGUINode(SITE_INSTRUCTOR_ROOM_TEXT_FIELD)).getText())
                .add(JSON_INSTRUCTOR_EMAIL, (String)((TextField) gui.getGUINode(SITE_INSTRUCTOR_EMAIL_TEXT_FIELD)).getText())
                .add(JSON_INSTRUCTOR_HOMEPAGE, (String)((TextField) gui.getGUINode(SITE_INSTRUCTOR_HOME_PAGE_TEXT_FIELD)).getText())
                .add(JSON_INSTRUCTOR_OH, (String)((TextArea) gui.getGUINode(SITE_INSTRUCTOR_OH_TEXT_AREA)).getText())
                .add(JSON_FAVICON, ((ImageView) gui.getGUINode(SITE_FAVICON_IMAGE_VIEW)).getImage().impl_getUrl())
                .add(JSON_NAVBAR, ((ImageView) gui.getGUINode(SITE_NAVBAR_IMAGE_VIEW)).getImage().impl_getUrl())
                .add(JSON_LEFT, ((ImageView) gui.getGUINode(SITE_LEFT_FOOTER_IMAGE_VIEW)).getImage().impl_getUrl())
                .add(JSON_RIGHT, ((ImageView) gui.getGUINode(SITE_RIGHT_FOOTER_IMAGE_VIEW)).getImage().impl_getUrl())
                .build();
        siteArrayBuilder.add(siteJson);
        return siteArrayBuilder.build();
    }
    
    public JsonArray saveSyllabus(AppGUIModule gui) {
        JsonArrayBuilder syllabusArrayBuilder = Json.createArrayBuilder();
        JsonObject syllabusJson = Json.createObjectBuilder()
                .add(JSON_DESCRIPTION, (String)((TextArea) gui.getGUINode(SYLLABUS_DESCRIPTION_TEXT_AREA)).getText())
                .add(JSON_TOPICS, (String)((TextArea) gui.getGUINode(SYLLABUS_TOPICS_TEXT_AREA)).getText())
                .add(JSON_PREREQUISITES, (String)((TextArea) gui.getGUINode(SYLLABUS_PREREQUISITES_TEXT_AREA)).getText())
                .add(JSON_OUTCOMES, (String)((TextArea) gui.getGUINode(SYLLABUS_OUTCOMES_TEXT_AREA)).getText())
                .add(JSON_TEXTBOOKS, (String)((TextArea) gui.getGUINode(SYLLABUS_TEXTBOOKS_TEXT_AREA)).getText())
                .add(JSON_GRADED, (String)((TextArea) gui.getGUINode(SYLLABUS_GRADED_COMPONENTS_TEXT_AREA)).getText())
                .add(JSON_GRADING_NOTE, (String)((TextArea) gui.getGUINode(SYLLABUS_GRADING_NOTE_TEXT_AREA)).getText())
                .add(JSON_ACADEMIC_DISHONESTY, (String)((TextArea) gui.getGUINode(SYLLABUS_ACADEMIC_DISHONESTY_TEXT_AREA)).getText())
                .add(JSON_SPECIAL_ASSISTANCE, (String)((TextArea) gui.getGUINode(SYLLABUS_SPECIAL_ASSISTANCE_TEXT_AREA)).getText())
                .build();
        syllabusArrayBuilder.add(syllabusJson);
        return syllabusArrayBuilder.build();
    }
    
    public JsonArray saveSchedulesStartEndTime(AppGUIModule gui) {
        JsonArrayBuilder datePickerArrayBuilder = Json.createArrayBuilder();
        DatePicker startDatePicker = (DatePicker) gui.getGUINode(SCHEDULE_START_DATEPICKER);
        DatePicker endDatePicker = (DatePicker) gui.getGUINode(SCHEDULE_END_DATEPICKER);
        String startDate = getLocalDateString(startDatePicker.getValue());
        String endDate = getLocalDateString(endDatePicker.getValue());
        JsonObject datePickerJson = Json.createObjectBuilder()
                .add(JSON_CALENDAR_START, startDate)
                .add(JSON_CALENDAR_END, endDate).build();
        datePickerArrayBuilder.add(datePickerJson);
        return datePickerArrayBuilder.build();
    }
    
    public JsonArray saveLecturesMeetingTime(CourseSiteData data) {
        JsonArrayBuilder lectureArrayBuilder = Json.createArrayBuilder();
        Iterator<MeetingTime> lecturesIterator = data.lecturesIterator();
        while(lecturesIterator.hasNext()) {
            MeetingTime mt = lecturesIterator.next();
            JsonObject mtJson = Json.createObjectBuilder()
                    .add(JSON_SECTION, mt.getSection())
                    .add(JSON_DAYS, mt.getDays())
                    .add(JSON_TIME, mt.getTime())
                    .add(JSON_ROOM, mt.getRoom()).build();
            lectureArrayBuilder.add(mtJson);
        }
        return lectureArrayBuilder.build();
    }
    
    public JsonArray saveRecitationsMeetingTime(CourseSiteData data) {
        JsonArrayBuilder recitationArrayBuilder = Json.createArrayBuilder();
        Iterator<MeetingTime> recitationsIterator = data.recitationsIterator();
        while(recitationsIterator.hasNext()) {
            MeetingTime mt = recitationsIterator.next();
            JsonObject mtJson = Json.createObjectBuilder()
                    .add(JSON_SECTION, mt.getSection())
                    .add(JSON_EXACTDATE, mt.getExactTime())
                    .add(JSON_ROOM, mt.getRoom())
                    .add(JSON_TA1, mt.getTa1())
                    .add(JSON_TA2, mt.getTa2()).build();
            recitationArrayBuilder.add(mtJson);
        }
        return recitationArrayBuilder.build();
    }
    
    public JsonArray saveLabsMeetingTime(CourseSiteData data) {
        JsonArrayBuilder labArrayBuilder = Json.createArrayBuilder();
        Iterator<MeetingTime> labsIterator = data.labsIterator();
        while(labsIterator.hasNext()) {
            MeetingTime mt = labsIterator.next();
            JsonObject mtJson = Json.createObjectBuilder()
                    .add(JSON_SECTION, mt.getSection())
                    .add(JSON_EXACTDATE, mt.getExactTime())
                    .add(JSON_ROOM, mt.getRoom())
                    .add(JSON_TA1, mt.getTa1())
                    .add(JSON_TA2, mt.getTa2()).build();
            labArrayBuilder.add(mtJson);
        }
        return labArrayBuilder.build();
    }
    
    public JsonArray saveSchedulesMeetingTime(CourseSiteData data) {
        JsonArrayBuilder scheduleArrayBuilder = Json.createArrayBuilder();
        Iterator<MeetingTime> schedulesIterator = data.scheduleIterator();
        while(schedulesIterator.hasNext()) {
            MeetingTime mt = schedulesIterator.next();
            JsonObject mtJson = Json.createObjectBuilder()
                    .add(JSON_TYPE, mt.getType())
                    .add(JSON_DATE, mt.getDate())
                    .add(JSON_TITLE, mt.getTitle())
                    .add(JSON_TOPIC, mt.getTopic())
                    .add(JSON_LINK, mt.getLink()).build();
            scheduleArrayBuilder.add(mtJson);
        }
        return scheduleArrayBuilder.build();
    }
    
    public void saveCourseInfoIuput() {
        AppGUIModule gui = app.getGUIModule();
        try {
            FileWriter fw = new FileWriter(new File(AppTemplate.RECENT_SUBJECT));
            FileWriter fw2 = new FileWriter(new File(AppTemplate.RECENT_NUMBERS));
            
            ComboBox subject = (ComboBox) gui.getGUINode(SITE_SUBJECT_COMBOBOX);
            ComboBox number = (ComboBox) gui.getGUINode(SITE_NUMBER_COMBOBOX);
            
            ObservableList<String> subjectItems = subject.getItems();
            ObservableList<String> numberItems = number.getItems();
            
            Iterator<String> subjectIterator = subjectItems.iterator();
            Iterator<String> numberIterator = numberItems.iterator();
            
            while(subjectIterator.hasNext()) {
                fw.write(subjectIterator.next());
                fw.write(System.lineSeparator());
            }
            fw.close();
            
            while(numberIterator.hasNext()) {
                fw2.write(numberIterator.next());
                fw2.write(System.lineSeparator());
            }
            fw2.close();
        }
        catch(IOException ioe) {
            ioe.printStackTrace();
        }
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
    
    // IMPORTING/EXPORTING DATA IS USED WHEN WE READ/WRITE DATA IN AN
    // ADDITIONAL FORMAT USEFUL FOR ANOTHER PURPOSE, LIKE ANOTHER APPLICATION

    @Override
    public void importData(AppDataComponent data, String filePath) throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void exportData(AppDataComponent initData, String filePath) throws IOException {
        AppGUIModule gui = app.getGUIModule();
        CourseSiteData data = (CourseSiteData) initData;
        String mainPath = "export/";
        new File(mainPath).mkdir();
        String cssPath = mainPath+"/css";
        new File(cssPath).mkdir();
        String imagesPath = mainPath+"/images";
        new File(imagesPath).mkdir();
        String jsPath = mainPath+"/js";
        new File(jsPath).mkdir();
        String jsExportPath = jsPath+"/";
        Path jsOrigionalPath = Paths.get("original_web/js");
        Files.walk(jsOrigionalPath).forEach(source -> {
            if(!source.getFileName().toString().equals("js")) {
                try {
                    Path jsExport = Paths.get(jsExportPath+source.getFileName());
                    Files.copy(source, jsExport, StandardCopyOption.REPLACE_EXISTING);
                }
                catch (IOException ex) {
                    System.out.println("Copy Error");
                }
            }
        });
        
        String cssExportPath = cssPath+"/";
        Path cssOrigionalPath = Paths.get("original_web/css");
        Files.walk(cssOrigionalPath).forEach(source -> {
            if(!source.getFileName().toString().equals("css")) {
                try {
                    Path cssExport = Paths.get(cssExportPath+source.getFileName());
                    Files.copy(source, cssExport, StandardCopyOption.REPLACE_EXISTING);
                }
                catch (IOException ex) {
                    System.out.println("Copy Error");
                }
            }
        });
        
        String imageExportPath = imagesPath+"/";
        Path imageOrigionalPath = Paths.get("original_web/images");
        Files.walk(imageOrigionalPath).forEach(source -> {
            if(!source.getFileName().toString().equals("css")) {
                try {
                    Path imageExport = Paths.get(imageExportPath+source.getFileName());
                    Files.copy(source, imageExport, StandardCopyOption.REPLACE_EXISTING);
                }
                catch (IOException ex) {
                    System.out.println("Copy Error");
                }
            }
        });
        
        String courseDataPath = mainPath+"/js/PageData.json";
        exportPage(data, courseDataPath);
        String officeHoursDataPath = mainPath+"/js/OfficeHoursData.json";
        exportOfficeHoursData(data, officeHoursDataPath);
        String syllabusDataPath = mainPath+"/js/SyllabusData.json";
        exportSyllabus(data, syllabusDataPath);
        String scheduleDataPath = mainPath+"/js/ScheduleData.json";
        exportSchedule(data, scheduleDataPath);
        String sectionsDataPath = mainPath+"/js/SectionsData.json";
        exportSection(data, sectionsDataPath);
        
        boolean hasHome = ((CheckBox) gui.getGUINode(SITE_HOME_CHECKBOX)).isSelected();
        boolean hasSyllabus = ((CheckBox) gui.getGUINode(SITE_SYLLABUS_CHECKBOX)).isSelected();
        boolean hasSchedule = ((CheckBox) gui.getGUINode(SITE_SCHEDULE_CHECKBOX)).isSelected();
        boolean hasHWs = ((CheckBox) gui.getGUINode(SITE_HWS_CHECKBOX)).isSelected();
        
        if(hasHome) {
            File folder = new File("original_web/index.html");
            Path from = Paths.get(folder.toURI());
            File target = new File(mainPath+"/index.html");
            if(target.exists()) {
                target.delete();
            }
            Path to = Paths.get(mainPath+"/index.html");
            Files.copy(from, to);
        }
        if(hasSyllabus) {
            File folder = new File("original_web/syllabus.html");
            Path from = Paths.get(folder.toURI());
            File target = new File(mainPath+"/syllabus.html");
            if(target.exists()) {
                target.delete();
            }
            Path to = Paths.get(mainPath+"/syllabus.html");
            Files.copy(from, to);
        }
        if(hasSchedule) {
            File folder = new File("original_web/schedule.html");
            Path from = Paths.get(folder.toURI());
            File target = new File(mainPath+"/schedule.html");
            if(target.exists()) {
                target.delete();
            }
            Path to = Paths.get(mainPath+"/schedule.html");
            Files.copy(from, to);
        }
        if(hasHWs) {
            File folder = new File("original_web/hws.html");
            Path from = Paths.get(folder.toURI());
            File target = new File(mainPath+"/hws.html");
            if(target.exists()) {
                target.delete();
            }
            Path to = Paths.get(mainPath+"/hws.html");
            Files.copy(from, to);
        }
        
        AppDialogsFacade.showExportDialog(app, mainPath+"/index.html");
    }
    
    public void exportPage(CourseSiteData data, String path) {
        AppGUIModule gui = app.getGUIModule();
        String faviconPath = ((ImageView) gui.getGUINode(SITE_FAVICON_IMAGE_VIEW)).getImage().impl_getUrl();
        String navbarPath = ((ImageView) gui.getGUINode(SITE_NAVBAR_IMAGE_VIEW)).getImage().impl_getUrl();
        String leftPath = ((ImageView) gui.getGUINode(SITE_NAVBAR_IMAGE_VIEW)).getImage().impl_getUrl();
        String rightPath = ((ImageView) gui.getGUINode(SITE_NAVBAR_IMAGE_VIEW)).getImage().impl_getUrl();
        JsonObject favicon = Json.createObjectBuilder()
                .add(JSON_HREF, "./images"+faviconPath.substring(faviconPath.lastIndexOf("/"))).build();
        JsonObject navbar = Json.createObjectBuilder()
                .add(JSON_HREF, JSON_SBU_WEB)
                .add(JSON_SRC, "./images"+navbarPath.substring(navbarPath.lastIndexOf("/")))
                .add(JSON_ALT, JSON_SBU).build();
        JsonObject bottom_left = Json.createObjectBuilder()
                .add(JSON_HREF, JSON_SBU_CS_WEB)
                .add(JSON_SRC, "./images"+leftPath.substring(leftPath.lastIndexOf("/")))
                .add(JSON_ALT, JSON_SBU).build();
        JsonObject bottom_right = Json.createObjectBuilder()
                .add(JSON_HREF, JSON_SBU_CS_WEB)
                .add(JSON_SRC, "./images"+rightPath.substring(rightPath.lastIndexOf("/")))
                .add(JSON_ALT, JSON_SBU_CS).build();
        JsonObject logosObject = Json.createObjectBuilder()
                .add(JSON_FAVICON, favicon)
                .add(JSON_NAVBAR, navbar)
                .add(JSON_LEFT, bottom_left)
                .add(JSON_RIGHT, bottom_right).build();

        JsonReader jsonReader = Json.createReader(new StringReader(((TextArea) gui.getGUINode(SITE_INSTRUCTOR_OH_TEXT_AREA)).getText()));
        JsonArray ohs = jsonReader.readArray();
        JsonObject author = Json.createObjectBuilder()
                .add(JSON_NAME, ((TextField) gui.getGUINode(SITE_INSTRUCTOR_NAME_TEXT_FIELD)).getText())
                .add(JSON_LINK, ((TextField) gui.getGUINode(SITE_INSTRUCTOR_HOME_PAGE_TEXT_FIELD)).getText())
                .add(JSON_EMAIL, ((TextField) gui.getGUINode(SITE_INSTRUCTOR_EMAIL_TEXT_FIELD)).getText())
                .add(JSON_ROOM, ((TextField) gui.getGUINode(SITE_INSTRUCTOR_ROOM_TEXT_FIELD)).getText())
                .add(JSON_HOURS, ohs).build();
        JsonArrayBuilder pagesArrayBuilder = Json.createArrayBuilder();
        boolean hasHome = ((CheckBox) gui.getGUINode(SITE_HOME_CHECKBOX)).isSelected();
        boolean hasSyllabus = ((CheckBox) gui.getGUINode(SITE_SYLLABUS_CHECKBOX)).isSelected();
        boolean hasSchedule = ((CheckBox) gui.getGUINode(SITE_SCHEDULE_CHECKBOX)).isSelected();
        boolean hasHWs = ((CheckBox) gui.getGUINode(SITE_HWS_CHECKBOX)).isSelected();
        if(hasHome) {
            JsonObject home = Json.createObjectBuilder()
                    .add(JSON_NAME, "Home")
                    .add(JSON_LINK, "index.html").build();
            pagesArrayBuilder.add(home);
        }
        if(hasSyllabus) {
            JsonObject syllabus = Json.createObjectBuilder()
                    .add(JSON_NAME, "Syllabus")
                    .add(JSON_LINK, "syllabus.html").build();
            pagesArrayBuilder.add(syllabus);
        }
        if(hasSchedule) {
            JsonObject schedule = Json.createObjectBuilder()
                    .add(JSON_NAME, "Schedule")
                    .add(JSON_LINK, "schedule.html").build();
            pagesArrayBuilder.add(schedule);
        }
        if(hasHWs) {
            JsonObject hws = Json.createObjectBuilder()
                    .add(JSON_NAME, "HWs")
                    .add(JSON_LINK, "hws.html").build();
            pagesArrayBuilder.add(hws);
        }
        JsonArray pagesArray = pagesArrayBuilder.build();
        
        JsonObject dataManagerJSO = Json.createObjectBuilder()
                .add(JSON_SUBJECT, ((ComboBox) gui.getGUINode(SITE_SUBJECT_COMBOBOX)).getValue().toString())
                .add(JSON_NUMBER, ((ComboBox) gui.getGUINode(SITE_NUMBER_COMBOBOX)).getValue().toString())
                .add(JSON_SEMESTER, ((ComboBox) gui.getGUINode(SITE_SEMESTER_COMBOBOX)).getValue().toString())
                .add(JSON_YEAR, ((ComboBox) gui.getGUINode(SITE_YEAR_COMBOBOX)).getValue().toString())
                .add(JSON_TITLE, ((TextField) gui.getGUINode(SITE_TITLE_TEXT_FIELD)).getText())
                .add(JSON_LOGOS, logosObject)
                .add(JSON_INSTRUCTOR, author)
                .add(JSON_PAGES, pagesArray)
                .build();
        
        Map<String, Object> properties = new HashMap<>(1);
        properties.put(JsonGenerator.PRETTY_PRINTING, true);
	JsonWriterFactory writerFactory = Json.createWriterFactory(properties);
	StringWriter sw = new StringWriter();
	JsonWriter jsonWriter = writerFactory.createWriter(sw);
	jsonWriter.writeObject(dataManagerJSO);
	jsonWriter.close();
        try{
            OutputStream os = new FileOutputStream(path);
            JsonWriter jsonFileWriter = Json.createWriter(os);
            jsonFileWriter.writeObject(dataManagerJSO);
            String prettyPrinted = sw.toString();
            PrintWriter pw = new PrintWriter(path);
            pw.write(prettyPrinted);
            pw.close();
        }
        catch(Exception e) {
            System.out.println("Page Json Error");
        }
    }
    
    public void exportOfficeHoursData(CourseSiteData data, String path) {
        AppGUIModule gui = app.getGUIModule();
        JsonReader instructorHoursReader = Json.createReader(new StringReader(((TextArea) gui.getGUINode(SITE_INSTRUCTOR_OH_TEXT_AREA)).getText()));
        JsonArray instructorHoursArray = instructorHoursReader.readArray();
        JsonObject instructorObject = Json.createObjectBuilder()
                .add(JSON_NAME, ((TextField) gui.getGUINode(SITE_INSTRUCTOR_NAME_TEXT_FIELD)).getText())
                .add(JSON_LINK, ((TextField) gui.getGUINode(SITE_INSTRUCTOR_HOME_PAGE_TEXT_FIELD)).getText())
                .add(JSON_EMAIL, ((TextField) gui.getGUINode(SITE_INSTRUCTOR_EMAIL_TEXT_FIELD)).getText())
                .add(JSON_ROOM, ((TextField) gui.getGUINode(SITE_INSTRUCTOR_ROOM_TEXT_FIELD)).getText())
                .add(JSON_PHOTO, "./images/RichardMcKenna.jpg")
                .add(JSON_HOURS, instructorHoursArray).build();
        
        // NOW BUILD THE TA JSON OBJCTS TO SAVE
	JsonArrayBuilder gradTAsArrayBuilder = Json.createArrayBuilder();
        JsonArrayBuilder undergradTAsArrayBuilder = Json.createArrayBuilder();
	Iterator<TeachingAssistantPrototype> tasIterator = data.teachingAssistantsIterator();
        while (tasIterator.hasNext()) {
            TeachingAssistantPrototype ta = tasIterator.next();
	    JsonObject taJson = Json.createObjectBuilder()
		    .add(JSON_NAME, ta.getName())
		    .add(JSON_EMAIL, ta.getEmail())
                    .add(JSON_TYPE, ta.getType().toString()).build();
            if (ta.getType().equals(TAType.Graduate.toString()))
                gradTAsArrayBuilder.add(taJson);
            else
                undergradTAsArrayBuilder.add(taJson);
	}
        JsonArray gradTAsArray = gradTAsArrayBuilder.build();
	JsonArray undergradTAsArray = undergradTAsArrayBuilder.build();

	// NOW BUILD THE OFFICE HOURS JSON OBJCTS TO SAVE
	JsonArrayBuilder officeHoursArrayBuilder = Json.createArrayBuilder();
        Iterator<TimeSlot> timeSlotsIterator = data.officeHoursIterator();
        while (timeSlotsIterator.hasNext()) {
            TimeSlot timeSlot = timeSlotsIterator.next();
            for (int i = 0; i < DayOfWeek.values().length; i++) {
                DayOfWeek dow = DayOfWeek.values()[i];
                tasIterator = timeSlot.getTAsIterator(dow);
                while (tasIterator.hasNext()) {
                    TeachingAssistantPrototype ta = tasIterator.next();
                    JsonObject tsJson = Json.createObjectBuilder()
                        .add(JSON_START_TIME, timeSlot.getStartTime().replace(":", "_"))
                        .add(JSON_DAY_OF_WEEK, dow.toString())
                        .add(JSON_NAME, ta.getName()).build();
                    officeHoursArrayBuilder.add(tsJson);
                }
            }
	}
	JsonArray officeHoursArray = officeHoursArrayBuilder.build();
        
        // THEN PUT IT ALL TOGETHER IN A JsonObject
	JsonObject dataManagerJSO = Json.createObjectBuilder()
		.add(JSON_START_HOUR, "" + data.getStartHour())
		.add(JSON_END_HOUR, "" + data.getEndHour())
                .add(JSON_INSTRUCTOR, instructorObject)
                .add(JSON_GRAD_TAS, gradTAsArray)
                .add(JSON_UNDERGRAD_TAS, undergradTAsArray)
                .add(JSON_OFFICE_HOURS, officeHoursArray)
		.build();
	
	// AND NOW OUTPUT IT TO A JSON FILE WITH PRETTY PRINTING
	Map<String, Object> properties = new HashMap<>(1);
	properties.put(JsonGenerator.PRETTY_PRINTING, true);
	JsonWriterFactory writerFactory = Json.createWriterFactory(properties);
	StringWriter sw = new StringWriter();
	JsonWriter jsonWriter = writerFactory.createWriter(sw);
	jsonWriter.writeObject(dataManagerJSO);
	jsonWriter.close();

	// INIT THE WRITER
        try{
            File target = new File(path);
            if(target.exists()) {
                target.delete();
            }
            OutputStream os = new FileOutputStream(path);
            JsonWriter jsonFileWriter = Json.createWriter(os);
            jsonFileWriter.writeObject(dataManagerJSO);
            String prettyPrinted = sw.toString();
            PrintWriter pw = new PrintWriter(path);
            pw.write(prettyPrinted);
            pw.close();
        }
	catch(IOException e) {
            System.out.println("OHData Json Error");
        }
    }
    
    public void exportSyllabus(CourseSiteData data, String path) {
        AppGUIModule gui = app.getGUIModule();
        JsonReader jsonReader;
        jsonReader = Json.createReader(new StringReader(((TextArea) gui.getGUINode(SYLLABUS_TOPICS_TEXT_AREA)).getText()));
        JsonArray topics = jsonReader.readArray();
        jsonReader = Json.createReader(new StringReader(((TextArea) gui.getGUINode(SYLLABUS_OUTCOMES_TEXT_AREA)).getText()));
        JsonArray outcomes  = jsonReader.readArray();
        jsonReader = Json.createReader(new StringReader(((TextArea) gui.getGUINode(SYLLABUS_TEXTBOOKS_TEXT_AREA)).getText()));
        JsonArray textbooks = jsonReader.readArray();
        jsonReader = Json.createReader(new StringReader(((TextArea) gui.getGUINode(SYLLABUS_GRADED_COMPONENTS_TEXT_AREA)).getText()));
        JsonArray gradedCom = jsonReader.readArray();
        JsonObject dataManagerJSO = Json.createObjectBuilder()
                .add(JSON_DESCRIPTION, ((TextArea) gui.getGUINode(SYLLABUS_DESCRIPTION_TEXT_AREA)).getText())
                .add(JSON_TOPICS, topics)
                .add(JSON_PREREQUISITES, ((TextArea) gui.getGUINode(SYLLABUS_PREREQUISITES_TEXT_AREA)).getText())
                .add(JSON_OUTCOMES, outcomes)
                .add(JSON_TEXTBOOKS, textbooks)
                .add(JSON_GRADED_COM, gradedCom)
                .add(JSON_GRADINGNOTE, ((TextArea) gui.getGUINode(SYLLABUS_GRADING_NOTE_TEXT_AREA)).getText())
                .add(JSON_ACADEMICDISHONESTY, ((TextArea) gui.getGUINode(SYLLABUS_ACADEMIC_DISHONESTY_TEXT_AREA)).getText())
                .add(JSON_SPECIALASSISTANCE, ((TextArea) gui.getGUINode(SYLLABUS_SPECIAL_ASSISTANCE_TEXT_AREA)).getText()).build();
        
        // AND NOW OUTPUT IT TO A JSON FILE WITH PRETTY PRINTING
	Map<String, Object> properties = new HashMap<>(1);
	properties.put(JsonGenerator.PRETTY_PRINTING, true);
	JsonWriterFactory writerFactory = Json.createWriterFactory(properties);
	StringWriter sw = new StringWriter();
	JsonWriter jsonWriter = writerFactory.createWriter(sw);
	jsonWriter.writeObject(dataManagerJSO);
	jsonWriter.close();

	// INIT THE WRITER
        try{
            OutputStream os = new FileOutputStream(path);
            JsonWriter jsonFileWriter = Json.createWriter(os);
            jsonFileWriter.writeObject(dataManagerJSO);
            String prettyPrinted = sw.toString();
            PrintWriter pw = new PrintWriter(path);
            pw.write(prettyPrinted);
            pw.close();
        }
	catch(IOException e) {
            System.out.println("Syllabus Json Error");
        }
    }
    
    public void exportSection(CourseSiteData data, String path) {
        JsonArrayBuilder lecturesArrayBuilder = Json.createArrayBuilder();
        JsonArrayBuilder labsArrayBuilder = Json.createArrayBuilder();
        JsonArrayBuilder reciArrayBuilder = Json.createArrayBuilder();
        Iterator<MeetingTime> lec = data.lecturesIterator();
        Iterator<MeetingTime> lab = data.labsIterator();
        Iterator<MeetingTime> reci = data.recitationsIterator();
        while(lec.hasNext()) {
            MeetingTime mt = lec.next();
            JsonObject lecJson = Json.createObjectBuilder()
                    .add(JSON_SECTION, mt.getSection())
                    .add(JSON_DAYS, mt.getDays())
                    .add(JSON_TIME, mt.getTime())
                    .add(JSON_ROOM, mt.getRoom()).build();
            lecturesArrayBuilder.add(lecJson);
        }
        JsonArray lecArray = lecturesArrayBuilder.build();
        while(lab.hasNext()) {
            MeetingTime mt = lab.next();
            JsonObject labJson = Json.createObjectBuilder()
                    .add(JSON_SECTION, mt.getSection())
                    .add(JSON_DAY_TIME, mt.getExactTime())
                    .add(JSON_LOCATION, mt.getRoom())
                    .add(JSON_TA_1, mt.getTa1())
                    .add(JSON_TA_2, mt.getTa2()).build();
            labsArrayBuilder.add(labJson);
        }
        JsonArray labArray = labsArrayBuilder.build();
        while(reci.hasNext()) {
            MeetingTime mt = reci.next();
            JsonObject reciJson = Json.createObjectBuilder()
                    .add(JSON_SECTION, mt.getSection())
                    .add(JSON_DAY_TIME, mt.getExactTime())
                    .add(JSON_LOCATION, mt.getRoom())
                    .add(JSON_TA_1, mt.getTa1())
                    .add(JSON_TA_2, mt.getTa2()).build();
            reciArrayBuilder.add(reciJson);
        }
        JsonArray reciArray = reciArrayBuilder.build();
        
        JsonObject dataManagerJSO = Json.createObjectBuilder()
               .add(JSON_LECTURES, lecArray)
               .add(JSON_LABS, labArray)
               .add(JSON_RECITATIONS, reciArray).build();
        
        // AND NOW OUTPUT IT TO A JSON FILE WITH PRETTY PRINTING
	Map<String, Object> properties = new HashMap<>(1);
	properties.put(JsonGenerator.PRETTY_PRINTING, true);
	JsonWriterFactory writerFactory = Json.createWriterFactory(properties);
	StringWriter sw = new StringWriter();
	JsonWriter jsonWriter = writerFactory.createWriter(sw);
	jsonWriter.writeObject(dataManagerJSO);
	jsonWriter.close();

	// INIT THE WRITER
        try{
            OutputStream os = new FileOutputStream(path);
            JsonWriter jsonFileWriter = Json.createWriter(os);
            jsonFileWriter.writeObject(dataManagerJSO);
            String prettyPrinted = sw.toString();
            PrintWriter pw = new PrintWriter(path);
            pw.write(prettyPrinted);
            pw.close();
            }
	catch(IOException e) {
            System.out.println("Section Json Error");
        }
    }
    
    public void exportSchedule(CourseSiteData data, String path) {
        AppGUIModule gui = app.getGUIModule();
        String startMonth = String.valueOf(((DatePicker) gui.getGUINode(SCHEDULE_START_DATEPICKER)).getValue().getMonthValue());
        String startDay = String.valueOf(((DatePicker) gui.getGUINode(SCHEDULE_START_DATEPICKER)).getValue().getDayOfMonth());
        String endMonth = String.valueOf(((DatePicker) gui.getGUINode(SCHEDULE_END_DATEPICKER)).getValue().getMonthValue());
        String endDay = String.valueOf(((DatePicker) gui.getGUINode(SCHEDULE_END_DATEPICKER)).getValue().getDayOfMonth());
        
        JsonArrayBuilder holiArrayBuilder = Json.createArrayBuilder();
        JsonArrayBuilder lecArrayBuilder = Json.createArrayBuilder();
        JsonArrayBuilder referencesArrayBuilder = Json.createArrayBuilder();
        JsonArrayBuilder hwsArrayBuilder = Json.createArrayBuilder();
        JsonArrayBuilder reciArrayBuilder = Json.createArrayBuilder();
        
        Iterator<MeetingTime> schedules = data.scheduleIterator();
        while(schedules.hasNext()) {
            MeetingTime mt = schedules.next();
            if(mt.getType().equals("Holiday")) {
                JsonObject holi = Json.createObjectBuilder()
                        .add(JSON_MONTH, mt.getDate().substring(0,mt.getDate().indexOf("/")))
                        .add(JSON_DAY, mt.getDate().substring(mt.getDate().indexOf("/")+1, mt.getDate().lastIndexOf("/")))
                        .add(JSON_TITLE, mt.getTitle())
                        .add(JSON_LINK, mt.getLink()).build();
                holiArrayBuilder.add(holi);
            }
            else if(mt.getType().equals("Lecture")) {
                JsonObject lec = Json.createObjectBuilder()
                        .add(JSON_MONTH, mt.getDate().substring(0,mt.getDate().indexOf("/")))
                        .add(JSON_DAY, mt.getDate().substring(mt.getDate().indexOf("/")+1, mt.getDate().lastIndexOf("/")))
                        .add(JSON_TITLE, mt.getTitle())
                        .add(JSON_TOPIC, mt.getTopic())
                        .add(JSON_LINK, mt.getLink()).build();
                lecArrayBuilder.add(lec);
            }
            else if(mt.getType().equals("Reference")) {
                JsonObject ref = Json.createObjectBuilder()
                        .add(JSON_MONTH, mt.getDate().substring(0,mt.getDate().indexOf("/")))
                        .add(JSON_DAY, mt.getDate().substring(mt.getDate().indexOf("/")+1, mt.getDate().lastIndexOf("/")))
                        .add(JSON_TITLE, mt.getTitle())
                        .add(JSON_TOPIC, mt.getTopic())
                        .add(JSON_LINK, mt.getLink()).build();
                referencesArrayBuilder.add(ref);
            }
            else if(mt.getType().equals("Recitation")) {
                JsonObject rec = Json.createObjectBuilder()
                        .add(JSON_MONTH, mt.getDate().substring(0,mt.getDate().indexOf("/")))
                        .add(JSON_DAY, mt.getDate().substring(mt.getDate().indexOf("/")+1, mt.getDate().lastIndexOf("/")))
                        .add(JSON_TITLE, mt.getTitle())
                        .add(JSON_TOPIC, mt.getTopic())
                        .add(JSON_LINK, mt.getLink()).build();
                reciArrayBuilder.add(rec);
            }
            else {
                JsonObject hw = Json.createObjectBuilder()
                        .add(JSON_MONTH, mt.getDate().substring(0,mt.getDate().indexOf("/")))
                        .add(JSON_DAY, mt.getDate().substring(mt.getDate().indexOf("/")+1, mt.getDate().lastIndexOf("/")))
                        .add(JSON_TITLE, mt.getTitle())
                        .add(JSON_TOPIC, mt.getTopic())
                        .add(JSON_LINK, mt.getLink())
                        .add("time", "")
                        .add("criteria", "none").build();
                hwsArrayBuilder.add(hw);
            }
        }
        JsonObject dataManagerJSO = Json.createObjectBuilder()
                .add(JSON_START_MONTH, startMonth)
                .add(JSON_START_DAY, startDay)
                .add(JSON_END_MONTH, endMonth)
                .add(JSON_END_DAY, endDay)
                .add(JSON_HOLIDAYS, holiArrayBuilder.build())
                .add(JSON_LECTURES, lecArrayBuilder.build())
                .add(JSON_REFERENCES, referencesArrayBuilder.build())
                .add(JSON_RECITATIONS, reciArrayBuilder.build())
                .add(JSON_HWS, hwsArrayBuilder.build()).build();
        
        // AND NOW OUTPUT IT TO A JSON FILE WITH PRETTY PRINTING
        Map<String, Object> properties = new HashMap<>(1);
        properties.put(JsonGenerator.PRETTY_PRINTING, true);
        JsonWriterFactory writerFactory = Json.createWriterFactory(properties);
        StringWriter sw = new StringWriter();
        JsonWriter jsonWriter = writerFactory.createWriter(sw);
        jsonWriter.writeObject(dataManagerJSO);
        jsonWriter.close();

        // INIT THE WRITER
        try{
            OutputStream os = new FileOutputStream(path);
            JsonWriter jsonFileWriter = Json.createWriter(os);
            jsonFileWriter.writeObject(dataManagerJSO);
            String prettyPrinted = sw.toString();
            PrintWriter pw = new PrintWriter(path);
            pw.write(prettyPrinted);
            pw.close();
        }
        catch(IOException e) {
            System.out.println("Schedule Json Error");
        }
    }
}