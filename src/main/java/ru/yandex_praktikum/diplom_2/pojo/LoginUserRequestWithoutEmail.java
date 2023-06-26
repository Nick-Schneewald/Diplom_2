package ru.yandex_praktikum.diplom_2.pojo;

public class LoginUserRequestWithoutEmail {
    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LoginUserRequestWithoutEmail(String password) {
        this.password = password;
    }

    public static LoginUserRequestWithoutEmail from(CreateUserRequest createUserRequest){
        return new LoginUserRequestWithoutEmail(createUserRequest.getPassword());
    }
}
