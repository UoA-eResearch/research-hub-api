package nz.ac.auckland.cer.model.content;

import nz.ac.auckland.cer.model.Product;

import javax.persistence.Entity;


@Entity
public class Reference extends UrlContent {

    public Reference()
    {
        super();
    }

    public Reference(String name, String url) {
        super(name, url);
    }

    public Reference(String name, String url, Product product) {
        super(name, url, product);
    }
}
