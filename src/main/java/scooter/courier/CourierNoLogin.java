package scooter.courier;

public class CourierNoLogin {
    private String password;
    private String firstName;

    public CourierNoLogin() {
    }

    public CourierNoLogin(String password, String firstName) {
        this.password = password;
        this.firstName = firstName;
    }

    public static CourierNoLogin randomNoLogin() {
        return new CourierNoLogin(Courier.random().getPassword(), Courier.random().getFirstName());
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
