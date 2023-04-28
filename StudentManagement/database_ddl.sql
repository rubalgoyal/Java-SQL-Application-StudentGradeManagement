DROP DATABASE IF EXISTS StudentEnrollment ;

CREATE DATABASE StudentEnrollment;

USE StudentEnrollment;

CREATE TABLE IF NOT EXISTS class(
	class_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
	term VARCHAR(20),
	course_number VARCHAR(20),
	section_number INT,
	class_description TEXT
);

CREATE TABLE IF NOT EXISTS category(
	category_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
	weight FLOAT,
	category_name VARCHAR(20),
	class_id INT,
	FOREIGN KEY (class_id) REFERENCES class(class_id)
);

CREATE TABLE student(
	student_id INT NOT NULL  PRIMARY KEY,
	username VARCHAR(200) UNIQUE,
	first_name VARCHAR(70),
    last_name VARCHAR(50),
	email_address VARCHAR(200),
	contact_number VARCHAR(20),
	address VARCHAR(500)
);

CREATE TABLE IF NOT EXISTS assignment(
	assignment_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
	assignment_name VARCHAR(100),
    assignment_description TEXT NULL,
	point_value FLOAT,
	class_id INT,
	category_id INT,
	FOREIGN KEY (class_id) REFERENCES class(class_id),
	FOREIGN KEY (category_id) REFERENCES category(category_id)
);
CREATE TABLE IF NOT EXISTS grades(
	student_id INT,
	assignment_id INT,
	grade FLOAT,
	FOREIGN KEY (assignment_id) REFERENCES assignment(assignment_id),
	FOREIGN KEY (student_id) REFERENCES student(student_id),
	PRIMARY KEY (student_id, assignment_id)
);

CREATE TABLE IF NOT EXISTS enrolled(
	student_id INT NOT NULL,
	class_id INT NOT NULL,	
	FOREIGN KEY (class_id) REFERENCES class(class_id),
	FOREIGN KEY (student_id) REFERENCES student(student_id),
	PRIMARY KEY (student_id, class_id)
);
  


--   
