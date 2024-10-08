package apitestscenarios;

import org.junit.Test;
import serialsclasses.BeforeAfterSteps;

public class GetUsersOrdersWithGetApiOrdersTest extends BeforeAfterSteps {

    @Test
    public void canGetOrdersWithAuthUser(){
        token = getToken(sendRegistrationRequest(email, password, name)).replaceFirst("Bearer ", "");
        setIngridients();
        response = sendGetOrderRequest(token);
        checkStatus (response, true, "200 OK");
    }

    @Test
    public void cannotGetOrdersWithoutAuthUser(){
        setIngridients();
        response = sendGetOrderRequest("");
        checkStatus (response, false, "401 Unauthorized");
    }
}
