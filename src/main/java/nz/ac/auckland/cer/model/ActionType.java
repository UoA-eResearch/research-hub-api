package nz.ac.auckland.cer.model;


import com.fasterxml.jackson.annotation.JsonFilter;

import javax.persistence.*;
import java.util.Set;


@Entity
@JsonFilter(ActionType.ENTITY_NAME)
public class ActionType {
    public static final String ENTITY_NAME = "ActionType";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @OneToMany(mappedBy="actionType")
    private Set<Content> contentItems;

    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Content> getContentItems() {
        return contentItems;
    }

    public void setContentItems(Set<Content> contentItems) {
        this.contentItems = contentItems;
    }
}