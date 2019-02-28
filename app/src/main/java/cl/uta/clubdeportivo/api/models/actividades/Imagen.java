package cl.uta.clubdeportivo.api.models.actividades;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Imagen {

    @SerializedName("ID")
    @Expose
    private String iD;
    @SerializedName("post_title")
    @Expose
    private String postTitle;
    @SerializedName("post_parent")
    @Expose
    private String postParent;
    @SerializedName("guid")
    @Expose
    private String guid;
    @SerializedName("post_mime_type")
    @Expose
    private String postMimeType;

    /**
     * No args constructor for use in serialization
     *
     */
    public Imagen() {
    }

    /**
     *
     * @param guid
     * @param postMimeType
     * @param postParent
     * @param postTitle
     * @param iD
     */
    public Imagen(String iD, String postTitle, String postParent, String guid, String postMimeType) {
        super();
        this.iD = iD;
        this.postTitle = postTitle;
        this.postParent = postParent;
        this.guid = guid;
        this.postMimeType = postMimeType;
    }

    public String getID() {
        return iD;
    }

    public void setID(String iD) {
        this.iD = iD;
    }

    public Imagen withID(String iD) {
        this.iD = iD;
        return this;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public Imagen withPostTitle(String postTitle) {
        this.postTitle = postTitle;
        return this;
    }

    public String getPostParent() {
        return postParent;
    }

    public void setPostParent(String postParent) {
        this.postParent = postParent;
    }

    public Imagen withPostParent(String postParent) {
        this.postParent = postParent;
        return this;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public Imagen withGuid(String guid) {
        this.guid = guid;
        return this;
    }

    public String getPostMimeType() {
        return postMimeType;
    }

    public void setPostMimeType(String postMimeType) {
        this.postMimeType = postMimeType;
    }

    public Imagen withPostMimeType(String postMimeType) {
        this.postMimeType = postMimeType;
        return this;
    }

}