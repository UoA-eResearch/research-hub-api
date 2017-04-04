package nz.ac.auckland.cer.model.categories;

import nz.ac.auckland.cer.model.Product;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import java.util.Set;

@Entity
public class Eligibility extends ManyToManyCategory {

    @ManyToMany(mappedBy="eligibleGroups")
    private Set<Product> products;

    public Eligibility() {
        super();
    }

    public Eligibility(int id) {
        super(id);
    }

    public Eligibility(String name) {
        super(name);
    }
}
