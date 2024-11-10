package ftmk.bitp3453.authenticationapp.firebase;

public class EnrollSubject {


    String enrollID,userID, subjectName,subjectCode;

    public EnrollSubject(String enrollID, String userID, String subjectName, String subjectCode) {
        this.enrollID = enrollID;
        this.userID = userID;
        this.subjectName = subjectName;
        this.subjectCode = subjectCode;
    }

    public String getEnrollID() {
        return enrollID;
    }

    public void setEnrollID(String enrollID) {
        this.enrollID = enrollID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getSubjectCode() {
        return subjectCode;
    }

    public void setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
    }

}
