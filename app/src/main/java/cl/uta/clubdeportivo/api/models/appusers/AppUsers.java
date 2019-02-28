package cl.uta.clubdeportivo.api.models.appusers;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AppUsers {

    @SerializedName("id")
    @Expose
    private Long id;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("link")
    @Expose
    private String link;
    @SerializedName("title")
    @Expose
    private Title title;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("nombres")
    @Expose
    private String nombres;
    @SerializedName("correo")
    @Expose
    private String correo;

    /**
     * No args constructor for use in serialization
     */
    public AppUsers() {
    }

    /**
     * @param id
     * @param title
     * @param nombres
     * @param status
     * @param userId
     * @param link
     * @param correo
     */
    public AppUsers(Long id, String status, String link, Title title, String userId, String nombres, String correo) {
        super();
        this.id = id;
        this.status = status;
        this.link = link;
        this.title = title;
        this.userId = userId;
        this.nombres = nombres;
        this.correo = correo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AppUsers withId(Long id) {
        this.id = id;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public AppUsers withStatus(String status) {
        this.status = status;
        return this;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public AppUsers withLink(String link) {
        this.link = link;
        return this;
    }

    public Title getTitle() {
        return title;
    }

    public void setTitle(Title title) {
        this.title = title;
    }

    public AppUsers withTitle(Title title) {
        this.title = title;
        return this;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public AppUsers withUserId(String userId) {
        this.userId = userId;
        return this;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public AppUsers withNombres(String nombres) {
        this.nombres = nombres;
        return this;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public AppUsers withCorreo(String correo) {
        this.correo = correo;
        return this;
    }

}

