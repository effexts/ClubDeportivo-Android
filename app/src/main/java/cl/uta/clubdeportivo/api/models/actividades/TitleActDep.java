package cl.uta.clubdeportivo.api.models.actividades;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TitleActDep {

    @SerializedName("rendered")
    @Expose
    private String rendered;

    /**
     * No args constructor for use in serialization
     *
     */
    public TitleActDep() {
    }

    /**
     *
     * @param rendered
     */
    public TitleActDep(String rendered) {
        super();
        this.rendered = rendered;
    }

    public String getRendered() {
        return rendered;
    }

    public void setRendered(String rendered) {
        this.rendered = rendered;
    }

    public TitleActDep withRendered(String rendered) {
        this.rendered = rendered;
        return this;
    }

}