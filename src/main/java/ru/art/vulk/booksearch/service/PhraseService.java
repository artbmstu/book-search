package ru.art.vulk.booksearch.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.art.vulk.booksearch.dao.IPhraseDAO;
import ru.art.vulk.booksearch.praseEntity.PhraseEntity;

import java.util.List;

@Service
public class PhraseService implements IPhraseService {
    @Autowired
    private IPhraseDAO phraseDAO;
    @Override
    public List<PhraseEntity> getAllPhrases() {
        return phraseDAO.getAllPhrases();
    }

    public synchronized void addPhrase(List<PhraseEntity> phrase) {
        phraseDAO.addPhrase(phrase);
    }
}
