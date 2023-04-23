import java.sql.*;
import java.util.Random;

public class StudentManagement {

    public static void addStudent(String userName, int studentId, String firstName, String lastName, Connection conn, ActiveClass activeClass){
        boolean isStudentIdExist = util.checkStudentExist(studentId, userName,conn);
        int classId = activeClass.getClassID();
        if(isStudentIdExist) {
            System.out.println("Student already exist, enrolling the student in class");
            enrollStudent(studentId, classId, conn);
        }
        else{
            String emailAddress = userName + "@u.xyz.edu";
            Random random = new Random();
            long randomNumber = 1000000000L + random.nextInt(900000000);
            String contactNumber = String.valueOf(randomNumber);
            String address = util.randomAddressGenerator();
            String sqlQuery = String.format(
                            """
                            INSERT INTO student (student_id, username, first_name,last_name,email_address,contact_number, address)
                            VALUES(?,?,?,?,?,?,?)""");
            try {
                conn.setAutoCommit(false);
                PreparedStatement statement = conn.prepareStatement(sqlQuery);
                statement.setInt(1, studentId);
                statement.setString(2, userName);
                statement.setString(3, firstName);
                statement.setString(4,lastName);
                statement.setString(5,emailAddress);
                statement.setString(6,contactNumber);
                statement.setString(7,address);

                int rowInserted = statement.executeUpdate();
                if (rowInserted > 0)
                    System.out.println("New student successfully inserted");
                conn.commit();
            } catch (SQLException s) {
                throw new RuntimeException(s);
            }
            enrollStudent(studentId,classId,conn);

        }
    }
    public static void addStudent(String userName, Connection conn, ActiveClass activeClass){
        int studentId = util.getStudentId(userName, conn);
        boolean isStudentExist = util.checkStudentExist(studentId,userName,conn);
        if(isStudentExist){
            enrollStudent(studentId,activeClass.getClassID(),conn);
        }
        else System.out.println("Student does not exist");
    }

    public static void showStudents(ActiveClass activeClass, Connection conn){
            String sqlQuery = String.format("""
                        SELECT s.student_id, s.username, s.first_name,s.last_name FROM student s
                        JOIN enrolled e
                            ON s.student_id = e.student_id
                            WHERE class_id = %d"""
                        ,activeClass.getClassID()
                    );
            try {
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(sqlQuery);
                System.out.println("Student ID\tUsername\tFirst Name\tLast Name");
                System.out.println("------------------------------------------------------------------");
                while (resultSet.next())
                    System.out.println(

                                    resultSet.getInt("student_id") + "\t\t"
                                    + resultSet.getString("username") + "\t\t"
                                    + resultSet.getString("first_name") + "\t\t"
                                    +resultSet.getString("last_name")
                    );
            } catch (SQLException s) {
                throw new RuntimeException(s);
            }
    }

    public static void showStudents(String lookupName,ActiveClass activeClass, Connection conn){
        String search = "%" + lookupName.toLowerCase() + "%";
        String sqlQuery = String.format("""
                        SELECT s.student_id, s.username, s.first_name,s.last_name FROM student s
                        JOIN enrolled e
                            ON s.student_id = e.student_id
                            WHERE class_id = %d
                            AND LOWER(s.username) LIKE '%s'
                        """
                ,activeClass.getClassID()
                ,search
        );
        try {
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(sqlQuery);
            System.out.println("Student ID\tUsername\tFirst Name\tLast Name");
            System.out.println("------------------------------------------------------------------");
            while (resultSet.next())
                System.out.println(

                        resultSet.getInt("student_id") + "\t\t"
                                + resultSet.getString("username") + "\t\t"
                                + resultSet.getString("first_name") + "\t\t"
                                +resultSet.getString("last_name")
                );
        } catch (SQLException s) {
            throw new RuntimeException(s);
        }
    }

    public void gradeAssignment(){
        //TODO implementation remaining
    }

    public static void enrollStudent(int studentId, int classId, Connection conn){
        boolean isStudentEnrolled = util.checkStudentEnrolled(studentId,classId,conn);
        if(!isStudentEnrolled) {
            String sqlQuery = String.format(
                    """
                            INSERT INTO enrolled(student_id, class_id) VALUES (?,?);""");
            try {
                conn.setAutoCommit(false);
                PreparedStatement statement = conn.prepareStatement(sqlQuery);
                statement.setInt(1, studentId);
                statement.setInt(2, classId);
                int rowInserted = statement.executeUpdate();
                if (rowInserted > 0)
                    System.out.println("Student successfully enrolled in class");
                conn.commit();
            } catch (SQLException s) {
                throw new RuntimeException(s);
            }
        }
        else
            System.out.println("Student already enrolled");
    }


}
