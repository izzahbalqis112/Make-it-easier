package ftmk.bitp3453.authenticationapp.firebase;

public class Course {

    String course_id,course_name;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    String key;

    public String getCourse_code() {
        return course_id;
    }

    public void setCourse_code(String course_id) {
        this.course_id = course_id;
    }

    public String getCourse_name() {
        return course_name;
    }

    public void setCourse_name(String course_name) {
        this.course_name = course_name;
    }


    public Course(String course_code, String course_name) {

        this.course_id = course_code;
        this.course_name = course_name;
    }

    public Course(String key, String course_code, String course_name) {

        this.key = key;
        this.course_id = course_code;
        this.course_name = course_name;
    }

    public Course()
    {

    }

}
