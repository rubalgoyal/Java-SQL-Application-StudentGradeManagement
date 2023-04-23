import javax.xml.transform.Result;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.Random;
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

    public static int getStudentId(String userName, Connection conn){
        String sqlQuery = String.format("""
                    SELECT student_id FROM student 
                    WHERE  username = '%s'; """
                ,userName
        );
        int studentId;
        try{
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(sqlQuery);
            resultSet.next();
            if(resultSet.getRow() == 0)
                studentId = -1;
            else
                studentId = resultSet.getInt("student_id");

        }catch (SQLException s){
            throw new RuntimeException(s);
        }
        return studentId;
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

    public static boolean checkStudentExist(int studentId,String userName,Connection conn){
        String sqlQuery = String.format(
                       """
                      SELECT COUNT(*) FROM student
                      WHERE student_id = %d
                        AND user_name = '%s'
                        
                      """
                ,studentId
                ,userName
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

    public static boolean checkStudentEnrolled(int studentId, int classId,Connection conn){
        String sqlQuery = String.format(
                        """
                        SELECT COUNT(*) From enrolled
                        WHERE student_id = %d
                            AND class_id = %d
                        """
                ,studentId
                ,classId
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



    public static String randomAddressGenerator() {
            String[] streets = {"Main St.", "Oak St.", "Park Ave.", "Broadway", "Maple St.", "Cedar St.", "Elm St.", "High St.", "1st St.", "2nd St."};
            String[] cities = {"New York", "Los Angeles", "Chicago", "Houston", "Philadelphia", "Phoenix", "San Antonio", "San Diego", "Dallas", "San Jose"};
            String[] states = {"NY", "CA", "IL", "TX", "PA", "AZ", "TX", "CA", "TX", "CA"};
            String[] zipCodes = {"10001", "90001", "60601", "77001", "19102", "85001", "78201", "92101", "75201", "95101"};
            Random random = new Random();
            String street = streets[random.nextInt(streets.length)];
            String city = cities[random.nextInt(cities.length)];
            String state = states[random.nextInt(states.length)];
            String zipCode = zipCodes[random.nextInt(zipCodes.length)];
            return street + "," + city + "," + state+ "," +zipCode;

    }



}

