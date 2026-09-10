package com.example.smartcourseplanner;

import java.util.*;

public class Course {
    private final CourseIDs courseID;
    private List<CourseIDs> prerequisites;
    private List<CourseIDs> corequisites;

    public Course(CourseIDs courseID) {
        this.courseID = courseID;
        this.prerequisites = new ArrayList<>();
        this.corequisites = new ArrayList<>();
    }

    public CourseIDs getCourseID() {return courseID;}

    public String getCourseName() {return courseID.getCourseName();}

    public EnumSet<Majors> getMajor() {return courseID.getMajor();}

    public List<CourseIDs> getPrerequisites() {return prerequisites;}

    public List<CourseIDs> getCorequisites() {return corequisites;}

    public int getCreditHours() {return courseID.getCreditHours();}

    public void addPrerequisite(CourseIDs prereqId) {
        if (this.courseID == prereqId)
            throw new IllegalArgumentException("A course cannot be a prerequisite of itself!");

        if (!prerequisites.contains(prereqId))
            prerequisites.add(prereqId);

    }

    public void addCorequisite(CourseIDs coreqId) {
        if (this.courseID == coreqId)
            throw new IllegalArgumentException("A course cannot be a co-requisite of itself!");

        if (!corequisites.contains(coreqId))
            corequisites.add(coreqId);
    }
}
