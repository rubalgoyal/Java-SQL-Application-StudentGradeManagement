USE StudentEnrollment;
-- SELECT * FROM student;
-- SELECT * 
-- FROM class
-- WHERE course_number = 'CS410';

-- SELECT * 
-- FROM class
-- WHERE course_number = 'CS410'
-- 	AND term = 'Sp23';
    
    --  SELECT class_id, course_number, term,section_number, class_description
--         FROM class;
--         
-- INSERT INTO category(category_name, weight, class_id) VALUES (?, ?, ?); 

-- SELECT COUNT(*) FROM category
-- WHERE category_name = '%s'
-- 	AND class_id = %d;

-- SELECT category_id,category_name, weight, category.class_id 
-- FROM category 
-- JOIN class
-- ON category.class_id = class.class_id
-- WHERE class.course_number = 'CS510';

-- SELECT * FROM assignment;




-- INSERT INTO assignment(assignment_name, point_value, class_id, category_id) VALUES ('Homework-1', 50, 7, 2);
-- INSERT INTO assignment(assignment_name, point_value, class_id, category_id) 
-- VALUES ('Homework-2', 50, 7, 2),
-- ('Homework-3', 50, 7, 2);
-- Get all the assignments available for class_id = 7 and category name = 'Assignments'

-- SET @cat_id = (
-- 			SELECT category_id
-- 			FROM category 
-- 			JOIN class
-- 				ON category.class_id = class.class_id
-- 			WHERE class.course_number = 'CS510'
-- 				AND category.category_name = 'Assignments'
-- );

-- SELECT @cat_id;

-- SELECT c.category_name, a.assignment_name, a.point_value
-- FROM assignment a
-- JOIN category c
-- ON a.category_id = c.category_id
-- WHERE c.class_id = 7
-- GROUP BY c.category_name, a.assignment_name, a.point_value
-- ORDER BY c.category_name;
-- 	

-- SELECT c.course_number, c.term, COUNT(e.student_id) AS num_students
-- FROM class c
-- INNER JOIN enrolled e
-- 	ON c.class_id = e.class_id
-- GROUP BY c.course_number, c.term;

select * from student;


    
    
    
