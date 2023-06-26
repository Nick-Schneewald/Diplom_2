package ru.yandex_praktikum.diplom_2.dataproviders;

import org.apache.commons.lang3.RandomStringUtils;
import ru.yandex_praktikum.diplom_2.pojo.*;

public class UserProvider {

    public static final String BASE_EMAIL_DOMAIN = "@yandex.ru";

    public static CreateUserRequest getRandomCreateUserRequest() {
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setEmail(RandomStringUtils.randomAlphabetic(8) + BASE_EMAIL_DOMAIN);
        createUserRequest.setPassword(RandomStringUtils.randomAlphabetic(8));
        createUserRequest.setName(RandomStringUtils.randomAlphabetic(8));
        return createUserRequest;
    }

    public static CreateUserRequestWithoutEmail getRandomCreateUserRequestWithoutEmail() {
        CreateUserRequestWithoutEmail createUserRequestWithoutEmail = new CreateUserRequestWithoutEmail();
        createUserRequestWithoutEmail.setPassword(RandomStringUtils.randomAlphabetic(8));
        createUserRequestWithoutEmail.setName(RandomStringUtils.randomAlphabetic(8));
        return createUserRequestWithoutEmail;
    }

    public static CreateUserRequestWithoutPass getRandomCreateUserRequestWithoutPass() {
        CreateUserRequestWithoutPass createUserRequestWithoutPass = new CreateUserRequestWithoutPass();
        createUserRequestWithoutPass.setEmail(RandomStringUtils.randomAlphabetic(8) + BASE_EMAIL_DOMAIN);
        createUserRequestWithoutPass.setName(RandomStringUtils.randomAlphabetic(8));
        return createUserRequestWithoutPass;
    }

    public static CreateUserRequestWithoutName getRandomCreateUserRequestWithoutName() {
        CreateUserRequestWithoutName createUserRequestWithoutName = new CreateUserRequestWithoutName();
        createUserRequestWithoutName.setEmail(RandomStringUtils.randomAlphabetic(8) + BASE_EMAIL_DOMAIN);
        createUserRequestWithoutName.setPassword(RandomStringUtils.randomAlphabetic(8));
        return createUserRequestWithoutName;
    }

    public static UpdateUserEmailRequest getRandomUpdateUserEmailRequest() {
        UpdateUserEmailRequest updateUserEmailRequest = new UpdateUserEmailRequest();
        updateUserEmailRequest.setEmail(RandomStringUtils.randomAlphabetic(8) + BASE_EMAIL_DOMAIN);
        return updateUserEmailRequest;
    }

    public static UpdateUserPassRequest getRandomUpdateUserPassRequest() {
        UpdateUserPassRequest updateUserPassRequest = new UpdateUserPassRequest();
        updateUserPassRequest.setPassword(RandomStringUtils.randomAlphabetic(8));
        return updateUserPassRequest;
    }

    public static UpdateUserNameRequest getRandomUpdateUserNameRequest() {
        UpdateUserNameRequest updateUserNameRequest = new UpdateUserNameRequest();
        updateUserNameRequest.setName(RandomStringUtils.randomAlphabetic(8));
        return updateUserNameRequest;
    }
}
