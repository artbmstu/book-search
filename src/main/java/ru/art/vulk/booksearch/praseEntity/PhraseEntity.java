package ru.art.vulk.booksearch.praseEntity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "BLOG")
public class PhraseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    public PhraseEntity (String text){
        this.text = text;
    }
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name="ID")
    private int id;
    @Column(name = "TEXT")
    private String text;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
