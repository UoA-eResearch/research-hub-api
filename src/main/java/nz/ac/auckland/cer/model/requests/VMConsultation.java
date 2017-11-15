package nz.ac.auckland.cer.model.requests;


public class VMConsultation {

    private String date;
    private String time;
    private String comments;

    public VMConsultation() {

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

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}
