package apitestscenarios;

import org.junit.Test;
import serialsclasses.AfterBeforeTests;

public class LoginUserWithPostApiAuthLoginTest extends AfterBeforeTests {

    @Test
    public void canLoginExistUser(){
        sendRegistrationRequest(email, password, name);
        response = sendLoginRequest(email, password);

        checkStatus (response, true, "200 OK");
    }

    @Test
    public void cannotLoginUserWithWrongEmail(){
        sendRegistrationRequest(email, password, name);
        response = sendLoginRequest(email + "ru", password);

        checkStatus (response, false, "401 Unauthorized");
    }

    @Test
    public void cannotLoginUserWithWrongPassword(){
        sendRegistrationRequest(email, password, name);
        response = sendLoginRequest(email, password + "123");

        checkStatus (response, false, "401 Unauthorized");
    }
}
