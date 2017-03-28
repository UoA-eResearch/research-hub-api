package nz.ac.auckland.cer.model.categories;

import nz.ac.auckland.cer.model.ManyToManyCategory;
import nz.ac.auckland.cer.model.Product;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Eligibility extends ManyToManyCategory {

    @ManyToMany(mappedBy="eligibility")
    private Set<Product> products;

    public Eligibility() {
        super();
    }

    public Eligibility(String name) {
        super(name);
    }
}
