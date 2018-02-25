package nz.ac.auckland.cer.model;


public class RequestConfig {

    private String id;
    private String shortDescription;
    private String category;
    private String subcategory;
    private String cmdbCiId;
    private String assignmentGroupId;
    private String businessServiceId;
    private String[] watchList;

    public RequestConfig() {

    }

    public RequestConfig(String id, String shortDescription, String category, String subcategory, String cmdbCiId, String assignmentGroupId, String businessServiceId, String[] watchList) {
        this.id = id;
        this.shortDescription = shortDescription;
        this.category = category;
        this.subcategory = subcategory;
        this.cmdbCiId = cmdbCiId;
        this.assignmentGroupId = assignmentGroupId;
        this.businessServiceId = businessServiceId;
        this.watchList = watchList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(String subcategory) {
        this.subcategory = subcategory;
    }

    public String getCmdbCiId() {
        return cmdbCiId;
    }

    public void setCmdbCiId(String cmdbCiId) {
        this.cmdbCiId = cmdbCiId;
    }

    public String getAssignmentGroupId() {
        return assignmentGroupId;
    }

    public void setAssignmentGroupId(String assignmentGroupId) {
        this.assignmentGroupId = assignmentGroupId;
    }

    public String getBusinessServiceId() {
        return businessServiceId;
    }

    public void setBusinessServiceId(String businessServiceId) {
        this.businessServiceId = businessServiceId;
    }

    public String[] getWatchList() {
        return watchList;
    }

    public void setWatchList(String[] watchList) {
        this.watchList = watchList;
    }
}