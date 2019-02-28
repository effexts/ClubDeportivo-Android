package cl.uta.clubdeportivo.api.models.appusers;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Title {

    @SerializedName("raw")
    @Expose
    private String raw;
    @SerializedName("rendered")
    @Expose
    private String rendered;

    /**
     * No args constructor for use in serialization
     *
     */
    public Title() {
    }

    /**
     *
     * @param raw
     * @param rendered
     */
    public Title(String raw, String rendered) {
        super();
        this.raw = raw;
        this.rendered = rendered;
    }

    public String getRaw() {
        return raw;
    }

    public void setRaw(String raw) {
        this.raw = raw;
    }

    public Title withRaw(String raw) {
        this.raw = raw;
        return this;
    }

    public String getRendered() {
        return rendered;
    }

    public void setRendered(String rendered) {
        this.rendered = rendered;
    }

    public Title withRendered(String rendered) {
        this.rendered = rendered;
        return this;
    }

}