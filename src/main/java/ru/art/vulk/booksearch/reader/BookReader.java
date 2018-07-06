package ru.art.vulk.booksearch.reader;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class BookReader {

    public static List<String> readBook(){
        List<String> bookRows = new ArrayList();
        try {
            Files.lines(Paths.get("src/main/resources/book.txt"), Charset.forName("windows-1251")).forEach(e -> bookRows.add(e));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bookRows;
    }
}
