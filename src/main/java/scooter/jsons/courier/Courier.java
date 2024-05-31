package scooter.jsons.courier;

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
        return new Courier(RandomStringUtils.randomAlphabetic(6,12),
                RandomStringUtils.randomAlphabetic(4) + RandomStringUtils.randomAlphabetic(4),
                RandomStringUtils.randomAlphabetic(6,12));
    }

    // создание курьера без имени
    public static Courier noName() {
        return new Courier(random().getLogin(), random().getPassword(), null);
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
