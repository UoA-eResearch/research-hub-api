package nz.ac.auckland.cer.model;


import java.util.List;

public class ListItem {

    private String type;
    private Integer id;
    private String title;
    private String subtitle;
    private String image;
    private String url;
    private float relevance;
    private List<String> categories;

    public ListItem(String type, Integer id, String title, String subtitle, String image, String url, float relevance) {
        this.type = type;
        this.id = id;
        this.title = title;
        this.subtitle = subtitle;
        this.image = image;
        this.url = url;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public float getRelevance() {
        return relevance;
    }

    public void setRelevance(float relevance) {
        this.relevance = relevance;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }
}
