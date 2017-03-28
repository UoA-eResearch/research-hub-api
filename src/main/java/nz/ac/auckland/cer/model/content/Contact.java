package nz.ac.auckland.cer.model.content;

import nz.ac.auckland.cer.model.Product;

import javax.persistence.Entity;


@Entity
public class Contact extends UrlContent {

    public Contact()
    {
        super();
    }

    public Contact(String name, String url) {
        super(name, url);
    }

    public Contact(String name, String url, Product product) {
        super(name, url, product);
    }
}
