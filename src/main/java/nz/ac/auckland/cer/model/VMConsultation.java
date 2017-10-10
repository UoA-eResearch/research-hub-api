package nz.ac.auckland.cer.model;


public class VMConsultation {

    private String requestorUpi;
    private String researcherUpi;
    private String date;
    private String time;
    private String location;
    private String comments;

    public VMConsultation() {

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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}
