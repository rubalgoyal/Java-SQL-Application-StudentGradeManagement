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

