package nz.ac.auckland.cer.model.content;

import nz.ac.auckland.cer.model.Product;

import javax.persistence.*;



@MappedSuperclass
public abstract class Content {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String name;

    @ManyToOne()
    @JoinColumn(name="product_id")
    private Product product;

    public Content() {

    }

    public Content(String name) {
        this.name = name;
    }

    public Content(String name, Product product) {
        this.name = name;
        this.product = product;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}