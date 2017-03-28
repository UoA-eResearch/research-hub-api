package nz.ac.auckland.cer.model.content;

import nz.ac.auckland.cer.model.Product;

import javax.persistence.Entity;


@Entity
public class Feature extends Content {

    public Feature() {
        super();
    }

    public Feature(String name) {
        super(name);
    }

    public Feature(String name, Product product) {
        super(name, product);
    }
}
