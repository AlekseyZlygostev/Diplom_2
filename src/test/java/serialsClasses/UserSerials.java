package serialsClasses;


import java.util.List;

public class UserSerials {

    private String email;
    private String password;
    private String name;
    private List<String> ingredients;

    public UserSerials() {
    }

    public UserSerials(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    public UserSerials(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    public UserSerials(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }
}
