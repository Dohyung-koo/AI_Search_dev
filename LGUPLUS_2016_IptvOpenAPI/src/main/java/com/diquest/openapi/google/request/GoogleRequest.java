package com.diquest.openapi.google.request;


import com.diquest.openapi.google.response.ErrorString;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown =  true)
public class GoogleRequest {

    @JsonProperty("requestId")
    private String requestId = "";

    @JsonProperty("languageCode")
    private String languageCode = "";

    @JsonProperty("customContext")
    private String customContext = "";

    @JsonProperty("deviceConfig")
    private DeviceConfig deviceConfig;

    @JsonProperty("structuredQuery")
    private StructuredQuery structuredQuery;

    private String errorCode="";

    private String host = "";
    private int port;

    private String collectionName ="";

    private String regionStr = "";

    private String genre = "";
    private String actor = "";
    private String director = "";
    private String related = "";

    private String searchType = "";

    public boolean hasErrorResponse() {
        if("".equals(errorCode)) {
            return false;
        }

        return true;
    }

    public String checkType() {
        String type = "";
        if(this.structuredQuery.getGenreList() != null) {
            setGenre(this.structuredQuery.getGenreList().get(0));
            type =  "genre";
        }
        else if(this.structuredQuery.getActorList() != null) {
            setActor(this.structuredQuery.getActorList().get(0).getName());
            type = "actor";
        }
        else if(this.structuredQuery.getDirectorList()!=null){
            setDirector(this.structuredQuery.getDirectorList().get(0).getName());
            type = "director";
        }
        if (this.structuredQuery.getRelatedPersonList()!=null){
            setRelated(this.structuredQuery.getRelatedPersonList().get(0).getName());
            type = "related";
        }
        return type;
    }

    public String getQ(){

        String queryIntent = this.structuredQuery.getQueryIntent();
        String q = "";

        if("ENTITY_SEARCH".equals(queryIntent) || "PLAY_TVM".equals(queryIntent)) {
            Entities e =this.structuredQuery.getEntitiesList().get(0);
            if(StringUtils.isNotEmpty(e.getContentId())){
                setSearchType("id");
                q = e.getContentId();
            }else if(StringUtils.isNotEmpty(e.getTitle())){
                setSearchType("name");
                q = e.getTitle();
            }else if(StringUtils.isNotEmpty(this.structuredQuery.getSearchQuery())){
                setSearchType("name");
                q = this.structuredQuery.getSearchQuery();
            }

        }  else if("PLAY".equals(queryIntent) || "SEARCH".equals(queryIntent)) {
            q = this.structuredQuery.getSearchQuery();
        } else if("SWITCH_CHANNEL".equals(queryIntent)) {
            if(StringUtils.isNotEmpty(this.structuredQuery.getChannelName())){
                setSearchType("name");
                q = this.structuredQuery.getChannelName();
            }else{
                setSearchType("number");
                q = this.structuredQuery.getChannelNumber();
            }

        }

        return q;
    }

    public String getSearchType() {
        return searchType;
    }

    public void setSearchType(String searchType) {
        this.searchType = searchType;
    }

    public String getActor() {
        return actor;
    }

    public void setActor(String actor) {
        this.actor = actor;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getRelated() {
        return related;
    }

    public void setRelated(String related) {
        this.related = related;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getRegionStr() {
        return regionStr;
    }

    public void setRegionStr(String regionStr) {
        this.regionStr = regionStr;
    }

    public String getCollectionName() {
        return collectionName;
    }

    public void setCollectionName(String collectionName) {
        this.collectionName = collectionName;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    public String getCustomContext() {
        return customContext;
    }

    public void setCustomContext(String customContext) {
        this.customContext = customContext;
    }

    public DeviceConfig getDeviceConfig() {
        return deviceConfig;
    }

    public void setDeviceConfig(DeviceConfig deviceConfig) {
        this.deviceConfig = deviceConfig;
    }

    public StructuredQuery getStructuredQuery() {
        return structuredQuery;
    }

    public void setStructuredQuery(StructuredQuery structuredQuery) {
        this.structuredQuery = structuredQuery;
    }

    @Override
    public String toString() {
        return "GoogleEntityRequest{" +
                "requestId='" + requestId + '\'' +
                ", languageCode='" + languageCode + '\'' +
                ", customContext='" + customContext + '\'' +
                ", deviceConfig=" + deviceConfig +
                ", structuredQuery=" + structuredQuery +
                '}';
    }
}
