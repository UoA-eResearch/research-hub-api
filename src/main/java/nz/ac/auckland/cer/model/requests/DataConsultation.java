package nz.ac.auckland.cer.model.requests;


import java.util.List;

public class DataConsultation {

    private String requestorUpi;
    private String researcherUpi;
    private String newOrExistingStorage;
    private String projectTitle;
    private String projectAbstract;
    private String projectEndDate;
    private String fieldOfResearch;
    private String requirements;
    private String shortName;
    private List<String> upis;
    private List<String> accessRights;
    private String dataOwner;
    private String dataContact;
    private String dataThisYear;
    private String dataNextYear;

    public DataConsultation() {

    }

    public String getRequestorUpi() {
        return requestorUpi;
    }

    public void setRequestorUpi(String requestorUpi) {
        this.requestorUpi = requestorUpi;
    }

    public String getResearcherUpi() {
        return researcherUpi;
    }

    public void setResearcherUpi(String researcherUpi) {
        this.researcherUpi = researcherUpi;
    }

    public String getNewOrExistingStorage() {
        return newOrExistingStorage;
    }

    public void setNewOrExistingStorage(String newOrExistingStorage) {
        this.newOrExistingStorage = newOrExistingStorage;
    }

    public String getProjectTitle() {
        return projectTitle;
    }

    public void setProjectTitle(String projectTitle) {
        this.projectTitle = projectTitle;
    }

    public String getProjectAbstract() {
        return projectAbstract;
    }

    public void setProjectAbstract(String projectAbstract) {
        this.projectAbstract = projectAbstract;
    }

    public String getProjectEndDate() {
        return projectEndDate;
    }

    public void setProjectEndDate(String projectEndDate) {
        this.projectEndDate = projectEndDate;
    }

    public String getFieldOfResearch() {
        return fieldOfResearch;
    }

    public void setFieldOfResearch(String fieldOfResearch) {
        this.fieldOfResearch = fieldOfResearch;
    }

    public String getRequirements() {
        return requirements;
    }

    public void setRequirements(String requirements) {
        this.requirements = requirements;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public List<String> getUpis() {
        return upis;
    }

    public void setUpis(List<String> upis) {
        this.upis = upis;
    }

    public List<String> getAccessRights() {
        return accessRights;
    }

    public void setAccessRights(List<String> accessRights) {
        this.accessRights = accessRights;
    }

    public String getDataOwner() {
        return dataOwner;
    }

    public void setDataOwner(String dataOwner) {
        this.dataOwner = dataOwner;
    }

    public String getDataContact() {
        return dataContact;
    }

    public void setDataContact(String dataContact) {
        this.dataContact = dataContact;
    }

    public String getDataThisYear() {
        return dataThisYear;
    }

    public void setDataThisYear(String dataThisYear) {
        this.dataThisYear = dataThisYear;
    }

    public String getDataNextYear() {
        return dataNextYear;
    }

    public void setDataNextYear(String dataNextYear) {
        this.dataNextYear = dataNextYear;
    }
}
