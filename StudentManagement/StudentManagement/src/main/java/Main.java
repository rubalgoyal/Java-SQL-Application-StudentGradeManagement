import java.sql.Connection;

public class Main {
    public static void main(String[] args) {
        String hostName = "localhost";
        int port = 56720;
        String dbName = "StudentEnrollment";
        String userName = "msandbox";
        String password = "REPLACEME";
        Connection conn = util.establishConnection(hostName, port,dbName,userName,password);
        ClassManagement classManagement = new ClassManagement();
//        classManagement.newClass("CS410","Sp23",1,"Database",conn);
//        classManagement.newClass("CS410","Fall23",1,"Database",conn);
//        classManagement.newClass("CS410","Summer23",1,"Database",conn);
//        classManagement.newClass("CS510","Fall23",2,"Database",conn);
//        classManagement.newClass("CS510","Summer23",2,"Database",conn);
//        classManagement.newClass("CS510","Sp23",2,"Database",conn);
//        classManagement.newClass("CS550","Sp23",2,"Operating System",conn);
//        classManagement.selectClass("CS510",  conn);
          classManagement.selectClass("CS510", "Sp23", conn);
//        classManagement.showClass();
//        CategoryAssignment.addCategory("Assignments", 0.15f, conn,classManagement.activeClass);
//        CategoryAssignment.showCategories(conn, classManagement.activeClass);
//        System.out.println(util.getCategoryID(classManagement.activeClass.getClassID(),"Assignments", conn ));
//        CategoryAssignment.addAssignment("Homework-1", "Assignments"," 1st homework", 50.0f,conn,classManagement.activeClass);
//        CategoryAssignment.addAssignment("Homework-2", "Assignments"," 2nd homework", 50.0f,conn,classManagement.activeClass);
//        CategoryAssignment.addAssignment("Homework-3", "Assignments"," 3rd homework", 50.0f,conn,classManagement.activeClass);
//        CategoryAssignment.addAssignment("Homework-4", "Assignments"," 4th homework", 50.0f,conn,classManagement.activeClass);
//        CategoryAssignment.showAssignment(classManagement.activeClass, conn);
//          StudentManagement.addStudent("johnsmith01", 1,"John","Smith",conn,classManagement.activeClass);
            StudentManagement.showStudents("Smi",classManagement.activeClass,conn);


    }
}