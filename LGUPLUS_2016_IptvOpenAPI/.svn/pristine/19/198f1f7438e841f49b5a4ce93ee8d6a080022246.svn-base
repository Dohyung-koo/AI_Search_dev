package com.diquest.openapi.google.response.entity;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.List;

public class Items {

    private String title;
    @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
    private String genre;
    @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
    private String id;
    @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
    private String description;
    @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
    private String rating;
    @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
    private String tmsId;
    @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
    private String tmsRootId;
    @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
    private String entityType;

    @JsonProperty("posterImage")
    @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
    private PosterImage posterImage;
    @JsonProperty("callToActions")
    private List<CallToActions> callToActions;

    @JsonProperty("badgings")
    @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
    private List<Badgings> badgings;

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public String getTmsId() {
        return tmsId;
    }

    public void setTmsId(String tmsId) {
        this.tmsId = tmsId;
    }

    public String getTmsRootId() {
        return tmsRootId;
    }

    public void setTmsRootId(String tmsRootId) {
        this.tmsRootId = tmsRootId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public PosterImage getPosterImage() {
        return posterImage;
    }

    public void setPosterImage(PosterImage posterImage) {
        this.posterImage = posterImage;
    }

    public List<CallToActions> getCallToActions() {
        return callToActions;
    }

    public void setCallToActions(List<CallToActions> callToActions) {
        this.callToActions = callToActions;
    }

    public List<Badgings> getBadgings() {
        return badgings;
    }

    public void setBadgings(List<Badgings> badgings) {
        this.badgings = badgings;
    }
}
