package cl.uta.clubdeportivo.api.models.actividades;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ContentActDep {

    @SerializedName("rendered")
    @Expose
    private String rendered;
    @SerializedName("protected")
    @Expose
    private Boolean _protected;

    /**
     * No args constructor for use in serialization
     *
     */
    public ContentActDep() {
    }

    /**
     *
     * @param rendered
     * @param _protected
     */
    public ContentActDep(String rendered, Boolean _protected) {
        super();
        this.rendered = rendered;
        this._protected = _protected;
    }

    public String getRendered() {
        return rendered;
    }

    public void setRendered(String rendered) {
        this.rendered = rendered;
    }

    public ContentActDep withRendered(String rendered) {
        this.rendered = rendered;
        return this;
    }

    public Boolean getProtected() {
        return _protected;
    }

    public void setProtected(Boolean _protected) {
        this._protected = _protected;
    }

    public ContentActDep withProtected(Boolean _protected) {
        this._protected = _protected;
        return this;
    }

}