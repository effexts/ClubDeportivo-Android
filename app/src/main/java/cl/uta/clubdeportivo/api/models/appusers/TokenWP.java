package cl.uta.clubdeportivo.api.models.appusers;

import com.google.gson.annotations.SerializedName;

public class TokenWP {
    @SerializedName("token")
    private String token;

    public TokenWP(String token) {
        this.token = token;
    }

    public TokenWP() {
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
