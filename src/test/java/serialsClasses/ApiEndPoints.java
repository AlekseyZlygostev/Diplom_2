package serialsClasses;

public class ApiEndPoints {
    private String createUser;
    private String loginUser;
    private String changeUser;
    private String createOrder;

    public ApiEndPoints() {
        this.createUser = "/api/auth/register";
        this.loginUser = "/api/auth/login";
        this.changeUser = "/api/auth/user";
        this.createOrder = "/api/orders";
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getLoginUser() {
        return loginUser;
    }

    public void setLoginUser(String loginUser) {
        this.loginUser = loginUser;
    }

    public String getChangeUser() {
        return changeUser;
    }

    public void setChangeUser(String changeUser) {
        this.changeUser = changeUser;
    }

    public String getCreateOrder() {
        return createOrder;
    }

    public void setCreateOrder(String createOrder) {
        this.createOrder = createOrder;
    }
}
