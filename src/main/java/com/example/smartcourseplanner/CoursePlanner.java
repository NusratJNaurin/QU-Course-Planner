package com.example.smartcourseplanner;

import java.util.*;

public class CoursePlanner {
    private HashMap<CourseIDs, Course> allCourses;
    //private HashSet<CourseIDs> completedCourses;

    public CoursePlanner() {
        this.allCourses = new HashMap<>();
        //this.completedCourses = new HashSet<>();
    }

    public void addCourse(Course c) {
        if (c == null || allCourses.containsKey(c.getCourseID())) return;
        allCourses.put(c.getCourseID(), c);
    }

    public void addPrerequisite(CourseIDs course, CourseIDs prereq) {
        Course c = allCourses.get(course);
        if (c == null)
            throw new IllegalArgumentException("Course not found: " + course);

        if (!allCourses.containsKey(prereq))
            throw new IllegalArgumentException("Prerequisite not found: " + prereq);

        c.addPrerequisite(prereq);
    }

    public void addCorequisite(CourseIDs course, CourseIDs coreq) {
        Course c1 = allCourses.get(course);
        Course c2 = allCourses.get(coreq);

        if (c1 == null)
            throw new IllegalArgumentException("Course not found: " + course);
        if (c2 == null)
            throw new IllegalArgumentException("Course not found: " + coreq);

        c1.addCorequisite(coreq);
        c2.addCorequisite(course);
    }

    public Course findCourse(CourseIDs cid) {
        return allCourses.get(cid);
    }

    private boolean hasTakenPrerequisites(Course c, Set<CourseIDs> completedCourses) {
        for (CourseIDs prereq : c.getPrerequisites()) {
            if (!completedCourses.contains(prereq))
                return false;
        }
        return true;
    }

    public boolean canTakeCourse(CourseIDs cid, Set<CourseIDs> completedCourses) {
        // can take a course if prereqs are met
        if (completedCourses == null)
            return false;
        if (completedCourses.contains(cid))  //can not retake a course
            return false;

        Course c = allCourses.get(cid);
        if (c == null) return false;

        return (hasTakenPrerequisites(c, completedCourses));
    }

    public ArrayList<Course> getAvailableCourses(Set<CourseIDs> completedCourses) {
        ArrayList<Course> available = new ArrayList<>();
        for (CourseIDs cid : allCourses.keySet()) {
            if (canTakeCourse(cid, completedCourses))
                available.add(allCourses.get(cid));
        }
        return available;
    }

    private Set<Course> makeCorequisiteBundle(Course c, Set<CourseIDs> completedCourses,
                                              Majors major, Set<Course> addedThisSemester) {
        Set<Course> bundle = new LinkedHashSet<>();
        Set<CourseIDs> visited = new HashSet<>();

        Stack<Course> stack = new Stack<>();
        stack.push(c);

        while (!stack.isEmpty()) {
            Course current = stack.pop();
            if (!visited.add(current.getCourseID())) // tells you if the course is there or not, if not it is added
                continue;      // If this course is already in the set, skip it.
            bundle.add(current);

            for (CourseIDs coreqId : current.getCorequisites()) {
                if (completedCourses.contains(coreqId))
                    continue;

                Course coreq = findCourse(coreqId);
                if (coreq == null || !coreq.getMajor().contains(major) || addedThisSemester.contains(coreq))
                    return null;
                stack.push(coreq);
            }
        }
        return bundle;
    }

    private int getCategoryPriorityWeight(CourseType type) {
        if (type == null) return 4; // Unclassified items drop to the bottom

        return switch (type) {
            case CORE -> 1;
            case COLLEGE_REQUIREMENT -> 2;
            case ELECTIVE -> 3;
        };
    }

    public Set<Course> suggestNextCourses(Set<CourseIDs> completedCourses, Majors major, int maxCredits) {
        List<Course> available = getAvailableCourses(completedCourses);
        available.sort(Comparator
                .comparingInt((Course c) ->
                        getCategoryPriorityWeight(c.getCourseID().getCourseType()))
                .thenComparing(
                        (Course c) -> c.getPrerequisites().isEmpty()
                )
        );

        Set<Course> addedThisSemester = new LinkedHashSet<>();
        int credits = 0;

        for (Course c : available) {
            if (addedThisSemester.contains(c))
                continue;

            if (!c.getMajor().contains(major))
                continue;

            Set<Course> bundle = makeCorequisiteBundle(c, completedCourses, major, addedThisSemester);
            if (bundle == null) // if bundle is invalid
                continue;

            int bundleCredits = 0;
            for (Course course : bundle)
                bundleCredits += course.getCreditHours();

            if (credits + bundleCredits <= maxCredits) {
                addedThisSemester.addAll(bundle);
                credits += bundleCredits;
            }
        }

        return addedThisSemester;
    }
}
