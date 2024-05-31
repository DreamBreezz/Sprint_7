package scooter.jsons.login;

public class CourierLoginNoPassword {
    private String login;

    public CourierLoginNoPassword(String login) {
        this.login = login;
    }

    //метод создания json, используя данные существующего курьера
    public static CourierLoginNoPassword from(CourierLogin courierLogin) {
        return new CourierLoginNoPassword(courierLogin.getLogin());
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }
}
