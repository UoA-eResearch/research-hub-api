package nz.ac.auckland.cer.model.categories;

import nz.ac.auckland.cer.model.Content;

import javax.persistence.*;


@MappedSuperclass
public abstract class ManyToOneCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String name;

    @ManyToOne
    @JoinColumn(name="content_id")
    private Content content;

    public ManyToOneCategory() {

    }

    public ManyToOneCategory(String name) {
        this.name = name;
    }

    public ManyToOneCategory(String name, Content content) {
        this.name = name;
        this.content = content;
    }

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

    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        this.content = content;
    }
}