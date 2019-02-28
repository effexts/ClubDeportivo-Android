package cl.uta.clubdeportivo.api.models.appusers;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import cl.uta.clubdeportivo.api.models.appusers.ContentAppUsers;

public class AppUsersTalleres {

    @SerializedName("id")
    @Expose
    private Long id;
    @SerializedName("slug")
    @Expose
    private String slug;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("link")
    @Expose
    private String link;
    @SerializedName("title")
    @Expose
    private Title title;
    @SerializedName("content")
    @Expose
    private ContentAppUsers content;
    @SerializedName("template")
    @Expose
    private String template;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("nombres")
    @Expose
    private String nombres;
    @SerializedName("correo")
    @Expose
    private String correo;
    @SerializedName("lista_talleres")
    @Expose
    private List<Long> listaTalleres = null;

    /**
     * No args constructor for use in serialization
     */
    public AppUsersTalleres() {
    }

    /**
     * @param template
     * @param content
     * @param id
     * @param listaTalleres
     * @param title
     * @param nombres
     * @param status
     * @param userId
     * @param link
     * @param slug
     * @param correo
     */
    public AppUsersTalleres(Long id, String slug, String status, String link, Title title, ContentAppUsers content, String template, String userId, String nombres, String correo, List<Long> listaTalleres) {
        super();
        this.id = id;
        this.slug = slug;
        this.status = status;
        this.link = link;
        this.title = title;
        this.content = content;
        this.template = template;
        this.userId = userId;
        this.nombres = nombres;
        this.correo = correo;
        this.listaTalleres = listaTalleres;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AppUsersTalleres withId(Long id) {
        this.id = id;
        return this;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public AppUsersTalleres withSlug(String slug) {
        this.slug = slug;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public AppUsersTalleres withStatus(String status) {
        this.status = status;
        return this;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public AppUsersTalleres withLink(String link) {
        this.link = link;
        return this;
    }

    public Title getTitle() {
        return title;
    }

    public void setTitle(Title title) {
        this.title = title;
    }

    public AppUsersTalleres withTitle(Title title) {
        this.title = title;
        return this;
    }

    public ContentAppUsers getContent() {
        return content;
    }

    public void setContent(ContentAppUsers content) {
        this.content = content;
    }

    public AppUsersTalleres withContent(ContentAppUsers content) {
        this.content = content;
        return this;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public AppUsersTalleres withTemplate(String template) {
        this.template = template;
        return this;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public AppUsersTalleres withUserId(String userId) {
        this.userId = userId;
        return this;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public AppUsersTalleres withNombres(String nombres) {
        this.nombres = nombres;
        return this;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public AppUsersTalleres withCorreo(String correo) {
        this.correo = correo;
        return this;
    }

    public List<Long> getListaTalleres() {
        return listaTalleres;
    }

    public void setListaTalleres(List<Long> listaTalleres) {
        this.listaTalleres = listaTalleres;
    }

    public AppUsersTalleres withListaTalleres(List<Long> listaTalleres) {
        this.listaTalleres = listaTalleres;
        return this;
    }

}
