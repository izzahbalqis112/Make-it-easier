package ftmk.bitp3453.authenticationapp.activities;

public class SubmitAssignment {

    String file_id;
    String file_name;
    String user_id;
    String submit_date;
    String file;
    String subject;

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }


    public SubmitAssignment(String file_id, String file_name, String user_id, String submit_date, String file, String subject) {
        this.file_id = file_id;
        this.file_name = file_name;
        this.user_id = user_id;
        this.submit_date = submit_date;
        this.file = file;
        this.subject = subject;
    }



    public String getFile_id() {
        return file_id;
    }

    public void setFile_id(String file_id) {
        this.file_id = file_id;
    }

    public String getFile_name() {
        return file_name;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getSubmit_date() {
        return submit_date;
    }

    public void setSubmit_date(String submit_date) {
        this.submit_date = submit_date;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }


    public SubmitAssignment(String file_id, String file_name, String user_id, String submit_date, String file) {
        this.file_id = file_id;
        this.file_name = file_name;
        this.user_id = user_id;
        this.submit_date = submit_date;
        this.file = file;
    }






}
