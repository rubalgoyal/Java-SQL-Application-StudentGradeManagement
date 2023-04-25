import java.sql.Connection;

public class Main {
    public static void main(String[] args) {
        String hostName = "localhost";
        int port = 57086;
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
//        classManagement.selectClass("CS410",  conn);
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
//            StudentManagement.addStudent("smithjohn08",5,"Smith", "John", conn,classManagement.activeClass);
//          StudentManagement.addStudent("bobalex09", 6,"Bob","Alex",conn,classManagement.activeClass);
//          StudentManagement.addStudent("smithblack09", 7,"Black","Smith",conn,classManagement.activeClass);

//            classManagement.listClasses(conn);
//            StudentManagement.gradeAssignment("Homework-1","johnsmith01", 62.5f,conn,classManagement.activeClass);

//            StudentManagement.showStudents(classManagement.activeClass,conn);
        GradesManagement.studentGrades("johnsmith01",classManagement.activeClass,conn);


    }
}