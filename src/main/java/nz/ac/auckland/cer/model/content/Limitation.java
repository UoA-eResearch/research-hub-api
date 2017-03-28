package nz.ac.auckland.cer.model.content;

import nz.ac.auckland.cer.model.Product;

import javax.persistence.Entity;


@Entity
public class Limitation extends Content {

    public Limitation() {
        super();
    }

    public Limitation(String name) {
        super(name);
    }

    public Limitation(String name, Product product) {
        super(name, product);
    }
}
