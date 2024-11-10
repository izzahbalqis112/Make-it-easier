package ftmk.bitp3453.authenticationapp.firebase;

public class Users {

    String userID;
    String password;
    String fullName;
    String email;
    String userRole;
    String phone;

    public Users(String userID, String password, String fullName, String email, String userRole, String phone) {
        this.userID = userID;
        this.password = password;
        this.fullName = fullName;
        this.email = email;
        this.userRole = userRole;
        this.phone = phone;
    }


    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }







}
