package cl.uta.clubdeportivo.api.models.appusers;

public class TokenWPBody {
    private String username;
    private String passwd;

    public TokenWPBody(String username, String passwd) {
        this.username = username;
        this.passwd = passwd;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }
}
