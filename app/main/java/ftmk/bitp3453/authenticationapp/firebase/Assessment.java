package ftmk.bitp3453.authenticationapp.firebase;

public class Assessment
{
    public String subject;
    public String file_id;
    public String file_name;
    public String file_description;
    public String upload_date;
    public String due_date;
    public String url;
    public String user_id;

    public String getDue_date() {
        return due_date;
    }

    public void setDue_date(String due_date) {
        this.due_date = due_date;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }



    public Assessment(String subject, String file_name, String file_description, String upload_date, String due_date, String url, String user_id) {
        this.subject = subject;
        this.file_name = file_name;
        this.file_description = file_description;
        this.upload_date = upload_date;
        this.due_date = due_date;
        this.url = url;
        this.user_id = user_id;
    }



    public Assessment(String subject, String file_id, String file_name, String file_description, String upload_date, String due_date, String url, String user_id) {
        this.subject = subject;
        this.file_id = file_id;
        this.file_name = file_name;
        this.file_description = file_description;
        this.upload_date = upload_date;
        this.due_date = due_date;
        this.url = url;
        this.user_id = user_id;
    }




    public Assessment()
    {

    }


    public String getFile_id() {
        return file_id;
    }

    public void setFile_id(String file_id) {
        this.file_id = file_id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }





    public String getFile_name() {
        return file_name;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }

    public String getUpload_date() {
        return upload_date;
    }

    public void setUpload_date(String upload_date) {
        this.upload_date = upload_date;
    }

    public String getFile_description() {
        return file_description;
    }

    public void setFile_description(String file_description) {
        this.file_description = file_description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }








}
