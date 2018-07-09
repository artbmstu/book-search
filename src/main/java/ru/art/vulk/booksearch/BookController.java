package ru.art.vulk.booksearch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.art.vulk.booksearch.reader.BookReader;

import java.io.FileWriter;
import java.util.*;
import java.util.stream.Collectors;

@RestController
public class BookController {

    @Autowired
    BookRepository repository;
//    @ElementCollection
    Map<String, Long> phrases = new HashMap();

//    private IPhraseService phraseService;
//    @Autowired
//    BookController (IPhraseService phraseService){
//        this.phraseService = phraseService;
//    }

    BookController (){}

    @GetMapping("/{book}")
    public String addStudent(@PathVariable String book) throws Exception{
        List<String> bookRows = BookReader.readBook(book);
        try (FileWriter file = new FileWriter(book + ".json")) {
            int count = 0;
            for (String rowText :
                    bookRows) {
                count++;
                file.append("{\"create\":{\"_index\":\"books\",\"_type\" : \""+ book +"\", \"_id\" : \"" + count + "\"}}\n");
                file.append("{\"row\" : \"" + count + "\", \"text\" : \"" + rowText + "\"}\n");
            }
        }
        return book + ".json создан";
    }

    @GetMapping("/{book}/top2phrases/{n}")
    public String topPhrases(@PathVariable String book, @PathVariable int n){
        List<String> words = new ArrayList<>();
        StringBuffer word = new StringBuffer();
        Iterator<BookEntity> iterator = repository.findAll().iterator();
        Map<String, Long> result = new HashMap();
        int count = 0;
        String text;
        Map sorted = null;
        while (iterator.hasNext()) {
            count++;
            if ((text = iterator.next().getText()) != null) {
                text = text.toLowerCase().replaceAll("[^a-zA-Zа-яА-Я ]", "");
                for (int i = 0; i < text.length(); i++) {
                    if (text.charAt(i) != ' ') {
                        word.append(text.charAt(i));
                    } else {
                        words.add(word.toString());
                        word.setLength(0);
                    }
                }
                words.add(word.toString());
            }
            words.removeIf(e -> e.equals(""));
            if (n <= words.size()) {
                for (int i = 0; i < words.size() - n + 1; i++) {
                    StringBuilder s = new StringBuilder();
                    for (int j = 0 + i; j < n + i; j++) {
                        s.append(words.get(j) + " ");
                    }
                    String string = s.toString();
                    if (phrases.containsKey(string)){
                        phrases.put(string, (phrases.get(string) + 1));
                    } else phrases.put(string, 1L);
//                phrases.add(new PhraseEntity(words.get(i) + " " + words.get(i + 1)));
//                PhraseEntity phraseEntity = new PhraseEntity();
//                phraseEntity.setText(words.get(i) + " " + words.get(i + 1));
                }
            }
//            phraseService.addPhrase(phrases);
            words.clear();
        }
        result = phrases.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed()).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));
        return result.toString();
    }
}