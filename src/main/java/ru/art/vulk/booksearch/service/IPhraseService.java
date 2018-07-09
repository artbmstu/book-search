package ru.art.vulk.booksearch.service;

import ru.art.vulk.booksearch.praseEntity.PhraseEntity;

import java.util.List;

public interface IPhraseService {
    List<PhraseEntity> getAllPhrases();
    void addPhrase(List<PhraseEntity> phrase);
}
