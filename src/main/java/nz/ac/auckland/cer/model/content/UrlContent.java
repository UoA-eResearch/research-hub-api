package nz.ac.auckland.cer.model.content;

import nz.ac.auckland.cer.model.Product;

import javax.persistence.MappedSuperclass;


@MappedSuperclass
public abstract class UrlContent extends Content {

    private String url;

    public UrlContent()
    {
        super();
    }

    public UrlContent(String name, String url) {
        super(name);
        this.url = url;
    }

    public UrlContent(String name, String url, Product product) {
        super(name, product);
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
