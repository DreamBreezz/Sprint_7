package scooter.jsons;

import org.apache.commons.lang3.RandomStringUtils;

public class Courier {
    private String login;
    private String password;
    private String firstName;

    public Courier() {
    }

    public Courier(String login, String password, String firstName) {
        this.login = login;
        this.password = password;
        this.firstName = firstName;
    }

    // какой-то дефолтный курьер
    public static Courier defaultGuy() {
        return new Courier("George1984", "MyStr0ngP@ssword", "GeorgeOrwell");
    }

    // метод для создания курьера с рандомными логином, паролем и именем
    public static Courier random() {
        return new Courier(
                "Login_" + RandomStringUtils.randomAlphabetic(4,6),
                RandomStringUtils.randomAlphabetic(4) + RandomStringUtils.randomNumeric(4),
                "Name_" + RandomStringUtils.randomAlphabetic(4,6)
        );
    }
    // создание курьера без логина
    public static Courier noLogin() {
        return new Courier(null,
                random().getPassword(),
                random().getFirstName());
    }

    // создание курьера без пароля
    public static Courier noPassword() {
        return new Courier(random().getLogin(),
                null,
                random().getFirstName());
    }

    // создание курьера без имени
    public static Courier noName() {
        return new Courier(random().getLogin(),
                random().getPassword(),
                null);
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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

}
