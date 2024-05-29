package scooter.jsons.courier;

public class CourierNoPassword {
    private String login;
    private String firstName;

    public CourierNoPassword() {
    }

    public CourierNoPassword(String login, String firstName) {
        this.login = login;
        this.firstName = firstName;
    }

    public static CourierNoPassword randomNoPassword() {
        return new CourierNoPassword(Courier.random().getLogin(), Courier.random().getFirstName());
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
