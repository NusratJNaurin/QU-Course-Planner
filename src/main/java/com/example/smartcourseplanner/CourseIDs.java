package com.example.smartcourseplanner;

import java.util.*;

public enum CourseIDs {
    CMPS151("Programming Concepts",3, CourseType.CORE, Majors.CE, Majors.CS),
    CMPS200("Computer Ethics",3, CourseType.CORE, Majors.CS),
    CMPS205("Discrete Structures",3, CourseType.CORE,Majors.CE, Majors.CS),
    CMPS251("Object-Oriented Programming",4, CourseType.CORE,Majors.CE, Majors.CS),
    CMPS303("Data Structures",4, CourseType.CORE,Majors.CE, Majors.CS),
    CMPS307("Introduction to Project Management & Entrepreneurship",2, CourseType.ELECTIVE, Majors.CS),
    CMPS310("Software Engineering",4, CourseType.CORE, Majors.CS),
    CMPS323("Design & Analysis of Algorithms",3, CourseType.CORE, Majors.CS),
    CMPS350("Web Development Fundamentals",3, CourseType.CORE, Majors.CS),
    CMPS351("Fundamentals of Database Systems",4, CourseType.CORE, Majors.CS),
    CMPS380("Cybersecurity Fundamentals",3, CourseType.CORE, Majors.CS),
    CMPS405("Operating Systems",4, CourseType.CORE,Majors.CE, Majors.CS),

    CMPE261("Digital Logic Design",4, CourseType.CORE, Majors.CE),
    CMPE263("Computer Architecture I",3, CourseType.CORE,Majors.CE, Majors.CS),
    CMPE355("Data Communication I",4, CourseType.CORE, Majors.CE),
    CMPE363("Computer Architecture II",3, CourseType.CORE, Majors.CE),
    CMPE364("Microprocessor Based Design",4, CourseType.CORE, Majors.CE),
    CMPE370("CE Practicum",1, CourseType.CORE, Majors.CE),
    CMPE457("Data Communication II",3, CourseType.CORE, Majors.CE),
    CMPE462("Computer Interfacing",3, CourseType.CORE, Majors.CE),
    CMPE476("Digital Signal Processing",4, CourseType.CORE, Majors.CE),

    ELEC201("Electric Circuits",3, CourseType.COLLEGE_REQUIREMENT, Majors.CE),
    ELEC231("Fundamentals of Electronics",3, CourseType.CORE, Majors.CE),
    ELEC351("Signals & Systems",3, CourseType.CORE, Majors.CE),

    GENG107("Engineering Skills & Ethics",3, CourseType.COLLEGE_REQUIREMENT, Majors.CE),
    GENG200("Probability & Statistics for Engineers",3, CourseType.COLLEGE_REQUIREMENT, Majors.CE, Majors.CS),
    GENG300("Numerical Methods",3, CourseType.COLLEGE_REQUIREMENT, Majors.CE, Majors.CS),
    GENG360("Engineering Economics",3, CourseType.COLLEGE_REQUIREMENT, Majors.CE),
    GENG498("Senior Design Project I",3, CourseType.CORE, Majors.CE, Majors.CS),
    GENG499("Senior Design Project II",3, CourseType.CORE, Majors.CE, Majors.CS),

    MATH101("Calculus I",3, CourseType.COLLEGE_REQUIREMENT, Majors.CE, Majors.CS),
    MATH102("Calculus II",3, CourseType.COLLEGE_REQUIREMENT, Majors.CE, Majors.CS),
    MATH211("Calculus III",3, CourseType.COLLEGE_REQUIREMENT, Majors.CE),
    MATH217("Mathematics for Engineers",3, CourseType.COLLEGE_REQUIREMENT, Majors.CE),
    MATH231("Linear Algebra",3, CourseType.COLLEGE_REQUIREMENT,Majors.CS),

    PHYS191("Physics for Engineering I",3, CourseType.COLLEGE_REQUIREMENT,Majors.CE, Majors.CS),
    PHYS192("Experimental Physics I",1, CourseType.COLLEGE_REQUIREMENT,Majors.CE, Majors.CS),
    PHYS193("Physics for Engineering II",3, CourseType.COLLEGE_REQUIREMENT,Majors.CE, Majors.CS),
    PHYS194("Experimental Physics II",1, CourseType.COLLEGE_REQUIREMENT,Majors.CE, Majors.CS);

    private final String courseName;
    private final int credits;
    private final CourseType courseType;
    private final EnumSet<Majors> major;

    CourseIDs(String s, int crhs, CourseType type, Majors... m) {
        courseName = s;
        credits = crhs;
        courseType = type;
        major = m.length == 0 ? EnumSet.allOf(Majors.class) : EnumSet.copyOf(List.of(m));
    }

    public String getCourseName() {
        return courseName;
    }

    public int getCreditHours() {
        return credits;
    }

    public EnumSet<Majors> getMajor() {
        return major;
    }

    public CourseType getCourseType() {
        return courseType;
    }
}
