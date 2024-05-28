package scooter.courier;

public class CourierNoName {
    private String login;
    private String password;

    public CourierNoName() {
    }

    public CourierNoName(String password, String firstName) {
        this.login = password;
        this.password = firstName;
    }

    public static CourierNoName randomNoName() {
        return new CourierNoName(Courier.random().getLogin(), Courier.random().getFirstName());
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }
}
