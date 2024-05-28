package scooter.courier;

public class CourierNoPassword {
    private String login;
    private String firstName;

    public CourierNoPassword() {
    }

    public CourierNoPassword(String password, String firstName) {
        this.login = password;
        this.firstName = firstName;
    }

    public static CourierNoPassword randomNoPassword() {
        return new CourierNoPassword(Courier.random().getPassword(), Courier.random().getFirstName());
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }
}
