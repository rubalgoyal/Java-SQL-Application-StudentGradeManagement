import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

public class CategoryAssignment {
    private static final Logger LOGGER = Logger.getLogger("Assignment Management");

    public static void showCategories(Connection conn , ActiveClass activeClass){
        if(activeClass == null)
            LOGGER.severe("Please select the current class");
        else {
            int classId = activeClass.getClassID();
            String sqlQuery = String.format("SELECT category_id, category_name, weight,class_id \n" +
                            "                                FROM category\n" +
                            "                                WHERE class_id = %d"
                    , classId
            );
            try {
                Statement statement = conn.createStatement();
                ResultSet resultSet = statement.executeQuery(sqlQuery);
                System.out.println("Course Number\tCategory\tWeight");
                System.out.println("------------------------------------------------------------------");
                while (resultSet.next())
                    System.out.println(
                            activeClass.getCourseNumber() + "\t\t" +
                                    resultSet.getString("category_name") + "\t\t"
                                    + resultSet.getFloat("weight")
                    );

            } catch (SQLException s) {
                throw new RuntimeException(s);
            }
        }

    }

    public static void addCategory(String categoryName, float weight, Connection conn , ActiveClass activeClass){
        int classId = activeClass.getClassID();
        boolean ifCategoryExist = util.checkCategoryExist(categoryName, classId, conn);
        if(!ifCategoryExist){
            String sqlQuery = "INSERT INTO category(category_name, weight, class_id) VALUES (?, ?, ?); ";
            try {
                conn.setAutoCommit(false);
                PreparedStatement statement = conn.prepareStatement(sqlQuery);
                statement.setString(1, categoryName);
                statement.setFloat(2, weight);
                statement.setInt(3, classId);
                int rowInserted = statement.executeUpdate();
                if (rowInserted > 0)
                    LOGGER.info("New category successfully inserted");
                conn.commit();

            } catch (SQLException s) {
                throw new RuntimeException(s);
            }
        }
        else
            LOGGER.severe("Category already exist for the selected course number " + activeClass.getCourseNumber());

    }

    public static void showAssignment(ActiveClass activeClass, Connection conn){
        if(activeClass == null)
            LOGGER.severe("Please select the class");

        else {
            String sqlQuery = String.format("SELECT c.category_name, a.assignment_name, a.point_value\n" +
                            "                            FROM assignment a\n" +
                            "                            JOIN category c\n" +
                            "                                ON a.category_id = c.category_id\n" +
                            "                            WHERE c.class_id = %d\n" +
                            "                            GROUP BY c.category_name, a.assignment_name, a.point_value\n" +
                            "                            ORDER BY c.category_name;"


                    , activeClass.getClassID()
            );
            try {
                Statement statement = conn.createStatement();
                ResultSet resultSet = statement.executeQuery(sqlQuery);
                System.out.println("Course Number\tCategory\tAssignment Name\tPoint Value");
                System.out.println("------------------------------------------------------------------");
                while (resultSet.next())
                    System.out.println(
                            activeClass.getCourseNumber() + "\t\t" +
                                    resultSet.getString("category_name") + "\t\t"
                                    + resultSet.getString("assignment_name") + "\t\t"
                                    + resultSet.getFloat("point_value")
                    );

            } catch (SQLException s) {
                throw new RuntimeException(s);
            }
        }

    }

    public static void addAssignment(String assignmentName, String categoryName, String description, float points, Connection conn, ActiveClass activeClass){
        int classId = activeClass.getClassID();
        int categoryId = util.getCategoryID(classId,categoryName,conn);
        if(categoryId != -1){
            boolean  ifExist = util.checkAssignmentExist(categoryId,classId,assignmentName,conn);
            if(!ifExist ){
                String sqlQuery = "INSERT INTO assignment(assignment_name,assignment_description, point_value, class_id, category_id) VALUES (?, ?, ?,?,?); ";
                try {
                    conn.setAutoCommit(false);
                    PreparedStatement statement = conn.prepareStatement(sqlQuery);
                    statement.setString(1, assignmentName);
                    statement.setString(2, description);
                    statement.setFloat(3, points);
                    statement.setInt(4,classId);
                    statement.setInt(5,categoryId);
                    int rowInserted = statement.executeUpdate();
                    if (rowInserted > 0)
                        LOGGER.info("New assignment successfully inserted");
                    conn.commit();
                } catch (SQLException s) {
                    throw new RuntimeException(s);
                }
            }
            else
                LOGGER.severe("This assignment name already exist for the given category");
        }
        else
            LOGGER.severe("Given category does not exist for course " + activeClass.getCourseNumber());
    }

}

