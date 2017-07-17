package nz.ac.auckland.cer.model;

import javax.persistence.*;


@Entity
@Table(name="content_keyword")
public class Keyword {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String keyword;

    @ManyToOne
    @JoinColumn(name="content_id")
    private Content content;

    public Keyword() {
        super();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        this.content = content;
    }
}
