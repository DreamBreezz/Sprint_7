package scooter.login;

public class CourierLoginNoLogin {
    private String password;

    public CourierLoginNoLogin(String password) {
        this.password = password;
    }

    //метод создания json, используя данные существующего курьера
    public static CourierLoginNoLogin from(CourierLogin courierLogin) {
        return new CourierLoginNoLogin(courierLogin.getPassword());
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String login) {
        this.password = login;
    }
}
