import java.sql.Connection;
import java.util.Scanner;
import java.util.logging.Logger;

public class EnrollmentApp {
    public final Logger LOGGERS = Logger.getLogger("");
    public void showHelp(){
        StringBuilder printMessage = new StringBuilder();
        String seperator = "-";
        printMessage.append("Please enter one of the following option along with suitable parameter\n");
        printMessage.append("Please mention your values at places <> \n");
        printMessage.append(seperator.repeat(100));
        printMessage.append("\nnew-class <course_number> <term> <section_number> <class_description> [To create new class]");
        printMessage.append("\nlist-classes [To list all the classes]");
        printMessage.append("\nselect-class <course_number> [To show the only section of course_number in the most recent term]");
        printMessage.append("\nselect-class <course_number> <term> [To see only section of course_number in term]");
        printMessage.append("\nselect-class <course_number> <term> <section_number> [To see specific section]");
        printMessage.append("\nshow-class [To see the currently active class]");
        printMessage.append("\nshow-categories [To list the categories with their weights]");
        printMessage.append("\nadd-category <name> <weight> [To add a new category]");
        printMessage.append("\nshow-assignment [To list the assignments with their point values]");
        printMessage.append("\nadd-assignment <assignment_name> <category> <description> <point> [add a new assignment]");
        printMessage.append("\nadd-student <username> <student_id> <Last_name> <First_name> [To add a student and enrolls them in the current class]");
        printMessage.append("\nadd-student <username> [To enrolls an already-existing student in the current class]");
        printMessage.append("\nshow-students [To show all students in the current class] ");
        printMessage.append("\nshow-students <string> [To see all students with ‘string’ in their name or username]");
        printMessage.append("\ngrade <assignment_name> <username> <grade> [To assign or update yje grade of student for the specific assignment]");
        printMessage.append("\nstudent-grades <username> [To see student's current grade ] ");
        printMessage.append("\ngradebook [To see the current class’s gradebook]");
        System.out.println(printMessage);
    }
    public void run(Connection connection, ClassManagement classManagement){
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\n");
            String[] userInput = scanner.nextLine().split(" ");
            if(userInput[0].equals("new-class")){
                if(userInput.length != 5) {
                    LOGGERS.warning("Please enter only Course number, term, section number, description  along with new-class");
                    continue;
                }
                classManagement.newClass(userInput[1],userInput[2],Integer.parseInt(userInput[3]),userInput[4],connection);
            } else if (userInput[0].equals("list-classes")) {
                if(userInput.length != 1){
                    LOGGERS.warning("Please do not provide any argument along with list-classes");
                    continue;
                }
                classManagement.listClasses(connection);
            } else if (userInput[0].equals("select-class")) {
                if(userInput.length == 2)
                    classManagement.selectClass(userInput[1], connection);
                else if (userInput.length == 3)
                    classManagement.selectClass(userInput[1],userInput[2],connection);
                else if (userInput.length == 4)
                    classManagement.selectClass(userInput[1],userInput[2],Integer.parseInt(userInput[3]),connection);
                else {
                    LOGGERS.warning("Please provide correct arguments, you can put course number, term,section number");
                    continue;
                }
            } else if (userInput[0].equals("show-class")){
                if(userInput.length !=1){
                    LOGGERS.warning("Please do not provide any argument along with show-class");
                    continue;
                }
                classManagement.showClass();
            } else if (userInput[0].equals("show-categories")) {
                if(userInput.length !=1 ){
                    LOGGERS.warning("Please do not provide any argument along with show-categories");
                    continue;
                }
                CategoryAssignment.showCategories(connection,classManagement.activeClass);
            }
            else if (userInput[0].equals("add-category")){
                if(userInput.length != 3) {
                    LOGGERS.warning("Please enter only category name and weight with add-category");
                    continue;
                }
                CategoryAssignment.addCategory(userInput[1], Float.parseFloat((userInput[2])),connection,classManagement.activeClass);
            }
            else if(userInput[0].equals("show-assignment")){
                if(userInput.length != 1) {
                    LOGGERS.warning("Please do not provide any argument along with show-assignment");
                    continue;
                }
                CategoryAssignment.showAssignment(classManagement.activeClass,connection);
            } else if (userInput[0].equals("add-assignment")) {
                if(userInput.length != 5){
                    LOGGERS.warning("Please enter only assignment name category description and points with add-assignment");
                    continue;
                }
                CategoryAssignment.addAssignment(userInput[1],userInput[2],userInput[3],Float.parseFloat(userInput[4]),connection,classManagement.activeClass);
            }
            else if(userInput[0].equals("add-student")){
                if(userInput.length == 5)
                    StudentManagement.addStudent(userInput[1],Integer.parseInt(userInput[2]), userInput[3],userInput[4],connection,classManagement.activeClass);
                else if (userInput.length == 2)
                    StudentManagement.addStudent(userInput[1],connection,classManagement.activeClass);
                else{
                    LOGGERS.warning("Please enter username or username student id lastname firstname along with add-student ");
                    continue;
                }
            }
            else if(userInput[0].equals("show-students")){
                if(userInput.length == 1)
                    StudentManagement.showStudents(classManagement.activeClass,connection);
                else if (userInput.length == 2) {
                    StudentManagement.showStudents(userInput[1],classManagement.activeClass,connection);
                }
                else{
                    LOGGERS.warning("Please enter show-students without any argument or put only one string value ");
                }
            } else if (userInput[0].equals("grade")) {
                if(userInput.length !=4){
                    LOGGERS.warning("Please enter assignment name username and assigned grade along with grade");
                    continue;
                }
                StudentManagement.gradeAssignment(userInput[1],userInput[2],Float.parseFloat(userInput[3]),connection,classManagement.activeClass);
            } else if (userInput[0].equals("student-grades")){
                if(userInput.length != 2){
                    LOGGERS.warning("Please enter username along with student-grade");
                    continue;
                }
                GradesManagement.studentGrades(userInput[1],classManagement.activeClass,connection);
            }
            else if(userInput[0].equals("gradebook")){
                if(userInput.length != 1){
                    LOGGERS.warning("Please do not provide any argument along with grade book");
                }
            }
            else if (userInput[0].equals("help"))
                showHelp();
            else {
                LOGGERS.severe("Please enter correct option as given below");
                showHelp();
            }
        }
    }
    public static void main(String[] args) {
        String hostName = "localhost";
        int port = 49219;
        String dbName = "StudentEnrollment";
        String userName = "msandbox";
        String password = "REPLACEME";

        /*
        String hostName = args[0];
        int port = Integer.parseInt(args[1]);
        String dbName = args[2];
        String userName = args[3];
        String password = args4];

         */
        Connection connection = util.establishConnection(hostName, port,dbName,userName,password);
        ClassManagement classManagement = new ClassManagement();
        EnrollmentApp mainInstance = new EnrollmentApp();
        mainInstance.run(connection, classManagement);
    }
}