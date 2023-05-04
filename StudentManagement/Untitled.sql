WITH overall_grade AS (
	SELECT s.username, 
				   c.course_number, 
				   c.term, 
				   ct.category_name, 
				   ct.weight, 
				   COALESCE(g.grade,0) grade,
				   a.point_value category_total,
				   CASE
						WHEN g.student_id IS NULL
							THEN 0
						ELSE
							a.point_value
				   END attempted_category_total
			FROM student s
			INNER JOIN enrolled e
				ON s.student_id = e.student_id
			INNER JOIN class c
				ON e.class_id = c.class_id
			INNER JOIN category ct
				ON ct.class_id = c.class_id
			INNER JOIN assignment a
				ON a.category_id = ct.category_id
				AND a.class_id = e.class_id
			LEFT JOIN grades g
				ON g.student_id = s.student_id
				ANd g.assignment_id = a.assignment_id
			WHERE s.username = 'johnsmith01'
	
)

-- SELECT username, course_number, term, weight,
-- 	   (SUM(grade)*100) / SUM(NULLIF(attempted_category_total,0)) attempted_grade
SELECT *
FROM overall_grade