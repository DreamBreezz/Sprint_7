package scooter;

public class CourierLogin {
    private String login;
    private String password;

    public CourierLogin() {
    }

    public static CourierLogin from(Courier courier) {
        return new CourierLogin(courier.getLogin(), courier.getPassword());
    }

    public CourierLogin(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
