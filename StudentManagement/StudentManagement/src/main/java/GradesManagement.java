import java.lang.Math;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

public class GradesManagement {
    private static final Logger LOGGER = Logger.getLogger("Grades Management");

    public static void studentGrades(String username, ActiveClass activeClass, Connection conn){
        int studentId = util.getStudentId(username,conn);
        boolean isStudentExist = util.checkStudentExist(studentId,username,conn);
        if(!isStudentExist)
            LOGGER.severe("Student does not exist");
        else{
            String sqlQuery = String.format(
                    """
                            WITH marks_calculation AS (
                            			SELECT s.student_id, s.username, c.course_number, c.term, ct.category_name, a.assignment_name, ct.weight, a.point_value overall_total_marks,
                            				   CASE
                            						WHEN g.grade IS NULL
                            							THEN NULL
                            						ELSE
                            							a.point_value
                            					END attempted_total_marks,
                            					g.grade
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
                            			WHERE s.username = '%s'
                            				AND c.class_id = %d
                            	),
                              agg_mark_calculation AS (
                            			SELECT student_id, username, course_number,term, category_name,assignment_name,weight,overall_total_marks,attempted_total_marks,grade,
                            				   ROUND(SUM(COALESCE(grade,0)) OVER (PARTITION BY username, course_number, term, category_name ORDER BY category_name),2) category_total_gained,
                                               ROUND(SUM(COALESCE(overall_total_marks,0)) OVER (PARTITION BY username, course_number, term, category_name ORDER BY category_name),2) overall_category_total,
                                               ROUND(SUM(COALESCE(attempted_total_marks,0)) OVER (PARTITION BY username, course_number, term, category_name ORDER BY category_name),2) attempted_category_total,
                                               COUNT(grade) OVER (PARTITION BY username, course_number, term, category_name) num_assignments_attempted,
                                               COUNT(COALESCE(grade,0)) OVER (PARTITION BY username, course_number, term, category_name) num_assignments_total
                                        FROM marks_calculation
                            	),
                              grade_calculated AS (
                            					SELECT username, course_number, term, category_name, assignment_name, weight, grade obtained_marks, category_total_gained, overall_category_total,attempted_category_total,
                            						   ROUND((category_total_gained*100*weight)/overall_category_total,2) category_grade_overall,
                            						   CASE
                            								WHEN COALESCE(category_total_gained,0) = 0
                            									THEN NULL
                            								ELSE
                            									ROUND((category_total_gained*100*weight)/attempted_category_total,2)
                            						   END category_grade_attempted,
                                                       CASE
                            								WHEN num_assignments_attempted = 0
                            									THEN 1
                            								ELSE
                            									num_assignments_attempted
                            						   END attempted_assignment_num,
                                                       num_assignments_total,
                                                       CASE
                            								WHEN COALESCE(grade,0) = 0
                            									THEN NULL
                            								ELSE
                            									ROUND(weight* 100,0)
                            						   END category_max_grade
                            					FROM agg_mark_calculation
                            	)
                            SELECT username, 
                                   course_number, 
                                   term, 
                                   category_name, 
                                   assignment_name, 
                                   obtained_marks, 
                                   category_grade_overall, 
                                   category_grade_attempted, 
                                   weight,
                            	   ROUND(SUM(category_grade_overall/num_assignments_total) OVER (PARTITION BY username, course_number, term),2) overall_grade,
                                   ROUND(
                                         SUM(COALESCE(category_grade_attempted,0)/num_assignments_total) OVER (PARTITION BY username, course_number, term)*100/
                            			 SUM(COALESCE(category_max_grade,0)/attempted_assignment_num) OVER (PARTITION BY username, course_number, term)
                                     ,2) overall_grade_attempted
                            FROM grade_calculated;
                            """
                    ,username
                    ,activeClass.getClassID()
            );
            try {
                Statement statement = conn.createStatement();
                ResultSet resultSet = statement.executeQuery(sqlQuery);
                System.out.println("Student Username: "+ username);
                System.out.println("Course Number: "+ activeClass.getCourseNumber());
                System.out.println("Term: " + activeClass.getTerm());
                System.out.printf("%-25s %-25s %-15s %-15s %-15s %-25s %-15s %-25s\n"
                        ,"Category"
                        ,"Assignment"
                        ,"Weight(%)"
                        ,"Marks"
                        ,"Cat. Grade"
                        ,"Cat. Grade Attempted"
                        ,"Total Grade"
                        ,"Total Grade Attempted"
                );
                System.out.println("--------------------------------------------------------------------------------------------------------------------------------------");
                while (resultSet.next()){
                    String obtainedMarks = resultSet.getFloat("obtained_marks") == 0 ? "Not Attempted": resultSet.getString("obtained_marks");
                    System.out.printf("%-25s %-25s %-15s %-15s %-15s %-25s %-15s %-25s\n"
                            ,resultSet.getString("category_name")
                            ,resultSet.getString("assignment_name")
                            , Math.round(resultSet.getFloat("weight")*100)
                            ,obtainedMarks
                            ,resultSet.getFloat("category_grade_overall")
                            ,resultSet.getFloat("category_grade_attempted")
                            ,resultSet.getFloat("overall_grade")
                            ,resultSet.getFloat("overall_grade_attempted")
                    );

                }

            } catch (SQLException s) {
                throw new RuntimeException(s);
            }
        }
    }

    public void gradeBook(){
        //TODO implementation remaining
    }
}
