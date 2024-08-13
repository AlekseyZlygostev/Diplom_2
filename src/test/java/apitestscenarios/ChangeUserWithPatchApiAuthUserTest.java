package apitestscenarios;


import org.junit.Test;
import serialsclasses.AfterBeforeTests;


public class ChangeUserWithPatchApiAuthUserTest extends AfterBeforeTests {

    @Test
    public void canChangeUserWithAuthorisation(){
        token = getToken(sendRegistrationRequest(email, password, name)).replaceFirst("Bearer ", "");
        response = sendPatchRequest(token,email + "ru", password + "123", name + "s");
        checkStatus (response, true, "200 OK");
    }

    @Test
    public void cannotChangeUserWithoutAuthorisation(){
        response = sendPatchRequest("", email + "ru", password + "123", name + "s");
        checkStatus (response, false,"401 Unauthorized");
    }

    @Test
    public void cannotChangeUserWithExistEmail(){
        token = getToken(sendRegistrationRequest(email, password, name)).replaceFirst("Bearer ", "");
        sendRegistrationRequest(name + email, password, name);
        response = sendPatchRequest(token,name + email, password, name);
        checkStatus (response, false,"403 Forbidden");
    }
}
