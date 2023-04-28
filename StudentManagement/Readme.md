## Table Structure
Database schema StudentEnrollment contains following tabels:

`class`: Repersents a class and includes information such as class ID, course number, section number, term and class description.
  __In this table, term has to be entered specifically as below, otherwise the logic to get most recent class in ClassManagement will fail.__
  - [ ] 'Fall2023' for Fall of 2023
  - [ ] 'Spri2023' for Spring of 2023
  - [ ] 'Summ2023' for Summer of 2023

`ctategory`: Defines different categories for gradeing within a class. Each category has a ctategory id,ctategory name, weight and is associated with a specific class.

`student`: Stores the information about students, including student id, username, first name, last name, email-address, address and contact number.

 `assignment`: Repersents assignments given in a class. It includes an assignment id, assignment name, assignment description, point value, class ID, category Id. It also has foreign key refrences to the class and category tables.
 
`grades`: Stores grades of each students's assignment. It includes assignment ID, student ID and the grades received. The table has forign key refrences to the assignment and student tabels.

`enrolled`: Tracks the enrollment of students in classes. It includes the studnet ID, class ID, serving as the many to many relationship table between student and class.

### Implementation

The `util.java` class contains all the methods such as _getCategoryId, checkStudentEnrolled, checkStudentExist, getCategoryId_ etc. which helps to check the existence of students, categories assignments in database.

The `ActiveClass.java` class stores all the attributes of the current class and provides getter and setter methods of each attribute.

The `ClassManagement.java` class contains all the methods to create and manage the class. The functionalities of class management are _newClass_ to the database if it doesn't already exist, _selectClass_ with different parameters and _assignActiveClass_ stores the result of selected class.

The `CategoryAssignment.java` class in Java provides methods for managing categories and assignments associated with a class. The functionalities of the class to add new categories to the database for a specific class if it doesn't already exist, based on the provided category name and weight. The `showAssignment` and `showCategories` methods display the categories and assignment of a given class by retrieving them from the database. 

The `StudentManagement.java` class contains methods to manage the studnets. The class contains the functionalities to add students if not already exist in database and enroll studnets in the class, if students are not enrolled in the class. The `gradeAssignment` method assigned the grades to students for the assignment after verify that if student is enrolled in that course or not. 

The `gradesManagement.java` class manages the grade record of students. The `studentGrades` displays  student’s current grade in each category and subtaotal of each category as well as student's overall grade in the class. The `gradebook` method displays the current class’s gradebook as well as information of student along with their total grades in class  

## Execution
