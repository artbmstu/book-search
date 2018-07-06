package ru.art.vulk.booksearch;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;

import static org.springframework.data.elasticsearch.annotations.FieldType.Text;

@Document(indexName = "books", type = "book")
public class BookEntity {
    @Id
    private int row;
    @Field (type = Text)
    private String text;

    public BookEntity(int row, String text) {
        super();
        this.row = row;
        this.text = text;
    }

    public BookEntity(){};

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }
}
