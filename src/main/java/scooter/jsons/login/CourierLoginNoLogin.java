package scooter.jsons.login;

import scooter.jsons.courier.Courier;

public class CourierLoginNoLogin {
    private String password;

    public CourierLoginNoLogin(String password) {
        this.password = password;
    }

    //метод создания json, используя данные существующего курьера
    public static CourierLoginNoLogin from(Courier courier) {
        return new CourierLoginNoLogin(courier.getPassword());
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String login) {
        this.password = login;
    }
}
