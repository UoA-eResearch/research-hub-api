package nz.ac.auckland.cer.model.content;

import nz.ac.auckland.cer.model.Product;

import javax.persistence.*;


@Entity
public class Requirement extends Content {

    public Requirement() {
        super();
    }

    public Requirement(String name) {
        super(name);
    }

    public Requirement(String name, Product product) {
        super(name, product);
    }
}
