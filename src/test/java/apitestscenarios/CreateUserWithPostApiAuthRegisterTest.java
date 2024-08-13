package apitestscenarios;


import io.restassured.response.Response;
import org.junit.Test;
import serialsclasses.AfterBeforeTests;

public class CreateUserWithPostApiAuthRegisterTest extends AfterBeforeTests {

    @Test
    public void canCreateUser(){
        response = sendRegistrationRequest(email, password, name);
        checkStatus (response, true, "200 OK");
    }

    @Test
    public void cannotCreateTwoIdenticalUsers(){
        response = sendRegistrationRequest(email, password, name);
        Response response1 = sendRegistrationRequest(email, password, name);

        checkStatus (response1, false, "403 Forbidden");

    }

    @Test
    public void cannotCreateUserWithoutEmail(){
        response = sendRegistrationRequest("", password, name);

        checkStatus (response, false, "403 Forbidden");
    }

    @Test
    public void cannotCreateUserWithoutPassword(){
        response = sendRegistrationRequest(email, "", name);

        checkStatus (response, false,"403 Forbidden");
    }

    @Test
    public void cannotCreateUserWithoutName(){
        response = sendRegistrationRequest(email, password, "");

        checkStatus (response, false,"403 Forbidden");
    }
}
