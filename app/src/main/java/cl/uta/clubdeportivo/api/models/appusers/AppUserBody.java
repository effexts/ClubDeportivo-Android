package cl.uta.clubdeportivo.api.models.appusers;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AppUserBody {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("title")
    @Expose
    private String title2;
    @SerializedName("user_id")
    @Expose
    private String user_id;
    @SerializedName("nombres")
    @Expose
    private String nombres;
    @SerializedName("correo")
    @Expose
    private String correo;

    public AppUserBody(String status, String title2, String user_id, String nombres, String correo) {
        this.status = status;
        this.title2 = title2;
        this.user_id = user_id;
        this.nombres = nombres;
        this.correo = correo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTitle2() {
        return title2;
    }

    public void setTitle2(String title) {
        this.title2 = title;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }
}
