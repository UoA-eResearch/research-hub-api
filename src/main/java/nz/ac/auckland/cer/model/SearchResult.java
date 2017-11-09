package nz.ac.auckland.cer.model;

import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.SqlResultSetMapping;


public class SearchResult {

    private String type;
    private Integer id;
    private String title;
    private String subtitle;
    private String image;
    private float relevance;

    public SearchResult(String type, Integer id, String title, String subtitle, String image, float relevance) {
        this.type = type;
        this.id = id;
        this.title = title;
        this.subtitle = subtitle;
        this.image = image;
        this.relevance = relevance;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public float getRelevance() {
        return relevance;
    }

    public void setRelevance(float relevance) {
        this.relevance = relevance;
    }
}
