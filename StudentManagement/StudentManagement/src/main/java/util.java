import javax.xml.transform.Result;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
public class util {
    private static String getConnectionString(String hostName, int port, String dbName){
        String connString = String.format("jdbc:mysql://%s:%d/%s?verifyServerCertificate=false&useSSL=true", hostName,port,dbName);
        return connString;
    }

    public static Connection establishConnection(String hostName, int port, String dbName, String userName, String password){
        String connString = getConnectionString(hostName, port, dbName);

        Connection conn =null;
        try {

            conn = DriverManager.getConnection(connString, userName, password);
            System.out.println("Connection Established");

        } catch (SQLException s){
            throw new RuntimeException(s);
        }
        return conn;
    }

    public static boolean checkClassExist(String term, String courseNumber, int sectionNumber , Connection conn){
        String sqlQuery = String.format(
                """
                        SELECT COUNT(*) FROM class
                        WHERE term = '%s'
                           AND course_number = '%s'
                           AND section_number = %d;
                        """
                , term
                , courseNumber
                , sectionNumber
        );
        boolean isExist = false;

        try{
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(sqlQuery);
            rs.next();
            int numRows = rs.getInt(1);
            isExist = numRows > 0;

        } catch (SQLException s){
            throw new RuntimeException(s);
        }
        return isExist;
    }

    public static boolean checkCategoryExist(String categoryName, int classId , Connection conn){
        String sqlQuery = String.format(
                """
                        SELECT COUNT(*) FROM category
                        WHERE category_name = '%s'
                        	AND class_id = %d;
                        """
                , categoryName
                , classId

        );
        boolean isExist = false;

        try{
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(sqlQuery);
            rs.next();
            int numRows = rs.getInt(1);
            isExist = numRows > 0;

        } catch (SQLException s){
            throw new RuntimeException(s);
        }
        return isExist;
    }

    public static int getCategoryID(int classId, String categoryName,Connection conn){
        String sqlQuery = String.format("""
                    SELECT category_id FROM category 
                    WHERE class_id = %d
                        AND category_name = '%s';
              
                """
                ,classId
                ,categoryName

        );
        int categoryId;
        try{
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(sqlQuery);
            resultSet.next();
            if(resultSet.getRow() == 0)
                categoryId = -1;
            else
                categoryId = resultSet.getInt("category_id");

        }catch (SQLException s){
            throw new RuntimeException(s);
        }
        return categoryId;

    }

    public static boolean checkAssignmentExist(int categoryId, int classId,String assignmentName, Connection conn){
        String sqlQuery = String.format("""
                    SELECT COUNT(*) FROM assignment 
                    WHERE class_id = %d
                     AND category_id = %d
                     AND assignment_name = '%s'
              
                """
                ,classId
                ,categoryId
                ,assignmentName
        );
        boolean isExist = false;
        try{
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(sqlQuery);
            rs.next();
            int numRows = rs.getInt(1);
            isExist = numRows > 0;

        } catch (SQLException s){
            throw new RuntimeException(s);
        }
        return isExist;
    }

    public static int getNumRows(ResultSet resultSet){
        int rowCount = 0;
        try{
            while(resultSet.next()){
                rowCount++;

            }
        } catch (SQLException s){
            throw new RuntimeException(s);
        }

        return rowCount;
    }


}

