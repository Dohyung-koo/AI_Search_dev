package com.diquest.openapi.iptvott.request;

public class Entities {

    private String contentId = "";
    private String title = "";
    private String tmsRoodId = "";
    private String entityType = "";

    public String getTmsRoodId() {
        return tmsRoodId;
    }

    public void setTmsRoodId(String tmsRoodId) {
        this.tmsRoodId = tmsRoodId;
    }

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    @Override
    public String toString() {
        return "Entities{" +
                "contentId='" + contentId + '\'' +
                ", title='" + title + '\'' +
                ", entityType='" + entityType + '\'' +
                '}';
    }
}
