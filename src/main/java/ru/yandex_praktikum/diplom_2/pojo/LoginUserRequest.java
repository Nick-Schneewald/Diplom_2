package ru.yandex_praktikum.diplom_2.pojo;

public class LoginUserRequest {
    private String email;
    private String password;

    public LoginUserRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getLogin() {
        return email;
    }

    public void setLogin(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public static LoginUserRequest from(CreateUserRequest createUserRequest){
        return new LoginUserRequest(createUserRequest.getEmail(), createUserRequest.getPassword());
    }
}
