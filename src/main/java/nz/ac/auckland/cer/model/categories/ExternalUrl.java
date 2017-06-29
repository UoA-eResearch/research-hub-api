package nz.ac.auckland.cer.model.categories;

import nz.ac.auckland.cer.model.Content;

import javax.persistence.Entity;


@Entity
public class ExternalUrl extends ManyToOneCategory {

    private String url;

    public ExternalUrl() {
        super();
    }

    public ExternalUrl(String name, String url) {
        super(name);
        this.url = url;
    }

    public ExternalUrl(String name, String url, Content content) {
        super(name, content);
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
