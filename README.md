# 🎓 Qatar University Course Planner
![Java Version](https://img.shields.io/badge/Java-25-orange.svg)
![UI Framework](https://img.shields.io/badge/JavaFX-21-blue.svg)
![Build System](https://img.shields.io/badge/Maven-3.8+-red.svg)
> A constraint-based course planner for QU engineering students, handling prerequisite validation and corequisite bundling for multi-major curriculum planning.

### 🚀 Project Overview
The **Qatar University (QU) Course Planner** is a JavaFX application designed to assist QU students in planning their academic semesters. It models course dependencies—prerequisites and corequisites—to generate semester-based course recommendations for **Computer Engineering (CE)** and **Computer Science (CS)** majors.

The application evaluates a student's completed courses and a desired credit limit to suggest a valid set of courses, ensuring that all interdependent corequisites are bundled together in the recommendation.

---

### 🔑 Key Features & Functions
*   **Prerequisite Validation**: The `canTakeCourse()` method verifies that all `CourseIDs` listed in a course's prerequisite list exist within the user's `completedCourses` set.
*   **Corequisite Chain Resolution**: The `suggestNextCourses()` method ensures that if a course is recommended, all its corequisites (identified via `makeCorequisiteBundle`) are also included, provided the total bundle credits do not exceed the user-defined `maxCredits`.
*   **Degree Progress Tracking**: A `ProgressBar` calculates completion based on the ratio of the number of completed courses to the total courses defined in the `CourseIDs` catalog.
*   **Major-Specific Filtering**: Filters the course catalog based on the `Majors` enum (CE or CS) defined in `CourseIDs`.

---

### 📸 Application Preview

---

### ⚙️ Prerequisites & Installation

#### System Requirements
*   **JDK 25**: The project is configured to target Java 25 (`<source>25</source>`, `<target>25</target>`).
*   **Maven 3.8+**: Used for build automation and dependency management.

#### Dependencies (Managed via Maven)
*   **JavaFX (Controls & FXML)**: Core UI framework.
*   **ControlsFX**: Used for advanced UI components.
*   **ValidatorFX**: Provides input validation utilities.
*   **JUnit 5**: Testing framework for unit and integration tests.

#### Installation
1. Clone the repository.
2. Ensure `JAVA_HOME` points to a JDK 25 installation.
3. Install dependencies and build the project:
   ```powershell
   mvn clean install
   ```

---

### 🏃 Usage Examples

#### Running the Application
The application can be launched using the JavaFX Maven plugin (recommended):
```powershell
mvn javafx:run
```
Alternatively, execute the `com.example.smartcourseplanner.Launcher` class.

#### Interaction Steps
1.  **Select Target Major**: Choose "Computer Engineering" or "Computer Science" from the dropdown.
2.  **Set Desired Credit Hours**: Use the spinner (12–21 credits) to define the maximum workload.
3.  **Mark Completed Courses**: Select courses from the checklist in the middle column.
4.  **Generate**: Click **"Generate Recommendations 🚀"**.
5.  **Review**: Valid course recommendations will appear in the "3. Suggested Courses" column.

---

### 🏛️ Architecture & Component Design
The system is structured into three primary layers:

#### 1. Domain Model (`com.example.smartcourseplanner`)
*   **`CourseIDs`**: An enumeration acting as the central data registry. It defines every course's unique ID, title, credit weight, `CourseType` (CORE, COLLEGE_REQUIREMENT, ELECTIVE), and the `Majors` it belongs to.
*   **`Course`**: A wrapper for `CourseIDs` that maintains runtime state, specifically `ArrayList` collections of prerequisite and corequisite `CourseIDs`.
*   **`Majors` & `CourseType`**: Enums defining the supported academic tracks (CE, CS) and course categorization.

#### 2. Planning Engine (`CoursePlanner`)
The `CoursePlanner` class manages the course graph and implements the selection logic:
*   **Data Representation**: Courses are stored in a `HashMap<CourseIDs, Course>`.
*   **Corequisite Bundling**: Uses a **stack-based Depth-First Search (DFS)** in `makeCorequisiteBundle()` to recursively identify all mutually dependent courses. If any corequisite in a chain is missing or invalid for the selected major, the entire bundle is discarded.
*   **Priority Logic**: Implements a weight-based sorting mechanism where `CORE` courses (Weight 1) are prioritized over `COLLEGE_REQUIREMENT` (Weight 2) and `ELECTIVE` (Weight 3).

#### 3. Presentation Layer (`SmartPlannerUI`)
A JavaFX-based dashboard that facilitates:
*   **State Management**: Tracks the `selectedMajor`, `selectedCreditLimit`, and a `Set` of `completedCourses`.
*   **Data Flow**: On user trigger, it passes the current state to the `CoursePlanner`, which returns a `Set<Course>` of recommendations displayed in a `ListView`.


---

### 📂 Project Structure
```text
src/
├── main/
│   ├── java/
│   │   ├── com.example.smartcourseplanner/
│   │   │   ├── Course.java              # Course relationship wrapper
│   │   │   ├── CourseIDs.java           # Central course catalog (Enum)
│   │   │   ├── CoursePlanner.java       # Recommendation engine & algorithms
│   │   │   ├── CourseType.java          # CORE, COLLEGE_REQUIREMENT, ELECTIVE
│   │   │   ├── HelloApplication.java    # JavaFX entry point
│   │   │   ├── HelloController.java     # FXML controller point
│   │   │   ├── Launcher.java            # Main entry point for JAR/IDE
│   │   │   ├── Majors.java              # Major definitions (CE, CS)
│   │   │   └── SmartPlannerUI.java      # Primary UI and graph initialization
│   │   └── module-info.java             # Java Module System configuration
│   └── resources/
│       └── com.example.smartcourseplanner/
│           └── hello-view.fxml          # FXML layout (unused by SmartPlannerUI)
└── pom.xml                              # Maven build and dependency config
```

---

### ⚠️ Limitations
*   **Course Availability**: Does not account for which semesters (Fall/Spring) specific courses are offered.
*   **Scheduling Conflicts**: Does not check for overlapping class times or timetable constraints.
*   **Global Optimization**: Generates a valid plan for the *next* semester only; it does not calculate the shortest path to graduation across multiple future semesters.
*   **External Integration**: Operates as a standalone tool and does not sync with university registration or student record systems.

---

### 🧪 Testing & Future Enhancements

#### Testing
Currently, the repository does not contain automated test suites (e.g., JUnit). All verification has been performed through manual UI testing of the recommendation logic. For future contributors, the project is configured for **JUnit 5**, and tests can be executed using:
```powershell
mvn test
```

#### Future Roadmap
*   **Semester Availability Filter**: Integrate logic to distinguish between Fall, Spring, and Summer course offerings.
*   **Conflict Detection**: Implement a timetable validator to check for overlapping time slots between recommended courses.
*   **Multi-Semester Pathing**: Extend the engine to generate a multi-semester graduation path rather than a single-semester recommendation.

---

### 📜 License
No explicit license is provided for this project.
