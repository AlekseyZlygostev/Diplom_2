package apitestscenarios;


import org.junit.Test;
import serialsclasses.AfterBeforeTests;
import java.util.ArrayList;
import java.util.Collections;

public class CreateOrderWithPostApiOrdersTest extends AfterBeforeTests {

    @Test
    public void canCreateOrderWithAuthorisation(){
        token = getToken(sendRegistrationRequest(email, password, name)).replaceFirst("Bearer ", "");
        setIngridients();
        response = sendPostOrderRequest(token, ingredients);
        checkStatus (response, true, "200 OK");
    }

    @Test
    public void canCreateOrderWithoutAuthorisation(){
        setIngridients();
        response = sendPostOrderRequest("", ingredients);
        checkStatus (response, true, "200 OK");
    }

    @Test
    public void cannotCreateOrderWithoutIngredients(){
        token = getToken(sendRegistrationRequest(email, password, name)).replaceFirst("Bearer ", "");
        response = sendPostOrderRequest(token, Collections.singletonList("1234567890"));
        checkStatus (response, "500 Internal Server Error");
    }

    @Test
    public void cannotCreateOrderWithWrongIngredientsHash(){
        token = getToken(sendRegistrationRequest(email, password, name)).replaceFirst("Bearer ", "");
        ingredients = new ArrayList<>();
        response = sendPostOrderRequest("", ingredients);
        checkStatus (response, false, "400 Bad Request");
    }
}
