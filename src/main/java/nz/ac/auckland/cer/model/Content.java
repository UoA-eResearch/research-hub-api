package nz.ac.auckland.cer.model;

import javax.persistence.*;

@Entity
public class Content {
    private int id;
    private Product product;
    private ContentCategory contentCategory;
    private String text;
    private String uri;

    public Content() {

    }

    public Content(Product product, ContentCategory contentCategory, String text, String uri) {
        this.product = product;
        this.contentCategory = contentCategory;
        this.text = text;
        this.uri = uri;
    }

    @ManyToOne
    @JoinColumn(name = "id")
    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product)
    {
        this.product = product;
    }
}