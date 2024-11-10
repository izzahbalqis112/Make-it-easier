package ftmk.bitp3453.authenticationapp.firebase;

public class Model {

    private String task_id,task_name,task_description,date,time,priority_level,prior_color,user_id;
    private boolean completed_task;

    public Model(String task_id, String task_name, String task_description, String date, String time, String priority_level, String prior_color, String user_id) {
        this.task_id = task_id;
        this.task_name = task_name;
        this.task_description = task_description;
        this.date = date;
        this.time = time;
        this.priority_level = priority_level;
        this.prior_color = prior_color;
        this.user_id = user_id;
    }



    public Model(boolean completed_task)
    {
        this.completed_task = completed_task;
    }

    public Model()
    {

    }


    public String getPriorColor() {
        return prior_color;
    }

    public void setPriorColor(String priorColor) {
        this.prior_color = prior_color;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }


    public String getTask_id() {
        return task_id;
    }

    public void setTask_id(String task_id) {
        this.task_id = task_id;
    }

    public String getTask_name() {
        return task_name;
    }

    public void setTask_name(String task_name) {
        this.task_name = task_name;
    }

    public String getTask_description() {
        return task_description;
    }

    public void setTask_description(String task_description) {
        this.task_description = task_description;
    }

    public String getPriority_level() {
        return priority_level;
    }

    public void setPriority_level(String priority_level) {
        this.priority_level = priority_level;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean isCompleted_task() {
        return completed_task;
    }

    public void setCompleted_task(boolean completed_task) {
        this.completed_task = completed_task;
    }










}
