package android.bignerdranch.studentmanager.model;

public class ClassManager {
    private String classID;
    private String className;
    private String startTime;
    private String endTime;
    private String classRoom;
    private int totalStudent;
    public ClassManager() {

    }

    public ClassManager(String classID, String className, String startTime, String endTime, String classRoom, int totalStudent) {
        this.classID = classID;
        this.className = className;
        this.startTime = startTime;
        this.endTime = endTime;
        this.classRoom = classRoom;
        this.totalStudent = totalStudent;
    }

    public String getClassID() {
        return classID;
    }

    public void setClassID(String classID) {
        this.classID = classID;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getStart() {
        return startTime;
    }

    public void setStart(String startTime) {
        this.startTime = startTime;
    }

    public String getEnd() {
        return endTime;
    }

    public void setEnd(String endTime) {
        this.endTime = endTime;
    }

    public String getClassRoom() {
        return classRoom;
    }

    public void setClassRoom(String classRoom) {
        this.classRoom = classRoom;
    }

    public int getTotalStudent() {
        return totalStudent;
    }

    public void setTotalStudent(int totalStudent) {
        this.totalStudent = totalStudent;
    }
}
