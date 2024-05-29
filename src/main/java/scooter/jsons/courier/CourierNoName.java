package scooter.jsons.courier;

public class CourierNoName {
    private String login;
    private String password;

    public CourierNoName() {
    }

    public CourierNoName(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public static CourierNoName randomNoName() {
        return new CourierNoName(Courier.random().getLogin(), Courier.random().getPassword());
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
