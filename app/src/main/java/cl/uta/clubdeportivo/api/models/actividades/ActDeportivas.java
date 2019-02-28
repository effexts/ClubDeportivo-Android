package cl.uta.clubdeportivo.api.models.actividades;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ActDeportivas {

    @SerializedName("id")
    @Expose
    private Long id;
    @SerializedName("slug")
    @Expose
    private String slug;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("link")
    @Expose
    private String link;
    @SerializedName("title")
    @Expose
    private TitleActDep titleActDep;
    @SerializedName("content")
    @Expose
    private ContentActDep content;
    @SerializedName("id_actividaddeportiva")
    @Expose
    private String idActividaddeportiva;
    @SerializedName("nombre_actividaddeportiva")
    @Expose
    private String nombreActividaddeportiva;
    @SerializedName("instructor")
    @Expose
    private String instructor;
    @SerializedName("lugar")
    @Expose
    private String lugar;
    @SerializedName("dias")
    @Expose
    private String dias;
    @SerializedName("horario_inicio")
    @Expose
    private String horarioInicio;
    @SerializedName("horario_termino")
    @Expose
    private String horarioTermino;
    @SerializedName("imagen")
    @Expose
    private Imagen imagen;
    /**
     * No args constructor for use in serialization
     *
     */
    public ActDeportivas() {
    }

    /**
     *
     * @param content
     * @param id
     * @param horarioTermino
     * @param titleActDep
     * @param instructor
     * @param nombreActividaddeportiva
     * @param status
     * @param lugar
     * @param idActividaddeportiva
     * @param link
     * @param slug
     * @param horarioInicio
     * @param type
     * @param dias
     * @param imagen
     */
    public ActDeportivas(Long id, String slug, String status, String type, String link, TitleActDep titleActDep, ContentActDep content, String idActividaddeportiva, String nombreActividaddeportiva, String instructor, String lugar, String dias, String horarioInicio, String horarioTermino, Imagen imagen) {
        super();
        this.id = id;
        this.slug = slug;
        this.status = status;
        this.type = type;
        this.link = link;
        this.titleActDep = titleActDep;
        this.content = content;
        this.idActividaddeportiva = idActividaddeportiva;
        this.nombreActividaddeportiva = nombreActividaddeportiva;
        this.instructor = instructor;
        this.lugar = lugar;
        this.dias = dias;
        this.horarioInicio = horarioInicio;
        this.horarioTermino = horarioTermino;
        this.imagen = imagen;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ActDeportivas withId(Long id) {
        this.id = id;
        return this;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public ActDeportivas withSlug(String slug) {
        this.slug = slug;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ActDeportivas withStatus(String status) {
        this.status = status;
        return this;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ActDeportivas withType(String type) {
        this.type = type;
        return this;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public ActDeportivas withLink(String link) {
        this.link = link;
        return this;
    }

    public TitleActDep getTitleActDep() {
        return titleActDep;
    }

    public void setTitleActDep(TitleActDep titleActDep) {
        this.titleActDep = titleActDep;
    }

    public ActDeportivas withTitle(TitleActDep titleActDep) {
        this.titleActDep = titleActDep;
        return this;
    }

    public ContentActDep getContent() {
        return content;
    }

    public void setContent(ContentActDep content) {
        this.content = content;
    }

    public ActDeportivas withContent(ContentActDep content) {
        this.content = content;
        return this;
    }

    public String getIdActividaddeportiva() {
        return idActividaddeportiva;
    }

    public void setIdActividaddeportiva(String idActividaddeportiva) {
        this.idActividaddeportiva = idActividaddeportiva;
    }

    public ActDeportivas withIdActividaddeportiva(String idActividaddeportiva) {
        this.idActividaddeportiva = idActividaddeportiva;
        return this;
    }

    public String getNombreActividaddeportiva() {
        return nombreActividaddeportiva;
    }

    public void setNombreActividaddeportiva(String nombreActividaddeportiva) {
        this.nombreActividaddeportiva = nombreActividaddeportiva;
    }

    public ActDeportivas withNombreActividaddeportiva(String nombreActividaddeportiva) {
        this.nombreActividaddeportiva = nombreActividaddeportiva;
        return this;
    }

    public String getInstructor() {
        return instructor;
    }

    public void setInstructor(String instructor) {
        this.instructor = instructor;
    }

    public ActDeportivas withInstructor(String instructor) {
        this.instructor = instructor;
        return this;
    }

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

    public ActDeportivas withLugar(String lugar) {
        this.lugar = lugar;
        return this;
    }

    public String getDias() {
        return dias;
    }

    public void setDias(String dias) {
        this.dias = dias;
    }

    public ActDeportivas withDias(String dias) {
        this.dias = dias;
        return this;
    }

    public String getHorarioInicio() {
        return horarioInicio;
    }

    public void setHorarioInicio(String horarioInicio) {
        this.horarioInicio = horarioInicio;
    }

    public ActDeportivas withHorarioInicio(String horarioInicio) {
        this.horarioInicio = horarioInicio;
        return this;
    }

    public String getHorarioTermino() {
        return horarioTermino;
    }

    public void setHorarioTermino(String horarioTermino) {
        this.horarioTermino = horarioTermino;
    }

    public ActDeportivas withHorarioTermino(String horarioTermino) {
        this.horarioTermino = horarioTermino;
        return this;
    }

    public Imagen getImagen() {
        return imagen;
    }

    public void setImagen(Imagen imagen) {
        this.imagen = imagen;
    }

    public ActDeportivas withImagen(Imagen imagen) {
        this.imagen = imagen;
        return this;
    }

}