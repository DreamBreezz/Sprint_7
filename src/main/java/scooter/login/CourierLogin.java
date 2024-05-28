package scooter.login;

import org.apache.commons.lang3.RandomStringUtils;
import scooter.courier.Courier;

public class CourierLogin {
    private String login;
    private String password;

    public CourierLogin() {
    }

    public CourierLogin(String login, String password) {
        this.login = login;
        this.password = password;
    }
    //метод создания json, используя данные существующего курьера
    public static CourierLogin from(Courier courier) {
        return new CourierLogin(courier.getLogin(), courier.getPassword());
    }

    //метод создания json с рандомным логином и паролем (для негативного теста)
    public static CourierLogin random() {
        return new CourierLogin(RandomStringUtils.randomAlphabetic(6,12),
                RandomStringUtils.randomAlphabetic(4) + RandomStringUtils.randomAlphabetic(4));
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
