package com.shu.bakingtime.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;
import org.parceler.ParcelProperty;

@Parcel
public class Step
{

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("shortDescription")
    @Expose
    private String shortDescription;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("videoURL")
    @Expose
    private String videoURL;
    @SerializedName("thumbnailURL")
    @Expose
    private String thumbnailURL;

    @ParcelProperty("id")
    public Integer getId() {
        return id;
    }

    @ParcelProperty("id")
    public void setId(Integer id) {
        this.id = id;
    }

    @ParcelProperty("shortDescription")
    public String getShortDescription() {
        return shortDescription;
    }

    @ParcelProperty("shortDescription")
    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    @ParcelProperty("description")
    public String getDescription() {
        return description;
    }

    @ParcelProperty("description")
    public void setDescription(String description) {
        this.description = description;
    }

    @ParcelProperty("videoURL")
    public String getVideoURL() {
        return videoURL;
    }

    @ParcelProperty("videoURL")
    public void setVideoURL(String videoURL) {
        this.videoURL = videoURL;
    }

    @ParcelProperty("thumbnailURL")
    public String getThumbnailURL() {
        return thumbnailURL;
    }

    @ParcelProperty("thumbnailURL")
    public void setThumbnailURL(String thumbnailURL) {
        this.thumbnailURL = thumbnailURL;
    }

}
