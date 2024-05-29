package scooter.jsons.login;

import scooter.jsons.CourierLogin;

public class CourierLoginWrongPassword {
    public String login;
    public String password;

    public CourierLoginWrongPassword(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public static CourierLoginWrongPassword from(CourierLogin courierLogin) {
        return new CourierLoginWrongPassword(
                courierLogin.getLogin(),    // логин берётся настоящий из ранее созданного json
                CourierLogin.random().getPassword());   // пароль генерится рандомный
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
