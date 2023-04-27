# CS510-Database

The repository contains the database conatins the database schema and SQL statements for a Student Enrollment syatem. The system is designed to manage
classes, categories, assignments,students, grades and enrollment information.

### Table Structure
Database schema StudentEnrollment contains following tabels:

`class`: Repersents a class and includes information such as class ID, course number, section number, term and class description.

`ctategory`: Defines different categories for gradeing within a class. Each category has a ctategory id,ctategory name, weight and is associated with a specific class.

`student`: Stores the information about students, including student id, username, first name, last name, email-address, address and contact number.

 `assignment`: Repersents assignments given in a class. It includes an assignment id, assignment name, assignment description, point value, class ID, category Id. It also has foreign key refrences to the class and category tables.
 
`grades`: Stores grades of each students's assignment. It includes assignment ID, student ID and the grades received. The table has forign key refrences to the assignment and student tabels.

`enrolled`: Tracks the enrollment of students in classes. It includes the studnet ID, class ID, serving as the many to many relationship table between student and class.

### Implementation

The `util.java` class contains all the methods such as `getCategoryId`, `checkStudentEnrolled`, `checkStudentExist`, `getCategoryId` etc. which helps to check the existence of students, categories assignments in database.

The `ActiveClass.java` class stores all the attributes of the currentclass and provides getter and setter methods of each attribute.

The `ClassManagement.java` class contains all the methods to create and manage the class. The functionalities of class management are `newClass`to the database if it doesn't already exist, `selectClass` with different parameters and `assignActiveClass` stores the result of selected class.

The `CategoryAssignment.java` class in Java provides methods for managing categories and assignments associated with a class. The functionalities of the class to add new categories to the database for a specific class if it doesn't already exist, based on the provided category name and weight. The `showAssignment` and `showCategories` methods display the categories and assignment of a given class by retrieving them from the database. 

The `StudentManagement.java` class contains methods to manage the studnets. The class contains the functionalities to add students if not already exist in database and enroll studnets in the class, if students are not enrolled in the class. The `gradeAssignment` method assigned the grades to students for the assignment after verify that if student is enrolled in that course or not. 








