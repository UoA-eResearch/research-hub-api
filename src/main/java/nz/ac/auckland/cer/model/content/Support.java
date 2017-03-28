package nz.ac.auckland.cer.model.content;

import nz.ac.auckland.cer.model.Product;

import javax.persistence.Entity;


@Entity
public class Support extends UrlContent {

    public Support()
    {
        super();
    }

    public Support(String name, String url) {
        super(name, url);
    }

    public Support(String name, String url, Product product) {
        super(name, url, product);
    }
}
