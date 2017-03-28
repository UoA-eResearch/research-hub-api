package nz.ac.auckland.cer.model.content;

import nz.ac.auckland.cer.model.Product;

import javax.persistence.Entity;


@Entity
public class Consideration extends Content {

    public Consideration() {
        super();
    }

    public Consideration(String name) {
        super(name);
    }

    public Consideration(String name, Product product) {
        super(name, product);
    }
}
