package ru.yandex_praktikum.diplom_2.pojo;

public class LoginUserRequestWithoutPass {
    private String email;

    public LoginUserRequestWithoutPass(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public static LoginUserRequestWithoutPass from(CreateUserRequest createUserRequest){
        return new LoginUserRequestWithoutPass(createUserRequest.getEmail());
    }
}
