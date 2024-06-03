package scooter.jsons;

import org.apache.commons.lang3.RandomStringUtils;

public class CourierLogin {
    private String login;
    private String password;

    public CourierLogin() {
    }

    public CourierLogin(String login, String password) {
        this.login = login;
        this.password = password;
    }
    // создание json на основе существующего курьера
    public static CourierLogin from(Courier courier) {
        return new CourierLogin(
                courier.getLogin(),
                courier.getPassword()
        );
    }

    // создание json с рандомным логином и паролем (для негативного теста)
    public static CourierLogin random() {
        return new CourierLogin(
                "Login_" + RandomStringUtils.randomAlphabetic(4,6),
                RandomStringUtils.randomAlphabetic(4) + RandomStringUtils.randomNumeric(4)
        );
    }

    // создание json без логина и с паролем существующего курьера
    public static CourierLogin noLogin(Courier courier) {
        return new CourierLogin(
                null,
                courier.getPassword()
        );
    }

    // создание json без пароля и с логином существующего курьера
    public static CourierLogin noPassword(Courier courier) {
        return new CourierLogin(
                courier.getLogin(),
                null
        );
    }

    // создание json с некорректным логином и с паролем существующего курьера
    public static CourierLogin wrongLogin(Courier courier) {
        return new CourierLogin(
                random().getLogin(),
                courier.getPassword()
        );
    }

    // создание json с некорректным паролем и с логином существующего курьера
    public static CourierLogin wrongPassword(Courier courier) {
        return new CourierLogin(
                courier.getLogin(),
                random().getPassword()
        );
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