import jdk.jshell.spi.ExecutionControl;

import java.sql.*;
import java.util.logging.Logger;

public class ClassManagement {
    private final Logger LOGGER = Logger.getLogger("Class Management");

    public ActiveClass activeClass = null;

    public  void newClass(String courseNumber, String term, int sectionNumber, String classDescription, Connection conn) {
        boolean checkClassExist = util.checkClassExist(term, courseNumber, sectionNumber, conn);
        if (checkClassExist)
            LOGGER.severe("Class already exist");
        else {
            String sqlQuery = "INSERT INTO class (course_number, term, section_number, class_description) VALUES (?,?,?,?)";
            try {
                conn.setAutoCommit(false);
                PreparedStatement statement = conn.prepareStatement(sqlQuery);
                statement.setString(1, courseNumber);
                statement.setString(2, term);
                statement.setInt(3, sectionNumber);
                statement.setString(4, classDescription);
                int rowInserted = statement.executeUpdate();
                if (rowInserted > 0)
                    LOGGER.info("New class successfully inserted");
                conn.commit();

            } catch (SQLException s) {
                throw new RuntimeException(s);
            }
        }
    }
    public void selectClass (String courseNumber, String term, Connection conn) {
        String sqlQuery = String.format(
                "SELECT class_id, course_number, term,section_number, class_description"
                +" FROM class WHERE course_number = '%s' AND term = '%s';"
                , courseNumber
                ,term
        );
        try {
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(sqlQuery);
            int rowCount = util.getNumRows(resultSet);
            if(rowCount > 1)
                LOGGER.warning("There are more than 1 sections please specify section also");
            else if (rowCount == 0) {
                LOGGER.severe("No such class exists. Please check your inputs or call list-classes");
            } else {
                LOGGER.info("class selected");
                resultSet = statement.executeQuery(sqlQuery);
                assignActiveClass(resultSet);

            }
        } catch (SQLException s) {
            throw new RuntimeException(s);
        }
    }
    public void selectClass (String courseNumber, Connection conn) {
        //TODO implement most recent
        String sqlQuery = String.format(
                "WITH course_term_yr AS (\n" +
                        "\tSELECT class_id, course_number, term,section_number, class_description, RIGHT(term,4) term_yr\n" +
                        "\tFROM class\n" +
                        "\tWHERE course_number = '%s' \n" +
                        "),\n" +
                        "course_ranked_yr AS (\n" +
                        "\tSELECT class_id, course_number, term,section_number, class_description, RANK() OVER (ORDER BY term_yr DESC) rnk\n" +
                        "\tFROM course_term_yr\n" +
                        "),\n" +
                        "recent_yr_course AS (\n" +
                        "\tSELECT class_id, course_number, term,section_number, class_description,\n" +
                        "           CASE\n" +
                        "\t\t\t\tWHEN LOWER(LEFT(term,4)) LIKE '%%fall%%'\n" +
                        "\t\t\t\t\tTHEN 1\n" +
                        "\t\t\t\tWHEN LOWER(LEFT(term,4)) LIKE '%%spri%%'\n" +
                        "\t\t\t\t\tTHEN 2\n" +
                        "\t\t\t\tWHEN LOWER(LEFT(term,4)) LIKE '%%summ%%'\n" +
                        "\t\t\t\t\tTHEN 3\n" +
                        "\t\t\tEND term_order\n" +
                        "\tFROM course_ranked_yr\n" +
                        "\tWHERE rnk = 1\n" +
                        ")\n" +
                        "\n" +
                        "SELECT class_id, course_number, term,section_number, class_description\n" +
                        "FROM recent_yr_course\n" +
                        "WHERE term_order = (SELECT MAX(term_order) FROM recent_yr_course)"
                , courseNumber
        );
        try {
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(sqlQuery);
            int rowCount = util.getNumRows(resultSet);
            if(rowCount > 1)
                LOGGER.warning("There are more than one courses please specify section and term also");
            else {
                LOGGER.info("class selected");
                resultSet = statement.executeQuery(sqlQuery);
                assignActiveClass(resultSet);
                LOGGER.info("class selected");
            }

        } catch (SQLException s) {
            throw new RuntimeException(s);
        }
    }
    public void selectClass (String courseNumber, String term, int sectionNumber, Connection conn) {
        boolean checkClassExist = util.checkClassExist(term, courseNumber, sectionNumber, conn);
        if (!checkClassExist)
            LOGGER.severe("Class does not exist, Please enter correct class or create");
        else{
            String sqlQuery = String.format(
                   "SELECT class_id, course_number, term,section_number, class_description\n" +
                           "                            FROM class\n" +
                           "                            WHERE course_number = '%s'\n" +
                           "                                AND term = '%s'\n" +
                           "                                AND section_number = %d;"
                    , courseNumber
                    ,term
                    ,sectionNumber
            );
            try {
                Statement statement = conn.createStatement();
                ResultSet resultSet = statement.executeQuery(sqlQuery);
                assignActiveClass(resultSet);
                LOGGER.info("class selected");
            } catch (SQLException s) {
                throw new RuntimeException(s);
            }

        }
    }
    public void listClasses (Connection conn) {
        String sqlQuery = "SELECT c.course_number, c.term, c.section_number, COUNT(e.student_id) AS num_students\n" +
                "FROM class c\n" +
                "LEFT JOIN enrolled e\n" +
                "\tON c.class_id = e.class_id\n" +
                "GROUP BY c.course_number, c.term, c.section_number\n" +
                "ORDER BY c.course_number, c.term, c.section_number;";
        try{
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(sqlQuery);
            System.out.printf("%-25s %-25s %-15s %-15s\n"
                    ,"Course Number"
                    ,"Term"
                    ,"Section"
                    ,"Num Enrolled Students"
            );
            System.out.println("\t\t");
            System.out.println("------------------------------------------------------------------");
            while (resultSet.next())
                System.out.printf("%-25s %-25s %-15s %-15s\n"
                        ,resultSet.getString("course_number")
                        ,resultSet.getString("term")
                        ,resultSet.getInt("section_number")
                        ,resultSet.getInt("num_students")
                );
        } catch (SQLException s) {
            throw new RuntimeException(s);
        }
    }
    public void showClass () {
        if(activeClass == null)
            System.out.println("No active class at present");
        else {
            System.out.println("Class ID\tCourse Number\tTerm\tSection Number\tClass Description");
            System.out.println("------------------------------------------------------------------");
            System.out.println(activeClass.getClassID()+"\t\t\t"+activeClass.getCourseNumber()
                    +"\t\t\t"+activeClass.getTerm()+"\t\t\t"+activeClass.getSectionNumber()
                    +"\t\t\t"+activeClass.getClassDescription()
            );
        }
    }
    private void assignActiveClass(ResultSet resultSet){
        try{
            if(resultSet.next()){
                if(activeClass == null)
                    activeClass = new ActiveClass(
                            resultSet.getInt("class_id"),
                            resultSet.getString("course_number"),
                            resultSet.getString("term"),
                            resultSet.getInt("section_number"),
                            resultSet.getString("class_description")
                    );
                else{
                    activeClass.setClassID(resultSet.getInt("class_id"));
                    activeClass.setCourseNumber(resultSet.getString("course_number"));
                    activeClass.setTerm(resultSet.getString("term"));
                    activeClass.setSectionNumber(resultSet.getInt("section_number"));
                    activeClass.setClassDescription(resultSet.getString("class_description"));
                }
            }
        } catch (SQLException s) {
            throw new RuntimeException(s);
        }

    }
}

