package nz.ac.auckland.cer.model.categories;

import nz.ac.auckland.cer.model.Product;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import java.util.Set;


@Entity
public class StudyLevel extends ManyToManyCategory {

    @ManyToMany(mappedBy="studyLevels")
    private Set<Product> products;

    public StudyLevel() {
        super();
    }

    public StudyLevel(int id) {
        super(id);
    }

    public StudyLevel(String name) {
        super(name);
    }
}
