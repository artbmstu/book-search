package ru.art.vulk.booksearch;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.search.SearchHit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.art.vulk.booksearch.reader.BookReader;

import java.io.FileWriter;
import java.util.*;
import java.util.stream.Collectors;

@RestController
public class BookController {
    @Autowired
    TransportClient client;

//    @Autowired
//    BookRepository repository;
    Map<String, Long> phrases = new HashMap();

    @GetMapping("/{book}")
    public String addStudent(@PathVariable String book) throws Exception {
        List<String> bookRows = BookReader.readBook(book);
        try (FileWriter file = new FileWriter(book + ".json")) {
            int count = 0;
            for (String rowText :
                    bookRows) {
                count++;
                file.append("{\"create\":{\"_index\":\"books\",\"_type\" : \"" + book + "\", \"_id\" : \"" + count + "\"}}\n");
                file.append("{\"row\" : \"" + count + "\", \"text\" : \"" + rowText + "\"}\n");
            }
        }
        return book + ".json создан";
    }

    @GetMapping("/{book}/topPhrases/{n}")
    public String topPhrases(@PathVariable String book, @PathVariable int n) {
        List<String> words = new ArrayList<>();
        StringBuilder word = new StringBuilder();
        StringBuilder phrase = new StringBuilder();

        SearchResponse scrollResp = client.prepareSearch("books")
                .setFetchSource("text","row" )
                .setScroll(new TimeValue(6000))
                .setSize(10000).get();
        do {
            for (SearchHit hit : scrollResp.getHits().getHits()) {
                String hitText = hit.getSource().values().toString();
                String text = hitText.toLowerCase().replaceAll("[^a-zA-Zа-яА-Я ]", "");
                for (int i = 0; i < text.length(); i++) {
                    if (text.charAt(i) != ' ') {
                        word.append(text.charAt(i));
                    } else {
                        words.add(word.toString());
                        word.setLength(0);
                    }
                }
                words.add(word.toString());
                words.removeIf(e -> e.equals(""));
                if (n <= words.size()) {
                    for (int i = 0; i < words.size() - n + 1; i++) {
                        for (int j = i; j < n + i; j++) {
                            phrase.append(words.get(j) + " ");
                        }
                        String string = phrase.toString();
                        if (phrases.containsKey(string)) {
                            phrases.put(string, (phrases.get(string) + 1));
                        } else phrases.put(string, 1L);
                        phrase.setLength(0);
                    }
                }
                words.clear();
            }
            scrollResp = client.prepareSearchScroll(scrollResp.getScrollId()).setScroll(new TimeValue(60000)).execute().actionGet();
        } while (scrollResp.getHits().getHits().length != 0);
        Map<String, Long> result = phrases.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed()).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));
        phrases.clear();
        return result.toString();
    }
}