package com.diquest.openapi.google.request;

import com.google.gson.JsonArray;
import org.codehaus.jackson.annotate.JsonProperty;

import java.util.List;

public class StructuredQuery {

    private String queryIntent = "";
    private String searchQuery = "";
    private String mediaType = "";

    @JsonProperty("entities")
    private List<Entities> entitiesList;

    @JsonProperty("genre")
    private List<String> genreList;

    @JsonProperty("actor")
    private List<Actor> actorList;

    @JsonProperty("director")
    private List<Director> directorList;

    @JsonProperty("relatedPerson")
    private List<RelatedPerson> relatedPersonList;


    private String channelName = "";
    private String channelNumber = "";
    private String channelId = "";

    public List<Actor> getActorList() {
        return actorList;
    }

    public void setActorList(List<Actor> actorList) {
        this.actorList = actorList;
    }

    public List<Director> getDirectorList() {
        return directorList;
    }

    public void setDirectorList(List<Director> directorList) {
        this.directorList = directorList;
    }

    public List<RelatedPerson> getRelatedPersonList() {
        return relatedPersonList;
    }

    public void setRelatedPersonList(List<RelatedPerson> relatedPersonList) {
        this.relatedPersonList = relatedPersonList;
    }

    public List<String> getGenreList() {
        return genreList;
    }

    public void setGenreList(List<String> genreList) {
        this.genreList = genreList;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getChannelNumber() {
        return channelNumber;
    }

    public void setChannelNumber(String channelNumber) {
        this.channelNumber = channelNumber;
    }

    public String getQueryIntent() {
        return queryIntent;
    }

    public void setQueryIntent(String queryIntent) {
        this.queryIntent = queryIntent;
    }

    public String getSearchQuery() {
        return searchQuery;
    }

    public void setSearchQuery(String searchQuery) {
        this.searchQuery = searchQuery;
    }

    public List<Entities> getEntitiesList() {
        return entitiesList;
    }
    public String getEntitiesStr(){
        String entitiesStr = "";
        int size = getEntitiesList().size();
        for (int i = 0; i < size; i++) {
            /*if(i == 0){
                entitiesStr += "[";
            }*/
            Entities e = getEntitiesList().get(i);
            entitiesStr += e.getContentId();
            entitiesStr += "/";
            entitiesStr += e.getEntityType();
            entitiesStr += "/";
            entitiesStr += e.getTitle();
            if(i == size-1){
                //entitiesStr+="]";
            }else{
                entitiesStr+="/";
            }
        }
        return  entitiesStr;
    }
    public void setEntitiesList(List<Entities> entitiesList) {
        this.entitiesList = entitiesList;
    }

    @Override
    public String toString() {
        return "StructuredQuery{" +
                "queryIntent='" + queryIntent + '\'' +
                ", searchQuery='" + searchQuery + '\'' +
                ", mediaType='" + mediaType + '\'' +
                ", entitiesList=" + entitiesList +
                ", genreList=" + genreList +
                ", channelName='" + channelName + '\'' +
                ", channelNumber='" + channelNumber + '\'' +
                ", channelId='" + channelId + '\'' +
                '}';
    }
}
