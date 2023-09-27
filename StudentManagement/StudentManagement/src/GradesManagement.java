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
                    "WITH marks_calculation AS (\n" +
                            "                            \t\t\tSELECT s.student_id, s.username, c.course_number, c.term, ct.category_name, a.assignment_name, ct.weight, a.point_value overall_total_marks,\n" +
                            "                            \t\t\t\t   CASE\n" +
                            "                            \t\t\t\t\t\tWHEN g.grade IS NULL\n" +
                            "                            \t\t\t\t\t\t\tTHEN NULL\n" +
                            "                            \t\t\t\t\t\tELSE\n" +
                            "                            \t\t\t\t\t\t\ta.point_value\n" +
                            "                            \t\t\t\t\tEND attempted_total_marks,\n" +
                            "                            \t\t\t\t\tg.grade\n" +
                            "                            \t\t\tFROM student s\n" +
                            "                            \t\t\tINNER JOIN enrolled e\n" +
                            "                            \t\t\t\tON s.student_id = e.student_id\n" +
                            "                            \t\t\tINNER JOIN class c\n" +
                            "                            \t\t\t\tON e.class_id = c.class_id\n" +
                            "                            \t\t\tINNER JOIN category ct\n" +
                            "                            \t\t\t\tON ct.class_id = c.class_id\n" +
                            "                            \t\t\tINNER JOIN assignment a\n" +
                            "                            \t\t\t\tON a.category_id = ct.category_id\n" +
                            "                            \t\t\t\tAND a.class_id = e.class_id\n" +
                            "                            \t\t\tLEFT JOIN grades g\n" +
                            "                            \t\t\t\tON g.student_id = s.student_id\n" +
                            "                            \t\t\t\tANd g.assignment_id = a.assignment_id\n" +
                            "                            \t\t\tWHERE s.username = '%s'\n" +
                            "                            \t\t\t\tAND c.class_id = %d\n" +
                            "                            \t),\n" +
                            "                              agg_mark_calculation AS (\n" +
                            "                            \t\t\tSELECT student_id, username, course_number,term, category_name,assignment_name,weight,overall_total_marks,attempted_total_marks,grade,\n" +
                            "                            \t\t\t\t   ROUND(SUM(COALESCE(grade,0)) OVER (PARTITION BY username, course_number, term, category_name ORDER BY category_name),2) category_total_gained,\n" +
                            "                                               ROUND(SUM(COALESCE(overall_total_marks,0)) OVER (PARTITION BY username, course_number, term, category_name ORDER BY category_name),2) overall_category_total,\n" +
                            "                                               ROUND(SUM(COALESCE(attempted_total_marks,0)) OVER (PARTITION BY username, course_number, term, category_name ORDER BY category_name),2) attempted_category_total,\n" +
                            "                                               COUNT(grade) OVER (PARTITION BY username, course_number, term, category_name) num_assignments_attempted,\n" +
                            "                                               COUNT(COALESCE(grade,0)) OVER (PARTITION BY username, course_number, term, category_name) num_assignments_total\n" +
                            "                                        FROM marks_calculation\n" +
                            "                            \t),\n" +
                            "                              grade_calculated AS (\n" +
                            "                            \t\t\t\t\tSELECT username, course_number, term, category_name, assignment_name, weight, grade obtained_marks, category_total_gained, overall_category_total,attempted_category_total,\n" +
                            "                            \t\t\t\t\t\t   ROUND((category_total_gained*100*weight)/overall_category_total,2) category_grade_overall,\n" +
                            "                            \t\t\t\t\t\t   CASE\n" +
                            "                            \t\t\t\t\t\t\t\tWHEN COALESCE(category_total_gained,0) = 0\n" +
                            "                            \t\t\t\t\t\t\t\t\tTHEN NULL\n" +
                            "                            \t\t\t\t\t\t\t\tELSE\n" +
                            "                            \t\t\t\t\t\t\t\t\tROUND((category_total_gained*100*weight)/attempted_category_total,2)\n" +
                            "                            \t\t\t\t\t\t   END category_grade_attempted,\n" +
                            "                                                       CASE\n" +
                            "                            \t\t\t\t\t\t\t\tWHEN num_assignments_attempted = 0\n" +
                            "                            \t\t\t\t\t\t\t\t\tTHEN 1\n" +
                            "                            \t\t\t\t\t\t\t\tELSE\n" +
                            "                            \t\t\t\t\t\t\t\t\tnum_assignments_attempted\n" +
                            "                            \t\t\t\t\t\t   END attempted_assignment_num,\n" +
                            "                                                       num_assignments_total,\n" +
                            "                                                       CASE\n" +
                            "                            \t\t\t\t\t\t\t\tWHEN COALESCE(grade,0) = 0\n" +
                            "                            \t\t\t\t\t\t\t\t\tTHEN NULL\n" +
                            "                            \t\t\t\t\t\t\t\tELSE\n" +
                            "                            \t\t\t\t\t\t\t\t\tROUND(weight* 100,0)\n" +
                            "                            \t\t\t\t\t\t   END category_max_grade\n" +
                            "                            \t\t\t\t\tFROM agg_mark_calculation\n" +
                            "                            \t)\n" +
                            "                            SELECT username, \n" +
                            "                                   course_number, \n" +
                            "                                   term, \n" +
                            "                                   category_name, \n" +
                            "                                   assignment_name, \n" +
                            "                                   obtained_marks, \n" +
                            "                                   category_grade_overall, \n" +
                            "                                   category_grade_attempted, \n" +
                            "                                   weight,\n" +
                            "                            \t   ROUND(SUM(category_grade_overall/num_assignments_total) OVER (PARTITION BY username, course_number, term),2) overall_grade,\n" +
                            "                                   ROUND(\n" +
                            "                                         SUM(COALESCE(category_grade_attempted,0)/num_assignments_total) OVER (PARTITION BY username, course_number, term)*100/\n" +
                            "                            \t\t\t SUM(COALESCE(category_max_grade,0)/attempted_assignment_num) OVER (PARTITION BY username, course_number, term)\n" +
                            "                                     ,2) overall_grade_attempted\n" +
                            "                            FROM grade_calculated;"
                    ,username
                    ,activeClass.getClassID()
            );
            try {
                Statement statement = conn.createStatement();
                ResultSet resultSet = statement.executeQuery(sqlQuery);
                System.out.println("--------------------------------------------------------------------------------------------------------------------------------------------------------");
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
                System.out.println("--------------------------------------------------------------------------------------------------------------------------------------------------------");
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

    public static void gradeBook(ActiveClass activeClass, Connection conn){

        String sqlQuery = String.format(
                "WITH marks_calculation AS (\n" +
                        "                                SELECT s.student_id, s.username, c.course_number, c.term, ct.category_name, a.assignment_name, ct.weight, a.point_value overall_total_marks,\n" +
                        "                                       CASE\n" +
                        "                                            WHEN g.grade IS NULL\n" +
                        "                                                THEN NULL\n" +
                        "                                            ELSE\n" +
                        "                                                a.point_value\n" +
                        "                                        END attempted_total_marks,\n" +
                        "                                        g.grade\n" +
                        "                                FROM student s\n" +
                        "                                INNER JOIN enrolled e\n" +
                        "                                    ON s.student_id = e.student_id\n" +
                        "                                INNER JOIN class c\n" +
                        "                                    ON e.class_id = c.class_id\n" +
                        "                                INNER JOIN category ct\n" +
                        "                                    ON ct.class_id = c.class_id\n" +
                        "                                INNER JOIN assignment a\n" +
                        "                                    ON a.category_id = ct.category_id\n" +
                        "                                    AND a.class_id = e.class_id\n" +
                        "                                LEFT JOIN grades g\n" +
                        "                                    ON g.student_id = s.student_id\n" +
                        "                                    ANd g.assignment_id = a.assignment_id\n" +
                        "                                WHERE c.class_id = %d\n" +
                        "                        ),\n" +
                        "                        agg_mark_calculation AS (\n" +
                        "                                SELECT student_id, username, course_number,term, category_name,weight,\n" +
                        "                                       SUM(COALESCE(grade,0)) total_marks_obtained,\n" +
                        "                                         SUM(overall_total_marks) total_marks,\n" +
                        "                                         SUM(COALESCE(attempted_total_marks,0)) total_marks_attempted\n" +
                        "                                  FROM marks_calculation\n" +
                        "                                  GROUP BY student_id, username, course_number,term, category_name,weight\n" +
                        "                        ),\n" +
                        "                         cat_grade_calculate AS (\n" +
                        "                                SELECT student_id, username, course_number, term, category_name,\n" +
                        "                                       ROUND(weight*100,0) max_grade,\n" +
                        "                                       CASE\n" +
                        "                                            WHEN total_marks_obtained = 0\n" +
                        "                                                THEN 0\n" +
                        "                                            ELSE\n" +
                        "                                                ROUND(weight*100,0)\n" +
                        "                                       END attempted_max_grade,\n" +
                        "                                       ROUND(total_marks_obtained*100*weight/total_marks,2) overall_grade,\n" +
                        "                                       CASE\n" +
                        "                                            WHEN total_marks_obtained = 0\n" +
                        "                                                THEN 0\n" +
                        "                                            ELSE\n" +
                        "                                                ROUND(total_marks_obtained*weight*100/total_marks_attempted, 2)\n" +
                        "                                       END attempted_grade\n" +
                        "                                FROM agg_mark_calculation\n" +
                        "                        )\n" +
                        "                        \n" +
                        "                      SELECT student_id, username, course_number, term,\n" +
                        "                           ROUND(SUM(overall_grade),2) class_grade,\n" +
                        "                             CASE\n" +
                        "                                WHEN SUM(attempted_grade) = 0\n" +
                        "                                    THEN 0\n" +
                        "                                ELSE\n" +
                        "                                        ROUND(SUM(attempted_grade)*100/SUM(attempted_max_grade),2)\n" +
                        "                          END attempted_class_grade\n" +
                        "                      FROM cat_grade_calculate\n" +
                        "                      GROUP BY student_id, username, course_number, term;"
                ,activeClass.getClassID()
        );
        try {
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(sqlQuery);
            System.out.println("--------------------------------------------------------------------------------------------------------------------------------------------------------");
            System.out.println("Course Number: "+ activeClass.getCourseNumber());
            System.out.println("Term: " + activeClass.getTerm());
            System.out.printf("%-25s %-25s %-15s %-15s\n"
                    ,"Student ID"
                    ,"Username"
                    ,"Overall Grade"
                    ,"Attempted Grade"
            );
            System.out.println("--------------------------------------------------------------------------------------------------------------------------------------------------------");
            while (resultSet.next()){
                System.out.printf("%-25s %-25s %-15s %-15s\n"
                        ,resultSet.getInt("student_id")
                        ,resultSet.getString("username")
                        ,resultSet.getFloat("class_grade")
                        ,resultSet.getFloat("attempted_class_grade")
                );

            }

        } catch (SQLException s) {
            throw new RuntimeException(s);
        }
    }
}
