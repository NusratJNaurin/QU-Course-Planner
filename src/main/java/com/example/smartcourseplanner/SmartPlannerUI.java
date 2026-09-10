package com.example.smartcourseplanner;

import javafx.application.Application;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.util.*;

public class SmartPlannerUI extends Application {

    // Connect to your backend class
    private final CoursePlanner planner = new CoursePlanner();
    private EnumSet<CourseIDs> courses = EnumSet.allOf(CourseIDs.class);
    private EnumSet<CourseIDs> completedCourses = EnumSet.noneOf(CourseIDs.class);
    private Majors selectedMajor = Majors.CE;
    private int selectedCreditLimit = 16;

    // UI Components that need updating

    private ListView<String> recommendationListView = new ListView<>();
    private final Label statusLabel = new Label("Select courses to see recommendations.");
    private final ProgressBar progressBar = new ProgressBar(0.0);


    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("🎓 Qatar University Course Planner");

        // Initialize backend graph data
        initializeGraphData();

        // --- HEADER ---
        HBox header = new HBox();
        header.setPadding(new Insets(5, 15, 15, 25)); // Top, Right, Bottom, Left spacing
        header.setAlignment(Pos.CENTER);
        header.setStyle("-fx-background-color: #1a202c; -fx-border-color: #2d3748; -fx-border-width: 0 0 1 0;");
        Label headingLabel = new Label("🎓 Smart Course Planner");
        headingLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #818cf8;");
        header.getChildren().add(headingLabel);

        // --- COLUMN 1: DEGREE FOCUS ---
        VBox col1 = new VBox(15);
        col1.setPadding(new Insets(15));
        col1.setStyle("-fx-background-color: #2d3748; -fx-background-radius: 8;");
        
        Label col1Header = new Label("1. Degree Focus");
        col1Header.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #9f7aea;");
        
        Label degreeLabel = new Label("Select Target Major:");
        degreeLabel.setStyle("-fx-text-fill: #cbd5e0;");
        
        ComboBox<String> degreeComboBox = new ComboBox<>();
        degreeComboBox.getItems().addAll("Computer Engineering", "Computer Science");
        degreeComboBox.setValue("Computer Engineering");
        degreeComboBox.setMaxWidth(Double.MAX_VALUE);
        degreeComboBox.setOnAction(e -> {
            switch (degreeComboBox.getValue()) {
                case "Computer Engineering" -> selectedMajor = Majors.CE;
                case "Computer Science" -> selectedMajor = Majors.CS;
            }
        });

        Label creditLabel = new Label("Desired Credit Hours:");
        creditLabel.setStyle("-fx-text-fill: #cbd5e0;");

        Spinner<Integer> creditSpinner = new Spinner<>(12, 21, selectedCreditLimit);
        creditSpinner.setMaxWidth(Double.MAX_VALUE);
        creditSpinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            selectedCreditLimit = newValue;

        });

        Label engineNotes = new Label("💡 Backend optimization evaluates graph vertices using prerequisite maps.");
        engineNotes.setStyle("-fx-text-fill: #718096; -fx-font-size: 11px;");
        engineNotes.setWrapText(true);

        col1.getChildren().addAll(col1Header, degreeLabel, degreeComboBox, creditLabel, creditSpinner, engineNotes);


        // --- COLUMN 2: COMPLETED CHECKLIST ---
        VBox col2 = new VBox(15);
        col2.setPadding(new Insets(15));
        col2.setStyle("-fx-background-color: #2d3748; -fx-background-radius: 8;");
        
        Label col2Header = new Label("2. Completed Courses");
        col2Header.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #9f7aea;");
        
        ScrollPane scrollPane = new ScrollPane();
        VBox checkboxContainer = new VBox(10);
        checkboxContainer.setPadding(new Insets(5));
        

        for (CourseIDs cid : courses) {
            Course id = planner.findCourse(cid);
            String courseName = (id != null) ? id.getCourseName() : "Unknown Course";
            CheckBox checkBox = new CheckBox(cid + ": " + courseName);
            checkBox.setStyle("-fx-text-fill: #e2e8f0;");

            checkBox.setOnAction(e -> {
                if (checkBox.isSelected()) {
                    completedCourses.add(cid);
                } else {
                    completedCourses.remove(cid);
                }
            });
            checkboxContainer.getChildren().add(checkBox);
        }
        scrollPane.setContent(checkboxContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: #2d3748; -fx-border-color: #4a5568;");
        
        col2.getChildren().addAll(col2Header, scrollPane);


        // --- COLUMN 3: RECOMMENDATIONS ---
        VBox col3 = new VBox(15);
        col3.setPadding(new Insets(15));
        col3.setStyle("-fx-background-color: #2d3748; -fx-background-radius: 8;");
        
        Label col3Header = new Label("3. Suggested Courses");
        col3Header.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #9f7aea;");
        
        recommendationListView.setStyle("-fx-background-color: #1a202c; -fx-text-fill: white;");
        statusLabel.setStyle("-fx-text-fill: #a0aec0; -fx-font-style: italic;");
        
        col3.getChildren().addAll(col3Header, recommendationListView, statusLabel);


        // --- SUBMIT BUTTON ---
        Button submitButton = new Button("Generate Recommendations 🚀");
        submitButton.setStyle("-fx-background-color: #6366f1; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10 20 10 20;");
        HBox buttonBox = new HBox(submitButton);
        buttonBox.setAlignment(Pos.CENTER);

        submitButton.setOnAction(e -> updateRecommendations());


        // --- MAIN ACCENT LAYOUT GRID ---
        GridPane mainDashboard = new GridPane();
        mainDashboard.setPadding(new Insets(20));
        mainDashboard.setHgap(15);
        mainDashboard.setVgap(15);
        mainDashboard.setStyle("-fx-background-color: #1a202c;"); // Dark theme

        ColumnConstraints colConstraints = new ColumnConstraints();
        colConstraints.setPercentWidth(33.3);
        mainDashboard.getColumnConstraints().addAll(colConstraints, colConstraints, colConstraints);

        // Map layout columns to grid coordinates
        mainDashboard.add(header, 0, 0, 3, 1); GridPane.setHalignment(header, HPos.CENTER);
        mainDashboard.add(col1, 0, 1);
        mainDashboard.add(col2, 1, 1);
        mainDashboard.add(col3, 2, 1);
        mainDashboard.add(buttonBox, 0, 2, 3, 1);

        // --- FOOTER PROGRESS SECTION ---
        HBox footer = new HBox(20);
        footer.setPadding(new Insets(15, 20, 15, 20));
        footer.setStyle("-fx-background-color: #2d3748;");
        progressBar.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(progressBar, Priority.ALWAYS);
        
        Label progressLabel = new Label("Degree Completion Matrix:");
        progressLabel.setStyle("-fx-text-fill: #e2e8f0; -fx-font-weight: bold;");
        footer.getChildren().addAll(progressLabel, progressBar);

        BorderPane appRoot = new BorderPane();
        appRoot.setCenter(mainDashboard);
        appRoot.setBottom(footer);

        Scene scene = new Scene(appRoot, 950, 700);
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    private void initializeGraphData() {
        for (CourseIDs id : CourseIDs.values()) {
            planner.addCourse(new Course(id));
        }

        planner.addPrerequisite(CourseIDs.CMPS251, CourseIDs.CMPS151);
        planner.addPrerequisite(CourseIDs.CMPS303, CourseIDs.CMPS251);
        planner.addPrerequisite(CourseIDs.CMPS310, CourseIDs.CMPS251);
        planner.addPrerequisite(CourseIDs.CMPS323, CourseIDs.CMPS303);
        planner.addPrerequisite(CourseIDs.CMPS323, CourseIDs.CMPS205);
        planner.addPrerequisite(CourseIDs.CMPS350, CourseIDs.CMPS251);
        planner.addPrerequisite(CourseIDs.CMPS351, CourseIDs.CMPS251);
        planner.addPrerequisite(CourseIDs.CMPS380, CourseIDs.CMPS303);
        planner.addPrerequisite(CourseIDs.CMPS405, CourseIDs.CMPE263);
        planner.addPrerequisite(CourseIDs.CMPS405, CourseIDs.CMPS303);

        planner.addPrerequisite(CourseIDs.CMPE261, CourseIDs.CMPS205);
        planner.addPrerequisite(CourseIDs.CMPE263, CourseIDs.CMPS205);
        planner.addPrerequisite(CourseIDs.CMPE263, CourseIDs.CMPS151);
        planner.addPrerequisite(CourseIDs.CMPE363, CourseIDs.CMPE263);
        planner.addPrerequisite(CourseIDs.CMPE363, CourseIDs.CMPE261);
        planner.addPrerequisite(CourseIDs.CMPE355, CourseIDs.CMPE263);
        planner.addPrerequisite(CourseIDs.CMPE364, CourseIDs.CMPE363);
        planner.addPrerequisite(CourseIDs.CMPE370, CourseIDs.CMPE261);
        planner.addCorequisite(CourseIDs.CMPE370, CourseIDs.GENG498);
        planner.addPrerequisite(CourseIDs.CMPE457, CourseIDs.CMPE355);
        planner.addPrerequisite(CourseIDs.CMPE462, CourseIDs.CMPE364);
        planner.addPrerequisite(CourseIDs.CMPE476, CourseIDs.ELEC351);

        planner.addCorequisite(CourseIDs.ELEC201, CourseIDs.MATH101);
        planner.addCorequisite(CourseIDs.ELEC201, CourseIDs.PHYS193);
        planner.addPrerequisite(CourseIDs.ELEC231, CourseIDs.ELEC201);
        planner.addPrerequisite(CourseIDs.ELEC351, CourseIDs.ELEC201);

        planner.addPrerequisite(CourseIDs.GENG200, CourseIDs.MATH102);
        planner.addCorequisite(CourseIDs.GENG200, CourseIDs.CMPE355);
        planner.addPrerequisite(CourseIDs.GENG300, CourseIDs.CMPS151);
        planner.addPrerequisite(CourseIDs.GENG300, CourseIDs.MATH102);
        planner.addPrerequisite(CourseIDs.GENG300, CourseIDs.MATH211);
        planner.addPrerequisite(CourseIDs.GENG300, CourseIDs.MATH231);
        planner.addPrerequisite(CourseIDs.GENG360, CourseIDs.MATH102);
        planner.addPrerequisite(CourseIDs.GENG498, CourseIDs.CMPS310);
        planner.addPrerequisite(CourseIDs.GENG499, CourseIDs.GENG498);

        planner.addPrerequisite(CourseIDs.MATH102, CourseIDs.MATH101);
        planner.addPrerequisite(CourseIDs.MATH211, CourseIDs.MATH102);
        planner.addPrerequisite(CourseIDs.MATH217, CourseIDs.MATH211);
        planner.addPrerequisite(CourseIDs.MATH231, CourseIDs.MATH101);

        planner.addPrerequisite(CourseIDs.PHYS191, CourseIDs.MATH101);
        planner.addCorequisite(CourseIDs.PHYS191, CourseIDs.PHYS192);
        planner.addPrerequisite(CourseIDs.PHYS193, CourseIDs.PHYS191);
        planner.addPrerequisite(CourseIDs.PHYS193, CourseIDs.PHYS192);
        planner.addCorequisite(CourseIDs.PHYS193, CourseIDs.PHYS194);
    }


    private void updateRecommendations() {
        recommendationListView.getItems().clear();

        Set<Course> recommendations = planner.suggestNextCourses(completedCourses, selectedMajor, selectedCreditLimit);

        for (Course course : recommendations) {
            recommendationListView.getItems().add(course.getCourseID() + " - " + course.getCourseName());
        }

        if (completedCourses.isEmpty()) {
            statusLabel.setText("Select courses to see recommendations.");
            progressBar.setProgress(0.0);
        } else {
            statusLabel.setText("✓ Graph evaluated: " + recommendations.size() + " options available.");
            double progress = (double) completedCourses.size() / courses.size();
            progressBar.setProgress(progress);
        }
    }

    
    public static void main(String[] args) {
        launch(args);
    }
}