public class ActiveClass {
    private int classID;
    private String courseNumber;
    private String term;
    private int sectionNumber;
    private String classDescription;

    public ActiveClass(int classID, String courseNumber, String term, int sectionNumber, String classDescription) {
        this.classID = classID;
        this.courseNumber = courseNumber;
        this.term = term;
        this.sectionNumber = sectionNumber;
        this.classDescription = classDescription;
    }

    public int getClassID() {
        return classID;
    }

    public void setClassID(int classID) {
        this.classID = classID;
    }

    public String getCourseNumber() {
        return courseNumber;
    }

    public void setCourseNumber(String courseNumber) {
        this.courseNumber = courseNumber;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public int getSectionNumber() {
        return sectionNumber;
    }

    public void setSectionNumber(int sectionNumber) {
        this.sectionNumber = sectionNumber;
    }

    public String getClassDescription() {
        return classDescription;
    }

    public void setClassDescription(String classDescription) {
        this.classDescription = classDescription;
    }

}
