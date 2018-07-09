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

    @GetMapping("/{book}/top2phrases")
    public String topPhrases(@PathVariable String book){
        List<String> words = new ArrayList<>();
        List<String> phrases = new ArrayList<>();
        StringBuilder word = new StringBuilder();
        Iterator<BookEntity> iterator = repository.findAll().iterator();

        while (iterator.hasNext()) {
            String text;
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
            for (int i = 0; i < words.size() - 1; i++) {
                phrases.add(words.get(i) + " " + words.get(i + 1));
            }
            words.clear();
        }
        Map<String, Long> counts =
                phrases.stream().collect(Collectors.groupingBy(e -> e, Collectors.counting()));
        Map result = counts.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed()).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));
        return result.toString();
    }

//    @GetMapping("/student/all")
//    public List<BookEntity> getStudents(){
//        Iterator<BookEntity> iterator = repository.findAll().iterator();
//        List<BookEntity> students = new ArrayList<>();
//        while (iterator.hasNext()){
//            students.add(iterator.next());
//        }
//        return students;
//    }
//
//    @GetMapping("/student/{id}")
//    public Optional<BookEntity> getStudent(@PathVariable Integer id){
//        return repository.findById(id);
//    }
//
//    @PutMapping("/student/{id}")
//    public BookEntity updateStudent(@PathVariable Integer id, @RequestBody BookEntity student){
//        Optional<BookEntity> std = repository.findById(id);
//        if (std.isPresent()){
//            BookEntity s = std.get();
//            s.setName(student.getName());
//            return repository.save(s);
//        }
//        else return null;
//    }
//
//    @DeleteMapping("/student/{id}")
//    public String deleteStudent(@PathVariable Integer id){
//        repository.deleteById(id);
//        return "Document Deleted";
//    }
}